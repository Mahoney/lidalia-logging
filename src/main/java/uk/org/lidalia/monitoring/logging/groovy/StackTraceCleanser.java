package uk.org.lidalia.monitoring.logging.groovy;

import ch.qos.logback.classic.spi.ILoggingEvent;
import uk.org.lidalia.monitoring.logging.EventTransformer;

public class StackTraceCleanser implements EventTransformer {
	@Override public ILoggingEvent transformEvent(ILoggingEvent toBeDecorated) {
		return new StackTraceCleansedLoggingEvent(toBeDecorated);
	}
}
