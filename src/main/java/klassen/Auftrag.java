package klassen;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Auftrag {
    private int kundenNr;
    private String vorName;
    private String nachName;
    private int buchNr;
    private String buchTitel;
    private String buchAutor;
    private LocalDate ausgeliehenAm;
    private LocalDate mussZurueckSein;

    public Auftrag(Buch buch, Kunde kunde){
        this.kundenNr = kunde.getKundenNr();
        this.vorName = kunde.getVorName();
        this.nachName = kunde.getNachName();

        this.buchNr = buch.getBuchNummer();
        this.buchTitel = buch.getBuchTitel();
        this.buchAutor = buch.getAutor();

        ausgeliehenAm = LocalDate.now();
        mussZurueckSein = ausgeliehenAm.plus(10, ChronoUnit.DAYS);
    }
    public Auftrag(int kundenNr, String vorName, String nachName, int buchNr, String buchTitel, String buchAutor, LocalDate ausgeliehenAm, LocalDate mussZurueckSein){
        this.kundenNr = kundenNr;
        this.vorName = vorName;
        this.nachName = nachName;
        this.buchNr = buchNr;
        this.buchTitel = buchTitel;
        this.buchAutor = buchAutor;
        this.ausgeliehenAm = ausgeliehenAm;
        this.mussZurueckSein = mussZurueckSein;
    }

    public int getKundenNr() {
        return kundenNr;
    }



    public int getBuchNr() {
        return buchNr;
    }



    public String getVorName() {
        return vorName;
    }



    public String getNachName() {
        return nachName;
    }



    public String getBuchTitel() {
        return buchTitel;
    }



    public String getBuchAutor() {
        return buchAutor;
    }



    public LocalDate getAusgeliehenAm() {
        return ausgeliehenAm;
    }



    public LocalDate getMussZurueckSein() {
        return mussZurueckSein;
    }


}
