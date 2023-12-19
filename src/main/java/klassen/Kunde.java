package klassen;

import java.time.LocalDate;

public class Kunde {

    private int kundenNr;
    private String vorName;
    private String nachName;
    private LocalDate geburtsDatum;
    private String strasse;
    private String strasseNR;
    private String plz;
    private String email;
    private String telefonNr;
    private String klasse;
    private String klassenLehrer;

    public Kunde(int kundenNr,String vorName, String nachName, LocalDate geburtsDatum, String strasse, String strasseNR, String plz, String email, String telefonNr, String klasse, String klassenLehrer) {
        this.kundenNr = kundenNr;
        this.vorName = vorName;
        this.nachName = nachName;
        this.geburtsDatum = geburtsDatum;
        this.strasse = strasse;
        this.strasseNR = strasseNR;
        this.plz = plz;
        this.telefonNr = telefonNr;
        this.email = email;
        this.klasse = klasse;
        this.klassenLehrer = klassenLehrer;
    }

    public String getVorName() {
        return vorName;
    }


    public String getNachName() {
        return nachName;
    }


    public LocalDate getGeburtsDatum() {
        return geburtsDatum;
    }


    public String getStrasse() {
        return strasse;
    }


    public String getStrasseNR() {
        return strasseNR;
    }


    public String getPlz() {
        return plz;
    }


    public String getEmail() {
        return email;
    }

    public String getTelefonNr() {
        return telefonNr;
    }


    public String getKlasse() {
        return klasse;
    }



    public String getKlassenLehrer() {
        return klassenLehrer;
    }


    public int getKundenNr() {
        return kundenNr;
    }


    public void setVorName(String vorName) {
        this.vorName = vorName;
    }

    public void setNachName(String nachName) {
        this.nachName = nachName;
    }

    public void setGeburtsDatum(LocalDate geburtsDatum) {
        this.geburtsDatum = geburtsDatum;
    }

    public void setStrasse(String strasse) {
        this.strasse = strasse;
    }

    public void setStrasseNR(String strasseNR) {
        this.strasseNR = strasseNR;
    }

    public void setPlz(String plz) {
        this.plz = plz;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setTelefonNr(String telefonNr) {
        this.telefonNr = telefonNr;
    }

    public void setKlasse(String klasse) {
        this.klasse = klasse;
    }

    public void setKlassenLehrer(String klassenLehrer) {
        this.klassenLehrer = klassenLehrer;
    }

    public boolean enthalten(String searchText) {
        boolean vorhanden;
        if(this.nachName.toLowerCase().contains(searchText.toLowerCase())){
            vorhanden = true;
        }
        else{
            vorhanden = this.vorName.toLowerCase().contains(searchText.toLowerCase());
        }
        return vorhanden;
    }


}
