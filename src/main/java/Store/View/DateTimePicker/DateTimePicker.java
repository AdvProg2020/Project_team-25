package Store.View.DateTimePicker;

import Store.View.SignUpAndLoginMenu;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Popup;
import javafx.stage.Window;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ResourceBundle;

public class DateTimePicker extends HBox implements Initializable {

    private ObjectProperty<LocalDateTime> dateTime;

    private final DateTimeFormatter formatter;

    private final Popup popupContainer;

    private final DateTimePickerPopup popup;

    @FXML
    private TextField textField;

    @FXML
    private Button button;

    public DateTimePicker() {
        this(LocalDateTime.now());
    }

    public DateTimePicker(final LocalDateTime dateTime) {
        this(dateTime, DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT));
    }

    public DateTimePicker(final LocalDateTime dateTime, final DateTimeFormatter formatter) {
        this.dateTime = new SimpleObjectProperty<LocalDateTime>(dateTime);
        this.formatter = formatter;
        this.popupContainer = new Popup();
        this.popup = new DateTimePickerPopup(this);

        final FXMLLoader fxmlLoader = new FXMLLoader(SignUpAndLoginMenu.class.getClassLoader().getResource("FXML/DateTimePicker.fxml"));
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
        textField.setText(formatter.format(dateTime.get()));

        dateTime.addListener(
                (observable, oldValue, newValue) -> {
                    popup.setDate(newValue.toLocalDate());
                    popup.setTime(newValue.toLocalTime());

                    textField.setText(formatter.format(newValue));
                });

        button.prefHeightProperty().bind(textField.heightProperty());

        popupContainer.getContent().add(popup);
        popupContainer.autoHideProperty().set(true);
    }

    public ObjectProperty<LocalDateTime> dateTimeProperty() {
        return dateTime;
    }

    void hidePopup() {
        final LocalDate date = popup.getDate();
        final LocalTime time = popup.getTime();
        dateTime.setValue(LocalDateTime.of(date, time));
        textField.setText(formatter.format(dateTime.get()));
        popupContainer.hide();
    }

    @FXML
    void handleButtonAction(ActionEvent event) {
        if (popupContainer.isShowing()) {
            popupContainer.hide();
        } else {
            final Window window = button.getScene().getWindow();

            final double x = window.getX()
                    + textField.localToScene(0,0).getX()
                    + textField.getScene().getX();
            final double y = window.getY()
                    + button.localToScene(0,0).getY()
                    + button.getScene().getY()
                    + button.getHeight();

            popupContainer.show(this.getParent(), x, y);
        }
    }
}