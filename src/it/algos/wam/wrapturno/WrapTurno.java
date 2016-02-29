package it.algos.wam.wrapturno;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.io.Serializable;

/**
 * Created by gac on 28 feb 2016.
 * Wrapper dei dati relativi a tutte le iscrizioni al turno
 * Al massimo ci sono 4 iscrizioni per turno (hardcoded)
 */
public class WrapTurno implements Serializable {

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
     * @param iscrizione singola iscrizione di un milite/volontario
     */
    public WrapTurno(Iscrizione iscrizione) {
        this(iscrizione, null, null, null);
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
}// end of class
