package com.slawomirkaczmarek.jalive.presentation;

import java.util.*;

public class PresentationDefinition extends Definition {

    private Map<String, String> resources;
    private List<SlideDefinition> slides;

    public Map<String, String> getResources() {
        if (resources == null) {
            resources = new HashMap<>();
        }
        return resources;
    }

    public List<SlideDefinition> getSlides() {
        if (slides == null) {
            slides = new ArrayList<>();
        }
        return slides;
    }
}
