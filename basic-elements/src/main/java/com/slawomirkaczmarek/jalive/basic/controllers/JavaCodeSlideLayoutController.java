package com.slawomirkaczmarek.jalive.basic.controllers;

import com.slawomirkaczmarek.jalive.basic.compilers.java.InMemoryJavaCompiler;
import com.slawomirkaczmarek.jalive.basic.enums.ResourceType;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JavaCodeSlideLayoutController extends LayoutController {

    private enum Resource implements ResourceDefinition {
        BACKGROUND_IMAGE(ResourceType.EXTERNAL, Image.class),
        CLASS_NAME(ResourceType.INTERNAL, String.class),
        SOURCE(ResourceType.EXTERNAL, String.class);

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
    
    private static final String PAGE_NO = "{N}";
    private static final String PAGE_START = "//#" + PAGE_NO;
    private static final String PAGE_END = "///#" + PAGE_NO;
    private static final String PATTERN = "^" + PAGE_START + "$.*^" + PAGE_END + "$";

    @FXML
    private StackPane background;

    @FXML
    private BorderPane canvas;

    @FXML
    private Pagination pagination;

    @FXML
    private Button compileAndRunButton;

    @FXML
    private TitledPane resultPane;

    @FXML
    private Label result;
    
    private List<TextArea> sourceControls;
    
    private String className;
    private String source;

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
            case CLASS_NAME:
            	className = (String) value;
            	break;
            case SOURCE:
            	prepareCode((String) value);
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
        return getClass().getClassLoader().getResource("layouts/JavaCodeSlideLayout.fxml");
    }

    @Override
    protected void setIds(String name) {
        background.setId(name + background.getId());
        pagination.setId(name + pagination.getId());
        compileAndRunButton.setId(name + compileAndRunButton.getId());
        resultPane.setId(name + resultPane.getId());
        result.setId(name + result.getId());
    }

    @FXML
    private void compileAndRunButtonMouseClickedHandler(MouseEvent event) {
    	String sourceCode = source;
    	for (int i = 0; i < sourceControls.size(); i++) {
    		sourceCode = sourceCode.replace(getPageEnd(i), sourceControls.get(i).getText());
    	}
    	String result = null;
    	try {
    		Class<?> clazz = InMemoryJavaCompiler.compile(className, sourceCode);
    		Runnable runnable = (Runnable) clazz.newInstance();
    		result = runnable.run();
    	} catch (Exception e) {
    		StringWriter stringWriter = new StringWriter();
    		PrintWriter printWriter = new PrintWriter(stringWriter);
    		e.printStackTrace(printWriter);
    		result = stringWriter.toString();
    	}
    	this.result.setText(result);
    }
    
    private void prepareCode(String source) {
    	sourceControls = new ArrayList<>();
    	int count = 0;
    	boolean found = false;
    	do {
    		Pattern pattern = Pattern.compile(PATTERN.replaceAll(PAGE_NO, String.valueOf(count)));
    		Matcher matcher = pattern.matcher(source);
    		if ((found = matcher.find())) {
    			String page = matcher.group();
    			String pageEnd = getPageEnd(count);
    			TextArea sourceControl = new TextArea();
    			sourceControl.setFocusTraversable(false);
    			sourceControl.setFont(Font.font("Consolas", 20.0));
    			sourceControl.setText(page.substring(pageEnd.length(), page.length() - pageEnd.length() - 1));
    			sourceControls.add(sourceControl);
    			source = source.replace(page, pageEnd);
    			count++;
    		}
    	} while (found);
    	pagination.setPageFactory((pageNo) -> {
    		return sourceControls.get(pageNo);
    	});
    	pagination.setPageCount(count);
    	this.source = source;
    }
    
    private String getPageEnd(int pageNo) {
    	return PAGE_END.replace(PAGE_NO, String.valueOf(pageNo));
    }
}
