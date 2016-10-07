package it.algos.wam.migration;

import com.vaadin.server.FontAwesome;
import it.algos.wam.entity.funzione.Funzione;
import it.algos.wam.entity.servizio.Servizio;
import it.algos.wam.entity.serviziofunzione.ServizioFunzione;
import it.algos.wam.entity.wamcompany.Organizzazione;
import it.algos.wam.entity.wamcompany.WamCompany;
import it.algos.webbase.web.lib.LibArray;

import java.util.ArrayList;
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
        List<Croce> listaVecchieCrociDaImportare = selezionaCrodiDaImportare(listaVecchieCrociEsistenti);

        if (listaVecchieCrociDaImportare != null) {
            for (Croce company : listaVecchieCrociDaImportare) {
                inizia(company, company.getSigla().toLowerCase());
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
     * Seleziona le croci da importare
     * Elimina ALGOS, di servizio
     * Elimina DEMO, da ricreare ex-nove
     * Elimina PAVT, non più attiva
     *
     * @param listaVecchieCrociEsistenti di webambulanze
     * @return croci da importare da webambulanze in WAM
     */
    private List<Croce> selezionaCrodiDaImportare(List<Croce> listaVecchieCrociEsistenti) {
        List<Croce> listaVecchieCrociDaImportare = new ArrayList<>();
        String[] escluse = {"ALGOS", "DEMO", "PAVT"};
        ArrayList<String> listaEscluse = LibArray.fromString(escluse);
        String code;

        for (Croce croce : listaVecchieCrociEsistenti) {
            code = croce.getSigla();
            if (!listaEscluse.contains(code)) {
                listaVecchieCrociDaImportare.add(croce);
            }// end of if cycle
        }// end of for cycle

        return listaVecchieCrociDaImportare;
    }// end of method

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
        List<Funzione> listaFunzioni;

        if (companyNew != null) {
            companyNew.delete();
        }// fine del blocco if

        companyNew = importSingolaCroce(companyOld, siglaCompanyNew);
        listaFunzioni = importFunzioni(companyOld, companyNew);
        importServizi(companyOld, companyNew, listaFunzioni);
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
    private List<Funzione> importFunzioni(Croce companyOld, WamCompany companyNew) {
        List<Funzione> listaFunzioniNuove = new ArrayList<>();
        Funzione funzioneCreata;
        List<FunzioneAmb> lista = FunzioneAmb.findAll(companyOld);

        if (lista != null && lista.size() > 0) {
            for (FunzioneAmb funz : lista) {
                funzioneCreata = creaSingolaFunzione(companyNew, funz);
                listaFunzioniNuove.add(funzioneCreata);
            }// end of for cycle
        }// end of if cycle

        return listaFunzioniNuove;
    }// end of method

    /**
     * Crea la singola funzione
     *
     * @param companyNew  company usata in wam
     * @param funzioneOld della companyOld
     * @return la nuova funzione
     */
    private Funzione creaSingolaFunzione(WamCompany companyNew, FunzioneAmb funzioneOld) {
        String code = funzioneOld.getSigla();
        String sigla = funzioneOld.getSigla_visibile();
        int ordine = funzioneOld.getOrdine();
        String descrizione = funzioneOld.getDescrizione();
        FontAwesome glyph = selezionaIcona(descrizione);

        return Funzione.crea(companyNew, code, sigla, descrizione, ordine, glyph);
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
     * @param companyOld    company usata in webambulanze
     * @param companyNew    company usata in wam
     * @param listaFunzioni nuove appena create
     */
    private List<Servizio> importServizi(Croce companyOld, WamCompany companyNew, List<Funzione> listaFunzioni) {
        List<Servizio> listaServiziNuovi = new ArrayList<>();
        List<ServizioAmb> lista = ServizioAmb.findAll(companyOld);
        Servizio servizioCreato;

        if (lista != null && lista.size() > 0) {
            for (ServizioAmb serv : lista) {
                servizioCreato = creaSingoloServizio(companyNew, serv, listaFunzioni);
                listaServiziNuovi.add(servizioCreato);
            }// end of for cycle
        }// end of if cycle

        return listaServiziNuovi;
    }// end of method

    /**
     * Crea il singola servizio
     *
     * @param companyNew       company usata in wam
     * @param servizioOld      della companyOld
     * @param listaFunzioniAll nuove appena create
     * @return il nuovo servizio
     */
    @SuppressWarnings("all")
    private Servizio creaSingoloServizio(WamCompany companyNew, ServizioAmb servizioOld, List<Funzione> listaFunzioniAll) {
        int ordine = servizioOld.getOrdine();
        String sigla = servizioOld.getSigla();
        String descrizione = servizioOld.getDescrizione();
        int numObbligatorie = servizioOld.getFunzioni_obbligatorie();
        List<ServizioFunzione> listaServizioFunzioni = recuperaFunzioni(companyNew, servizioOld, numObbligatorie, listaFunzioniAll);
        int colore = 0;
        int oraInizio = servizioOld.getOra_inizio();
        int minutiInizio = servizioOld.getMinuti_inizio();
        int oraFine = servizioOld.getOra_fine();
        int minutiFine = servizioOld.getMinuti_fine();

        return Servizio.crea(companyNew, sigla, true, descrizione, ordine, colore, true, oraInizio, minutiInizio, oraFine, minutiFine, null, listaServizioFunzioni);
    }// end of method

    /**
     * Crea la lista delle funzioni per il servizio
     *
     * @param servizioOld      della companyOld
     * @param listaFunzioniAll nuove appena create
     * @return lista di nuove funzioni
     */
    @SuppressWarnings("all")
    private List<ServizioFunzione> recuperaFunzioni(WamCompany companyNew, ServizioAmb servizioOld, int numObbligatorie, List<Funzione> listaFunzioniAll) {
        List<ServizioFunzione> listaServizioFunzioni = new ArrayList<>();
        ServizioFunzione servFunz;
        FunzioneAmb funzOld = null;
        String code;
        boolean obbligatoria = false;
        ArrayList<Long> chiavi = new ArrayList();
        chiavi.add(servizioOld.getFunzione1_id());
        chiavi.add(servizioOld.getFunzione2_id());
        chiavi.add(servizioOld.getFunzione3_id());
        chiavi.add(servizioOld.getFunzione4_id());
        int pos = 0;

        for (long key : chiavi) {
            pos++;
            funzOld = null;
            if (key > 0) {
                funzOld = FunzioneAmb.find(key);
            }// end of if cycle
            if (funzOld != null) {
                code = funzOld.getSigla();
                for (Funzione funz : listaFunzioniAll) {
                    obbligatoria = false;
                    if (code.equals(funz.getCode())) {
                        if (pos <= numObbligatorie) {
                            obbligatoria = true;
                        }// end of if cycle
                        servFunz = new ServizioFunzione(companyNew, null, funz, obbligatoria, pos);
                        listaServizioFunzioni.add(servFunz);
                    }// end of if cycle
                }// end of for cycle
            }// end of if cycle
        }// end of for cycle

        return listaServizioFunzioni;
    }// end of method

}// end of class
