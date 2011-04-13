package uk.org.lidalia.logging.groovy;

import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.classic.spi.StackTraceElementProxy;

import java.util.LinkedList;
import java.util.List;

class StackTraceCleansedThrowableProxy implements IThrowableProxy {

	private final IThrowableProxy decoratedThrowable;
	private final IThrowableProxy cleansedCause;
	private final StackTraceElementProxy[] cleansedStackTraceElementProxyArray;
	private final int commonFrames;

	StackTraceCleansedThrowableProxy(final IThrowableProxy decoratedThrowable) {
		this(null, decoratedThrowable);
	}

	private StackTraceCleansedThrowableProxy(final StackTraceCleansedThrowableProxy parent, final IThrowableProxy decoratedThrowable) {
		this.decoratedThrowable = decoratedThrowable;
		this.cleansedStackTraceElementProxyArray = cleanse(decoratedThrowable.getStackTraceElementProxyArray());
		if (decoratedThrowable.getCause() == null) {
			cleansedCause = null;
		} else {
			cleansedCause = new StackTraceCleansedThrowableProxy(this, decoratedThrowable.getCause());
		}
		this.commonFrames = findNumberOfCommonFrames(parent);
	}

	private static StackTraceElementProxy[] cleanse(final StackTraceElementProxy[] stackTraceElementProxies) {
		final List<StackTraceElementProxy> newTrace = new LinkedList<StackTraceElementProxy>();
		for (StackTraceElementProxy stackTraceElement : stackTraceElementProxies) {
			if (isApplicationClass(stackTraceElement.getStackTraceElement())) {
				newTrace.add(stackTraceElement);
			}
		}

		final StackTraceElementProxy[] clean = new StackTraceElementProxy[newTrace.size()];
		newTrace.toArray(clean);
		return clean;
	}

	private static boolean isApplicationClass(final StackTraceElement stackTraceElement) {
		final String className = stackTraceElement.getClassName();
		return (!className.startsWith("sun.reflect.")
				&& !className.startsWith("java.lang.reflect.")
				&& !className.startsWith("org.codehaus.groovy.runtime.")
				&& !className.startsWith("org.codehaus.groovy.reflection.")
				&& !className.startsWith("groovy.lang.")
				&& stackTraceElement.getFileName() != null
				&& stackTraceElement.getLineNumber() >= 0
		);
	}

	private int findNumberOfCommonFrames(StackTraceCleansedThrowableProxy parent) {
		if (parent == null) {
			return 0;
		}
		StackTraceElementProxy[] stackTraceElementProxies = getStackTraceElementProxyArray();
		StackTraceElementProxy[] parentStackTraceElementProxies = parent.getStackTraceElementProxyArray();
    int steIndex = getStackTraceElementProxyArray().length - 1;
    int parentIndex = parentStackTraceElementProxies.length - 1;
    int count = 0;
    while (steIndex >= 0 && parentIndex >= 0) {
      StackTraceElementProxy ste = getStackTraceElementProxyArray()[steIndex];
      StackTraceElementProxy otherSte = parentStackTraceElementProxies[parentIndex];
      if (ste.equals(otherSte)) {
        count++;
      } else {
        break;
      }
      steIndex--;
      parentIndex--;
    }
    return count;
  }

	public String getMessage() {
		return decoratedThrowable.getMessage();
	}

	public String getClassName() {
		return decoratedThrowable.getClassName();
	}

	public StackTraceElementProxy[] getStackTraceElementProxyArray() {
		return cleansedStackTraceElementProxyArray;
	}

	public int getCommonFrames() {
		return commonFrames;
	}

	public IThrowableProxy getCause() {
		return cleansedCause;
	}
}
