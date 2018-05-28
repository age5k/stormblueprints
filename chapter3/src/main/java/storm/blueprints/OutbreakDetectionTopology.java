package storm.blueprints;

import org.apache.storm.generated.StormTopology;
import org.apache.storm.trident.Stream;
import org.apache.storm.trident.TridentTopology;
import org.apache.storm.trident.operation.builtin.Count;
import org.apache.storm.tuple.Fields;

//https://github.com/cjie888/storm-trident/blob/master/src/main/java/com/cjie/storm/trident/outbreak/OutbreakDetectionTopology.java
public class OutbreakDetectionTopology {

	public static StormTopology buildTopology() {
		TridentTopology topology = new TridentTopology();
		DiagnosisEventSpout spout = new DiagnosisEventSpout();
		Stream inputStream = topology.newStream("event", spout);
		//
		inputStream.each(new Fields("event"), //
				new DiseaseFilter())//
				.each(new Fields("event"), //
						new CityAssignment(), //
						new Fields("city"))//
				.each(new Fields("event", "city"), //
						new HourAssignment(), //
						new Fields("hour", "cityDiseaseHour"))//
				.groupBy(new Fields("cityDiseaseHour"))//
				.persistentAggregate(new OutbreakTrendFactory(), //
						new Count(), //
						new Fields("count"))
				.newValuesStream()//
				.each(new Fields("cityDiseaseHour", "count"), //
						new OutbreakDetector(), //
						new Fields("alert"))//
				.each(new Fields(), //
						new DispatchAlert(), //
						new Fields())//
		;

		return topology.build();
	}
}
