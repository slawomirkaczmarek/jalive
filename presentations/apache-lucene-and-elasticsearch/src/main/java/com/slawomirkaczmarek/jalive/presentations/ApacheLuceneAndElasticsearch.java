package com.slawomirkaczmarek.jalive.presentations;

import com.slawomirkaczmarek.jalive.core.PresentationManager;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ApacheLuceneAndElasticsearch extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        PresentationManager presentationManager = new PresentationManager();
        Path path = Paths.get(getClass().getClassLoader().getResource("apache_lucene_and_elasticsearch.jlv").toURI());
        presentationManager.loadPresentation(path);
        presentationManager.loadPresentationViewer(primaryStage);
        primaryStage.show();
    }
}
