package com.zhangyq.annotion;

import java.lang.annotation.*;

/**
 * 此注解用来将拥有该注解的bean的变量实例注入进去
 * Created by zhangyq on 2017/8/25.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Qualifier {
    String value() default "";
}
