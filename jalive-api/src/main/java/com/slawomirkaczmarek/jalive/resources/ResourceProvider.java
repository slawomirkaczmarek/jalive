package com.slawomirkaczmarek.jalive.resources;

public interface ResourceProvider {
    Object getResource(String path, Class<?> clazz);
}
