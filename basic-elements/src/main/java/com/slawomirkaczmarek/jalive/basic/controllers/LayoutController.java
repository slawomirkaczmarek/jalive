package com.slawomirkaczmarek.jalive.basic.controllers;

import com.slawomirkaczmarek.jalive.basic.interfaces.ResourceDefinition;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public abstract class LayoutController {

    private Node layout;

    public void init(String name) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getLocation());
        loader.setController(this);
        layout = loader.load();
        setIds(name);
    }

    public Node getLayout() {
        return layout;
    }

    public abstract List<ResourceDefinition> getResourcesList();
    public abstract void setResource(String id, Object value);
    public abstract void setWidth(double width);
    public abstract void setHeight(double height);
    protected abstract URL getLocation();
    protected abstract void setIds(String name);
}
