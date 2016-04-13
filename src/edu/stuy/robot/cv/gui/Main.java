package edu.stuy.robot.cv.gui;

import java.io.ByteArrayInputStream;
import java.util.HashMap;

import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Main extends Application {
    private TabPane root;
    private Scene scene;
    private HashMap<Integer, ControlsController> tabs = new HashMap<Integer, ControlsController>();
    private ModuleRunner moduleRunner = new ModuleRunner();
    private HashMap<String, ImageFrame> images = new HashMap<String, ImageFrame>();

    private void quit() {
        Platform.exit();
        System.exit(0);
    }

    public synchronized void postImage(Mat m, String label, VisionModule requester) {
        String key = requester.hashCode() + label;
        // Convert raw image to PNG
        MatOfByte buffer = new MatOfByte();
        Imgcodecs.imencode(".png", m, buffer);
        Image image = new Image(new ByteArrayInputStream(buffer.toArray()));
        // Check if an ImageFrame already exists
        ImageFrame existingFrame = images.get(key);
        if (existingFrame == null) {
            VBox container = new VBox();
            container.setAlignment(Pos.CENTER);
            ImageView imageView = new ImageView(image);
            Text text = new Text(label);
            text.getStyleClass().add("image-label");
            container.getChildren().addAll(imageView, text);
            images.put(key, new ImageFrame(imageView, text));
            Platform.runLater(() -> {
                tabs.get(requester.hashCode()).flowPane.getChildren().add(container);
            });
            container.setOnMouseClicked((event) -> {
                if (event.getButton().equals(MouseButton.PRIMARY)) {
                    if (event.getClickCount() == 2) {
                        ImageViewer.getInstance().showImage(label, imageView);
                    }
                }
            });
        } else {
            // Update the existing ImageFrame
            Platform.runLater(() -> {
                existingFrame.imageView.setImage(image);
                existingFrame.imageView.toFront();
                existingFrame.label.toFront();
            });
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

    private class ImageFrame {
        private ImageView imageView;
        private Text label;

        public ImageFrame(ImageView imageView, Text label) {
            this.imageView = imageView;
            this.label = label;
        }
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/main.fxml"));
            root = loader.load();
            scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("css/main.css").toString());

            CaptureSource cs = new DeviceCaptureSource(1); // Generally a webcam is port 0 and the LifeCam is 1
            VisionModule vm = new StuyVisionModule();
            FXMLLoader tabLoader = new FMLLoader(getClass().getResource("fxml/module_main.fxml"));
            final SplitPane moduleContainer = tabLoader.load();
            ControlsController ctlr = tabLoader.getController();
            ctlr.setup(vm);
            tabs.put(vm.hashCode(), ctlr);

            new Thread(new Runnable() {
                    @Override
                    public void run() {
                        for (;;) {
                            Mat frame = cs.read();
                            if (frame == null) {
                                cs.reinitializeCaptureSource();
                                DebugPrinter.println("Looping capture source");
                            } else {
                                new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            vm.run(this, frame);
                                        }
                                    }).start();
                            }
                        }
                    }
                }).start();
            Thread.sleep(1000 / 30);
        }
    }
}
