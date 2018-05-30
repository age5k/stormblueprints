package chapter4.operations;

import org.apache.storm.trident.operation.BaseFilter;
import org.apache.storm.trident.tuple.TridentTuple;

public class BooleanFilter extends BaseFilter {
	public boolean isKeep(TridentTuple tuple) {
		return tuple.getBoolean(0);
	}
}
