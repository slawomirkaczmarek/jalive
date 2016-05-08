package com.slawomirkaczmarek.jalive.elements;

public interface Presentation extends Element {
    void addResource(String name, String path);
    void addSlide(int index, Slide slide);
    void addStylesheet(String stylesheet);
    void nextSlide();
    void previousSlide();
    void goToSlide(int number);
}
