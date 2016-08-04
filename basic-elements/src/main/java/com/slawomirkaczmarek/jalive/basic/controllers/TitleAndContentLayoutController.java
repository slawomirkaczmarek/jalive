package com.slawomirkaczmarek.jalive.basic.controllers;

import com.slawomirkaczmarek.jalive.basic.enums.ResourceType;
import com.slawomirkaczmarek.jalive.basic.interfaces.ResourceDefinition;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;

import java.net.URL;
import java.util.Arrays;
import java.util.List;

public class TitleAndContentLayoutController extends LayoutController {

    private enum Resource implements ResourceDefinition {
        BACKGROUND_IMAGE(ResourceType.EXTERNAL, Image.class),
        TITLE(ResourceType.INTERNAL, String.class),
        CONTENT(ResourceType.INTERNAL, String.class);

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
    private StackPane background;

    @FXML
    private Label title;

    @FXML
    private Label content;

    @FXML
    private GridPane canvas;

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
            case CONTENT:
                content.setText((String) value);
                break;
            case TITLE:
                title.setText((String) value);
                break;
        }
    }

    @Override
    public void setWidth(double width) {
        canvas.setScaleX(width / canvas.getPrefWidth());
    }

    @Override
    public void setHeight(double height) {
        canvas.setScaleY(height / canvas.getPrefHeight());
    }

    @Override
    protected URL getLocation() {
        return getClass().getClassLoader().getResource("layouts/TitleAndContentLayout.fxml");
    }

    @Override
    protected void setIds(String name) {
        background.setId(name + background.getId());
        title.setId(name + title.getId());
        content.setId(name + content.getId());
    }
}
