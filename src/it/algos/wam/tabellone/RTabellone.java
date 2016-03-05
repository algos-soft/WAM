package it.algos.wam.tabellone;

/**
 * Contenitore dei componenti grafici per una riga di tabellone.
 * Questo non è un componente grafico ma solo un wrapper.
 * I componenti verranno estratti dal tabellone e posizionati nella griglia.
 * Created by alex on 21/02/16.
 */
public class RTabellone {
    CServizio servizio;
    CFunzioni ruoli;
    CTurnoDisplay[] turni;

    public RTabellone(CServizio servizio, CFunzioni ruoli, CTurnoDisplay... turni) {
        this.servizio=servizio;
        this.ruoli=ruoli;
        this.turni=turni;

    }


    public CServizio getServizio() {
        return servizio;
    }

    public CFunzioni getRuoli() {
        return ruoli;
    }

    public CTurnoDisplay[] getTurni() {
        return turni;
    }

}
