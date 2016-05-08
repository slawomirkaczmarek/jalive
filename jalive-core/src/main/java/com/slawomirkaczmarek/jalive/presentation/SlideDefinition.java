package com.slawomirkaczmarek.jalive.presentation;

public class SlideDefinition extends Definition implements Comparable<SlideDefinition> {

    private int number;

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    @Override
    public int compareTo(SlideDefinition o) {
        return Integer.compare(number, o.getNumber());
    }
}
