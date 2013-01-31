package com.mmoscovich.fixtures.test.annotation;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import com.mmoscovich.fixtures.config.FixtureLoaderConfig;

@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface FixtureGroup {
	String value() default FixtureLoaderConfig.DEFAULT_GROUP_NAME;
	String version() default "default";
}
