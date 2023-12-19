package ch.kofmel;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import klassen.Auftrag;
import klassen.Buch;
import klassen.Datenbank;
import klassen.Kunde;

import java.sql.Connection;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class BuchAuslehnenFenster extends Stage {
    private final TableView<Buch> tabelleBuch;
    private final TableView<Kunde> tabelleKunde;
    private final HauptFenster hauptFenster;
    private List<Buch> buchListe;
    private List<Kunde> kundeListe;

    public BuchAuslehnenFenster(Stage hauptStage, HauptFenster hauptFenster){
        setTitle("Buch ausleihen");
        this.hauptFenster = hauptFenster;
        BorderPane bp = new BorderPane();
        bp.setPadding(new Insets(5));

        tabelleBuch = new TableView<>();
        tabelleKunde = new TableView<>();

        createWindowCenter(bp);
        createWindowOben(bp);
        createWindowUnten(bp);

        initModality(Modality.WINDOW_MODAL);
        initOwner(hauptStage);
        Scene scene = new Scene(bp);
        setScene(scene);
        sizeToScene();
        show();
    }

    private void createWindowCenter(BorderPane bp){
        HBox hBoxCenter = new HBox(tabelleBuch,tabelleKunde);
        hBoxCenter.setAlignment(Pos.CENTER);
        listenErstellen();

        ///Kundentabelle erstellen mit den Spalten und befüllen mit allen Kunden/////////////////
        TableColumn<Kunde,Integer> kundenNR =  new TableColumn<>("Kunden Nr");
        TableColumn<Kunde,String> kundeVorname = new TableColumn<>("Vorname");
        TableColumn<Kunde,String> kundenNachname = new TableColumn<>("Nachname");
        TableColumn<Kunde, LocalDate> kundeGeburtstag = new TableColumn<>("Geb. Datum");
        TableColumn<Kunde,String> kundenPLZ = new TableColumn<>("PLZ");
        TableColumn<Kunde,String> kundenStrasse = new TableColumn<>("Strasse");
        TableColumn<Kunde,String> kundenStrasseNr = new TableColumn<>("Nr");

        kundenNR.setCellValueFactory(new PropertyValueFactory<>("kundenNr"));
        kundeVorname.setCellValueFactory(new PropertyValueFactory<>("vorName"));
        kundenNachname.setCellValueFactory(new PropertyValueFactory<>("nachName"));
        kundeGeburtstag.setCellValueFactory(new PropertyValueFactory<>("geburtsDatum"));
        kundenPLZ.setCellValueFactory(new PropertyValueFactory<>("plz"));
        kundenStrasse.setCellValueFactory(new PropertyValueFactory<>("strasse"));
        kundenStrasseNr.setCellValueFactory(new PropertyValueFactory<>("strasseNR"));

        tabelleKunde.getColumns().add(kundenNR);
        tabelleKunde.getColumns().add(kundeVorname);
        tabelleKunde.getColumns().add(kundenNachname);
        tabelleKunde.getColumns().add(kundeGeburtstag);
        tabelleKunde.getColumns().add(kundenPLZ);
        tabelleKunde.getColumns().add(kundenStrasse);
        tabelleKunde.getColumns().add(kundenStrasseNr);
        tabelleKunde.setItems(FXCollections.observableList(kundeListe));
        ///////////////////////////////////////////////////////////////////////////////////////////

        ///Buchtabelle erstellen mit den Spalten und befüllen mit allen Büchern///////////////////
        TableColumn<Buch,Integer> buchNr = new TableColumn<>("Buch Nr");
        TableColumn<Buch,String> buchTitel = new TableColumn<>("Titel");
        TableColumn<Buch,String> buchAutor = new TableColumn<>("Autor");
        TableColumn<Buch,String> buchISBN = new TableColumn<>("ISBN");
        TableColumn<Buch,String> buchVerlag = new TableColumn<>("Verlag");
        TableColumn<Buch,String> buchSprache = new TableColumn<>("Sprache");
        TableColumn<Buch,String> buchSeitenzahl = new TableColumn<>("Seitenzahl");

        buchNr.setCellValueFactory(new PropertyValueFactory<>("buchNummer"));
        buchTitel.setCellValueFactory(new PropertyValueFactory<>("buchTitel"));
        buchAutor.setCellValueFactory(new PropertyValueFactory<>("autor"));
        buchISBN.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        buchVerlag.setCellValueFactory(new PropertyValueFactory<>("verlag"));
        buchSprache.setCellValueFactory(new PropertyValueFactory<>("sprache"));
        buchSeitenzahl.setCellValueFactory(new PropertyValueFactory<>("seitenZahl"));

        tabelleBuch.getColumns().add(buchNr);
        tabelleBuch.getColumns().add(buchTitel);
        tabelleBuch.getColumns().add(buchAutor);
        tabelleBuch.getColumns().add(buchISBN);
        tabelleBuch.getColumns().add(buchVerlag);
        tabelleBuch.getColumns().add(buchSprache);
        tabelleBuch.getColumns().add(buchSeitenzahl);
        tabelleBuch.setItems(FXCollections.observableList(buchListe));
        ////////////////////////////////////////////////////////////////////////////////////////////

        bp.setCenter(hBoxCenter);
    }
    /*Erstellung des oberen Teils der GUI mit den Suchfeldern für Kunde und Buch*/
    private void createWindowOben(BorderPane bp){
        HBox hBoxContainer = new HBox();
        hBoxContainer.setSpacing(400);
        hBoxContainer.setAlignment(Pos.CENTER);

        HBox hBoxLinks = new HBox();
        hBoxLinks.setSpacing(5);
        hBoxLinks.setPadding(new Insets(5));

        HBox hBoxRechts = new HBox();
        hBoxRechts.setSpacing(5);
        hBoxRechts.setPadding(new Insets(5));

        //Linke Seite für das Buchsuchfeld
        Label sucheBuchLabel = new Label("Buch");
        TextField buchSucheFeld = new TextField();
        buchSucheFeld.textProperty().addListener((observable, oldValue, newValue) -> buchSuchen(newValue));
        hBoxLinks.setAlignment(Pos.CENTER_LEFT);
        hBoxLinks.getChildren().addAll(sucheBuchLabel,buchSucheFeld);

        //Rechteseite für das Kundensuchfeld
        Label sucheKundeLabel = new Label("Kunde");
        TextField kundenSucheFeld = new TextField();
        kundenSucheFeld.textProperty().addListener((observable, oldValue, newValue) -> kundeSuchen(newValue));
        hBoxRechts.setAlignment(Pos.CENTER_RIGHT);
        hBoxRechts.getChildren().addAll(sucheKundeLabel,kundenSucheFeld);

        //Alles aufnehmen
        hBoxContainer.getChildren().addAll(hBoxLinks,hBoxRechts);
        bp.setTop(hBoxContainer);
    }
    //Unterteil der GUI mit dem Speicherbutton
    private void createWindowUnten(BorderPane bp){
        HBox hBoxUnten = new HBox();
        hBoxUnten.setAlignment(Pos.CENTER);
        hBoxUnten.setSpacing(5);
        hBoxUnten.setPadding(new Insets(5));
        Button btnSpeichern = new Button("Speichern");
        btnSpeichern.setOnAction(e -> auftragSpeichernInDB());

        hBoxUnten.getChildren().add(btnSpeichern);

        bp.setBottom(hBoxUnten);
    }
    /*Funktion für den Button speichern
    * liest das angewählte Buch und den Kunden aus überprüft, ob beide einen Wert haben,
    * wenn ja, wird eine Verbindung hergestellt zu der Datenbank und in der Methode speichernAuftrag der Datenbank
    * ein Auftrag erstellt und übergeben */
    private void auftragSpeichernInDB(){
        Buch auftragBuch = tabelleBuch.getSelectionModel().getSelectedItem();
        Kunde auftragKunde = tabelleKunde.getSelectionModel().getSelectedItem();

        if(auftragBuch != null && auftragKunde != null){
            Connection connection = Datenbank.datenbankVerbinden();
            Datenbank.speichernAuftrag(connection,new Auftrag(auftragBuch,auftragKunde));
            Datenbank.buchIstAusgeliehen(connection, auftragBuch);
            Datenbank.verbindungSchliessen(connection);
            listenErstellen();
            buchTabelleAktualisieren();
            hauptFenster.tabelleAktualisieren();
        }
    }
    private void listenErstellen(){
        Connection connection = Datenbank.datenbankVerbinden();
        if(connection != null){
            kundeListe = Datenbank.getBenutzerList(connection);
            buchListe = Datenbank.getBuchListVerfuegbar(connection);
            Datenbank.verbindungSchliessen(connection);
        }
    }

    private void buchTabelleAktualisieren(){
        tabelleBuch.setItems(FXCollections.observableList(buchListe));
    }

    private void buchSuchen(String buchSuche){
        tabelleBuch.setItems(null);
        if(!buchSuche.equals("")){
            List<Buch> buchSuchList = buchListe.stream().filter(e -> e.teilDesBuches(buchSuche)).collect(Collectors.toList());
            tabelleBuch.setItems(FXCollections.observableList(buchSuchList));
        }
        else{
            buchTabelleAktualisieren();
        }

    }
    private void kundeSuchen(String kundeSuche){
        if(!kundeSuche.equals("")){
            List<Kunde> kundeSuchListe = kundeListe.stream().filter(k -> k.enthalten(kundeSuche)).collect(Collectors.toList());
            tabelleKunde.setItems(FXCollections.observableList(kundeSuchListe));
        }
        else{
            tabelleKunde.setItems(FXCollections.observableList(kundeListe));
        }

    }
}
