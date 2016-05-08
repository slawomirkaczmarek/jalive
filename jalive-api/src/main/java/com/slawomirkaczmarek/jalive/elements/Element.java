package com.slawomirkaczmarek.jalive.elements;

import com.slawomirkaczmarek.jalive.exceptions.ParameterException;
import com.slawomirkaczmarek.jalive.resources.ResourceProvider;
import javafx.scene.Node;

public interface Element {
    void setName(String name);
    void setParameter(String name, String value) throws ParameterException;
    void build(ResourceProvider resourceProvider);
    void setWidth(double width);
    void setHeight(double height);
    Node getLayout();
}
