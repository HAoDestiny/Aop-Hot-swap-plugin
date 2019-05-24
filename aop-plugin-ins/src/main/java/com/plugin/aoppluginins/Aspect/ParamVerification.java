package com.plugin.aoppluginins.Aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * Created by DESTINY on 2019/4/28.
 */

@Aspect
@Component
public class ParamVerification {

    @Pointcut("execution (* com.plugin.aoppluginins.controller.TestController.*(..))")
    public void Verify() {
    }


    @Before("Verify()")
    public void doBefore(JoinPoint joinPoint) {
        // 这里不用实现操作 操作已转移到了aop插件工具包 plugin-ins-1.0.jar包  进行了热加载
    }


}
