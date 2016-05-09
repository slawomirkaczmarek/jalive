package com.slawomirkaczmarek.jalive.controller;

import com.slawomirkaczmarek.jalive.core.PresentationManager;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class PresentationViewerController {

    private Stage stage;

    @FXML
    private AnchorPane rootPane;

    @FXML
    private Button fullScreenButton;

    public void init(PresentationManager presentationManager, Stage stage) {
        this.stage = stage;
        stage.setResizable(false);
        stage.fullScreenProperty().addListener((observable, oldValue, newValue) -> {
            fullScreenButton.setVisible(!newValue);
        });
        rootPane.widthProperty().addListener((observable, oldValue, newValue) -> {
            presentationManager.setPresentationWidth(newValue.doubleValue());
        });
        rootPane.heightProperty().addListener((observable, oldValue, newValue) -> {
            presentationManager.setPresentationHeight(newValue.doubleValue());
        });
        rootPane.getChildren().add(presentationManager.getPresentationLayout());
        fullScreenButton.toFront();
        Scene scene = new Scene(rootPane);
        scene.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case SPACE:
                case RIGHT:
                    presentationManager.nextSlide();
                    break;
                case BACK_SPACE:
                case LEFT:
                    presentationManager.previousSlide();
                    break;
            }
        });
        stage.setScene(scene);
    }

    @FXML
    private void fullScreenButtonMouseClickedHandler(MouseEvent event) {
        stage.setFullScreen(true);
    }
}
