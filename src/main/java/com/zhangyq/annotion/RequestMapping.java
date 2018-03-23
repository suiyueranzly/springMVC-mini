package com.zhangyq.annotion;

import java.lang.annotation.*;

/**
 * Created by zhangyq on 2017/8/25.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestMapping {
    String value() default "";
}
