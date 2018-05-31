package chapter4;

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.StormSubmitter;
import org.apache.storm.generated.StormTopology;
import org.apache.storm.kafka.Broker;
import org.apache.storm.kafka.StaticHosts;
import org.apache.storm.kafka.StringScheme;
import org.apache.storm.kafka.trident.GlobalPartitionInformation;
import org.apache.storm.kafka.trident.OpaqueTridentKafkaSpout;
import org.apache.storm.kafka.trident.TridentKafkaConfig;
import org.apache.storm.spout.SchemeAsMultiScheme;
import org.apache.storm.trident.Stream;
import org.apache.storm.trident.TridentTopology;
import org.apache.storm.tuple.Fields;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import chapter4.EWMA.Time;
import chapter4.operations.BooleanFilter;
import chapter4.operations.JsonProjectFunction;
import chapter4.operations.MovingAverageFunction;
import chapter4.operations.ThresholdFilterFunction;
import chapter4.operations.XMPPFunction;

/**
 * 
 * Topology: <code>
 * 
 * 	[KfakaSport]---each--->[JsonProject]---project--->[MovingAverage]---->[ThreshHoldFilter]---->[XMPPNotifier]
 * 
 * </code>
 * 
 * 
 * @author Wu
 *
 */
public class LogAnalysisTopology {

	private static final Logger LOG = LoggerFactory.getLogger(LogAnalysisTopology.class);

	public static StormTopology buildTopology() {
		TridentTopology topology = new TridentTopology();
		Broker broker0 = new Broker(MyConfig.Kafka_Host, MyConfig.Kafka_Port);

		GlobalPartitionInformation partitionInfo = new GlobalPartitionInformation("log-analysis");
		partitionInfo.addPartition(0, broker0);
		StaticHosts kafkaHosts = new StaticHosts(partitionInfo);

		TridentKafkaConfig spoutConf = new TridentKafkaConfig(kafkaHosts, MyConfig.Topic);
		spoutConf.scheme = new SchemeAsMultiScheme(new StringScheme());
		// spoutConf.forceStartOffsetTime(-1);
		spoutConf.startOffsetTime = -1;
		OpaqueTridentKafkaSpout spout = new OpaqueTridentKafkaSpout(spoutConf);
		Stream spoutStream = topology.newStream("kafka-stream", spout);

		Fields jsonFields = new Fields("level", "timestamp", "message", "logger");
		Stream parsedStream = spoutStream.each(new Fields("str"), new JsonProjectFunction(jsonFields), jsonFields);
		// drop the unparsed JSON to reduce tuple size
		parsedStream = parsedStream.project(jsonFields);
		EWMA ewma = new EWMA().sliding(1.0, Time.MINUTES).withAlpha(EWMA.ONE_MINUTE_ALPHA);
		Stream averageStream = parsedStream.each(new Fields("timestamp"), new MovingAverageFunction(ewma, Time.MINUTES),
				new Fields("average"));
		ThresholdFilterFunction tff = new ThresholdFilterFunction(50D);
		Stream thresholdStream = averageStream.each(new Fields("average"), tff, new Fields("change", "threshold"));
		Stream filteredStream = thresholdStream.each(new Fields("change"), new BooleanFilter());
		filteredStream.each(filteredStream.getOutputFields(), new XMPPFunction(new NotifyMessageMapper()),
				new Fields());
		return topology.build();
	}

	public static void main(String[] args) throws Exception {
		LOG.info("main enter.");
		Config conf = new Config();

		conf.setMaxSpoutPending(5);
		if (args.length == 0) {
			LOG.info("submit to local cluster.");
			LocalCluster cluster = new LocalCluster();
			cluster.submitTopology("log-analysis", conf, buildTopology());
			LOG.info("done submit to local cluster.");

		} else {
			LOG.info("submit to remote cluster.");
			conf.setNumWorkers(3);
			StormSubmitter.submitTopology(args[0], conf, buildTopology());
		}
	}
}
