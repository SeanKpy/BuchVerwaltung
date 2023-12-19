package ch.kofmel;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import klassen.Buch;
import klassen.Datenbank;

import java.sql.Connection;

public class NeuesBuchFenster extends Stage {

    private TextField buchTitel;
    private TextField autor;
    private TextField isbn;
    private TextField verlag;
    private TextField seitenZahl;
    private TextField sprache;

    public NeuesBuchFenster(Stage hauptStage){
        setTitle("Neues Buch hinzufügen");
        BorderPane bp = new BorderPane();

        createCenterGUI(bp);
        createBottomGUI(bp);
        initModality(Modality.WINDOW_MODAL);
        initOwner(hauptStage);
        Scene scene = new Scene(bp);
        setScene(scene);
        sizeToScene();
        show();

    }

    private void createBottomGUI(BorderPane bp) {
        Button speichernButton = new Button("Speichern");
        speichernButton.setOnAction(e -> neuesBuchSpeichern());
        HBox hBox = new HBox();
        hBox.setPadding(new Insets(5));
        hBox.setAlignment(Pos.CENTER);
        hBox.getChildren().add(speichernButton);
        bp.setBottom(hBox);
    }


    private void createCenterGUI(BorderPane bp){
        Label labelBuchTitel = new Label("Buchtitel");
        Label labelAutor = new Label("Autor");
        Label labelISBN = new Label("ISBN");
        Label labelVerlag = new Label("Verlag");
        Label labelSeitenzahl = new Label("Seitenzahl");
        Label labelSprache = new Label("Sprache");

        buchTitel = new TextField("Buchtitel");
        autor = new TextField();
        isbn = new TextField();
        verlag = new TextField();
        seitenZahl = new TextField();
        sprache = new TextField();

        GridPane gp = new GridPane();
        gp.setHgap(5);
        gp.setVgap(5);
        gp.setPadding(new Insets(5));
        gp.add(labelBuchTitel,0,0);
        gp.add(labelAutor,0,1);
        gp.add(labelISBN,0,2);
        gp.add(labelVerlag,0,3);
        gp.add(labelSeitenzahl,0,4);
        gp.add(labelSprache,0,5);

        gp.add(buchTitel,1,0);
        gp.add(autor,1,1);
        gp.add(isbn,1,2);
        gp.add(verlag,1,3);
        gp.add(seitenZahl,1,4);
        gp.add(sprache,1,5);

        bp.setCenter(gp);
    }

    private void neuesBuchSpeichern() {
        String tempTitel = buchTitel.getText().isEmpty() ? "Keine Angabe" : buchTitel.getText();
        String tempAutor = autor.getText().isEmpty() ? "Keine Angabe" : autor.getText();
        String tempISBN = isbn.getText().isEmpty() ? "Keine Angabe" : isbn.getText();
        String tempVerlag = verlag.getText().isEmpty() ? "Keine Angabe" : verlag.getText();
        String tempSeitenZahl = seitenZahl.getText().isEmpty() ? "Keine Angabe" : seitenZahl.getText();
        String tempSprache = sprache.getText().isEmpty() ? "Keine Angabe" : sprache.getText();

        int buchNr = Datenbank.getBuchNr();

        Buch tempBuch = new Buch(buchNr,tempTitel,tempAutor,tempISBN,tempVerlag,tempSeitenZahl,tempSprache,false);

        Connection connection = Datenbank.datenbankVerbinden();
        Datenbank.speichernBuch(connection,tempBuch);
        Datenbank.verbindungSchliessen(connection);

        textFelderLeeren();
    }

    private void textFelderLeeren() {
        buchTitel.clear();
        autor.clear();
        isbn.clear();
        verlag.clear();
        seitenZahl.clear();
        sprache.clear();
    }
}
