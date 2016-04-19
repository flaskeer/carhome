package com.hao.util.permission;

import java.lang.annotation.*;

/**
 * Created by user on 2016/4/19.
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequirePermission {



}
