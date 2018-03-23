package com.zhangyq.annotion;

import java.lang.annotation.*;

/**
 * Created by zhangyq on 2017/8/25.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Service {
    String value() default "";
}
