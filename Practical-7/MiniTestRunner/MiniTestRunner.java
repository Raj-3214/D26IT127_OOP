import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;

/** Marks a no-arg method as a test, like JUnit's @Test. */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@interface Run {
}

/** The class under test. Only @Run methods should be executed. */
class SampleTests {
    @Run
    void testAddition() {
        System.out.println("  testAddition ran");
    }

    @Run
    void testGreeting() {
        System.out.println("  testGreeting ran");
    }

    @Run
    void testThatFails() {
        System.out.println("  testThatFails ran");
        throw new IllegalStateException("intentional failure");
    }

    void helperMethod() {
        System.out.println("  helperMethod ran (BUG: should not have been called)");
    }

    @Run
    void testWithArgument(int ignored) {
        System.out.println("  testWithArgument ran (BUG: should not have been called)");
    }
}

/** Finds and invokes only the @Run methods of a class. */
public class MiniTestRunner {

    public static void main(String[] args) throws Exception {
        Class<?> testClass = SampleTests.class;
        Object instance = testClass.getDeclaredConstructor().newInstance();

        int ran = 0;
        int failed = 0;

        for (Method method : testClass.getDeclaredMethods()) {
            if (!method.isAnnotationPresent(Run.class)) {
                continue; // not a test
            }
            if (method.getParameterCount() != 0) {
                System.out.println("SKIPPED " + method.getName() + ": @Run methods must have no parameters");
                continue;
            }

            method.setAccessible(true);
            ran++;
            try {
                method.invoke(instance);
            } catch (java.lang.reflect.InvocationTargetException e) {
                failed++;
                System.out.println("FAILED " + method.getName() + ": " + e.getCause());
            }
        }

        System.out.println(ran + " test(s) ran, " + failed + " failed");
    }
}
