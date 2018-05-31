package chapter4.logback;

import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import chapter4.Formatter;
import chapter4.MessageFormatter;
import chapter4.MyConfig;

public class KafkaAppender extends AppenderBase<ILoggingEvent> {
	private String topic = MyConfig.Topic;
	private String zookeeperHost;
	private String bootstrapServers = MyConfig.Kafka_Host + ":" + MyConfig.Kafka_Port;

	public String getBootstrapServers() {
		return bootstrapServers;
	}

	public void setBootstrapServers(String bootstrapServers) {
		this.bootstrapServers = bootstrapServers;
	}

	private Producer<String, String> producer;
	private Formatter formatter;

	// java bean definitions used to inject
	// configuration values from logback.xml
	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public String getZookeeperHost() {
		return zookeeperHost;
	}

	public void setZookeeperHost(String zookeeperHost) {
		this.zookeeperHost = zookeeperHost;
	}

	public Formatter getFormatter() {
		return formatter;
	}

	public void setFormatter(Formatter formatter) {
		this.formatter = formatter;
	}

	@Override
	public void start() {
		if (this.formatter == null) {
			this.formatter = new MessageFormatter();
		}
		super.start();
		Properties props = new Properties();
		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
		props.put(ProducerConfig.CLIENT_ID_CONFIG, "logback-kafka-appender");
		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
				org.apache.kafka.common.serialization.StringSerializer.class.getName());
		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
				org.apache.kafka.common.serialization.StringSerializer.class.getName());

		this.producer = new KafkaProducer<String, String>(props);
	}

	@Override
	public void stop() {
		super.stop();
		this.producer.close();
	}

	@Override
	protected void append(ILoggingEvent event) {
		String payload = this.formatter.format(event);
		ProducerRecord<String, String> data = new ProducerRecord<String, String>(this.topic, payload);
		this.producer.send(data);
	}
}
