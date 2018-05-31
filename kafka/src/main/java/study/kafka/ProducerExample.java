package study.kafka;

import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
//kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic testTopic --from-beginning
//kafka-console-producer.sh --broker-list localhost:9092 --topic testTopic

public class ProducerExample {
	public static final String BOOTSTRAP_SERVERS = "192.168.88.130:9092";
	public static final String TOPIC = "my-topic";

	public static void main(String[] args) {
		Properties props = new Properties();
		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
		// props.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, config.compressionCodec)
		// props.put(ProducerConfig.SEND_BUFFER_CONFIG, config.socketBuffer.toString)
		// props.put(ProducerConfig.RETRY_BACKOFF_MS_CONFIG,
		// config.retryBackoffMs.toString)
		// props.put(ProducerConfig.METADATA_MAX_AGE_CONFIG,
		// config.metadataExpiryMs.toString)
		// props.put(ProducerConfig.MAX_BLOCK_MS_CONFIG, config.maxBlockMs.toString)
		// props.put(ProducerConfig.ACKS_CONFIG, config.requestRequiredAcks)
		// props.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG,
		// config.requestTimeoutMs.toString)
		// props.put(ProducerConfig.RETRIES_CONFIG,
		// config.messageSendMaxRetries.toString)
		// props.put(ProducerConfig.LINGER_MS_CONFIG, config.sendTimeout.toString)
		// props.put(ProducerConfig.BUFFER_MEMORY_CONFIG,
		// config.maxMemoryBytes.toString)
		// props.put(ProducerConfig.BATCH_SIZE_CONFIG,
		// config.maxPartitionMemoryBytes.toString)
		// props.put(ProducerConfig.CLIENT_ID_CONFIG, "console-producer")
		props.put(ProducerConfig.CLIENT_ID_CONFIG, "KafkaExampleProducer");
		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
				"org.apache.kafka.common.serialization.StringSerializer");
		// props.put("bootstrap.servers", "localhost:9092");
		// props.put("bootstrap.servers", "192.168.110.128:9092");
		/*
		 * props.put("acks", "all"); props.put("retries", 0); props.put("batch.size",
		 * 16384); props.put("linger.ms", 1); props.put("buffer.memory", 33554432);
		 */

		Producer<String, String> producer = new KafkaProducer<>(props);
		for (int i = 0; i < 100; i++)
			producer.send(new ProducerRecord<String, String>(TOPIC, Integer.toString(i)));

		producer.close();
	}
}
