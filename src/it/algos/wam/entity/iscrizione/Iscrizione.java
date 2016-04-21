package it.algos.wam.entity.iscrizione;

import it.algos.wam.entity.companyentity.WamCompanyEntity;
import it.algos.wam.entity.serviziofunzione.ServizioFunzione;
import it.algos.wam.entity.turno.Turno;
import it.algos.wam.entity.volontario.Volontario;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import java.sql.Timestamp;

/**
 * Created by gac on 28 feb 2016.
 * Entity che rappresenta una iscrizione di un volontario a un turno.
 * L'iscrizione è relativa a una certa funzione tra quelle previste nel servizio.
 */
@Entity
public class Iscrizione extends WamCompanyEntity {

    // versione della classe per la serializzazione
    private static final long serialVersionUID = 1L;

    // turno di riferimento
    @ManyToOne
    private Turno turno;

    // volontario che si iscrive
    @OneToOne
    private Volontario volontario = null;

    // a quale funzione del servizio il volontario si iscrive
    @OneToOne
    private ServizioFunzione servizioFunzione = null;

    //--ultima modifica a questa iscrizione, effettuata dal milite/volontario che si è iscritto
    //--serve per bloccare le modifiche dopo un determinato intervallo di tempo
    private Timestamp lastModifica = null;

    //--durata effettiva del turno del milite/volontario di questa iscrizione
    private int minutiEffettivi = 0;

    //--eventuali problemi di presenza del milite/volontario di questa iscrizione nel turno
    //--serve per evidenziare il problema nel tabellone
    private boolean esisteProblema = false;

    //--eventuale nota associata al milite/volontario
    //--serve per evidenziare il problema nel tabellone
    private String nota;

    /**
     * Costruttore vuoto
     */
    public Iscrizione() {
         this(null, null, null);
    }// end of constructor

    /**
     * Costruttore con la funzione e il volontario
     *
     * @param turno     turno di riferimento
     * @param serFun     a quale funzione del servizio il volontario si iscrive
     * @param volontario milite/volontario assegnato alle funzione prevista per questa iscrizione (obbligatorio)
     */
    public Iscrizione(Turno turno, Volontario volontario, ServizioFunzione serFun) {
        setTurno(turno);
        setVolontario(volontario);
        setServizioFunzione(serFun);
        if(turno!=null){
            setMinutiEffettivi(turno.getMinutiTotali());    // quando viene creata ha lo stesso tempo del turno
        }
    }// end of constructor

    public Volontario getVolontario() {
        return volontario;
    }// end of getter method

    public void setVolontario(Volontario milite) {
        this.volontario = milite;
    }//end of setter method

    public ServizioFunzione getServizioFunzione() {
        return servizioFunzione;
    }

    public void setServizioFunzione(ServizioFunzione servizioFunzione) {
        this.servizioFunzione = servizioFunzione;
    }

    public Timestamp getLastModifica() {
        return lastModifica;
    }// end of getter method

    public void setLastModifica(Timestamp lastModifica) {
        this.lastModifica = lastModifica;
    }//end of setter method

    public int getMinutiEffettivi() {
        return minutiEffettivi;
    }// end of getter method

    public void setMinutiEffettivi(int oreEffettive) {
        this.minutiEffettivi = oreEffettive;
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

    public Turno getTurno() {
        return turno;
    }

    public void setTurno(Turno turno) {
        this.turno = turno;
    }

    /**
     * Ritorna true se ha una nota
     */
    public boolean hasNota(){
        return (getNota()!=null && !getNota().isEmpty());
    }

}// end of class
