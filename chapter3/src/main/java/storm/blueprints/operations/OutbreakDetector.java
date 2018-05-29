package storm.blueprints.operations;

import java.util.ArrayList;
import java.util.List;

import org.apache.storm.trident.operation.BaseFunction;
import org.apache.storm.trident.operation.TridentCollector;
import org.apache.storm.trident.tuple.TridentTuple;

public class OutbreakDetector extends BaseFunction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final int THRESHOLD = 10000;

	@Override
	public void execute(TridentTuple tuple, TridentCollector collector) {
		String key = (String) tuple.getValue(0);
		Long count = (Long) tuple.getValue(1);
		if (count < THRESHOLD) {
			List<Object> values = new ArrayList<>();
			values.add("Outbreak detected for[" + key + "]!");
			collector.emit(values);
		}
	}

}
