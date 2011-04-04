package uk.org.lidalia.monitoring.logging;

import ch.qos.logback.classic.spi.ILoggingEvent;

public interface EventTransformer {
	public ILoggingEvent transformEvent(ILoggingEvent toBeDecorated);
}
