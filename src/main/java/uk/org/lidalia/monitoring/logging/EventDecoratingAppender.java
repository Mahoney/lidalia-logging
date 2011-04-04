package uk.org.lidalia.monitoring.logging;

import ch.qos.logback.classic.spi.ILoggingEvent;

public class EventDecoratingAppender extends DecoratingAppender {

	private EventDecoratorFactory eventDecoratorFactory;

	@Override
	protected void append(ILoggingEvent eventObject) {
			ILoggingEvent decoratedEvent = eventDecoratorFactory.decorateEvent(eventObject);
			decoratedAppender.doAppend(decoratedEvent);
	}

	public void setEventDecoratorFactory(EventDecoratorFactory eventDecoratorFactory) {
		this.eventDecoratorFactory = eventDecoratorFactory;
	}

	public void start() {
		if (this.eventDecoratorFactory == null) {
      addError("No event decorator factory for the appender named \"" + name + "\".");
    } else {
			super.start();
		}
  }
}
