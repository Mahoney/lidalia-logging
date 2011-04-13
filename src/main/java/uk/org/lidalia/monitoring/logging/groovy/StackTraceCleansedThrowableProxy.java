package uk.org.lidalia.monitoring.logging.groovy;

import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.classic.spi.StackTraceElementProxy;

import java.util.LinkedList;
import java.util.List;

class StackTraceCleansedThrowableProxy implements IThrowableProxy {

    private final IThrowableProxy realThrowable;
    private final IThrowableProxy cleansedCause;
    private final StackTraceElementProxy[] cleansedStackTraceElementProxyArray;

    StackTraceCleansedThrowableProxy(IThrowableProxy realThrowable) {
        this.realThrowable = realThrowable;
        if (realThrowable.getCause() == null) {
			cleansedCause = null;
		} else {
			cleansedCause= new StackTraceCleansedThrowableProxy(realThrowable.getCause());
		}
        this.cleansedStackTraceElementProxyArray = cleanse(realThrowable.getStackTraceElementProxyArray());
    }

    private static StackTraceElementProxy[] cleanse(StackTraceElementProxy[] stackTraceElementProxies) {
        List<StackTraceElementProxy> newTrace = new LinkedList<StackTraceElementProxy>();
        for (StackTraceElementProxy stackTraceElement : stackTraceElementProxies) {
            if (isApplicationClass(stackTraceElement.getStackTraceElement())) {
                newTrace.add(stackTraceElement);
            }
        }

        StackTraceElementProxy[] clean = new StackTraceElementProxy[newTrace.size()];
        newTrace.toArray(clean);
        return clean;
    }

    private static boolean isApplicationClass(StackTraceElement stackTraceElement) {
        return (!stackTraceElement.getClassName().startsWith("sun.reflect")
            && !stackTraceElement.getClassName().startsWith("java.lang.reflect")
            && !stackTraceElement.getClassName().startsWith("org.codehaus.groovy.runtime")
            && !stackTraceElement.getClassName().startsWith("org.codehaus.groovy.reflection")
            && !stackTraceElement.getClassName().startsWith("groovy.lang")
            && stackTraceElement.getFileName() != null
        );
    }

    public String getMessage() {
        return realThrowable.getMessage();
    }

    public String getClassName() {
        return realThrowable.getClassName();
    }

    public StackTraceElementProxy[] getStackTraceElementProxyArray() {
        return cleansedStackTraceElementProxyArray;
    }

    public int getCommonFrames() {
        return realThrowable.getCommonFrames();
    }

    public IThrowableProxy getCause() {
        return cleansedCause;
    }
}
