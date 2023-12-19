package ch.kofmel;


import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import klassen.Buch;
import klassen.Datenbank;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AlleBuecherFenster extends Stage {
    private TableView<Buch> alleBuecherTabelle;
    private List<Buch> buchList;


    public AlleBuecherFenster(Stage hauptStage) {
        setTitle("Komplette Bücherliste");
        BorderPane bp = new BorderPane();
        alleBuecherTabelle = new TableView<>();
        buchList = new ArrayList<>();
        createCenter(bp);
        createTop(bp);

        Scene scene = new Scene(bp);
        initModality(Modality.WINDOW_MODAL);
        initOwner(hauptStage);
        setScene(scene);
        sizeToScene();
        show();
    }

    private void createTop(BorderPane bp){
        HBox hBox = new HBox();
        hBox.setSpacing(20);
        hBox.setPadding(new Insets(5,5,0,5));

        CheckBox deleteCheckBox = new CheckBox("Auswahl Löschen?");
        Button deleteButton = new Button("Löschen");
        deleteButton.setOnAction(e -> buchLoeschen());

        deleteCheckBox.setOnAction(e -> deleteButton.setDisable(!deleteCheckBox.isSelected()));
        deleteButton.setDisable(!deleteCheckBox.isSelected());

        Label sucheLabel = new Label("Buchsuchen:");
        TextField sucheField = new TextField();
        sucheField.textProperty().addListener((observable, oldValue, newValue) -> buchSuchen(newValue));

        hBox.getChildren().add(sucheLabel);
        hBox.getChildren().add(sucheField);

        hBox.getChildren().add(deleteCheckBox);
        hBox.getChildren().add(deleteButton);
        bp.setTop(hBox);
    }

    private void createCenter(BorderPane bp) {
        HBox hBox = new HBox(alleBuecherTabelle);
        hBox.setPadding(new Insets(10,0,0,0));
        TableColumn<Buch,Integer> buchNr = new TableColumn<>("Buch Nr");
        TableColumn<Buch,String> buchTitel = new TableColumn<>("Titel");
        TableColumn<Buch,String> buchAutor = new TableColumn<>("Autor");
        TableColumn<Buch,String> buchISBN = new TableColumn<>("ISBN");
        TableColumn<Buch,String> buchVerlag = new TableColumn<>("Verlag");
        TableColumn<Buch,String> buchSprache = new TableColumn<>("Sprache");
        TableColumn<Buch,String> buchSeitenzahl = new TableColumn<>("Seitenzahl");

        ////Speziell hier wird der String der eingeführt wird durch eine Methode in Buch bestimmt.//////
        TableColumn<Buch,String> istVerfuegbar = new TableColumn<>("Verfügbar");
        istVerfuegbar.setCellValueFactory(new PropertyValueFactory<>("vorhanden"));

        buchNr.setCellValueFactory(new PropertyValueFactory<>("buchNummer"));
        buchTitel.setCellValueFactory(new PropertyValueFactory<>("buchTitel"));
        buchAutor.setCellValueFactory(new PropertyValueFactory<>("autor"));
        buchISBN.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        buchVerlag.setCellValueFactory(new PropertyValueFactory<>("verlag"));
        buchSprache.setCellValueFactory(new PropertyValueFactory<>("sprache"));
        buchSeitenzahl.setCellValueFactory(new PropertyValueFactory<>("seitenZahl"));

        alleBuecherTabelle.getColumns().add(buchNr);
        alleBuecherTabelle.getColumns().add(buchTitel);
        alleBuecherTabelle.getColumns().add(buchAutor);
        alleBuecherTabelle.getColumns().add(buchISBN);
        alleBuecherTabelle.getColumns().add(istVerfuegbar);
        alleBuecherTabelle.getColumns().add(buchVerlag);
        alleBuecherTabelle.getColumns().add(buchSprache);
        alleBuecherTabelle.getColumns().add(buchSeitenzahl);
        listeAktualisieren();

        bp.setCenter(hBox);
    }

    private void listeAktualisieren(){
        Connection connection = Datenbank.datenbankVerbinden();
        if(connection != null){
            buchList = Datenbank.getBuchListAlle(connection);
            alleBuecherTabelle.setItems(FXCollections.observableList(buchList));
            Datenbank.verbindungSchliessen(connection);
        }
    }
    //Methode zum Löschen eines Buches, aber nur wenn dies nicht ausgeliehen ist(Wird in der ersten IF anweisung überprüft)
    private void buchLoeschen(){
        if(alleBuecherTabelle.getSelectionModel().getSelectedItem() != null && !alleBuecherTabelle.getSelectionModel().getSelectedItem().isIstAusgeliehen()){
            Connection connection = Datenbank.datenbankVerbinden();
            if(connection != null){
                Datenbank.loeschenBuch(connection, alleBuecherTabelle.getSelectionModel().getSelectedItem());
                Datenbank.verbindungSchliessen(connection);
                listeAktualisieren();
            }

        }
    }
    //Sucht ein bestimmtes Buch
    private void buchSuchen(String buchSuche) {
        alleBuecherTabelle.setItems(null);
        if (!buchSuche.equals("")) {
            List<Buch> buchSuchList = buchList.stream().filter(e -> e.teilDesBuches(buchSuche)).collect(Collectors.toList());
            alleBuecherTabelle.setItems(FXCollections.observableList(buchSuchList));
        } else {
            alleBuecherTabelle.setItems(FXCollections.observableList(buchList));
        }
    }


}
