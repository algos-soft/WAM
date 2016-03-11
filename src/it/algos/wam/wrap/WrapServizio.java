package it.algos.wam.wrap;

import it.algos.wam.entity.funzione.Funzione;

import javax.persistence.ManyToOne;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by gac on 01 mar 2016.
 * Wrapper dei dati relativi a tutte le funzioni di un servizio
 * Al massimo ci sono 4 funzioni per turno (hardcoded)
 */
public class WrapServizio implements Serializable {

    // versione della classe per la serializzazione
    private static final long serialVersionUID = 1L;

    //--singola funzione da assegnare ad un milite/volontario
    @ManyToOne
    private Funzione funzione1 = null;
    private boolean obbligatoria1;

    //--singola funzione da assegnare ad un milite/volontario
    @ManyToOne
    private Funzione funzione2 = null;
    private boolean obbligatoria2;

    //--singola funzione da assegnare ad un milite/volontario
    @ManyToOne
    private Funzione funzione3 = null;
    private boolean obbligatoria3;

    //--singola funzione da assegnare ad un milite/volontario
    @ManyToOne
    private Funzione funzione4 = null;
    private boolean obbligatoria4;


    /**
     * Costruttore minimo con tutte le properties obbligatorie
     *
     * @param funzione1 singola funzione di un milite/volontario
     */
    public WrapServizio(Funzione funzione1) {
        this(funzione1, null, null, null);
    }// end of constructor


    /**
     * Costruttore
     *
     * @param funzione1 singola funzione di un milite/volontario
     * @param funzione2 singola funzione di un milite/volontario
     */
    public WrapServizio(Funzione funzione1, Funzione funzione2) {
        this(funzione1, funzione2, null);
    }// end of constructor

    /**
     * Costruttore
     *
     * @param funzione1 singola funzione di un milite/volontario
     * @param funzione2 singola funzione di un milite/volontario
     * @param funzione3 singola funzione di un milite/volontario
     */
    public WrapServizio(Funzione funzione1, Funzione funzione2, Funzione funzione3) {
        this(funzione1, funzione2, funzione3, null);
    }// end of constructor

    /**
     * Costruttore completo
     *
     * @param funzione1 singola funzione di un milite/volontario
     * @param funzione2 singola funzione di un milite/volontario
     * @param funzione3 singola funzione di un milite/volontario
     * @param funzione4 singola funzione di un milite/volontario
     */
    public WrapServizio(Funzione funzione1, Funzione funzione2, Funzione funzione3, Funzione funzione4) {
        setFunzione1(funzione1);
        setFunzione2(funzione2);
        setFunzione3(funzione3);
        setFunzione4(funzione4);
    }// end of constructor

    @Override
    public String toString() {
        String stringa = "";
        Funzione funzione;

        if (funzione1 == null) {
            stringa = "Non nulla, ma senza funzioni (errore)";
        } else {
            stringa += funzione1.toString();
        }// end of if/else cycle

        if (funzione2 != null) {
            stringa += " + ";
            stringa += funzione2.toString();
        }// end of if cycle

        if (funzione3 != null) {
            stringa += " + ";
            stringa += funzione3.toString();
        }// end of if cycle

        if (funzione4 != null) {
            stringa += " + ";
            stringa += funzione4.toString();
        }// end of if cycle

        return stringa;
    }// end of method

    /**
     * Ritorna l'elenco di tutte le funzioni previste per questo servizio
     * @return le funzioni
     */
    public ArrayList<Funzione> getFunzioni() {
        ArrayList<Funzione> funzioni=new ArrayList<>();
        funzioni.add(funzione1);
        funzioni.add(funzione2);
        funzioni.add(funzione3);
        funzioni.add(funzione4);
        return funzioni;
    }// end of method


    /**
     * Ritorna l'elenco delle funzioni obbligatorie previste per questo servizio
     * @return le funzioni obbligatorie
     */
    public Funzione[] getFunzioniObbligatorie(){
        ArrayList<Funzione> funzioniObblig = new ArrayList();
        for (Wrap w : getWrappers()) {
            if (w.obbligatoria){
                funzioniObblig.add(w.funzione);
            }
        }
        return funzioniObblig.toArray(new Funzione[0]);
    }


    /**
     * Ritorna la lista dei wrappers funzione - obbligatoria
     * @return la lista dei wrappers
     */
    public ArrayList<Wrap> getWrappers() {
        ArrayList<Wrap> lista = null;

        if (funzione1 != null) {
            lista = new ArrayList<>();
            lista.add(new Wrap(funzione1, obbligatoria1));
        }// end of if cycle

        if (funzione2 != null && lista != null) {
            lista.add(new Wrap(funzione2, obbligatoria2));
        }// end of if cycle

        if (funzione3 != null && lista != null) {
            lista.add(new Wrap(funzione3, obbligatoria3));
        }// end of if cycle

        if (funzione4 != null && lista != null) {
            lista.add(new Wrap(funzione4, obbligatoria4));
        }// end of if cycle

        return lista;
    }// end of method


    public ArrayList<String> getSigleFunzioni() {
        ArrayList<String> lista = null;
        lista = new ArrayList<>();
        for (Funzione funzione : getFunzioni()) {
            lista.add(funzione.getSigla());
        }// end of for cycle
        return lista;
    }// end of method


    public Funzione getFunzione1() {
        return funzione1;
    }// end of getter method

    public void setFunzione1(Funzione funzione1) {
        this.funzione1 = funzione1;
    }//end of setter method

    public boolean isObbligatoria1() {
        return obbligatoria1;
    }// end of getter method

    public void setObbligatoria1(boolean obbligatoria1) {
        this.obbligatoria1 = obbligatoria1;
    }//end of setter method

    public Funzione getFunzione2() {
        return funzione2;
    }// end of getter method

    public void setFunzione2(Funzione funzione2) {
        this.funzione2 = funzione2;
    }//end of setter method

    public boolean isObbligatoria2() {
        return obbligatoria2;
    }// end of getter method

    public void setObbligatoria2(boolean obbligatoria2) {
        this.obbligatoria2 = obbligatoria2;
    }//end of setter method

    public Funzione getFunzione3() {
        return funzione3;
    }// end of getter method

    public void setFunzione3(Funzione funzione3) {
        this.funzione3 = funzione3;
    }//end of setter method

    public boolean isObbligatoria3() {
        return obbligatoria3;
    }// end of getter method

    public void setObbligatoria3(boolean obbligatoria3) {
        this.obbligatoria3 = obbligatoria3;
    }//end of setter method

    public Funzione getFunzione4() {
        return funzione4;
    }// end of getter method

    public void setFunzione4(Funzione funzione4) {
        this.funzione4 = funzione4;
    }//end of setter method

    public boolean isObbligatoria4() {
        return obbligatoria4;
    }// end of getter method

    public void setObbligatoria4(boolean obbligatoria4) {
        this.obbligatoria4 = obbligatoria4;
    }//end of setter method


    public class Wrap {
        public Funzione funzione = null;
        public boolean obbligatoria;

        public Wrap(Funzione funzione, boolean obbligatoria) {
            this.funzione = funzione;
            this.obbligatoria = obbligatoria;
        }// end of constructor
    }// end of inner class

}// end of class
