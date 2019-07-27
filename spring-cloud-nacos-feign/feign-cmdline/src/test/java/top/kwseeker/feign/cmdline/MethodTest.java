package top.kwseeker.feign.cmdline;

import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.Date;

public class MethodTest {

    @Test
    public void declaringClassTest() {
        Method[] methods = ClassForTest.class.getDeclaredMethods();
        Assert.assertEquals(ClassForTest.class, methods[0].getDeclaringClass());
        Assert.assertEquals(ClassForTest.class, methods[1].getDeclaringClass());
    }

    private static class ClassForTest {
        public String getClassName() {
            return this.getClass().getName();
        }

        public Date getCurrentDate() {
            return new Date();
        }
    }
}
