package chapter4.operations;

import java.util.Map;

import org.apache.storm.shade.org.json.simple.JSONValue;
import org.apache.storm.trident.operation.BaseFunction;
import org.apache.storm.trident.operation.TridentCollector;
import org.apache.storm.trident.tuple.TridentTuple;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonProjectFunction extends BaseFunction {

	private static final long serialVersionUID = 1L;

	private static final Logger LOG = LoggerFactory.getLogger(JsonProjectFunction.class);
	private Fields fields;

	public JsonProjectFunction(Fields fields) {
		this.fields = fields;
	}

	@Override
	public void execute(TridentTuple tuple, TridentCollector collector) {
		LOG.debug("execute tuple:[{}],collector:[{}]", tuple, collector);

		String json = tuple.getString(0);
		Map<String, Object> map = (Map<String, Object>) JSONValue.parse(json);
		Values values = new Values();
		for (int i = 0; i < this.fields.size(); i++) {
			values.add(map.get(this.fields.get(i)));
		}
		collector.emit(values);
	}
}