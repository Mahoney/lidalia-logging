package uk.org.lidalia.logging.groovy;

import ch.qos.logback.classic.spi.StackTraceElementProxy;
import ch.qos.logback.classic.spi.ThrowableProxy;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class StackTraceCleansedThrowableProxyTests {

	@Test public void cleansedProxyDoesNotContainUndesirableFrames() {
		StackTraceElement[] stackTraceElements = new StackTraceElement[] {
				new StackTraceElement("valid.Class1", "method", "file", 1),
				new StackTraceElement("sun.reflect.SomeClass", "method", "file", 1),
				new StackTraceElement("java.lang.reflect.SomeClass", "method", "file", 1),
				new StackTraceElement("org.codehaus.groovy.runtime.SomeClass", "method", "file", 1),
				new StackTraceElement("org.codehaus.groovy.reflection.SomeClass", "method", "file", 1),
				new StackTraceElement("groovy.lang.SomeClass", "method", "file", 1),
				new StackTraceElement("valid.Class1", "method", null, 1),
				new StackTraceElement("valid.Class1", "method", "file", -1),
				new StackTraceElement("valid.Class2", "method", "file", 1)
		};

		Throwable t = new Throwable();
		t.setStackTrace(stackTraceElements);
		ThrowableProxy decoratedProxy = new ThrowableProxy(t);

		StackTraceCleansedThrowableProxy proxy = new StackTraceCleansedThrowableProxy(decoratedProxy);
		StackTraceElementProxy[] stackTraceElementProxies = proxy.getStackTraceElementProxyArray();
		assertEquals(2, stackTraceElementProxies.length);
		assertEquals(stackTraceElements[0], stackTraceElementProxies[0].getStackTraceElement());
		assertEquals(stackTraceElements[8], stackTraceElementProxies[1].getStackTraceElement());

	}

	@Test public void cleansedProxyHasCorrectNumberOfCommonFrames() {
		StackTraceElement[] parentStackTraceElements = new StackTraceElement[] {
				new StackTraceElement("valid.Class1", "method", "file", 1),
				new StackTraceElement("sun.reflect.SomeClass", "method", "file", 1),
				new StackTraceElement("java.lang.reflect.SomeClass", "method", "file", 1),
				new StackTraceElement("org.codehaus.groovy.runtime.SomeClass", "method", "file", 1),
				new StackTraceElement("org.codehaus.groovy.reflection.SomeClass", "method", "file", 1),
				new StackTraceElement("groovy.lang.SomeClass", "method", "file", 1),
				new StackTraceElement("valid.Class1", "method", null, 1),
				new StackTraceElement("valid.Class1", "method", "file", -1),
				new StackTraceElement("valid.Class2", "method", "file", 1)
		};

		StackTraceElement[] causeStackTraceElements = new StackTraceElement[] {
				new StackTraceElement("valid.CauseClass1", "method", "file", 1),
				new StackTraceElement("sun.reflect.SomeClass", "method", "file", 1),
				new StackTraceElement("java.lang.reflect.SomeClass", "method", "file", 1),
				new StackTraceElement("org.codehaus.groovy.runtime.SomeClass", "method", "file", 1),
				new StackTraceElement("org.codehaus.groovy.reflection.SomeClass", "method", "file", 1),
				new StackTraceElement("groovy.lang.SomeClass", "method", "file", 1),
				new StackTraceElement("valid.Class1", "method", null, 1),
				new StackTraceElement("valid.Class1", "method", "file", -1),
				new StackTraceElement("valid.Class2", "method", "file", 1)
		};
		Throwable cause = new Throwable();
		cause.setStackTrace(causeStackTraceElements);
		Throwable parent = new Throwable(cause);
		parent.setStackTrace(parentStackTraceElements);

		ThrowableProxy decoratedProxy = new ThrowableProxy(parent);
		assertEquals(0, decoratedProxy.getCommonFrames());
		assertEquals(8, decoratedProxy.getCause().getCommonFrames());

		StackTraceCleansedThrowableProxy proxy = new StackTraceCleansedThrowableProxy(decoratedProxy);
		assertEquals(0, proxy.getCommonFrames());
		assertEquals(1, proxy.getCause().getCommonFrames());
	}
}
