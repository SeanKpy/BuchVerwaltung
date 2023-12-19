package klassen;


public class Buch {
    private int buchNummer;
    private String buchTitel;
    private String autor;
    private String isbn;
    private String verlag;
    private String seitenZahl;
    private String sprache;
    private boolean istAusgeliehen;

    public Buch(int buchNummer,String titel,String autor, String isbn, String verlag, String seitenZahl, String sprache,boolean istAusgeliehen){
        this.buchNummer = buchNummer;
        this.buchTitel = titel;
        this.autor = autor;
        this.isbn = isbn;
        this.verlag = verlag;
        this.seitenZahl = seitenZahl;
        this.sprache = sprache;
        this.istAusgeliehen = istAusgeliehen;

    }

    public String getBuchTitel(){

        return buchTitel;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getVerlag() {
        return verlag;
    }

    public String getSeitenZahl() {
        return seitenZahl;
    }

    public String getSprache() {
        return sprache;
    }

    public String getAutor() {
        return autor;
    }

    public boolean isIstAusgeliehen() {
        return istAusgeliehen;
    }


    public int getBuchNummer(){
        return buchNummer;
    }

    public String getVorhanden(){
        if(this.isIstAusgeliehen()){
            return "Nein";
        }
        else{
            return "Ja";
        }
    }

    public boolean teilDesBuches(String buchTeil){
        return this.buchTitel.toLowerCase().contains(buchTeil.toLowerCase()) || this.autor.toLowerCase().contains(buchTeil.toLowerCase()) || this.verlag.toLowerCase().contains(buchTeil.toLowerCase());
    }
}
