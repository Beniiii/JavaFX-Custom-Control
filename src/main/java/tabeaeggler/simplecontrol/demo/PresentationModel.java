package tabeaeggler.simplecontrol.demo;

import javax.swing.text.SimpleAttributeSet;

import javafx.beans.property.*;
import javafx.scene.paint.Color;

public class PresentationModel {
    // all the properties waiting for being displayed
    private final StringProperty demoTitle = new SimpleStringProperty("Power Station");
    private final IntegerProperty leistung1 = new SimpleIntegerProperty();
    private final IntegerProperty leistung2 = new SimpleIntegerProperty();
    private final IntegerProperty leistung3 = new SimpleIntegerProperty();
    private final IntegerProperty leistung4 = new SimpleIntegerProperty();
    private final IntegerProperty leistungTotal = new SimpleIntegerProperty();
    private final IntegerProperty anzahlLadepunkte = new SimpleIntegerProperty();


    // all getters and setters (generated via "Code -> Generate -> Getter and Setter)
    public String getDemoTitle() {
        return demoTitle.get();
    }
    public StringProperty demoTitleProperty() {
        return demoTitle;
    }
    public void setDemoTitle(String demoTitle) {
        this.demoTitle.set(demoTitle);
    }

    public int getLeistung1() {
        return leistung1.get();
    }
    public void setLeistung1(int leistung1) {
        this.leistung1.set(leistung1);
    }
    public IntegerProperty leistung1Property () {
        return leistung1;
    }
    public int getLeistung2() {
        return leistung2.get();
    }
    public void setLeistung2(int leistung2) {
        this.leistung2.set(leistung2);
    }
    public IntegerProperty leistung2Property () {
        return leistung2;
    }
    public int getLeistung3() {
        return leistung3.get();
    }
    public void setLeistung3(int leistung3) {
        this.leistung3.set(leistung3);
    }
    public IntegerProperty leistung3Property () {
        return leistung3;
    }
    public int getLeistung4() {
        return leistung4.get();
    }
    public void setLeistung4(int leistung4) {
        this.leistung4.set(leistung4);
    }
    public IntegerProperty leistung4Property () {
        return leistung4;
    }

    public int getLeistungTotal() {
        return leistungTotal.get();
    }
    public void setLeistungTotal(int leistungTotal) {
        this.leistungTotal.set(leistungTotal);
    }
    public IntegerProperty leistungTotalProperty () {
        return leistungTotal;
    }

    public int getAnzahlLadepunkte() {
        return anzahlLadepunkte.get();
    }
    public void setAnzahlLadepunkte(int anzahl) {
        this.anzahlLadepunkte.set(anzahl);
    }
    public IntegerProperty anzahlLadepunkteProperty () {
        return anzahlLadepunkte;
    }

}