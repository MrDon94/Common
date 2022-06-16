package com.github.common.util.parse;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 参数自动解析KEY
 * <pre>
 * <code>
 *  BindKey(value="name')
 *  String mName;
 *  BindKey(value="ID')
 *  int id;
 * </code>
 * </pre>
 */
@Inherited
@Retention(RUNTIME)
@Target(FIELD)
public @interface BindKey {
    /**
     * value 默认为参数名称
     */
    String value();
}