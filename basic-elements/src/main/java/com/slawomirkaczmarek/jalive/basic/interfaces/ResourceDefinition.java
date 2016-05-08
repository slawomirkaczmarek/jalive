package com.slawomirkaczmarek.jalive.basic.interfaces;

import com.slawomirkaczmarek.jalive.basic.enums.ResourceType;

public interface ResourceDefinition {

    String getId();
    ResourceType getType();
    Class<?> getResourceClass();
}
