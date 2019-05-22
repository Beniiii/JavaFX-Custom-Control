package cuie.project.template_simplecontrol.demo;

import cuie.project.template_simplecontrol.PowerStation;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.util.converter.NumberStringConverter;

/**
 * @author Dieter Holz
 * @author Tabea Eggler
 * @author Benjamin Huber
 */
public class DemoPane extends BorderPane {

    private final PresentationModel pm;

    // declare the custom control
    private PowerStation powerStation;
    private TextField leistung1;
    private TextField leistung2;
    private TextField leistung3;
    private TextField leistung4;


    public DemoPane(PresentationModel pm) {
        this.pm = pm;
        initializeControls();
        layoutControls();
        setupBindings();
    }

    private void initializeControls() {
        setPadding(new Insets(10));
        leistung1  = new TextField();
        leistung2  = new TextField();
        leistung3  = new TextField();
        leistung4  = new TextField();
        powerStation = new PowerStation();
    }

    private void layoutControls() {
        VBox controlPane = new VBox(new Label("Set the power:"),
                leistung1, leistung2, leistung3, leistung4);
        controlPane.setPadding(new Insets(0, 50, 0, 50));
        controlPane.setSpacing(10);

        setCenter(powerStation);
        setRight(controlPane);
    }

    private void setupBindings() {
        leistung1.textProperty().bindBidirectional(pm.leistung1Property(), new NumberStringConverter());
        powerStation.leistung1Property().bindBidirectional(pm.leistung1Property());

        leistung2.textProperty().bindBidirectional(pm.leistung2Property(), new NumberStringConverter());
        powerStation.leistung2Property().bindBidirectional(pm.leistung2Property());

        leistung3.textProperty().bindBidirectional(pm.leistung3Property(), new NumberStringConverter());
        powerStation.leistung3Property().bindBidirectional(pm.leistung3Property());

        leistung4.textProperty().bindBidirectional(pm.leistung4Property(), new NumberStringConverter());
        powerStation.leistung4Property().bindBidirectional(pm.leistung4Property());
    }

}
