package storm.blueprints;

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.generated.StormTopology;
import org.apache.storm.trident.Stream;
import org.apache.storm.trident.TridentState;
import org.apache.storm.trident.TridentTopology;
import org.apache.storm.trident.fluent.GroupedStream;
import org.apache.storm.tuple.Fields;

import storm.blueprints.operations.CityAssignment;
import storm.blueprints.operations.Count;
import storm.blueprints.operations.DiseaseFilter;
import storm.blueprints.operations.DispatchAlert;
import storm.blueprints.operations.HourAssignment;
import storm.blueprints.operations.OutbreakDetector;

//https://github.com/cjie888/storm-trident/blob/master/src/main/java/com/cjie/storm/trident/outbreak/OutbreakDetectionTopology.java
public class OutbreakDetectionTopology {

	public static StormTopology buildTopology() {
		TridentTopology topology = new TridentTopology();
		DiagnosisEventSpout spout = new DiagnosisEventSpout();
		Stream inputStream = topology.newStream("event", spout);
		//
		inputStream = inputStream.each(new Fields("event"), //
				new DiseaseFilter())//
		;
		inputStream = inputStream.each(new Fields("event"), //
				new CityAssignment(), //
				new Fields("city"))//
		;
		inputStream = inputStream.each(new Fields("event", "city"), //
				new HourAssignment(), //
				new Fields("hour", "cityDiseaseHour"))// HourAssignment生成一个cityDiseaseHour的key值.
		;
		//
		GroupedStream groupedStream = inputStream.groupBy(new Fields("cityDiseaseHour"))// 强制分片,将特定字段相同的tuple分到同一片.
		;
		// groupBy 产生一个GroupedStream数据流.
		TridentState state = groupedStream.persistentAggregate(new OutbreakTrendFactory(), // 分片后aggregate函数在每个分组中运行.
				new Count(), //
				new Fields("count"));
		inputStream = state.newValuesStream()//
		;
		inputStream = inputStream.each(new Fields("cityDiseaseHour", "count"), //
				new OutbreakDetector(), //
				new Fields("alert"))//
		;
		inputStream = inputStream.each(new Fields(), //
				new DispatchAlert(), //
				new Fields())//
		;

		return topology.build();
	}

	public static void main(String[] args) throws Exception {
		Config conf = new Config();
		LocalCluster cluster = new LocalCluster();
		cluster.submitTopology("cdc", conf, buildTopology());
		Thread.sleep(200000);
		cluster.shutdown();
	}
}
