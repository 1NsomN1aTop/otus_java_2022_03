package proxy;

import annotation.Log;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;
import static proxy.MyProxy.MethodSignature.methodToSignature;

public class MyProxy {

    private MyProxy(){
    }

    record MethodSignature(String name, String[] params) {
        static MethodSignature methodToSignature(Method m) {
            return new MethodSignature(
                    m.getName(),
                    stream(m.getParameterTypes()).map(Class::toString).toArray(String[]::new)
            );
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            MethodSignature that = (MethodSignature) o;
            return name.equals(that.name) && Arrays.equals(this.params, that.params);
        }

        @Override
        public int hashCode() {
            int result = Objects.hash(name);
            result = 31 * result + Objects.hashCode(Arrays.toString(params));
            return result;
        }
    }

    static Calculator create(Calculator wrapped) {
        InvocationHandler handler = new LoggedMethodInvocationHandler(wrapped);
        return (Calculator) Proxy.newProxyInstance(
                wrapped.getClass().getClassLoader(),
                wrapped.getClass().getInterfaces(),
                handler
        );
    }

    static class LoggedMethodInvocationHandler implements InvocationHandler {
        public static final String METHOD_LOG = "\nexecuted method: %s, param:%s";
        private final Calculator clazz;
        private final Set<MethodSignature> annotatedMethods;

        LoggedMethodInvocationHandler(final Calculator clazz) {
            this.clazz = clazz;
            this.annotatedMethods = getAnnotatedMethods(clazz);
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Exception {
            if (annotatedMethods.contains(methodToSignature(method))) {
                System.out.printf(METHOD_LOG.formatted(method.getName(), Arrays.toString(args)));
            }
            return method.invoke(clazz, args);
        }

        private static Set<MethodSignature> getAnnotatedMethods(Calculator clazz) {
            return stream(clazz.getClass().getMethods())
                    .filter(method -> method.isAnnotationPresent(Log.class))
                    .map(MethodSignature::methodToSignature)
                    .collect(Collectors.toSet());
        }

        @Override
        public String toString() {
            return "DemoInvocationHandler{" +
                    "myClass=" + clazz +
                    '}';
        }
    }
}
