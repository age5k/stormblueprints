package storm.blueprints;

import org.apache.storm.trident.state.map.NonTransactionalMap;

public class OutbreakTrendState extends NonTransactionalMap<Long> {

	public OutbreakTrendState(OutbreakTrendBackingMap outbreakTrendBackingMap) {
		super(outbreakTrendBackingMap);
	}

}
