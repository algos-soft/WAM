package it.algos.wam.tabellone;

import it.algos.wam.entity.servizio.Servizio;

import java.time.LocalDate;

/**
 * Wrapper che trasporta le informazioni per la creazione di un nuovo turno
 * Created by alex on 7-03-2016.
 */
public class InfoNewTurnoWrap {
    private Servizio servizio;
    private LocalDate data;

    public InfoNewTurnoWrap(Servizio servizio, LocalDate data) {
        this.servizio = servizio;
        this.data = data;
    }

    public Servizio getServizio() {
        return servizio;
    }

    public LocalDate getData() {
        return data;
    }
}
