package it.algos.wam.migration;

import com.vaadin.ui.MenuBar;
import it.algos.webbase.web.entity.BaseEntity;
import it.algos.webbase.web.query.AQuery;
import org.eclipse.persistence.annotations.ReadOnly;

import javax.persistence.*;
import java.util.List;

/**
 * Created by gac on 08 ott 2016.
 * Entity per un utente
 * Entity della vecchia versione di webambulanze da cui migrare i dati. Solo lettura
 * <p>
 * Classe di tipo JavaBean
 * 1) la classe deve avere un costruttore senza argomenti
 * 2) le proprietà devono essere private e accessibili solo con get, set e is (usato per i boolena al posto di get)
 * 3) la classe deve implementare l'interfaccia Serializable (la fa nella superclasse)
 * 4) la classe non deve contenere nessun metodo per la gestione degli eventi
 */
@Entity
@Table(name = "Utente")
@Access(AccessType.PROPERTY)
@ReadOnly
public class UtenteAmb extends BaseEntity {

    //--croce di riferimento
    @ManyToOne
    private CroceAmb croce;

    //--milite di riferimento
    @OneToOne
    private VolontarioAmb milite;

    private String username;
    private String nickname;
    private String password;
    private String pass;
    private boolean enabled = true;
    private boolean account_expired = false;
    private boolean account_locked = false;
    private boolean password_expired = false;

    /**
     * Costruttore senza argomenti
     * Necessario per le specifiche JavaBean
     */
    public UtenteAmb() {
    }// end of constructor

    /**
     * Recupera una istanza della Entity usando la query standard della Primary Key
     * Nessun filtro sulla company, perché la primary key è unica
     *
     * @param id valore (unico) della Primary Key
     * @return istanza della Entity, null se non trovata
     */
    public static UtenteAmb find(long id, EntityManager manager) {
        if (manager != null) {
            return (UtenteAmb) AQuery.find(UtenteAmb.class, id, manager);
        }// end of if cycle
        return null;
    }// end of static method

    /**
     * Recupera una istanza della Entity usando la query per una property specifica
     *
     * @return istanza della Entity, null se non trovata
     */
    @SuppressWarnings("unchecked")
    public static List<UtenteAmb> getList(CroceAmb company, EntityManager manager) {
        return (List<UtenteAmb>) AQuery.getList(UtenteAmb.class, UtenteAmb_.croce, company, manager);
    }// end of method

    /**
     * Recupera una istanza della Entity usando la query per una property specifica
     *
     * @param volontario valore della property
     * @return istanza della Entity, null se non trovata
     */
    @SuppressWarnings("unchecked")
    public static UtenteAmb getEntityByVolontario(List<UtenteAmb> entities, VolontarioAmb volontario) {
        UtenteAmb instance = null;
        CroceAmb croce = volontario.getCroce();
        long keyID = volontario.getId();
        VolontarioAmb milite;

        if (entities != null && entities.size() > 0) {
            for (UtenteAmb utente : entities) {
                milite = utente.getMilite();
                if (milite != null) {
                    if (utente.getCroce() == croce && milite.getId() == keyID) {
                        instance = utente;
                        break;
                    }// end of if cycle
                }// end of if cycle
            }// end of for cycle
        }// end of if cycle

        return instance;
    }// end of method

    public CroceAmb getCroce() {
        return croce;
    }// end of getter method

    public void setCroce(CroceAmb croce) {
        this.croce = croce;
    }//end of setter method

    public VolontarioAmb getMilite() {
        return milite;
    }// end of getter method

    public void setMilite(VolontarioAmb milite) {
        this.milite = milite;
    }//end of setter method

    public String getUsername() {
        return username;
    }// end of getter method

    public void setUsername(String username) {
        this.username = username;
    }//end of setter method

    public String getNickname() {
        return nickname;
    }// end of getter method

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }//end of setter method

    public String getPassword() {
        return password;
    }// end of getter method

    public void setPassword(String password) {
        this.password = password;
    }//end of setter method

    public String getPass() {
        return pass;
    }// end of getter method

    public void setPass(String pass) {
        this.pass = pass;
    }//end of setter method

    public boolean isEnabled() {
        return enabled;
    }// end of getter method

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }//end of setter method

    public boolean isAccount_expired() {
        return account_expired;
    }// end of getter method

    public void setAccount_expired(boolean account_expired) {
        this.account_expired = account_expired;
    }//end of setter method

    public boolean isAccount_locked() {
        return account_locked;
    }// end of getter method

    public void setAccount_locked(boolean account_locked) {
        this.account_locked = account_locked;
    }//end of setter method

    public boolean isPassword_expired() {
        return password_expired;
    }// end of getter method

    public void setPassword_expired(boolean password_expired) {
        this.password_expired = password_expired;
    }//end of setter method
}// end of entity class
