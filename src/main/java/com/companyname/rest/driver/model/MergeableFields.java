package com.companyname.rest.driver.model;

import java.lang.reflect.Field;

public class MergeableFields {

    public <T> T merge(T mergeFrom) {
        Class<?> clazz = this.getClass();
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            Object remoteValue = null;
            try {
                remoteValue = field.get(mergeFrom);
                if (null == field.get(this) && null != remoteValue && !MergeableFields.class.isAssignableFrom(field.getType())) {
                    field.set(this, remoteValue);
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Not possible to merge GlobalConfig into " + clazz.getSimpleName(), e);
            }
        }
        return (T) this;
    }

}
