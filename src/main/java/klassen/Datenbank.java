package klassen;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.sql.*;

public class Datenbank {
    private final static String createTabelleBuch = "CREATE TABLE Buch (buchNR INT PRIMARY KEY, " +
            "titel VARCHAR(100), " +
            "autor VARCHAR(20), " +
            "isbn VARCHAR(100), " +
            "verlag VARCHAR(50), " +
            "seitenzahl VARCHAR(10), " +
            "sprache VARCHAR(30), "+
            "ausgeliehen BOOLEAN)";

    private final static String createTabelleKunde = "CREATE TABLE Kunde (kundeNR INT PRIMARY KEY, " +
            "vorname VARCHAR(20), " +
            "nachname VARCHAR(30), " +
            "geburtstag DATE, " +
            "strasse VARCHAR(30), " +
            "strassennr VARCHAR(20), " +
            "plz VARCHAR(20), " +
            "telefon VARCHAR(30), " +
            "email VARCHAR(60), " +
            "klasse VARCHAR(30), " +
            "lehrer VARCHAR(30))";
    private final static String createTabelleAuftrag = "CREATE TABLE Auftrag (kundenNR INT," +
            "vorname VARCHAR(50), " +
            "nachname VARCHAR(50), " +
            "buchNR INT, " +
            "buchtitel VARCHAR(50), " +
            "autor VARCHAR(50), " +
            "startdatum DATE, " +
            "enddatum DATE)";

    public static List<Auftrag> getAuftragList(Connection connection){
        String buecherListeQuery = "SELECT * FROM Auftrag";
        List<Auftrag> gespeicherteAuftrag = new ArrayList<>();
        ResultSet resultSet;
        try {
            Statement stmt = connection.createStatement();
            resultSet = stmt.executeQuery(buecherListeQuery);

            if (resultSet != null) {
                while (resultSet.next()) {
                    int kundenNR = resultSet.getInt("kundenNR");
                    String vorname = resultSet.getString("vorname");
                    String nachname = resultSet.getString("nachname");

                    int buchNR = resultSet.getInt("buchNR");
                    String buchtitel = resultSet.getString("buchtitel");
                    String autor = resultSet.getString("autor");
                    LocalDate startdatum = resultSet.getDate("startdatum").toLocalDate();
                    LocalDate enddatum = resultSet.getDate("enddatum").toLocalDate();

                    gespeicherteAuftrag.add(new Auftrag(kundenNR,vorname,nachname,buchNR,buchtitel,autor,startdatum,enddatum));
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }

        return gespeicherteAuftrag;
    }
    public static List<Buch> getBuchListVerfuegbar(Connection connection){
        String getListQuery = "SELECT * FROM Buch WHERE ausgeliehen=false";
        List<Buch> buchList = new ArrayList<>();
        ResultSet resultSet;
        try {
            Statement stmt = connection.createStatement();
            resultSet = stmt.executeQuery(getListQuery);
            if(resultSet != null){
                while (resultSet.next()){
                    int buchID = resultSet.getInt("buchNR");
                    String titel = resultSet.getString("titel");
                    String autor = resultSet.getString("autor");
                    String isbn = resultSet.getString("isbn");
                    String  verlag = resultSet.getString("verlag");
                    String seitenZahl = resultSet.getString("seitenzahl");
                    String sprache = resultSet.getString("sprache");
                    boolean ausgeliehen = resultSet.getBoolean("ausgeliehen");

                    buchList.add(new Buch(buchID,titel,autor,isbn,verlag,seitenZahl,sprache,ausgeliehen));
                }
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return buchList;
    }
    public static List<Buch> getBuchListAlle(Connection connection){
        String getListQuery = "SELECT * FROM Buch";
        List<Buch> buchList = new ArrayList<>();
        ResultSet resultSet;
        try {
            Statement stmt = connection.createStatement();
            resultSet = stmt.executeQuery(getListQuery);
            if(resultSet != null){
                while (resultSet.next()){
                    int buchID = resultSet.getInt("buchNR");
                    String titel = resultSet.getString("titel");
                    String autor = resultSet.getString("autor");
                    String isbn = resultSet.getString("isbn");
                    String  verlag = resultSet.getString("verlag");
                    String seitenZahl = resultSet.getString("seitenzahl");
                    String sprache = resultSet.getString("sprache");
                    boolean ausgeliehen = resultSet.getBoolean("ausgeliehen");

                    buchList.add(new Buch(buchID,titel,autor,isbn,verlag,seitenZahl,sprache,ausgeliehen));
                }
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return buchList;
    }


    /*Funktion für das Kundenbearbeiten Fenster,
    * die Funktion gibt eine Liste mit allen Kunden zurück*/
    public static List<Kunde> getBenutzerList(Connection connection){
        String benutzerListeQuery = "SELECT * FROM Kunde";
        List<Kunde> gespeicherteKunde = new ArrayList<>();
        ResultSet resultSet;
        try {
            Statement stmt = connection.createStatement();
            resultSet = stmt.executeQuery(benutzerListeQuery);

            if(resultSet!=null){
                while (resultSet.next()){
                    int kundenNR = resultSet.getInt("kundeNR");
                    String vorName = resultSet.getString("vorname");
                    String nachName = resultSet.getString("nachname");
                    LocalDate geburt = resultSet.getDate("geburtstag").toLocalDate();
                    String strasse = resultSet.getString("strasse");
                    String strasseNr = resultSet.getString("strassennr");
                    String plz = resultSet.getString("plz");
                    String telNr = resultSet.getString("telefon");
                    String email = resultSet.getString("email");
                    String klasse = resultSet.getString("klasse");
                    String lehrer = resultSet.getString("lehrer");

                    gespeicherteKunde.add(new Kunde(kundenNR,vorName,nachName,geburt,strasse,strasseNr,plz,telNr,email,klasse,lehrer));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return gespeicherteKunde;
    }
    //Sucht in der Tabelle nach der kleinst möglichen Kundennummer die vergeben werden kann
    //sodas, die Kundennummern der gelösten Kunden wieder verwendet werden kann.
    public static int getBenutzerID(){
        int neueID=0;
        boolean vorhanden=true;
        Connection connection = datenbankVerbinden();
        try {
            Statement stmt = connection.createStatement();
            ResultSet resultSet = stmt.executeQuery("SELECT kundeNR FROM Kunde");
            if(resultSet!=null){
                ArrayList<Integer> ids =new ArrayList<>();
                while (resultSet.next()){
                    ids.add(resultSet.getInt("kundeNR"));
                }
                while (vorhanden){
                    neueID++;
                    vorhanden = ids.contains(neueID);
                }
                verbindungSchliessen(connection);
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }

        return neueID;
    }
    //Methode um eine neue Buchnummer zu generieren, das verfahren ist gleich wie bei den Kunden.
    public static int getBuchNr(){
        int neueNr=0;
        boolean vorhanden=true;
        Connection connection = datenbankVerbinden();
        try {
            Statement stmt = connection.createStatement();
            ResultSet resultSet = stmt.executeQuery("SELECT buchNR FROM Buch");
            if(resultSet!=null){
                ArrayList<Integer> ids =new ArrayList<>();
                while (resultSet.next()){
                    ids.add(resultSet.getInt("buchNR"));
                }
                while (vorhanden){
                    neueNr++;
                    vorhanden = ids.contains(neueNr);
                }
                verbindungSchliessen(connection);
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }

        return neueNr;
    }
    //Methode zum speicheren eines neuen Benutzers.
    //Die Methode erwartet eine Connection und den neuen Kunden zu speichern.
    public static void speichernBenutzer(Connection connection,Kunde neuerKunde){
        String kundenSpeichernQuery = "INSERT INTO Kunde VALUES(?,?,?,?,?,?,?,?,?,?,?)";
        try{
            PreparedStatement prepStmt = connection.prepareStatement(kundenSpeichernQuery);
            prepStmt.setInt(1,neuerKunde.getKundenNr());
            prepStmt.setString(2,neuerKunde.getVorName());
            prepStmt.setString(3,neuerKunde.getNachName());
            prepStmt.setDate(4, Date.valueOf(neuerKunde.getGeburtsDatum()));
            prepStmt.setString(5, neuerKunde.getStrasse());
            prepStmt.setString(6, neuerKunde.getStrasseNR());
            prepStmt.setString(7,neuerKunde.getPlz());
            prepStmt.setString(8, neuerKunde.getTelefonNr());
            prepStmt.setString(9, neuerKunde.getEmail());
            prepStmt.setString(10,neuerKunde.getKlasse());
            prepStmt.setString(11, neuerKunde.getKlassenLehrer());

            prepStmt.executeUpdate();

        }catch (SQLException e){
            e.printStackTrace();
        }

    }
    //Methode zum aktualisieren eines Kunden
    public static void aktualisierenBenutzer(Connection connection, Kunde kunde) {
        String aktualisierenQuery = "UPDATE Kunde SET vorname = ?, nachname = ?, geburtstag = ?, strasse = ?, strassennr = ?, plz = ?, telefon = ?, email = ?, klasse = ?, lehrer = ? WHERE kundeNR = ?";

        try {
            PreparedStatement prepStmt = connection.prepareStatement(aktualisierenQuery);
            prepStmt.setString(1, kunde.getVorName());
            prepStmt.setString(2, kunde.getNachName());
            prepStmt.setDate(3, Date.valueOf(kunde.getGeburtsDatum()));
            prepStmt.setString(4, kunde.getStrasse());
            prepStmt.setString(5, kunde.getStrasseNR());
            prepStmt.setString(6, kunde.getPlz());
            prepStmt.setString(7, kunde.getTelefonNr());
            prepStmt.setString(8, kunde.getEmail());
            prepStmt.setString(9, kunde.getKlasse());
            prepStmt.setString(10, kunde.getKlassenLehrer());
            prepStmt.setInt(11, kunde.getKundenNr());

            prepStmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    //Methode zum löschen eines Kunden
    public static void loeschenBenutzer(Connection connection, Kunde kunde){
        String loeschenQueryKunde = "DELETE FROM Kunde WHERE kundeNR = ?";
        try{
            PreparedStatement prepStmt = connection.prepareStatement(loeschenQueryKunde);
            prepStmt.setInt(1,kunde.getKundenNr());
            prepStmt.executeUpdate();
        }
        catch (SQLException e){
            e.printStackTrace();
        }

    }
    //Methode zum löschen eines Buches
    public static void loeschenBuch(Connection connection, Buch buch){
        String loeschenQueryKunde = "DELETE FROM Buch WHERE buchNR = ?";
        try{
            PreparedStatement prepStmt = connection.prepareStatement(loeschenQueryKunde);
            prepStmt.setInt(1,buch.getBuchNummer());
            prepStmt.executeUpdate();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }
    //Methode zum löschen eines Auftrages
    public static void loeschenAuftag(Connection connection, Auftrag auftrag){
        String loeschenQueryKunde = "DELETE FROM Auftrag WHERE kundenNR = ? AND buchNR = ?";
        try{
            PreparedStatement prepStmt = connection.prepareStatement(loeschenQueryKunde);
            prepStmt.setInt(1,auftrag.getKundenNr());
            prepStmt.setInt(2,auftrag.getBuchNr());
            prepStmt.executeUpdate();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

    //Funktion um ein neues Buch zu speichern
    public static void speichernBuch(Connection connection,Buch neuesBuch){
        String buchSpeichernQuery = "INSERT INTO Buch VALUES(?,?,?,?,?,?,?,?)";
        try {
            PreparedStatement prepStmt = connection.prepareStatement(buchSpeichernQuery);

            prepStmt.setInt(1, neuesBuch.getBuchNummer());
            prepStmt.setString(2, neuesBuch.getBuchTitel());
            prepStmt.setString(3, neuesBuch.getAutor());
            prepStmt.setString(4, neuesBuch.getIsbn());
            prepStmt.setString(5, neuesBuch.getVerlag());
            prepStmt.setString(6, neuesBuch.getSeitenZahl());
            prepStmt.setString(7, neuesBuch.getSprache());
            prepStmt.setBoolean(8, neuesBuch.isIstAusgeliehen());

            prepStmt.executeUpdate();
        }
        catch (SQLException e){
            e.printStackTrace();
        }

    }
    //Funktion um einen neuen Auftrag zu speichern
    public static void speichernAuftrag(Connection connection,Auftrag neuerAuftrag){
        String auftragSpeichernQuery = "INSERT INTO Auftrag VALUES(?,?,?,?,?,?,?,?)";
        try {
            PreparedStatement prepStmt = connection.prepareStatement(auftragSpeichernQuery);
            prepStmt.setInt(1,neuerAuftrag.getKundenNr());
            prepStmt.setString(2, neuerAuftrag.getVorName());
            prepStmt.setString(3, neuerAuftrag.getNachName());
            prepStmt.setInt(4,neuerAuftrag.getBuchNr());
            prepStmt.setString(5, neuerAuftrag.getBuchTitel());
            prepStmt.setString(6, neuerAuftrag.getBuchAutor());
            prepStmt.setDate(7, Date.valueOf(neuerAuftrag.getAusgeliehenAm()));
            prepStmt.setDate(8,Date.valueOf(neuerAuftrag.getMussZurueckSein()));

            prepStmt.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    public static void buchIstAusgeliehen(Connection connection, Buch buch){
        String istAusgeliehenQuery = "UPDATE Buch SET ausgeliehen=true WHERE buchNR=?";
        try {
            PreparedStatement prepStmt = connection.prepareStatement(istAusgeliehenQuery);
            prepStmt.setInt(1, buch.getBuchNummer());
            prepStmt.executeUpdate();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }
    //Erstellt die Datenbank, wenn sie noch nicht vorhanden ist
    //Und gibt eine Connection zurück
    public static Connection datenbankVerbinden()  {
        String query = "jdbc:derby:BuchUndBenutzerDatenbank;create=true";
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(query);
            System.out.println("Verbindung hergestellt");
        }
        catch (SQLException e){
            e.printStackTrace();
        }

        return connection;
    }
    public static void verbindungSchliessen(Connection conn){
        try {
            conn.close();
            System.out.println("Verbindung getrennt.");
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }
    // Erstellt die Tabellen in der Datenbank, wenn sie noch nicht vorhanden sind
    public static void tabellenErstellen(Connection conn){
        try{
            PreparedStatement prepStatement = conn.prepareStatement(createTabelleKunde);
            prepStatement.executeUpdate();

            prepStatement = conn.prepareStatement(createTabelleBuch);
            prepStatement.executeUpdate();

            prepStatement = conn.prepareStatement(createTabelleAuftrag);
            prepStatement.executeUpdate();
            System.out.println("Tabellen erstellt");

        }
        catch (SQLException e){
            if(e.getSQLState().equals("X0Y32")){
                System.out.println("Tabellen geladen");
            }
            else {
                e.printStackTrace();
                System.out.println("Fehler beim erstellen der Tabellen");
            }
        }
    }
}
