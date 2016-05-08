package com.slawomirkaczmarek.jalive.basic.controllers;

import com.slawomirkaczmarek.jalive.basic.enums.ResourceType;
import com.slawomirkaczmarek.jalive.basic.interfaces.ResourceDefinition;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.layout.*;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BlankLayoutController extends LayoutController {

    private enum Resource implements ResourceDefinition {
        BACKGROUND_IMAGE(ResourceType.EXTERNAL, Image.class);

        private final ResourceType type;
        private final Class<?> resourceClass;

        Resource(ResourceType type, Class<?> resourceClass) {
            this.type = type;
            this.resourceClass = resourceClass;
        }

        @Override
        public String getId() {
            return name();
        }

        @Override
        public ResourceType getType() {
            return type;
        }

        @Override
        public Class<?> getResourceClass() {
            return resourceClass;
        }
    }

    @FXML
    protected Pane background;

    @Override
    public List<ResourceDefinition> getResourcesList() {
        return Arrays.asList(Resource.values());
    }

    @Override
    public void setResource(String id, Object value) {
        Resource resource = Resource.valueOf(id);
        switch (resource) {
            case BACKGROUND_IMAGE:
                BackgroundImage backgroundImage = new BackgroundImage(
                        (Image) value,
                        BackgroundRepeat.NO_REPEAT,
                        BackgroundRepeat.NO_REPEAT,
                        BackgroundPosition.DEFAULT,
                        new BackgroundSize(1.0, 1.0, true, true, false, false));
                background.setBackground(new Background(backgroundImage));
                break;
        }
    }

    @Override
    public void setWidth(double width) { }

    @Override
    public void setHeight(double height) { }

    @Override
    protected URL getLocation() {
        return getClass().getClassLoader().getResource("layouts/BlankLayout.fxml");
    }

    @Override
    protected void setIds(String name) {
        background.setId(name + background.getId());
    }
}
