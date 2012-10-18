package uk.org.lidalia.logging;

import ch.qos.logback.classic.spi.ILoggingEvent;

public class EventTransformingAppender extends DecoratingAppender {

    private EventTransformer eventTransformer;

    @Override
    protected void append(final ILoggingEvent eventObject) {
        final ILoggingEvent decoratedEvent = eventTransformer.transformEvent(eventObject);
        decoratedAppender.doAppend(decoratedEvent);
    }

    public void setEventTransformer(final EventTransformer eventTransformer) {
        this.eventTransformer = eventTransformer;
    }

    public void start() {
        if (this.eventTransformer == null) {
            addError("No event decorator factory for the appender named \"" + name + "\".");
        } else {
            super.start();
        }
    }
}
