package it.algos.wam.entity.turnoiscrizione;

import it.algos.wam.entity.companyentity.WamCompanyEntity;
import it.algos.wam.entity.iscrizione.Iscrizione;
import it.algos.wam.entity.turno.Turno;
import it.algos.webbase.web.entity.BaseEntity;
import it.algos.webbase.web.query.AQuery;
import org.apache.commons.beanutils.BeanUtils;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;

/**
 * Classe di tipo JavaBean.
 * <p>
 * 1) la classe deve avere un costruttore senza argomenti
 * 2) le proprietà devono essere private e accessibili solo con get, set e is (usato per i boolena al posto di get)
 * 3) la classe deve implementare l'interfaccia Serializable (la fa nella superclasse)
 * 4) la classe non deve contenere nessun metodo per la gestione degli eventi
 */
@Entity
public class TurnoIscrizione extends WamCompanyEntity {

    private static final long serialVersionUID = 1L;

    @NotNull
    @ManyToOne
    private Turno turno = null;

    @NotNull
    @ManyToOne
    private Iscrizione iscrizione = null;

    /**
     * Costruttore senza argomenti
     * Necessario per le specifiche JavaBean
     */
	public TurnoIscrizione() {
		this(null, null);
    }// end of constructor

    /**
     * Costruttore completo
     *
     */
	public TurnoIscrizione(Turno turno, Iscrizione iscrizione) {
		super();
		this.setTurno(turno);
        this.setIscrizione(iscrizione);
	}// end of general constructor

    /**
     * Recupera una istanza di TurnoIscrizione usando la query standard della Primary Key
     *
     * @param id valore della Primary Key
     * @return istanza di TurnoIscrizione, null se non trovata
     */
	public static TurnoIscrizione find(long id) {
		TurnoIscrizione instance = null;
		BaseEntity entity = AQuery.queryById(TurnoIscrizione.class, id);

		if (entity != null) {
			if (entity instanceof TurnoIscrizione) {
				instance = (TurnoIscrizione) entity;
			}// end of if cycle
		}// end of if cycle

		return instance;
	}// end of method

    /**
     * Recupera una istanza di TurnoIscrizione usando la query di una property specifica
     *
     * @param sigla valore della property Sigla
     * @return istanza di TurnoIscrizione, null se non trovata
     */
	public static TurnoIscrizione findByv(String sigla) {
		TurnoIscrizione instance = null;
		BaseEntity entity = AQuery.queryOne(TurnoIscrizione.class, TurnoIscrizione_.sigla, sigla);

		if (entity != null) {
			if (entity instanceof TurnoIscrizione) {
				instance = (TurnoIscrizione) entity;
			}// end of if cycle
		}// end of if cycle

		return instance;
	}// end of method

    /**
     * Recupera il valore del numero totale di records della della Entity
     *
     * @return numero totale di records della tavola
     */
    public static int count() {
        int totRec = 0;
        long totTmp = AQuery.getCount(TurnoIscrizione.class);

        if (totTmp > 0) {
            totRec = (int) totTmp;
        }// fine del blocco if

        return totRec;
    }// end of method

    /**
     * Recupera una lista (array) di tutti i records della Entity
     *
     * @return lista di tutte le istanze di TurnoIscrizione
     */
    @SuppressWarnings("unchecked")
    public static ArrayList<TurnoIscrizione> findAll() {
        return (ArrayList<TurnoIscrizione>) AQuery.getLista(TurnoIscrizione.class);
    }// end of method

    public Turno getTurno() {
        return turno;
    }// end of getter method

    public void setTurno(Turno turno) {
        this.turno = turno;
    }//end of setter method

    public Iscrizione getIscrizione() {
        return iscrizione;
    }// end of getter method

    public void setIscrizione(Iscrizione iscrizione) {
        this.iscrizione = iscrizione;
    }//end of setter method

    /**
     * Clone di questa istanza
     * Una DIVERSA istanza (indirizzo di memoria) con gi STESSI valori (property)
     * È obbligatoria invocare questo metodo all'interno di un codice try/catch
     *
     * @return nuova istanza di TurnoIscrizione con gli stessi valori dei parametri di questa istanza
     */
    @Override
    @SuppressWarnings("all")
    public TurnoIscrizione clone() throws CloneNotSupportedException {
        try {
            return (TurnoIscrizione) BeanUtils.cloneBean(this);
        } catch (Exception ex) {
            throw new CloneNotSupportedException();
        }// fine del blocco try-catch
    }// end of method

}// end of domain class
