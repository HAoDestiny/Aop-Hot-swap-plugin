package com.destiny.plugin;

import org.springframework.aop.MethodBeforeAdvice;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Date;

/**
 * Created by DESTINY on 2019/5/24.
 */
public class TestPlugin implements MethodBeforeAdvice {

    private int count;

    protected void count(Method m) {
        System.out.println(this.getClass().getPackage().getName() + " -----> " + m.getName());
        count ++;
    }

    public void before(Method method, Object[] objects, Object o) throws Throwable {
        count(method);
        System.out.println(String.format("The method invoked time=" + new Date().getTime() + ", count = %s",  count) + "args = " + Arrays.toString(objects));
    }
}
