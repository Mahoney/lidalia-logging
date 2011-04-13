package uk.org.lidalia.logging;

import ch.qos.logback.classic.spi.ILoggingEvent;

public interface EventTransformer {
	public ILoggingEvent transformEvent(ILoggingEvent toBeDecorated);
}
