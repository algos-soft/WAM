package it.algos.wam.wrapturno;

import it.algos.wam.entity.funzione.Funzione;
import it.algos.wam.entity.milite.Milite;

import javax.persistence.ManyToOne;
import java.sql.Timestamp;

/**
 * Created by gac on 28 feb 2016.
 * Wrapper dei dati relativi ad una singola iscrizione al turno
 * Al massimo ci sono 4 iscrizioni per turno (hardcoded)
 */
public class Iscrizione {

    //--funzione prevista per il tipo di servizio
    @ManyToOne
    private Funzione funzione = null;

    //--milite/volontario assegnato alle funzione prevista per questa iscrizione
    @ManyToOne
    private Milite milite = null;

    //--ultima modifica a questa iscrizione, effettuata dal milite/volontario che si è iscritto
    //--serve per bloccare le modifiche dopo un determinato intervallo di tempo
    private Timestamp lastModifica = null;

    //--durata effettiva del turno del milite/volontario di questa iscrizione
    private int oreEffettive = 0;

    //--eventuali problemi di presenza del milite/volontario di questa iscrizione nel turno
    //--serve per evidenziare il problema nel tabellone
    private boolean esisteProblema = false;

    //--eventuale nota associata al milite/volontario
    //--serve per evidenziare il problema nel tabellone
    private String nota;


    /**
     * Costruttore minimo con tutte le properties obbligatorie
     *
     * @param funzione funzione prevista per il tipo di servizio (obbligatorio)
     * @param milite   milite/volontario assegnato alle funzione prevista per questa iscrizione (obbligatorio)
     */
    public Iscrizione(Funzione funzione, Milite milite) {
        this(funzione, milite, null, 0, false, "");
    }// end of constructor

    /**
     * Costruttore completo
     *
     * @param funzione       funzione prevista per il tipo di servizio (obbligatorio)
     * @param milite         milite/volontario assegnato alle funzione prevista per questa iscrizione (obbligatorio)
     * @param lastModifica   ultima modifica a questa iscrizione, effettuata dal milite/volontario che si è iscritto
     * @param oreEffettive   durata effettiva del turno del milite/volontario di questa iscrizione
     * @param esisteProblema eventuali problemi di presenza del milite/volontario di questa iscrizione nel turno
     * @param nota           eventuale nota associata al milite/volontario
     */
    public Iscrizione(Funzione funzione, Milite milite, Timestamp lastModifica, int oreEffettive, boolean esisteProblema, String nota) {
        setFunzione(funzione);
        setMilite(milite);
        setLastModifica(lastModifica);
        setOreEffettive(oreEffettive);
        setEsisteProblema(esisteProblema);
        setNota(nota);
    }// end of constructor

    public Funzione getFunzione() {
        return funzione;
    }// end of getter method

    public void setFunzione(Funzione funzione) {
        this.funzione = funzione;
    }//end of setter method

    public Milite getMilite() {
        return milite;
    }// end of getter method

    public void setMilite(Milite milite) {
        this.milite = milite;
    }//end of setter method

    public Timestamp getLastModifica() {
        return lastModifica;
    }// end of getter method

    public void setLastModifica(Timestamp lastModifica) {
        this.lastModifica = lastModifica;
    }//end of setter method

    public int getOreEffettive() {
        return oreEffettive;
    }// end of getter method

    public void setOreEffettive(int oreEffettive) {
        this.oreEffettive = oreEffettive;
    }//end of setter method

    public boolean isEsisteProblema() {
        return esisteProblema;
    }// end of getter method

    public void setEsisteProblema(boolean esisteProblema) {
        this.esisteProblema = esisteProblema;
    }//end of setter method

    public String getNota() {
        return nota;
    }// end of getter method

    public void setNota(String nota) {
        this.nota = nota;
    }//end of setter method
}// end of class
