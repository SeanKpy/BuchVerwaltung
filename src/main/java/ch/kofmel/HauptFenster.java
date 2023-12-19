package ch.kofmel;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import klassen.Auftrag;
import klassen.Datenbank;

import java.sql.Connection;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class HauptFenster extends Application{

    private Stage stage;
    private TableView<Auftrag> tabelle;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        stage = primaryStage;
        BorderPane hauptBorderPane = new BorderPane();

        Connection connection = Datenbank.datenbankVerbinden();
        Datenbank.tabellenErstellen(connection);
        Datenbank.verbindungSchliessen(connection);

        createWindowOben(hauptBorderPane);
        tabelle = new TableView<>();
        createWindowCenter(hauptBorderPane);
        createWindowLinks(hauptBorderPane);

        Scene scene = new Scene(hauptBorderPane);
        stage.setTitle("EasyBiblio");
        stage.sizeToScene();
        stage.setScene(scene);
        stage.show();

    }
    //Erstellt das Zentrum mit der TableView und den Büchern mit den Kunden die ausgeliehen sind
    //greift auf die Datenbank zu und liest Aufträge aus
    private void createWindowCenter(BorderPane hauptBorderPane){
        VBox vBoxMitte = new VBox();
        vBoxMitte.setSpacing(5);
        vBoxMitte.setAlignment(Pos.CENTER);
        Label titelCenter = new Label("Ausgeliehene Bücher");
        titelCenter.setFont(Font.font("Arial",FontWeight.BOLD,20));

        vBoxMitte.getChildren().addAll(titelCenter,tabelle);

        //////////////////////////////////////////////////////////////
        TableColumn<Auftrag,Integer> buchNummer = new TableColumn<>("Buchnummer");
        TableColumn<Auftrag,String> buchName = new TableColumn<>("Buchtitel");
        TableColumn<Auftrag,String> autor = new TableColumn<>("Autor");

        TableColumn<Auftrag,Integer> kundenNr = new TableColumn<>("Kunden Nr:");
        TableColumn<Auftrag,String>  vorname = new TableColumn<>("Ausgeliehen an: Vorname");
        TableColumn<Auftrag,String> nachname = new TableColumn<>("Nachname");
        TableColumn<Auftrag,LocalDate> ausgeliehenAm = new TableColumn<>("Ausgeliehen am");
        TableColumn<Auftrag,LocalDate> mussZurueckAm = new TableColumn<>("Rückgabe am");

        //Die Tabelle braucht eine ObservableList, dazu steht eine Methode bereit die eine normale Liste umwandelt.
        tabelleAktualisieren();

        buchNummer.setCellValueFactory(new PropertyValueFactory<>("buchNr"));
        buchName.setCellValueFactory(new PropertyValueFactory<>("buchTitel"));
        autor.setCellValueFactory(new PropertyValueFactory<>("buchAutor"));

        kundenNr.setCellValueFactory(new PropertyValueFactory<>("kundenNr"));
        vorname.setCellValueFactory(new PropertyValueFactory<>("vorName"));
        nachname.setCellValueFactory(new PropertyValueFactory<>("nachName"));
        ausgeliehenAm.setCellValueFactory(new PropertyValueFactory<>("ausgeliehenAm"));
        mussZurueckAm.setCellValueFactory(new PropertyValueFactory<>("mussZurueckSein"));

        tabelle.getColumns().add(buchNummer);
        tabelle.getColumns().add(buchName);
        tabelle.getColumns().add(autor);
        tabelle.getColumns().add(kundenNr);
        tabelle.getColumns().add(vorname);
        tabelle.getColumns().add(nachname);
        tabelle.getColumns().add(ausgeliehenAm);
        tabelle.getColumns().add(mussZurueckAm);

        tabelle.setOnMouseClicked(e -> auftragBeendenDialog(tabelle.getSelectionModel().getSelectedItem(),e.getClickCount()));
        /////////////////////////////////////////////////////////////
        hauptBorderPane.setCenter(vBoxMitte);

    }
    //Erstellt die Linkeseite der BoderPane mit den Buttons
    //für das Öffnen verschiedener Fenster und Optionen
    private void createWindowLinks(BorderPane hauptBorderPane){
        VBox vBoxLinks = new VBox();
        vBoxLinks.setAlignment(Pos.CENTER);
        vBoxLinks.setPadding(new Insets(10));
        vBoxLinks.setSpacing(20);

        Button neuerKunde = new Button("Neuer Kunde");
        neuerKunde.setOnAction(e -> erstellenNeuerKundeFenster());

        Button deletKunde = new Button("Kunden bearbeiten");
        deletKunde.setOnAction(e -> bearbeitenKundeFenster());

        Button neuesBuch = new Button("Neues Buch");
        neuesBuch.setOnAction(e -> neuesBuchFenster());

        Button ausleihe = new Button("Buch auslehnen");
        ausleihe.setOnAction(e -> ausleiheFenster());

        Button alleBuecher = new Button("Alle Bücher");
        alleBuecher.setOnAction(e -> alleBuecherFenster());

        vBoxLinks.getChildren().addAll(neuerKunde,deletKunde,neuesBuch,alleBuecher,ausleihe);

        hauptBorderPane.setLeft(vBoxLinks);
    }
    //Erstellt die Oberseite der BorderPane mit einer MenuBar
    private void createWindowOben(BorderPane hauptBorderPane){
        MenuBar menuBar = new MenuBar();
        Menu datei = new Menu("Datei");
        MenuItem neueKunde = new MenuItem("Neuer Kunde");
        neueKunde.setOnAction(e -> new NeuerBenutzerFenster(stage));

        MenuItem bearbeitKunde = new MenuItem("Kunde bearbeiten");
        bearbeitKunde.setOnAction(e -> new BenutzerBearbeitenFenster(stage));

        MenuItem neuesBuch = new MenuItem("Neues Buch");
        neuesBuch.setOnAction(e -> new NeuerBenutzerFenster(stage));

        MenuItem alleBuecher = new MenuItem("Alle Bücher");
        alleBuecher.setOnAction(e -> new AlleBuecherFenster(stage));

        MenuItem buchAuslehnen = new MenuItem("Buch ausleihe");
        buchAuslehnen.setOnAction(e -> new BuchAuslehnenFenster(stage,this));

        MenuItem beenden = new MenuItem("Beenden");
        beenden.setOnAction(e -> Platform.exit());

        datei.getItems().addAll(neueKunde,bearbeitKunde,neuesBuch,buchAuslehnen,new SeparatorMenuItem(),beenden);
        menuBar.getMenus().add(datei);

        hauptBorderPane.setTop(menuBar);
    }
    //Methode um die tabelle zu aktualisieren, auch von dem Buchausleihefenster das eine Referenz auf das Hauptfenster erhält.
    public void tabelleAktualisieren(){
        Connection connection = Datenbank.datenbankVerbinden();
        if(connection != null){
            List<Auftrag> auftragList = Datenbank.getAuftragList(connection);
            Datenbank.verbindungSchliessen(connection);
            tabelle.setItems(FXCollections.observableList(auftragList));
        }
    }
    /*Erstellt einen Dialog der fragt, ob man den Eintrag wirklich löschen will.
    * Dazu erhält die Methode den Auftrag und die Anzahl klicks zum Überprüfen,
    * ob es gewollt ist ein Fenster anzuzeigen*/
    private void auftragBeendenDialog(Auftrag auftrag, int clickCount){
        if(clickCount == 2 && auftrag != null){
            Dialog<ButtonType> dialog = new Dialog<>();

            dialog.setTitle("Auftrag beenden");
            dialog.initModality(Modality.APPLICATION_MODAL);

            DialogPane dialogPane = new DialogPane();
            dialogPane.setHeaderText("Sind Sie sicher, dass Sie den Auftrag mit der \nKundennummer: " + auftrag.getKundenNr()+"\nVorname: " + auftrag.getVorName()+ "\nBuchtitel: "+auftrag.getBuchTitel()+" löschen wollen?");


            ButtonType okButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
            ButtonType cancelButtonType = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
            dialogPane.getButtonTypes().addAll(okButtonType, cancelButtonType);

            dialog.setDialogPane(dialogPane);

            //Hier wird gewartet auf dad Resultat, welcher Button gedrückt wurde
            Optional<ButtonType> result = dialog.showAndWait();
            if(result.isPresent() && result.get().getButtonData() == ButtonBar.ButtonData.OK_DONE){
                auftragLoeschen(auftrag);
            }
        }
    }
    private void auftragLoeschen(Auftrag auftrag){
        Connection connection = Datenbank.datenbankVerbinden();
        if(connection != null){
            Datenbank.loeschenAuftag(connection,auftrag);
            Datenbank.verbindungSchliessen(connection);
            tabelleAktualisieren();
        }
    }


    private void erstellenNeuerKundeFenster(){
        new NeuerBenutzerFenster(stage);
    }
    private void bearbeitenKundeFenster(){
        new BenutzerBearbeitenFenster(stage);
    }
    private void ausleiheFenster() {
        new BuchAuslehnenFenster(stage, this);
    }

    private void neuesBuchFenster() {
        new NeuesBuchFenster(stage);
    }
    private void alleBuecherFenster(){
        new AlleBuecherFenster(stage);

    }
}