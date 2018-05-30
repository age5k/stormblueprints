package chapter4;

import ch.qos.logback.classic.spi.ILoggingEvent;

public interface Formatter {
	public String format(ILoggingEvent event);
}
