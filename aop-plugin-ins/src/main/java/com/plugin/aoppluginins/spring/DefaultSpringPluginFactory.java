package com.plugin.aoppluginins.spring;

import com.alibaba.fastjson.JSON;
import com.plugin.aoppluginins.entity.Plugin;
import com.plugin.aoppluginins.entity.PluginConfig;
import org.aopalliance.aop.Advice;
import org.apache.commons.io.IOUtils;
import org.springframework.aop.Advisor;
import org.springframework.aop.framework.Advised;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * aop 是对类的增强 增强后的类都实现了Advised的接口 --->通知对象
 *
 * 插件提供的是一个实现了aop中的一个Advice的接口的一个通知
 * Created by DESTINY on 2019/5/24.
 */

@Service
public class DefaultSpringPluginFactory implements ApplicationContextAware {

    @Value("classpath:pluginConfig.json")
    private Resource areaRes;

    private ApplicationContext applicationContext;

    private Map<String, PluginConfig> configMap = new HashMap<>();
    private Map<String, Advice> adviceCache = new HashMap<>();

    // 初始化插件列表
    public Collection<PluginConfig> flushConfigs() throws IOException {
        String config = IOUtils.toString(areaRes.getInputStream(), Charset.forName("UTF-8"));

        Plugin plugins = JSON.parseObject(config, Plugin.class);

        for (PluginConfig pluginConfig : plugins.getConfigs()) {
            configMap.putIfAbsent(pluginConfig.getId(), pluginConfig);
        }

        return configMap.values();
    }

    // 激活插件
    public void activePlugin(String pluginId) {
        if (!configMap.containsKey(pluginId)) {
            throw new RuntimeException(String.format("这个插件不存在id=%s", pluginId));
        }

        PluginConfig pluginConfig = configMap.get(pluginId);
        pluginConfig.setActive(true);

        // 获取String容器中全部bean name
        String[] beanDefinitionNames = applicationContext.getBeanDefinitionNames();

        for (String beanName : beanDefinitionNames) {
            Object bean = applicationContext.getBean(beanName);
            if (bean == this) {
                continue;
            }
            if (null == bean) {
                continue;
            }
            if (!(bean instanceof Advised)) { // 判断是否属于通知对象
                continue;
            }
            if (findAdvised((Advised) bean, pluginConfig.getClassName())) { // 判断增强bean(Advised)中是否已经包含了插件中的通知类(Advice)
                continue;
            }

            Advice advice = null;
            try {
                advice = buildAdvice(pluginConfig);
                ((Advised) bean).addAdvice(advice); // 添加拦截
                System.out.println("插件激活成功");
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("安装失败", e);
            }
        }
    }

    // 取消插件
    public void enablePlugin(String pluginId) {
        if (!configMap.containsKey(pluginId)) {
            throw new RuntimeException(String.format("这个插件不存在id=%s", pluginId));
        }

        PluginConfig pluginConfig = configMap.get(pluginId);
        pluginConfig.setActive(false);

        // 获取String容器中全部bean name
        String[] beanDefinitionNames = applicationContext.getBeanDefinitionNames();

        for (String beanName : beanDefinitionNames) {
            Object bean = applicationContext.getBean(beanName);
            if (bean == this) {
                continue;
            }
            if (null == bean) {
                continue;
            }
            if (!(bean instanceof Advised)) { // 判断是否属于通知对象
                continue;
            }
            if (findAdvised((Advised) bean, pluginConfig.getClassName())) { // 判断增强bean(Advised)中是否已经包含了插件中的通知类(Advice)
                Advice advice = null;
                try {
                    advice = buildAdvice(pluginConfig);
                    ((Advised) bean).removeAdvice(advice); // 添加拦截

                    System.out.println("插件关闭成功");
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new RuntimeException("插件关闭失败", e);
                }
            }
        }
    }

    //////////// 核心代码 动态加载jar///////////
    // 插件安装 通过加载jar包
    public Advice buildAdvice(PluginConfig config) throws Exception {
        if (adviceCache.containsKey(config.getClassName())) {
            return adviceCache.get(config.getClassName());
        }

        // 获取插件jar包路径 转URL
        URL targetUrl = new URL(config.getJarRemoteUrl());

        // 获取jar包加载器
        URLClassLoader classLoader = (URLClassLoader) getClass().getClassLoader();

        boolean isLoader = false;
        for (URL url : classLoader.getURLs()) {
            if (url.equals(targetUrl)) { // 判断当前是否加载了插件jar包
                isLoader = true;
                break;
            }
        }

        // 加载jar包
        if (!isLoader) {
            Method add = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
            add.setAccessible(true); // 打开权限
            add.invoke(classLoader, targetUrl); // 加载本地jar包
        }

        // 加载插件jar包中的实现类，并创建class对象
        Class<?> adviceClass = classLoader.loadClass(config.getClassName());

        adviceCache.put(adviceClass.getName(), (Advice) adviceClass.newInstance());
        return adviceCache.get(adviceClass.getName());
    }

    // 判断aop增强bean(Advised 实现类)中是否已包含插件中的通知类(Advice实现类)
    public boolean findAdvised(Advised advised, String className) {
        boolean ret = false;
        Advisor[] advisors = advised.getAdvisors();
        for (Advisor a : advisors) {
            Advice advice = a.getAdvice();
            if (className.equals(advice.getClass().getName())) {
                ret = true;
                break;
            }
        }
        return ret;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
