package com.slawomirkaczmarek.jalive.basic.controllers;

import com.slawomirkaczmarek.jalive.basic.compilers.java.InMemoryJavaCompiler;
import com.slawomirkaczmarek.jalive.basic.enums.HttpMethod;
import com.slawomirkaczmarek.jalive.basic.enums.ResourceType;
import com.slawomirkaczmarek.jalive.basic.http.RESTClient;
import com.slawomirkaczmarek.jalive.basic.interfaces.ResourceDefinition;
import com.slawomirkaczmarek.jalive.basic.interfaces.Runnable;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Font;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RESTCallSlideLayoutController extends LayoutController {

    private enum Resource implements ResourceDefinition {
        BACKGROUND_IMAGE(ResourceType.EXTERNAL, Image.class),
        ADDRESS(ResourceType.INTERNAL, String.class),
        REQUESTS(ResourceType.EXTERNAL, String.class);

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

    private static final String PAGE_MARKER = "///";
    private static final String NEW_LINE = "\n";

    @FXML
    private StackPane background;

    @FXML
    private VBox canvas;

    @FXML
    private TextField address;

    @FXML
    private Pagination pagination;

    @FXML
    private Button executeButton;

    @FXML
    private RadioButton getRadioButton;

    @FXML
    private RadioButton postRadioButton;

    @FXML
    private RadioButton putRadioButton;

    @FXML
    private RadioButton deleteRadioButton;

    @FXML
    private Label result;
    
    private List<TextArea> requestControls;

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
            case ADDRESS:
            	address.setText((String) value);
            	break;
            case REQUESTS:
            	prepareRequests((String) value);
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
        return getClass().getClassLoader().getResource("layouts/RESTCallSlideLayout.fxml");
    }

    @Override
    protected void setIds(String name) {
        background.setId(name + background.getId());
        address.setId(name + address.getId());
        pagination.setId(name + pagination.getId());
        executeButton.setId(name + executeButton.getId());
        getRadioButton.setId(name + getRadioButton.getId());
        postRadioButton.setId(name + postRadioButton.getId());
        putRadioButton.setId(name + putRadioButton.getId());
        deleteRadioButton.setId(name + deleteRadioButton.getId());
        result.setId(name + result.getId());
    }

    @FXML
    private void executeButtonMouseClickedHandler(MouseEvent event) {
        String url = address.getText();
        HttpMethod method = null;
        if (getRadioButton.isSelected()) {
            method = HttpMethod.GET;
        } else if (postRadioButton.isSelected()) {
            method = HttpMethod.POST;
        } else if (putRadioButton.isSelected()) {
            method = HttpMethod.PUT;
        } else if (deleteRadioButton.isSelected()) {
            method = HttpMethod.DELETE;
        }
        String content = requestControls.get(pagination.getCurrentPageIndex()).getText();
        RESTClient client = new RESTClient();
        String result = null;
        try {
            result = client.executeRequest(url, method, content);
        } catch (Exception e) {
            StringWriter stringWriter = new StringWriter();
            PrintWriter printWriter = new PrintWriter(stringWriter);
            e.printStackTrace(printWriter);
            result = stringWriter.toString();
        }
        this.result.setText(result);
    }
    
    private void prepareRequests(String requests) {
    	requestControls = new ArrayList<>();
        String lines[] = requests.split("\\r\\n|\\n|\\r");
        boolean onPage = false;
        StringBuilder pageBuilder = null;
        for (String line : lines) {
            if (PAGE_MARKER.equals(line.trim())) {
                if (onPage) {
                    TextArea requestControl = new TextArea();
                    requestControl.setFocusTraversable(false);
                    requestControl.setFont(Font.font("Consolas", 16.0));
                    requestControl.setText(pageBuilder.toString());
                    requestControls.add(requestControl);
                } else {
                    pageBuilder = new StringBuilder();
                }
                onPage = !onPage;
            } else {
                if (onPage) {
                    pageBuilder.append(line).append(NEW_LINE);
                }
            }
        }
    	pagination.setPageFactory((pageNo) -> {
    		return pageNo < requestControls.size() ? requestControls.get(pageNo) : null;
    	});
    	pagination.setPageCount(requestControls.size());
    }
}
