package org.spring.training.boot.infrastructure.external.bpp;

import org.apache.commons.lang3.StringUtils;
import org.spring.training.boot.infrastructure.external.annotation.Trimmed;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;

public class TrimmedAnnotationBeanPostProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class<?> beanClass = bean.getClass();
        Trimmed trimmedAnnotation = beanClass.getAnnotation(Trimmed.class);
        if (trimmedAnnotation != null) {
            return enhanceBean(beanClass, trimmedAnnotation);
        }
        return bean;
    }

    private Object enhanceBean(Class<?> beanClass, Trimmed annotation) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(beanClass);
        enhancer.setClassLoader(beanClass.getClassLoader());

        MethodInterceptor methodInterceptor = ((instance, method, args, methodProxy) -> {
            if (annotation.arguments()) {
                for (int i = 0; i < args.length; i++) {
                    Object arg = args[i];
                    args[i] = tryTrim(arg);
                }
            }

            Object returnValue = methodProxy.invokeSuper(instance, args);
            if (annotation.returnValue()) {
                returnValue = tryTrim(returnValue);
            }

            return returnValue;
        });

        enhancer.setCallback(methodInterceptor);
        return enhancer.create();
    }

    private Object tryTrim(Object value) {
        if (value instanceof String str) {
            if (!StringUtils.isEmpty(str)) {
                return str.trim();
            }
        }
        return value;
    }
}
