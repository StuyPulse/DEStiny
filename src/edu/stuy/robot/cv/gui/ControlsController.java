package edu.stuy.robot.cv.gui;

import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.util.ArrayList;

import edu.stuy.robot.cv.util.DebugPrinter;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Slider;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class ControlsController {

    @FXML
    FlowPane flowPane;
    @FXML
    VBox controlsContainer;
    @FXML
    Button restoreDefaults;

    final DecimalFormat formatter = new DecimalFormat("#.###");

    public void setup(VisionModule module) {
        /*ArrayList<VariableWrapper> variables = new ArrayList<>();
        restoreDefaults.setOnAction((event) -> {
            for (VariableWrapper var : variables) {
                var.restoreDefault();
            }
        });
        ArrayList<Node> variableContainers = new ArrayList<>();
        for (Field f : module.getClass().getFields()) {
            Class<?> fType = f.getType();
            if (fType.isAssignableFrom(IntegerSliderVariable.class)) {
                DebugPrinter.println("Found IntegerSliderVariable: " + f.getName());
                IntegerSliderVariable isv = null;
                try {
                    isv = (IntegerSliderVariable) f.get(module);
                }
                catch (IllegalArgumentException | IllegalAccessException e) {
                    e.printStackTrace();
                }
                VBox sliderContainer = new VBox();
                sliderContainer.setAlignment(Pos.CENTER);
                Slider slider = new Slider(isv.MIN, isv.MAX, isv.DEFAULT);
                slider.setShowTickMarks(true);
                Text value = new Text(Integer.toString(isv.DEFAULT));
                value.getStyleClass().add("slider-value");
                Text label = new Text(isv.LABEL);
                label.getStyleClass().add("slider-label");
                final IntegerSliderVariable finalIsv = isv;
                sliderContainer.getChildren().addAll(slider, value, label);
                slider.valueProperty().addListener(new ChangeListener<Number>() {
                    @Override
                    public void changed(ObservableValue<? extends Number> observable, Number oldValue,
                            Number newValue) {
                        int intValue = newValue.intValue();
                        finalIsv.set(intValue);
                        value.setText(Integer.toString(intValue));
                    }
                });
                variables.add(new SliderVariableWrapper(slider, isv));
                variableContainers.add(sliderContainer);
            }
            else if (fType.isAssignableFrom(DoubleSliderVariable.class)) {
                DebugPrinter.println("Found DoubleSliderVariable: " + f.getName());
                DoubleSliderVariable dsv = null;
                try {
                    dsv = (DoubleSliderVariable) f.get(module);
                }
                catch (IllegalArgumentException | IllegalAccessException e) {
                    e.printStackTrace();
                }
                VBox sliderContainer = new VBox();
                sliderContainer.setAlignment(Pos.CENTER);
                Slider slider = new Slider(dsv.MIN, dsv.MAX, dsv.DEFAULT);
                slider.setShowTickMarks(true);
                Text value = new Text(formatter.format(dsv.DEFAULT));
                value.getStyleClass().add("slider-value");
                Text label = new Text(dsv.LABEL);
                label.getStyleClass().add("slider-label");
                final DoubleSliderVariable finalDsv = dsv;
                sliderContainer.getChildren().addAll(slider, value, label);
                slider.valueProperty().addListener(new ChangeListener<Number>() {
                    @Override
                    public void changed(ObservableValue<? extends Number> observable, Number oldValue,
                            Number newValue) {
                        double doubleValue = newValue.doubleValue();
                        finalDsv.set(doubleValue);
                        value.setText(formatter.format(doubleValue));
                    }
                });
                variables.add(new SliderVariableWrapper(slider, finalDsv));
                variableContainers.add(sliderContainer);
            }
            else if (fType.isAssignableFrom(BooleanVariable.class)) {
                DebugPrinter.println("Found BooleanVariable: " + f.getName());
                BooleanVariable bv = null;
                try {
                    bv = (BooleanVariable) f.get(module);
                }
                catch (IllegalArgumentException | IllegalAccessException e) {
                    e.printStackTrace();
                }
                VBox checkBoxContainer = new VBox();
                checkBoxContainer.setAlignment(Pos.CENTER);
                CheckBox checkBox = new CheckBox(bv.LABEL);
                checkBox.setSelected(bv.DEFAULT);
                checkBox.getStyleClass().add("boolean-label");
                final BooleanVariable finalBv = bv;
                checkBoxContainer.getChildren().addAll(checkBox);
                checkBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
                    @Override
                    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue,
                            Boolean newValue) {
                        boolean booleanValue = newValue.booleanValue();
                        finalBv.setValue(booleanValue);
                    }
                });
                variables.add(new BooleanVariableWrapper(checkBox, finalBv));
                variableContainers.add(checkBoxContainer);
            }
        }
        Platform.runLater(() -> {
            controlsContainer.getChildren().addAll(variableContainers);
        });*/
    }

    private abstract class VariableWrapper {
        public abstract void restoreDefault();
    }

    private class SliderVariableWrapper extends VariableWrapper {
        private Slider slider;
        private NumberVariable sliderVariable;

        private SliderVariableWrapper(Slider slider, NumberVariable sliderVariable) {
            this.slider = slider;
            this.sliderVariable = sliderVariable;
        }

        public void restoreDefault() {
            sliderVariable.restoreDefault();
            slider.setValue(sliderVariable.getValue().doubleValue());
        }
    }

    private class BooleanVariableWrapper extends VariableWrapper {
        private CheckBox checkBox;
        private BooleanVariable booleanVariable;

        private BooleanVariableWrapper(CheckBox checkBox, BooleanVariable booleanVariable) {
            this.checkBox = checkBox;
            this.booleanVariable = booleanVariable;
        }

        public void restoreDefault() {
            booleanVariable.restoreDefault();
            checkBox.setSelected(booleanVariable.getValue());
        }
    }
}
