package com.yjq.programmer.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author 杨杨吖
 * @QQ 823208782
 * @WX yjqi12345678
 * @create 2021-03-26 18:27
 */

/**
 * bean对象工具类
 */
@Component
public class SpringBeanUtil implements ApplicationContextAware{

        //ApplicationContext对象是Spring开源框架的上下文对象实例，在项目运行时自动装载Handler内的所有信息到内存。
        private static ApplicationContext applicationContext;

        @Override
        public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
            if (SpringBeanUtil.applicationContext == null) {
                SpringBeanUtil.applicationContext = applicationContext;
            }
        }

        //获取applicationContext
        public static ApplicationContext getApplicationContext() {
            return applicationContext;
        }

        //通过name获取 Bean.
        public static Object getBean(String name) {
            return getApplicationContext().getBean(name);
        }

        //通过class获取Bean.
        public static <T> T getBean(Class<T> clazz) {
            return getApplicationContext().getBean(clazz);
        }

        //通过name,以及Clazz返回指定的Bean
        public static <T> T getBean(String name, Class<T> clazz) {
            return getApplicationContext().getBean(name, clazz);
        }


}
