package com.slawomirkaczmarek.jalive.basic.builders;

import com.slawomirkaczmarek.jalive.basic.controllers.*;
import com.slawomirkaczmarek.jalive.basic.enums.LayoutType;
import com.slawomirkaczmarek.jalive.basic.interfaces.ResourceDefinition;
import com.slawomirkaczmarek.jalive.resources.ResourceProvider;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;

import java.io.IOException;
import java.util.Map;

public class LayoutBuilder {

    public static LayoutController buildLayout(LayoutType layoutType, String name, Map<String, String> parameters, ResourceProvider resourceProvider) throws IOException {
        LayoutController result = null;
        switch (layoutType) {
            case BLANK:
                result = new BlankLayoutController();
                break;
            case TITLE:
                result = new TitleSlideLayoutController();
                break;
            case TITLE_AND_CONTENT:
                result = new TitleAndContentLayoutController();
                break;
            case JAVA_SOURCE:
                result = new JavaCodeSlideLayoutController();
                break;
            case REST_CALL:
                result = new RESTCallSlideLayoutController();
                break;
        }
        if (result != null) {
            result.init(name);
            setResources(result, parameters, resourceProvider);
        }
        return result;
    }

    private static void setResources(LayoutController layoutController, Map<String, String> parameters, ResourceProvider resourceProvider) {
        for (ResourceDefinition resourceDefinition : layoutController.getResourcesList()) {
            if (parameters.containsKey(resourceDefinition.getId())) {
                Object resource = null;
                switch (resourceDefinition.getType()) {
                    case EXTERNAL:
                        resource = resourceProvider.getResource(parameters.get(resourceDefinition.getId()), resourceDefinition.getResourceClass());
                        break;
                    case INTERNAL:
                        resource = parameters.get(resourceDefinition.getId());
                        break;
                }
                if (resource != null) {
                    layoutController.setResource(resourceDefinition.getId(), resource);
                }
            }
        }
    }
}
