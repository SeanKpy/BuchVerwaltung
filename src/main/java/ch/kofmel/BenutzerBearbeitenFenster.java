package ch.kofmel;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import klassen.Kunde;
import klassen.Datenbank;

import java.sql.Connection;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class BenutzerBearbeitenFenster extends Stage {
    private final TableView<Kunde> benutzerTabelle;

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
    private CheckBox loeschenCheckBox;
    private List<Kunde> kundeListe;


    public BenutzerBearbeitenFenster(Stage primaryStage){
        setTitle("Benutzer bearbeiten");
        HBox hBox = new HBox();
        hBox.setSpacing(10);
        hBox.setPadding(new Insets(0,10,0,0));

        kundeListeErstellen();
        benutzerTabelle = new TableView<>();
        erstellenDerTabelle(hBox);
        erstellenDesGridPane(hBox);

        Scene scene = new Scene(hBox);
        setScene(scene);
        initModality(Modality.WINDOW_MODAL);
        initOwner(primaryStage);
        sizeToScene();
        show();
    }
    //Kundenliste der Datenbank auslesen
    private void kundeListeErstellen(){
        Connection connection = Datenbank.datenbankVerbinden();
        if(connection != null){
            kundeListe = Datenbank.getBenutzerList(connection);
            Datenbank.verbindungSchliessen(connection);
        }

    }
    //Funktion für die linke Seite zu erstellen mit der TableView,
    //und der Sucheeingabe für die Benutzersuche
    private void erstellenDerTabelle(HBox hBox){
        //Für die komplette linke Seite
        VBox vBox = new VBox();
        vBox.setSpacing(10);

        //Für Such Label und Sucheingabe und Button
        HBox suchBox = new HBox();
        suchBox.setSpacing(10);
        suchBox.setAlignment(Pos.CENTER);
        TextField sucheFeld = new TextField();
        sucheFeld.textProperty().addListener((observable, oldValue, newValue) -> checkEingabe(newValue));
        suchBox.getChildren().add(new Label("Suche:"));
        suchBox.getChildren().add(sucheFeld );
        vBox.getChildren().add(suchBox);

        //Die Colums werden erstellt und die Tabelle erhält die Daten
        setItemsInTabelle();
        TableColumn<Kunde, Integer> columnID = new TableColumn<>("KundeID");
        TableColumn<Kunde, String> columnVorName = new TableColumn<>("Vorname");
        TableColumn<Kunde, String> columnNachName = new TableColumn<>("Nachname");
        TableColumn<Kunde, LocalDate> columnGebDat = new TableColumn<>("Geb. Datum");

        columnGebDat.setCellValueFactory(new PropertyValueFactory<>("geburtsDatum"));
        columnID.setCellValueFactory(new PropertyValueFactory<>("kundenNr"));
        columnVorName.setCellValueFactory(new PropertyValueFactory<>("vorName"));
        columnNachName.setCellValueFactory(new PropertyValueFactory<>("nachName"));

        benutzerTabelle.getColumns().add(columnID);
        benutzerTabelle.getColumns().add(columnVorName);
        benutzerTabelle.getColumns().add(columnNachName);
        benutzerTabelle.getColumns().add(columnGebDat);

        benutzerTabelle.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> anwahlDerEintraege(newValue));
        vBox.getChildren().add(benutzerTabelle);

        hBox.getChildren().add(vBox);
    }

    //Funktion zur Erstellung der ganzen rechten Seite mit allen TextFields,
    //und den dazugehörigen Labels. Eine Checkbox zum Löschen des Benutzers
    private void erstellenDesGridPane(HBox hBox){
        GridPane gridPane = new GridPane();
        VBox vBox = new VBox();
        vBox.setSpacing(10);
        hBox.getChildren().add(vBox);
        vBox.getChildren().add(gridPane);
        gridPane.setHgap(5);
        gridPane.setVgap(5);
        loeschenCheckBox = new CheckBox();

        Button speichern = new Button("Speichern");
        speichern.setOnAction(e -> speichernOderDelet());
        vBox.setAlignment(Pos.CENTER);
        vBox.getChildren().add(speichern);

        Label loeschenLabel = new Label("Löschen?");
        Label vorNameLabel = new Label("Vorname:");
        Label nachNameLabel = new Label("Nachname:");
        Label strasseLabel = new Label("Strasse:");
        Label geburtstagLabel = new Label("Geb. Datum:");
        Label strassenNrLabel = new Label("Nr:");
        Label plzLabel = new Label("PLZ:");
        Label eMailLabel = new Label("E-Mail:");
        Label telNr = new Label("Telefon:");
        Label klassenLabel = new Label("Klasse:");
        Label lehrer = new Label("Klassenlehrer/in");

        eingabeVorname = new TextField();
        eingabeNachname = new TextField();
        eingabeStrasse = new TextField();
        eingabeStrNr = new TextField();
        eingabeGeburtsDatum = new DatePicker();
        eingabePLZ = new TextField();
        eingabeEmail = new TextField();
        eingabeTelNr = new TextField();
        eingabeLehrer = new TextField();
        eingabeKlasse = new TextField();

        gridPane.add(loeschenCheckBox,0,0);
        gridPane.add(vorNameLabel,0,1);
        gridPane.add(nachNameLabel,0,2);
        gridPane.add(geburtstagLabel,0,3);
        gridPane.add(strasseLabel,0,4);
        gridPane.add(strassenNrLabel,2,4);
        gridPane.add(plzLabel,0,5);
        gridPane.add(eMailLabel,0,6);
        gridPane.add(telNr,0,7);
        gridPane.add(klassenLabel,0,8);
        gridPane.add(lehrer,0,9);

        gridPane.add(loeschenLabel,1,0);
        gridPane.add(eingabeVorname,1,1);
        gridPane.add(eingabeNachname,1,2);
        gridPane.add(eingabeGeburtsDatum,1,3);
        gridPane.add(eingabeStrasse,1,4);
        gridPane.add(eingabeStrNr,3,4);
        gridPane.add(eingabePLZ,1,5);
        gridPane.add(eingabeEmail,1,6);
        gridPane.add(eingabeTelNr,1,7);
        gridPane.add(eingabeKlasse,1,8);
        gridPane.add(eingabeLehrer,1,9);


    }
    //Methode die mit dem Suchfeld verbunden ist, sie erhält immer den aktuellen text der im Feld steht.
    //Die Methode überprüft ob die Eingabe im Vor oder Nachname enthalten ist
    private void checkEingabe(String eingabe){
        List<Kunde> durchsuchteListe;
        if(!eingabe.equals("")){
            durchsuchteListe = kundeListe.stream().filter(k -> k.enthalten(eingabe)).collect(Collectors.toList());
            benutzerTabelle.setItems(FXCollections.observableList(durchsuchteListe));
        }
        else{
            setItemsInTabelle();
        }

    }
    private void setItemsInTabelle(){
        benutzerTabelle.setItems(FXCollections.observableList(kundeListe));
    }
    //Setzt die Daten des ausgewählten Benutzers in die TextFelder zum bearbeiten.
    private void anwahlDerEintraege(Kunde zuBearbeitenKunde){
        if(zuBearbeitenKunde != null){
            eingabeVorname.setText(zuBearbeitenKunde.getVorName());
            eingabeNachname.setText(zuBearbeitenKunde.getNachName());
            eingabeStrasse.setText(zuBearbeitenKunde.getStrasse());
            eingabeStrNr.setText(zuBearbeitenKunde.getStrasseNR());
            eingabeGeburtsDatum.setValue(zuBearbeitenKunde.getGeburtsDatum());
            eingabePLZ.setText(String.valueOf(zuBearbeitenKunde.getPlz()));
            eingabeEmail.setText(zuBearbeitenKunde.getEmail());
            eingabeTelNr.setText(zuBearbeitenKunde.getTelefonNr());
            eingabeLehrer.setText(zuBearbeitenKunde.getKlassenLehrer());
            eingabeKlasse.setText(zuBearbeitenKunde.getKlasse());
        }
    }
    //Die Methode ist mit dem Speichern Button verbunden.
    //Methode zum Speichern oder zum Löschen eines bearbeiteten Kunden.
    private void speichernOderDelet(){
        Kunde bearbeitenKunde = benutzerTabelle.getSelectionModel().getSelectedItem();
        if(bearbeitenKunde != null){
            Connection connection = Datenbank.datenbankVerbinden();
            if(loeschenCheckBox.isSelected()){
                Datenbank.loeschenBenutzer(connection,bearbeitenKunde);
                loeschenCheckBox.setSelected(false);
            }
            else {
                neueEingabenSetzen(bearbeitenKunde);
                Datenbank.aktualisierenBenutzer(connection, bearbeitenKunde);

            }
            Datenbank.verbindungSchliessen(connection);
            textFelderleeren();
            kundeListeErstellen();
            setItemsInTabelle();
        }
        else {
            System.out.println("IST NULL");
        }


    }
    //Methode um die Textfelder auszulesen und dem Kunden die neuen Werte zu setzen
    private void neueEingabenSetzen(Kunde bearbeitKunde){
        bearbeitKunde.setVorName(eingabeVorname.getText());
        bearbeitKunde.setNachName(eingabeNachname.getText());
        bearbeitKunde.setGeburtsDatum(eingabeGeburtsDatum.getValue());
        bearbeitKunde.setStrasse(eingabeStrasse.getText());
        bearbeitKunde.setStrasseNR(eingabeStrNr.getText());
        bearbeitKunde.setPlz(eingabePLZ.getText());
        bearbeitKunde.setTelefonNr(eingabeTelNr.getText());
        bearbeitKunde.setEmail(eingabeEmail.getText());
        bearbeitKunde.setKlasse(eingabeKlasse.getText());
        bearbeitKunde.setKlassenLehrer(eingabeLehrer.getText());
    }

    //Wenn ein Kunde/Benutzer bearbeitet wurde, werden die Textfelder wieder geleert
    private void textFelderleeren(){
        eingabeVorname.clear();
        eingabeNachname.clear();
        eingabeGeburtsDatum.setValue(null);
        eingabeStrasse.clear();
        eingabeStrNr.clear();
        eingabePLZ.clear();
        eingabeTelNr.clear();
        eingabeEmail.clear();
        eingabeKlasse.clear();
        eingabeLehrer.clear();
    }

}
