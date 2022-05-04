import annotations.After;
import annotations.Before;
import annotations.Test;
import utils.ReflectionHelper;

import java.lang.reflect.Method;
import java.util.*;

import static utils.ReflectionHelper.getMethodStreamByAnnotation;

public class Runner {

    public static void main(String[] args) {
        Runner.runClassWithTests(MyTest.class);
    }

    private static void runClassWithTests(Class<?> testClass) {

        Optional<Method> methodBefore = getMethodStreamByAnnotation(testClass, Before.class).findFirst();
        Optional<Method> methodAfter = getMethodStreamByAnnotation(testClass, After.class).findFirst();
        List<Method> testMethods = getMethodStreamByAnnotation(testClass, Test.class)
                .sorted(Comparator.comparing(Method::getName))
                .toList();

        int successfullTestsCount = 0;
        int failedTestsCount = 0;

        for (Method method : testMethods) {
            System.out.printf(">> %s...\n", method.getAnnotation(Test.class).displayName());

            Object object = ReflectionHelper.instantiate(testClass);

            try {
                methodBefore.ifPresent(value -> ReflectionHelper.callMethod(object, value.getName()));
                ReflectionHelper.callMethod(object, method.getName());
                System.out.println("\t\tтест успешно пройден");
                successfullTestsCount++;
            } catch (Exception e) {
                System.out.printf("\t\tпроизошло исключение: %s%n", e);
                failedTestsCount++;
            } finally {
                methodAfter.ifPresent(value -> callAfterMethod(value, object));
            }
        }

        System.out.printf("\t>> Всего тестов: %s. Успешных: %s. С ошибками: %s\n",
                (successfullTestsCount + failedTestsCount),
                successfullTestsCount,
                failedTestsCount);
    }


    private static void callAfterMethod(Method methodAfter, Object object) {
        try {
            ReflectionHelper.callMethod(object, methodAfter.getName());
        } catch (Exception e) {
            System.out.printf("\t\tпроизошло исключение (after): %s%n", e);
        }
    }
}
