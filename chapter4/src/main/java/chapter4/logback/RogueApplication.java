package chapter4.logback;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RogueApplication {
	private static final Logger LOG = LoggerFactory.getLogger(RogueApplication.class);

	public static void main(String[] args) throws Exception {
		for (int x = 0; x < 100; x++) {

			int slowCount = 6;
			int fastCount = 15;
			// slow state
			for (int i = 0; i < slowCount; i++) {
				LOG.warn("This is a warning (slow state).");
				Thread.sleep(5000);
			}
			// enter rapid state
			for (int i = 0; i < fastCount; i++) {
				LOG.warn("This is a warning (rapid state).");
				Thread.sleep(1000);
			}
			// return to slow state
			for (int i = 0; i < slowCount; i++) {
				LOG.warn("This is a warning (slow state).");
				Thread.sleep(5000);
			}
		}
	}
}