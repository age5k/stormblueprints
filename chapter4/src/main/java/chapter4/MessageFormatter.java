package chapter4;

import ch.qos.logback.classic.spi.ILoggingEvent;

public class MessageFormatter implements Formatter {
	public String format(ILoggingEvent event) {
		return event.getFormattedMessage();
	}
}
