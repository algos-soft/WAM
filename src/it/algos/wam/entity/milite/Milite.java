package it.algos.wam.entity.milite;

import it.algos.webbase.multiazienda.CompanyEntity;
import it.algos.webbase.web.entity.BaseEntity;
import it.algos.webbase.web.query.AQuery;
import org.apache.commons.beanutils.BeanUtils;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Entity;
import java.util.ArrayList;

/**
 * Entity che descrive un Milite
 */
@Entity
public class Milite extends CompanyEntity {

	@NotEmpty
	private String nome = "";

	public Milite() {
		this("");
    }

	public Milite(String nome) {
		super();
		this.setNome(nome);
	}

    /**
     * Recupera una istanza di Milite usando la query standard della Primary Key
     *
     * @param id valore della Primary Key
     * @return istanza di Milite, null se non trovata
     */
	public static Milite find(long id) {
		Milite instance = null;
		BaseEntity entity = AQuery.queryById(Milite.class, id);

		if (entity != null) {
			if (entity instanceof Milite) {
				instance = (Milite) entity;
			}// end of if cycle
		}// end of if cycle

		return instance;
	}// end of method

    /**
     * Recupera una istanza di Milite usando la query di una property specifica
     *
     * @param nome valore della property Nome
     * @return istanza di Milite, null se non trovata
     */
	public static Milite findByNome(String nome) {
		Milite instance = null;
		BaseEntity entity = AQuery.queryOne(Milite.class, Milite_.nome, nome);

		if (entity != null) {
			if (entity instanceof Milite) {
				instance = (Milite) entity;
			}// end of if cycle
		}// end of if cycle

		return instance;
	}// end of method

    /**
     * Recupera il valore del numero totale di records della Domain Class
     *
     * @return numero totale di records della tavola
     */
    public synchronized static int count() {
        int totRec = 0;
        long totTmp = AQuery.getCount(Milite.class);

        if (totTmp > 0) {
            totRec = (int) totTmp;
        }// fine del blocco if

        return totRec;
    }// end of method

    /**
     * Recupera una lista (array) di tutti i records della Domain Class
     *
     * @return lista di tutte le istanze di Milite
     */
    @SuppressWarnings("unchecked")
    public synchronized static ArrayList<Milite> findAll() {
        return (ArrayList<Milite>) AQuery.getLista(Milite.class);
    }// end of method

    @Override
    public String toString() {
        return nome;
    }// end of method

    /**
     * @return the nome
     */
    public String getNome() {
        return nome;
    }// end of getter method

    /**
     * @param nome the nome to set
     */
    public void setNome(String nome) {
        this.nome = nome;
    }// end of setter method


    /**
     * Clone di questa istanza
     * Una DIVERSA istanza (indirizzo di memoria) con gi STESSI valori (property)
     * È obbligatoria invocare questo metodo all'interno di un codice try/catch
     *
     * @return nuova istanza di Milite con gli stessi valori dei parametri di questa istanza
     */
    @Override
    @SuppressWarnings("all")
    public Milite clone() throws CloneNotSupportedException {
        try {
            return (Milite) BeanUtils.cloneBean(this);
        } catch (Exception ex) {
            throw new CloneNotSupportedException();
        }// fine del blocco try-catch
    }// end of method

}// end of domain class
