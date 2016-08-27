package it.algos.wam.migration;

import it.algos.wam.entity.wamcompany.WamCompany;
import it.algos.webbase.web.entity.BaseEntity;
import it.algos.webbase.web.query.AQuery;
import org.eclipse.persistence.annotations.ReadOnly;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

@Entity
@ReadOnly
public class Croce extends BaseEntity {

    private String sigla;
    private String descrizione;
    private String presidente;
    private String indirizzo;

    /**
     * Costruttore senza argomenti
     * Necessario per le specifiche JavaBean
     */
    public Croce() {
    }// end of constructor

    /**
     * Recupera una istanza della Entity usando la query per una property specifica
     *
     * @param sigla valore della property code
     *
     * @return istanza della Entity, null se non trovata
     */
    public static Croce findByCode(String sigla) {
        Croce instance = null;

        EntityManagerFactory factory = Persistence.createEntityManagerFactory("Webambulanzelocal");
        EntityManager manager = factory.createEntityManager();
        BaseEntity entity = AQuery.queryOne(Croce.class, Croce_.sigla, sigla, manager);
        manager.close();

        if (entity != null) {
            if (entity instanceof Croce) {
                instance = (Croce) entity;
            }// end of if cycle
        }// end of if cycle

        return instance;
    }// end of method

    public String getSigla() {
        return sigla;
    }

    public void setSigla(String sigla) {
        this.sigla = sigla;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public String getPresidente() {
        return presidente;
    }

    public void setPresidente(String presidente) {
        this.presidente = presidente;
    }

    public String getIndirizzo() {
        return indirizzo;
    }

    public void setIndirizzo(String indirizzo) {
        this.indirizzo = indirizzo;
    }
}
