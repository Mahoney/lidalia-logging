package uk.org.lidalia.monitoring.logging.groovy;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.classic.spi.LoggerContextVO;
import org.slf4j.Marker;

import java.util.Map;

final class StackTraceCleansedLoggingEvent implements ILoggingEvent {

    private final ILoggingEvent realEvent;
    private final IThrowableProxy cleansedThrowableProxy;

    StackTraceCleansedLoggingEvent(ILoggingEvent realEvent) {
        this.realEvent = realEvent;
        if (realEvent.getThrowableProxy() == null) {
			cleansedThrowableProxy = null;
		} else {
			cleansedThrowableProxy = new StackTraceCleansedThrowableProxy(realEvent.getThrowableProxy());
		}
    }

    public String getThreadName() {
        return realEvent.getThreadName();
    }

    public Level getLevel() {
        return realEvent.getLevel();
    }

    public String getMessage() {
        return realEvent.getMessage();
    }

    public Object[] getArgumentArray() {
        return realEvent.getArgumentArray();
    }

    public String getFormattedMessage() {
        return realEvent.getFormattedMessage();
    }

    public String getLoggerName() {
        return realEvent.getLoggerName();
    }

    public LoggerContextVO getLoggerContextVO() {
        return realEvent.getLoggerContextVO();
    }

    public IThrowableProxy getThrowableProxy() {
        return cleansedThrowableProxy;
    }

    public StackTraceElement[] getCallerData() {
        return realEvent.getCallerData();
    }

    public boolean hasCallerData() {
        return realEvent.hasCallerData();
    }

    public Marker getMarker() {
        return realEvent.getMarker();
    }

    public Map<String, String> getMDCPropertyMap() {
        return realEvent.getMDCPropertyMap();
    }

    public Map<String, String> getMdc() {
        return realEvent.getMdc();
    }

    public long getTimeStamp() {
        return realEvent.getTimeStamp();
    }

    public void prepareForDeferredProcessing() {
        realEvent.prepareForDeferredProcessing();
    }
}
