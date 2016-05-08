package com.slawomirkaczmarek.jalive.core;

import com.slawomirkaczmarek.jalive.controller.PresentationViewerController;
import com.slawomirkaczmarek.jalive.elements.Element;
import com.slawomirkaczmarek.jalive.elements.Presentation;
import com.slawomirkaczmarek.jalive.elements.Slide;
import com.slawomirkaczmarek.jalive.exceptions.ParameterException;
import com.slawomirkaczmarek.jalive.io.PresentationFile;
import com.slawomirkaczmarek.jalive.io.PresentationFiles;
import com.slawomirkaczmarek.jalive.presentation.PresentationDefinition;
import com.slawomirkaczmarek.jalive.presentation.SlideDefinition;
import com.slawomirkaczmarek.jalive.resources.ResourceProvider;
import com.sun.corba.se.spi.orbutil.fsm.Input;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PresentationManager implements ResourceProvider {

    private Path presentationFilePath;
    private PresentationFile presentationFile;
    private Presentation presentation;

    public void loadPresentation(Path path) throws IOException, URISyntaxException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, ParameterException {
        presentationFilePath = path;
        try (PresentationFile presentationFile = PresentationFiles.newPresentationFile(path)) {
            this.presentationFile = presentationFile;
            PresentationDefinition presentationDefinition = presentationFile.readDefinition();
            presentation = newInstance(presentationDefinition.getClassName());
            presentation.setName(presentationDefinition.getName());
            loadParameters(presentation, presentationDefinition.getParameters());
            loadResources(presentationDefinition.getResources());
            loadSlides(presentationDefinition.getSlides());
            presentation.build(this);
            presentation.addStylesheet(presentationFile.readStyle());
        }
        presentationFile = null;
    }

    public void loadPresentationViewer(Stage stage) throws IOException, URISyntaxException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getClassLoader().getResource("layouts/PresentationViewerLayout.fxml"));
        loader.load();
        PresentationViewerController presentationViewerController = loader.getController();
        presentationViewerController.init(this, stage);
    }

    public void setPresentationWidth(double width) {
        presentation.setWidth(width);
    }

    public void setPresentationHeight(double height) {
        presentation.setHeight(height);
    }

    public void nextSlide() {
        presentation.nextSlide();
    }

    public void previousSlide() {
        presentation.previousSlide();
    }

    public Node getPresentationLayout() {
        return presentation.getLayout();
    }

    @Override
    public Object getResource(String path, Class<?> clazz) {
        Object result = null;
        try {
            if (presentationFile == null) {
                try (PresentationFile presentationFile = PresentationFiles.newPresentationFile(presentationFilePath)) {
                    result = loadResource(presentationFile, path, clazz);
                }
            } else {
                result = loadResource(presentationFile, path, clazz);
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return result;
    }

    private void loadParameters(Element element, Map<String, String> parameters) throws ParameterException {
        for (Map.Entry<String, String> parameter : parameters.entrySet()) {
            element.setParameter(parameter.getKey(), parameter.getValue());
        }
    }

    private void loadResources(Map<String, String> resources) {
        for (Map.Entry<String, String> resource : resources.entrySet()) {
            presentation.addResource(resource.getKey(), resource.getValue());
        }
    }

    private void loadSlides(List<SlideDefinition> slideDefinitions) throws NoSuchMethodException, InstantiationException, ParameterException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
        Collections.sort(slideDefinitions, Collections.reverseOrder());
        for (SlideDefinition slideDefinition : slideDefinitions) {
            Slide slide = loadSlide(slideDefinition);
            slide.setName(slideDefinition.getName());
            presentation.addSlide(0, slide);
        }
    }

    private Slide loadSlide(SlideDefinition slideDefinition) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, ParameterException {
        Slide result = newInstance(slideDefinition.getClassName());
        loadParameters(result, slideDefinition.getParameters());
        return result;
    }

    private Object loadResource(PresentationFile presentationFile, String path, Class<?> clazz) throws InvocationTargetException, InstantiationException, IllegalAccessException, IOException {
        Object result = null;
        if (clazz == String.class) {
            result = loadStringResource(presentationFile, path);
        } else {
            result = loadObjectResource(presentationFile, path, clazz);
        }
        return result;
    }

    private String loadStringResource(PresentationFile presentationFile, String path) throws IOException {
        String result;
        InputStream inputStream = presentationFile.readResource(path);
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            result = reader.lines().collect(Collectors.joining("\n"));
        }
        return result;
    }

    private <T> T loadObjectResource(PresentationFile presentationFile, String path, Class<T> clazz) throws IOException, IllegalAccessException, InvocationTargetException, InstantiationException {
        T result = null;
        Class[] parameterTypes = { InputStream.class };
        try {
            Constructor<T> constructor = clazz.getConstructor(parameterTypes);
            try (InputStream inputStream = presentationFile.readResource(path)) {
                Object[] parameters = { inputStream };
                result = constructor.newInstance(parameters);
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return result;
    }

    private <T> T newInstance(String className) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Class clazz = Class.forName(className);
        Constructor constructor = clazz.getConstructor();
        return (T) constructor.newInstance();
    }
}
