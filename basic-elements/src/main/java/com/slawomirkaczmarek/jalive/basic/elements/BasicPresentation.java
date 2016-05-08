package com.slawomirkaczmarek.jalive.basic.elements;

import com.slawomirkaczmarek.jalive.elements.Presentation;
import com.slawomirkaczmarek.jalive.elements.Slide;
import com.slawomirkaczmarek.jalive.exceptions.ParameterException;
import com.slawomirkaczmarek.jalive.resources.ResourceProvider;
import javafx.animation.PathTransition;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.PathElement;
import javafx.util.Duration;

import java.util.*;

public class BasicPresentation implements Presentation, ResourceProvider {

    private String name;
    private ResourceProvider resourceProvider;
    private Map<String, String> resources;
    private List<Slide> slides;
    private GridPane layout;
    private double screenWidth;
    private double x;
    private double y;
    private int currentSlide;

    public BasicPresentation() {
        resources = new HashMap<>();
        slides = new ArrayList<>();
        currentSlide = 0;
    }

    @Override
    public void addResource(String name, String path) {
        resources.put(name, path);
    }

    @Override
    public void addSlide(int index, Slide slide) {
        slides.add(index, slide);
    }

    @Override
    public void addStylesheet(String stylesheet) {
        layout.getStylesheets().add(stylesheet);
    }

    @Override
    public void setWidth(double width) {
        double layoutWidth = width * slides.size();
        screenWidth = width;
        x = layoutWidth / 2.0;
        layout.setPrefWidth(layoutWidth);
        for (Slide slide : slides) {
            StackPane stackPane = (StackPane) slide.getLayout().getParent();
            stackPane.setPrefWidth(width);
            slide.setWidth(width);
        }
        layout.setTranslateX(currentSlide * screenWidth * -1.0);
    }

    @Override
    public void setHeight(double height) {
        y = height / 2.0;
        layout.setPrefHeight(height);
        for (Slide slide : slides) {
            StackPane stackPane = (StackPane) slide.getLayout().getParent();
            stackPane.setPrefHeight(height);
            slide.setHeight(height);
        }
        layout.setTranslateY(0.0);
    }

    @Override
    public void nextSlide() {
        goToSlide(currentSlide + 1);
    }

    @Override
    public void previousSlide() {
        goToSlide(currentSlide - 1);
    }

    @Override
    public void goToSlide(int number) {
        if (number > -1 && number < slides.size() && number != currentSlide) {
            switchSlide(currentSlide, number);
            currentSlide = number;
        }
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void setParameter(String name, String value) throws ParameterException { }

    @Override
    public void build(ResourceProvider resourceProvider) {
        this.resourceProvider = resourceProvider;
        layout = new GridPane();
        int index = 0;
        for (Slide slide : slides) {
            slide.build(this);
            Node slideLayout = slide.getLayout();
            StackPane stackPane = new StackPane(slideLayout);
            layout.addColumn(index++, stackPane);
        }
    }

    @Override
    public Node getLayout() {
        return layout;
    }

    @Override
    public Object getResource(String path, Class<?> clazz) {
        Object result = null;
        if (resources.containsKey(path) && resourceProvider != null) {
            result = resourceProvider.getResource(resources.get(path), clazz);
        }
        return result;
    }

    private double calculateSlideXPosition(int number) {
        return x - number * screenWidth;
    }

    private void switchSlide(int from, int to) {
        PathElement[] pathElements = {
                new MoveTo(calculateSlideXPosition(from), y),
                new LineTo(calculateSlideXPosition(to), y)
        };
        Path path = new Path();
        path.getElements().addAll(pathElements);
        PathTransition transition = new PathTransition();
        transition.setNode(layout);
        transition.setPath(path);
        transition.setDuration(new Duration(600));
        transition.play();
    }
}
