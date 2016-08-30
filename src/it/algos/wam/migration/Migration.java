package it.algos.wam.migration;

import com.vaadin.server.FontAwesome;
import it.algos.wam.entity.funzione.Funzione;
import it.algos.wam.entity.servizio.Servizio;
import it.algos.wam.entity.wamcompany.Organizzazione;
import it.algos.wam.entity.wamcompany.WamCompany;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * Created by Gac on 25 ago 2016.
 * Importa i dati -on line- della vecchia versione del programma (webambulanze)
 * Il nome della croce (company) può essere diverso
 */
public class Migration {

    private final static String DB_OLD_LOCAL = "";
    private final static String DB_OLD_SERVER = "";
    private final static String DB_NEW_LOCAL = "";
    private final static String DB_NEW_SERVER = "";
    private final static boolean SIGLA_MAIUSCOLA = false;
    private String dbOld = DB_OLD_LOCAL; // cambiare alla fine dei test
    private String dbNew = DB_OLD_LOCAL; // cambiare alla fine dei test


    /**
     * Costruttore
     */
    public Migration() {
        List<Croce> listaVecchieCrociEsistenti = Croce.findAll();

        if (listaVecchieCrociEsistenti != null) {
            for (Croce company : listaVecchieCrociEsistenti) {
                inizia(company, company.getSigla());
            }// end of for cycle
        }// end of if cycle

    }// end of constructor


    /**
     * Costruttore
     *
     * @param siglaCompanyOld nome della company usata in webambulanze
     */
    public Migration(String siglaCompanyOld) {
        this(siglaCompanyOld, siglaCompanyOld);
    }// end of constructor


    /**
     * Costruttore
     *
     * @param siglaCompanyOld nome della company usata in webambulanze
     * @param siglaCompanyNew nome della company usata in wam
     */
    public Migration(String siglaCompanyOld, String siglaCompanyNew) {
        Croce companyOld = Croce.findBySigla(siglaCompanyOld);
        inizia(companyOld, siglaCompanyNew);
    }// end of constructor

    /**
     * Cerca una company con la sigla siglaCroceNew
     * La cancella, con tutti i dati
     * Cerca una company con la sigla siglaCroceOld
     * Importa i dati
     *
     * @param companyOld      company usata in webambulanze
     * @param siglaCompanyNew nome della company usata in wam
     */
    private void inizia(Croce companyOld, String siglaCompanyNew) {
        WamCompany companyNew = WamCompany.findByCode(siglaCompanyNew);

        if (companyNew != null) {
            companyNew.delete();
        }// fine del blocco if

        companyNew = importSingolaCroce(companyOld, siglaCompanyNew);
        importFunzioni(companyOld, companyNew);
        importServizi(companyOld, companyNew);
    }// end of method

    /**
     * Importa la croce
     *
     * @param companyOld      company usata in webambulanze
     * @param siglaCompanyNew nome della company da usare in wam
     * @return la nuova company
     */
    private WamCompany importSingolaCroce(Croce companyOld, String siglaCompanyNew) {
        WamCompany companyNew;

        if (SIGLA_MAIUSCOLA) {
            siglaCompanyNew = siglaCompanyNew.toUpperCase();
        } else {
            siglaCompanyNew = siglaCompanyNew.toLowerCase();
        }// end of if/else cycle

        companyNew = new WamCompany();
        companyNew.setCompanyCode(siglaCompanyNew);
        companyNew.setOrganizzazione(Organizzazione.get(companyOld.getOrganizzazione()));
        companyNew.setName(companyOld.getDescrizione());
        companyNew.setAddress1(companyOld.getIndirizzo());
        companyNew.setPresidente(companyOld.getPresidente());
        companyNew.save();

        return companyNew;
    }// end of method


    /**
     * Importa le funzioni della croce
     *
     * @param companyOld company usata in webambulanze
     * @param companyNew company usata in wam
     */
    private void importFunzioni(Croce companyOld, WamCompany companyNew) {
        List<FunzioneAmb> lista = FunzioneAmb.findAll(companyOld);

        if (lista != null && lista.size() > 0) {
            FunzioneAmb funzA = FunzioneAmb.findByCompanyAndSigla(companyOld, "aut");
        }// end of if cycle

        if (lista != null && lista.size() > 0) {
            for (FunzioneAmb funz : lista) {
                creaSingolaFunzione(companyNew, funz);
            }// end of for cycle
        }// end of if cycle

    }// end of method


    /**
     * Crea la singola funzione
     *
     * @param companyNew  company usata in wam
     * @param funzioneOld della companyOld
     * @return la nuova funzione
     */
    private Funzione creaSingolaFunzione(WamCompany companyNew, FunzioneAmb funzioneOld) {
        String sigla = funzioneOld.getSigla_visibile();
        int ordine = funzioneOld.getOrdine();
        String descrizione = funzioneOld.getDescrizione();
        FontAwesome glyph = selezionaIcona(descrizione);

        return Funzione.crea(companyNew, (EntityManager) null, sigla, ordine, descrizione, glyph);
    }// end of method


    /**
     * Elabora la vecchia descrizione per selezionare una icona adeguata
     *
     * @param descrizione usata in webambulanze
     * @return la FontAwesome selezionata
     */
    private FontAwesome selezionaIcona(String descrizione) {
        FontAwesome glyph = null;
        String autista = "utista";
        String soc = "Soccorritore";
        String soc2 = "Primo";
        String ser = "Centralino";
        String ser2 = "Pulizie";
        String ser3 = "Ufficio";

        if (descrizione.contains(autista)) {
            glyph = FontAwesome.AMBULANCE;
        } else {
            if (descrizione.contains(soc) || descrizione.contains(soc2)) {
                glyph = FontAwesome.HEART;
            } else {
                if (descrizione.contains(ser) || descrizione.contains(ser2) || descrizione.contains(ser3)) {
                    glyph = FontAwesome.USER;
                } else {
                    glyph = FontAwesome.STETHOSCOPE;
                }// end of if/else cycle
            }// end of if/else cycle
        }// end of if/else cycle

        return glyph;
    }// end of method


    /**
     * Importa i servizi della croce
     *
     * @param companyOld company usata in webambulanze
     * @param companyNew company usata in wam
     */
    private void importServizi(Croce companyOld, WamCompany companyNew) {
        List<ServizioAmb> lista = ServizioAmb.findAll(companyOld);

        if (lista != null && lista.size() > 0) {
            ServizioAmb funzA = ServizioAmb.findByCompanyAndSigla(companyOld, "aut");
        }// end of if cycle

        if (lista != null && lista.size() > 0) {
            for (ServizioAmb serv : lista) {
                creaSingoloServizio(companyNew, serv);
            }// end of for cycle
        }// end of if cycle

    }// end of method


    /**
     * Crea il singola servizio
     *
     * @param companyNew  company usata in wam
     * @param servizioOld della companyOld
     * @return il nuovo servizio
     */
    private Servizio creaSingoloServizio(WamCompany companyNew, ServizioAmb servizioOld) {
//        String sigla = funzioneOld.getSigla_visibile();
//        int ordine = funzioneOld.getOrdine();
//        String descrizione = funzioneOld.getDescrizione();
//        FontAwesome glyph = selezionaIcona(descrizione);

//        return Servizio.crea(companyNew, (EntityManager)null, sigla, ordine, descrizione, glyph);
        return null;
    }// end of method

}// end of class
