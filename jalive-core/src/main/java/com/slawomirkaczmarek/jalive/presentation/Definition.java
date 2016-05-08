package com.slawomirkaczmarek.jalive.presentation;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;

public class Definition {

    private String className;
    private String name;
    private Map<String, String> parameters;

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, String> getParameters() {
        if (parameters == null) {
            parameters = new HashMap<>();
        }
        return parameters;
    }
}
