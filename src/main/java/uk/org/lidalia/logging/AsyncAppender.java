package uk.org.lidalia.logging;

import ch.qos.logback.classic.spi.ILoggingEvent;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AsyncAppender extends DecoratingAppender {

	private ExecutorService executor;
	private String threadConfiguration;

	@Override
	protected void append(final ILoggingEvent eventObject) {
		executor.execute(new Runnable() {
			@Override
			public void run() {
				decoratedAppender.doAppend(eventObject);
			}
		});
	}

	public void setThreads(String threadConfiguration) {
		this.threadConfiguration = threadConfiguration;
	}

	@Override
	public void start() {
		if (threadConfiguration == null || threadConfiguration.trim().length() == 0) {
			executor = Executors.newSingleThreadExecutor();
			addInfo("Using ExecutorService with single thread");
		} else if (threadConfiguration.equals("unlimited")) {
			executor = Executors.newCachedThreadPool();
		} else {
			try {
				int threads = evaluateThreadConfiguration();
				executor = Executors.newFixedThreadPool(threads);
				addInfo("Using ExecutorService with " + threads + " threads.");
			} catch (Exception e) {
				addError("Unable to parse [" + threadConfiguration + "] into an integer.", e);
			}
		}
		if (executor != null) {
			super.start();
		}
	}

	private int evaluateThreadConfiguration() throws ScriptException {
		String expression = threadConfiguration.replaceAll("R", String.valueOf(Runtime.getRuntime().availableProcessors()));
		ScriptEngineManager mgr = new ScriptEngineManager();
		ScriptEngine engine = mgr.getEngineByName("JavaScript");
		Number result = (Number) engine.eval(expression);
		return result.intValue();
	}

	@Override
	public void stop() {
		executor.shutdown();
		super.stop();
	}
}
