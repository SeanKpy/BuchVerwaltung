package ch.kofmel;


import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import klassen.Kunde;
import klassen.Datenbank;

import java.sql.Connection;
import java.time.LocalDate;

public class NeuerBenutzerFenster extends Stage{

    private TextField eingabeVorname;
    private TextField eingabeNachname;
    private TextField eingabeStrasse;
    private TextField eingabeStrNr;
    private TextField eingabePLZ;
    private TextField eingabeEmail;
    private TextField eingabeTelNr;
    private DatePicker eingabeGeburtsDatum;
    private TextField eingabeLehrer;
    private TextField eingabeKlasse;
    private CheckBox checkBox;



    public NeuerBenutzerFenster(Stage hauptStage) {
        setTitle("Neuer Kunde");
        GridPane gridPane = new GridPane();
        gridPane.setVgap(5);
        gridPane.setHgap(5);
        gridPane.setPadding(new Insets(5));

        setzenLabels(gridPane);
        setzenTextFields(gridPane);
        setzenButtons(gridPane);

        initModality(Modality.WINDOW_MODAL);
        initOwner(hauptStage);

        Scene scene = new Scene(gridPane);
        setMinHeight(400);
        setMinWidth(550);
        setScene(scene);
        show();
    }
    //Funktion für die Labels zu setzen und die Checkbox
    private void setzenLabels(GridPane gp){
        Label schueler = new Label("Schüler/in?");
        checkBox = new CheckBox();
        checkBox.setOnAction(e -> schuelerAktiv());
        Label nameLabel = new Label("Vorname:");
        Label nachNameLabel = new Label("Nachname:");
        Label strasseLabel = new Label("Strasse:");
        Label geburtstagLabel = new Label("Geb. Datum:");
        Label strassenNrLabel = new Label("Nr:");
        Label plzLabel = new Label("PLZ:");
        Label eMailLabel = new Label("E-Mail:");
        Label telNr = new Label("Telefon:");

        Label klassenLabel = new Label("Klasse:");
        Label lehrer = new Label("Klassenlehrer/in");

        gp.add(schueler,0,0);
        gp.add(checkBox,1,0);
        gp.add(nameLabel,0,1);
        gp.add(nachNameLabel,0,2);
        gp.add(geburtstagLabel,0,3);
        gp.add(strasseLabel,0,4);
        gp.add(strassenNrLabel,2,4);
        gp.add(plzLabel,0,5);
        gp.add(eMailLabel,0,6);
        gp.add(telNr,0,7);

        gp.add(klassenLabel,0,8);
        gp.add(lehrer,0,9);

    }
    //Funktion für die CheckBox, wenn es ein Schüler ist gibt es andere Eingaben
    private void schuelerAktiv(){
        eingabeEmail.setDisable(checkBox.isSelected());
        eingabeStrasse.setDisable(checkBox.isSelected());
        eingabeStrNr.setDisable(checkBox.isSelected());
        eingabePLZ.setDisable(checkBox.isSelected());
        eingabeTelNr.setDisable(checkBox.isSelected());

        eingabeKlasse.setDisable(!checkBox.isSelected());
        eingabeLehrer.setDisable(!checkBox.isSelected());
    }
    //Funktion um die TextFields zu initialisieren und zu setzten
    private void setzenTextFields(GridPane gp){
        eingabeVorname = new TextField();
        eingabeNachname = new TextField();
        eingabeStrasse = new TextField();
        eingabeStrNr = new TextField();

        eingabeGeburtsDatum = new DatePicker();

        eingabePLZ = new TextField();
        eingabeEmail = new TextField();
        eingabeTelNr = new TextField();

        eingabeLehrer = new TextField();
        eingabeLehrer.setDisable(true);
        eingabeKlasse = new TextField();
        eingabeKlasse.setDisable(true);

        gp.add(eingabeVorname,1,1);
        gp.add(eingabeNachname,1,2);
        gp.add(eingabeGeburtsDatum,1,3);
        gp.add(eingabeStrasse,1,4);
        gp.add(eingabeStrNr,3,4);
        gp.add(eingabePLZ,1,5);
        gp.add(eingabeEmail,1,6);
        gp.add(eingabeTelNr,1,7);

        gp.add(eingabeKlasse,1,8);
        gp.add(eingabeLehrer,1,9);
    }
    private void setzenButtons(GridPane gp){
        Button speichern = new Button("Speichern");
        speichern.setOnAction(e -> kundeErstellen());
        gp.add(speichern,4,9);
    }

    //Button Funktion um einen neuen Benutzer zu speichern
    //Felder, die Standard mässig nicht aktiv sind, werden mit Strings befüllt.

    private void kundeErstellen(){

        int kundenNr = Datenbank.getBenutzerID();
        String tempLehrer = "Kein Schüler";
        String tempKlasse = "Kein Schüler";

        try{
            String tempVorName = eingabeVorname.getText().isEmpty() ? "Keine Angaben" : eingabeVorname.getText();
            String tempNachName = eingabeNachname.getText().isEmpty() ? "Keine Angaben" : eingabeNachname.getText();
            LocalDate tempGeburiDat = (eingabeGeburtsDatum.getValue() != null) ? eingabeGeburtsDatum.getValue() : LocalDate.now();

            String tempStrasse = eingabeStrasse.getText().isEmpty() ? "Keine Angaben" : eingabeStrasse.getText();
            String tempStrNr = eingabeStrNr.getText().isEmpty() ? "Keine Angaben" : eingabeStrNr.getText();
            String tempPLZ = eingabePLZ.getText().isEmpty() ? "Keine Angabe" : eingabePLZ.getText();
            String tempEmail = eingabeEmail.getText().isEmpty() ? "Keine Angaben" : eingabeEmail.getText();
            String tempTelNr = eingabeTelNr.getText().isEmpty() ? "Keine Angaben" : eingabeTelNr.getText();
            if(checkBox.isSelected()){
                tempLehrer = eingabeLehrer.getText();
                tempKlasse = eingabeKlasse.getText();
            }
            Kunde tempKunde = new Kunde(kundenNr,tempVorName,tempNachName,tempGeburiDat,tempStrasse,tempStrNr,tempPLZ,tempEmail,tempTelNr,tempLehrer,tempKlasse);
            kundeInDBSpeichern(tempKunde);
            leerenTextFelder();

        }
        catch (Exception exception){
            exception.printStackTrace();
            Alert alertNrFehler = new Alert(Alert.AlertType.INFORMATION,"Bei einer Eingabe ist ein Fehler passiert\nBitte nochmal überprüfen",ButtonType.OK);
            alertNrFehler.showAndWait();
        }

    }
    //Funktion die aufgerufen wird nach dem ein neuer Kunde erstellt wurde
    //um ihn in der Datenbank zu speichern
    private void kundeInDBSpeichern(Kunde kunde){
        Connection connection = Datenbank.datenbankVerbinden();
        Datenbank.speichernBenutzer(connection,kunde);
        Datenbank.verbindungSchliessen(connection);
    }

    //Nach erfolgreichen speichern werden die Textfelder geleert
    private void leerenTextFelder(){
        eingabeVorname.clear();
        eingabeNachname.clear();
        eingabeGeburtsDatum.setValue(null);
        eingabeStrasse.clear();
        eingabeStrNr.clear();
        eingabePLZ.clear();
        eingabeEmail.clear();
        eingabeTelNr.clear();
        eingabeLehrer.clear();
        eingabeKlasse.clear();
    }
}
