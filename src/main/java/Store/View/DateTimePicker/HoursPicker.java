package Store.View.DateTimePicker;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class HoursPicker extends GridPane implements Initializable {

    private static final int NUM_BUTTONS = 12;

    private final List<ToggleButton> buttonList = new ArrayList<ToggleButton>(NUM_BUTTONS);

    private final DateTimePickerPopup parentContainer;

    @FXML
    private ToggleGroup hoursToggleGroup;

    @FXML
    private ToggleButton zeroButton;

    @FXML
    private ToggleButton oneButton;

    @FXML
    private ToggleButton twoButton;

    @FXML
    private ToggleButton threeButton;

    @FXML
    private ToggleButton fourButton;

    @FXML
    private ToggleButton fiveButton;

    @FXML
    private ToggleButton sixButton;

    @FXML
    private ToggleButton sevenButton;

    @FXML
    private ToggleButton eightButton;

    @FXML
    private ToggleButton nineButton;

    @FXML
    private ToggleButton tenButton;

    @FXML
    private ToggleButton elevenButton;

    @FXML
    private ToggleButton amPmButton;

    HoursPicker(DateTimePickerPopup parentContainer) {

        this.parentContainer = parentContainer;

        final FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getResource(
                        "HoursPicker.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        buttonList.add(zeroButton);
        buttonList.add(oneButton);
        buttonList.add(twoButton);
        buttonList.add(threeButton);
        buttonList.add(fourButton);
        buttonList.add(fiveButton);
        buttonList.add(sixButton);
        buttonList.add(sevenButton);
        buttonList.add(eightButton);
        buttonList.add(nineButton);
        buttonList.add(tenButton);
        buttonList.add(elevenButton);

        amPmButton.prefHeightProperty().bind(zeroButton.heightProperty().multiply(3).add(getHgap() * 2));
        amPmButton.prefWidthProperty().set(35);

        amPmButton.selectedProperty().addListener(
                new ChangeListener<Boolean>() {
                    @Override
                    public void changed(
                            final ObservableValue<? extends Boolean> observable,
                            final Boolean oldValue,
                            final Boolean newValue) {
                        int offset = 0;

                        if (newValue) {
                            amPmButton.setText("PM");
                            offset = NUM_BUTTONS;
                        } else {
                            amPmButton.setText("AM");
                        }

                        for (int i=0; i < NUM_BUTTONS; i++) {
                            buttonList.get(i).setText(String.format("%02d", i + offset));
                        }
                    }
                });
        int hour = parentContainer.getHour();

        int offset = 0;
        if (hour > 11) {
            amPmButton.setSelected(true);
            offset = -12;
        }
        hoursToggleGroup.selectToggle(buttonList.get(hour + offset));

        hoursToggleGroup.selectedToggleProperty().addListener(
                new ChangeListener<Toggle>() {
                    @Override
                    public void changed(
                            ObservableValue<? extends Toggle> observable,
                            Toggle oldValue,
                            Toggle newValue) {

                        if (newValue == null) {
                            hoursToggleGroup.selectToggle(oldValue);
                        } else {
                            parentContainer.restoreTimePanel();
                        }
                    }
                });
    }

    int getHour() {
        int hour = buttonList.indexOf(hoursToggleGroup.getSelectedToggle());
        if (amPmButton.isSelected()) {
            hour += 12;
        }
        return hour;
    }
}