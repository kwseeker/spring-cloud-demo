package top.kwseeker.feign.cmdline;

import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.TypeVariable;

public class GenericClassTest {

    @Test
    public void classTest() {

        GenericParadigmClass<String, Object, String> genericParadigmClass =
                new GenericParadigmClass<>("kwseeker", new Object(), "键值对");
        TypeVariable[] typeVariables = genericParadigmClass.getClass().getTypeParameters();
        Assert.assertEquals(3, typeVariables.length);
        for (TypeVariable tV : typeVariables) {
            System.out.println(tV.getName());
        }
    }

    //定义泛型类型
    private static class GenericParadigmClass<K, V, T> {

        private K key;
        private V value;
        private T description;

        public GenericParadigmClass(K key, V value, T description) {
            this.key = key;
            this.value = value;
            this.description = description;
        }
    }
}
