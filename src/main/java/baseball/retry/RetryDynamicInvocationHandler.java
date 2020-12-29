package baseball.retry;

import baseball.ui.OutputView;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class RetryDynamicInvocationHandler implements InvocationHandler {
    private final Object target;
    private final OutputView outputView;
    private final Map<String, Method> methods = new HashMap<>();

    public RetryDynamicInvocationHandler(Object target, OutputView outputView) {
        this.target = target;
        this.outputView = outputView;

        for (Method method : target.getClass().getDeclaredMethods()) {
            this.methods.put(method.getName(), method);
        }
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Method targetMethod = methods.get(method.getName());
        if (targetMethod.isAnnotationPresent(RetryIfException.class)) {
            return invokeWithRetry(targetMethod, args);
        }
        return method.invoke(target, args);
    }

    private Object invokeWithRetry(Method method, Object[] args) throws Throwable {
        try {
            return method.invoke(target, args);
        } catch (InvocationTargetException e) {
            outputView.printException(e.getCause());
            return invokeWithRetry(method, args);
        }
    }
}
