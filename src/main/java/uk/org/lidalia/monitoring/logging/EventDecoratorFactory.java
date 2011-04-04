package uk.org.lidalia.monitoring.logging;

import ch.qos.logback.classic.spi.ILoggingEvent;

public interface EventDecoratorFactory {
	public ILoggingEvent decorateEvent(ILoggingEvent toBeDecorated);
}
