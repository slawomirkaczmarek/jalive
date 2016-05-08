package com.slawomirkaczmarek.jalive.basic.elements;

import com.slawomirkaczmarek.jalive.basic.builders.LayoutBuilder;
import com.slawomirkaczmarek.jalive.basic.controllers.LayoutController;
import com.slawomirkaczmarek.jalive.basic.enums.LayoutType;
import com.slawomirkaczmarek.jalive.elements.Slide;
import com.slawomirkaczmarek.jalive.exceptions.ParameterException;
import com.slawomirkaczmarek.jalive.resources.ResourceProvider;
import javafx.scene.Node;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class BasicSlide implements Slide {

    private static final String PARAMETER_LAYOUT = "LAYOUT";

    private String name;
    private Map<String, String> parameters = new HashMap<>();
    private LayoutController layoutController;

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void setParameter(String name, String value) throws ParameterException {
        parameters.put(name, value);
    }

    @Override
    public void build(ResourceProvider resourceProvider) {
        try {
            layoutController = LayoutBuilder.buildLayout(LayoutType.valueOf(parameters.get(PARAMETER_LAYOUT)), name, parameters, resourceProvider);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setWidth(double width) {
        layoutController.setWidth(width);
    }

    @Override
    public void setHeight(double height) {
        layoutController.setHeight(height);
    }

    @Override
    public Node getLayout() {
        return layoutController.getLayout();
    }
}
