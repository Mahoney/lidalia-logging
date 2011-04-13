package uk.org.lidalia.logging;

import ch.qos.logback.classic.spi.ILoggingEvent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AsyncAppender extends DecoratingAppender {

	private final ExecutorService executor = Executors.newCachedThreadPool();

	@Override
	protected void append(final ILoggingEvent eventObject) {
		executor.execute(new Runnable() {
			@Override
			public void run() {
				decoratedAppender.doAppend(eventObject);
			}
		});
	}

	@Override public void stop() {
		executor.shutdown();
		super.stop();
	}
}
