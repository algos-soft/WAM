package it.algos.wam.wrap;

import it.algos.wam.entity.volontario.Volontario;

import javax.persistence.ManyToOne;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by gac on 28 feb 2016.
 * Wrapper dei dati relativi a tutte le iscrizioni al turno
 * Al massimo ci sono 4 iscrizioni per turno (hardcoded)
 */
public class WrapTurno implements Serializable {

    // versione della classe per la serializzazione
    private static final long serialVersionUID = 1L;

    //--singola iscrizione di un milite/volontario
    @ManyToOne
    private Iscrizione iscrizione1 = null;

    //--singola iscrizione di un milite/volontario
    @ManyToOne
    private Iscrizione iscrizione2 = null;

    //--singola iscrizione di un milite/volontario
    @ManyToOne
    private Iscrizione iscrizione3 = null;

    //--singola iscrizione di un milite/volontario
    @ManyToOne
    private Iscrizione iscrizione4 = null;

    /**
     * Costruttore minimo con tutte le properties obbligatorie
     *
     * @param iscrizione1 singola iscrizione di un milite/volontario
     */
    public WrapTurno(Iscrizione iscrizione1) {
        this(iscrizione1, null, null, null);
    }// end of constructor

    /**
     * Costruttore
     *
     * @param iscrizione1 singola iscrizione di un milite/volontario
     * @param iscrizione2 singola iscrizione di un milite/volontario
     */
    public WrapTurno(Iscrizione iscrizione1, Iscrizione iscrizione2) {
        this(iscrizione1, iscrizione2, null, null);
    }// end of constructor

    /**
     * Costruttore
     *
     * @param iscrizione1 singola iscrizione di un milite/volontario
     * @param iscrizione2 singola iscrizione di un milite/volontario
     * @param iscrizione3 singola iscrizione di un milite/volontario
     */
    public WrapTurno(Iscrizione iscrizione1, Iscrizione iscrizione2, Iscrizione iscrizione3) {
        this(iscrizione1, iscrizione2, iscrizione3, null);
    }// end of constructor

    /**
     * Costruttore completo
     *
     * @param iscrizione1 singola iscrizione di un milite/volontario
     * @param iscrizione2 singola iscrizione di un milite/volontario
     * @param iscrizione3 singola iscrizione di un milite/volontario
     * @param iscrizione4 singola iscrizione di un milite/volontario
     */
    public WrapTurno(Iscrizione iscrizione1, Iscrizione iscrizione2, Iscrizione iscrizione3, Iscrizione iscrizione4) {
        setIscrizione1(iscrizione1);
        setIscrizione2(iscrizione2);
        setIscrizione3(iscrizione3);
        setIscrizione4(iscrizione4);
    }// end of constructor

    @Override
    public String toString() {
        String stringa = "";
        Volontario milite;

        if (iscrizione1 == null) {
            stringa = "Non nulla, ma senza militi (errore)";
        } else {
            milite = iscrizione1.getVolontario();
            if (milite != null) {
                stringa += milite.toString();
            }// end of if cycle
        }// end of if/else cycle

        if (iscrizione2 != null) {
            milite = iscrizione2.getVolontario();
            if (milite != null) {
                stringa += " + ";
                stringa += milite.toString();
            }// end of if cycle
        }// end of if cycle

        if (iscrizione3 != null) {
            milite = iscrizione3.getVolontario();
            if (milite != null) {
                stringa += " + ";
                stringa += milite.toString();
            }// end of if cycle
        }// end of if cycle

        if (iscrizione4 != null) {
            milite = iscrizione4.getVolontario();
            if (milite != null) {
                stringa += " + ";
                stringa += milite.toString();
            }// end of if cycle
        }// end of if cycle

        return stringa;
    }// end of method

    @SuppressWarnings("all")
    public ArrayList<Iscrizione> getIscrizioni() {
        ArrayList<Iscrizione> lista = null;

        if (iscrizione1 != null) {
            lista = new ArrayList<>();
            lista.add(iscrizione1);
        }// end of if/else cycle

        if (iscrizione2 != null) {
            lista.add(iscrizione2);
        }// end of if cycle

        if (iscrizione3 != null) {
            lista.add(iscrizione3);
        }// end of if cycle

        if (iscrizione4 != null) {
            lista.add(iscrizione4);
        }// end of if cycle

        return lista;
    }// end of method


    public Iscrizione getIscrizione1() {
        return iscrizione1;
    }// end of getter method

    public void setIscrizione1(Iscrizione iscrizione1) {
        this.iscrizione1 = iscrizione1;
    }//end of setter method

    public Iscrizione getIscrizione2() {
        return iscrizione2;
    }// end of getter method

    public void setIscrizione2(Iscrizione iscrizione2) {
        this.iscrizione2 = iscrizione2;
    }//end of setter method

    public Iscrizione getIscrizione3() {
        return iscrizione3;
    }// end of getter method

    public void setIscrizione3(Iscrizione iscrizione3) {
        this.iscrizione3 = iscrizione3;
    }//end of setter method

    public Iscrizione getIscrizione4() {
        return iscrizione4;
    }// end of getter method

    public void setIscrizione4(Iscrizione iscrizione4) {
        this.iscrizione4 = iscrizione4;
    }//end of setter method

    /**
     * Assegna una lista di iscrizioni.
     * @param iscrizioni le iscrizioni
     */
    public void setIscrizioni(Iscrizione[] iscrizioni) {
        iscrizione1=null;
        iscrizione2=null;
        iscrizione3=null;
        iscrizione4=null;

        if(iscrizioni.length>0){
            iscrizione1=iscrizioni[0];
        }
        if(iscrizioni.length>1){
            iscrizione2=iscrizioni[1];
        }
        if(iscrizioni.length>2){
            iscrizione3=iscrizioni[2];
        }
        if(iscrizioni.length>3){
            iscrizione4=iscrizioni[3];
        }
    }

}// end of class
