package it.algos.wam.entity.test;

import it.algos.wam.entity.companyentity.WamCompanyEntity;
import it.algos.wam.entity.servizio.Servizio;
import it.algos.wam.entity.serviziofunzione.ServizioFunzione;
import it.algos.wam.entity.turno.Turno;
import it.algos.wam.entity.volontario.Volontario;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

/**
 * Entity per fare dei test sulle relazioni.
 */
@Entity
public class TestIscrizione extends WamCompanyEntity {

    // versione della classe per la serializzazione
    private static final long serialVersionUID = 1L;

    // turno di riferimento
    @ManyToOne
    private TestTurno turno;

    // volontario che si iscrive
    @OneToOne
    private Volontario volontario = null;

    // a quale funzione del servizio il volontario si iscrive
    @OneToOne
    private ServizioFunzione servizioFunzione = null;


    private String sigla;
    /**
     * Costruttore vuoto
     */
    public TestIscrizione() {
    }// end of constructor

    public TestTurno getTurno() {
        return turno;
    }

    public void setTurno(TestTurno turno) {
        this.turno = turno;
    }

    public String getSigla() {
        return sigla;
    }

    public void setSigla(String sigla) {
        this.sigla = sigla;
    }

    public Volontario getVolontario() {
        return volontario;
    }

    public void setVolontario(Volontario volontario) {
        this.volontario = volontario;
    }

    public ServizioFunzione getServizioFunzione() {
        return servizioFunzione;
    }

    public void setServizioFunzione(ServizioFunzione servizioFunzione) {
        this.servizioFunzione = servizioFunzione;
    }
}// end of class
