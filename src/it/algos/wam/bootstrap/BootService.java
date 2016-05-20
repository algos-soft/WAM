package it.algos.wam.bootstrap;

import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.colorpicker.Color;
import it.algos.wam.WAMApp;
import it.algos.wam.entity.funzione.Funzione;
import it.algos.wam.entity.iscrizione.Iscrizione;
import it.algos.wam.entity.servizio.Servizio;
import it.algos.wam.entity.serviziofunzione.ServizioFunzione;
import it.algos.wam.entity.turno.Turno;
import it.algos.wam.entity.volontario.Volontario;
import it.algos.wam.entity.wamcompany.WamCompany;
import it.algos.webbase.web.entity.EM;
import it.algos.webbase.web.lib.LibDate;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by gac on 25 feb 2016.
 * Classe statica astratta.
 */
public abstract class BootService {

    /**
     * Creazione iniziale di una croce demo
     * Visibile a tutti
     * <p>
     * La crea SOLO se non esiste già
     */
    public static void creaCompanyDemo() {

        WamCompany company = creaCroceDemo();
        initCompany(company, true, true);
    }// end of static method

    /**
     * Inizializza una croce appena creata, con alcuni dati di esempio
     * Visibile solo a noi (developer)
     * Crea alcune funzioni standard
     * Crea una lista di volontari di esempio
     * Crea alcuni servizi di esempio
     *
     * @param company croce di appartenenza
     */
    public static void initCompany(WamCompany company) {
        initCompany(company, true, false);
    }// end of static method

    /**
     * Inizializza una croce appena creata, con alcuni dati di esempio
     * Visibile solo a noi (developer)
     * Crea alcune funzioni standard
     * Crea una lista di volontari di esempio
     * Crea alcuni servizi di esempio
     * Crea alcuni turni vuoti (opzionale)
     * Crea le iscrizioni per i turni creati (opzionale)
     *
     * @param company   croce di appartenenza
     * @param creaTurni flag per la creazione di turni vuoti
     * @param company   flag per la creazione delle iscrizioni per i turni
     */
    public static void initCompany(WamCompany company, boolean creaTurni, boolean creaIscrizioni) {
        ArrayList<Funzione> listaFunzioni;
        ArrayList<Volontario> listaVolontari;
        ArrayList<Servizio> listaServizi;
        ArrayList<Turno> listaTurni = null;
        EntityManager manager = EM.createEntityManager();

        listaFunzioni = creaFunzioni(company, manager);
        listaVolontari = creaVolontari(company, manager, listaFunzioni);
        listaServizi = creaServizi(company, manager, listaFunzioni);

        if (creaTurni) {
            listaTurni = creaTurniVuoti(company, manager, listaServizi);
        }// end of if cycle

        if (creaIscrizioni) {
            riempieTurni(company, manager, listaTurni, listaVolontari, listaServizi);
        }// end of if cycle

        manager.close();
    }// end of static method

    /**
     * Creazione iniziale dei dati generali per la croce demo
     * Li crea SOLO se non esistono già
     */
    private static WamCompany creaCroceDemo() {
        WamCompany company = WamCompany.findByCode(WAMApp.DEMO_COMPANY_CODE);

        if (company == null) {
            company = new WamCompany(WAMApp.DEMO_COMPANY_CODE, "Demo");
            company.setAddress1("Via Turati, 12");
            company.setAddress1("20199 Garbagnate Milanese");
            company.setContact("Mario Bianchi");
            company.setEmail("info@crocedemo.it");
            company.setVaiSubitoTabellone(true);
            company.save();
        }// end of if cycle
        return company;
    }// end of static method

    /**
     * Creazione iniziale di alcune funzioni standard per la croce selezionata
     * Le crea SOLO se non esistono già
     *
     * @param company croce di appartenenza
     * @param manager the EntityManager to use
     *
     * @return lista delle funzioni create
     */
    @SuppressWarnings("unchecked")
    private static ArrayList<Funzione> creaFunzioni(WamCompany company, EntityManager manager) {
        ArrayList<Funzione> listaFunzioni = new ArrayList<>();
        ArrayList lista = new ArrayList<>();

        if (company == null) {
            return null;
        }// end of if cycle

        lista.add(Arrays.asList("aut", "Aut", "Autista", FontAwesome.WHEELCHAIR));
        lista.add(Arrays.asList("soc", "Soc", "Soccorritore", FontAwesome.USER));
        lista.add(Arrays.asList("bar", "Bar", "Barelliere", FontAwesome.USER_MD));

        lista.add(Arrays.asList("aut-118", "Aut-118", "Autista emergenza abilitato 118", FontAwesome.WHEELCHAIR));
        lista.add(Arrays.asList("aut-msa", "Aut-msa", "Autista automedica abilitato 118", FontAwesome.AMBULANCE));
        lista.add(Arrays.asList("aut-amb", "Aut-amb", "Autista ambulanza abilitato 118", FontAwesome.AMBULANCE));
        lista.add(Arrays.asList("aut-ord", "Aut-ord", "Autista ordinario", FontAwesome.AMBULANCE));

        lista.add(Arrays.asList("soc-pri", "1° Soc", "Primo soccorritore", FontAwesome.USER));
        lista.add(Arrays.asList("soc-sec", "2° Soc", "Secondo soccorritore", FontAwesome.STETHOSCOPE));
        lista.add(Arrays.asList("soc-ter", "3° Soc", "Terzo soccorritore", FontAwesome.USER));

        lista.add(Arrays.asList("soc-dae", "DAE", "Soccorritore abilitato DAE", FontAwesome.USER));
        lista.add(Arrays.asList("soc-ord", "Soc", "Soccorritore ordinario", FontAwesome.USER));
        lista.add(Arrays.asList("bar-aff", "Bar-aff", "Barelliere in affiancamento", FontAwesome.USER));
        lista.add(Arrays.asList("avis", "Avis", "Operatore trasporto AVIS", FontAwesome.USER));
        lista.add(Arrays.asList("cent", "Cen", "Centralinista", FontAwesome.USER));

        for (int k = 0; k < lista.size(); k++) {
            listaFunzioni.add(creaFunzBase(company, manager, k + 1, (List) lista.get(k)));
        }// end of for cycle

        return listaFunzioni;
    }// end of static method

    /**
     * Creazione della singola funzione per la croce selezionata
     * La crea SOLO se non esiste già
     *
     * @param company  croce di appartenenza
     * @param manager  the EntityManager to use
     * @param ordine   di presentazione nelle liste
     * @param listaTmp di alcune property
     *
     * @return istanza di Funzione
     */
    private static Funzione creaFunzBase(WamCompany company, EntityManager manager, int ordine, List listaTmp) {
        String sigla = (String) listaTmp.get(0);
        String descrizione = (String) listaTmp.get(1);
        String note = (String) listaTmp.get(2);
        FontAwesome glyph = (FontAwesome) listaTmp.get(3);

        return Funzione.crea(company, manager, sigla, descrizione, ordine, note, glyph);
    }// end of static method

    /**
     * Creazione iniziale di alcuni volontari per la croce selezionata
     * Li crea SOLO se non esistono già
     *
     * @param company croce di appartenenza
     * @param manager the EntityManager to use
     * @param funz    lista delle funzioni di questa croce
     *
     * @return lista dei volontari creati
     */
    @SuppressWarnings("unchecked")
    private static ArrayList<Volontario> creaVolontari(WamCompany company, EntityManager manager, ArrayList<Funzione> funz) {
        ArrayList<Volontario> listaVolontari = new ArrayList<>();
        ArrayList lista = new ArrayList<>();

        if (company == null) {
            return null;
        }// end of if cycle

        lista.add(Arrays.asList("Mario", "Brambilla"));
        lista.add(Arrays.asList("Giovanna", "Durante", funz));
        lista.add(Arrays.asList("Diego", "Bertini", funz.get(3)));
        lista.add(Arrays.asList("Roberto", "Marchetti", funz.get(2), funz.get(3)));
        lista.add(Arrays.asList("Edoardo", "Politi"));
        lista.add(Arrays.asList("Sabina", "Roncelli"));
        lista.add(Arrays.asList("Lucia", "Casaroli", funz.get(1)));

        for (int k = 0; k < lista.size(); k++) {
            listaVolontari.add(creaVolBase(company, manager, (List) lista.get(k)));
        }// end of for cycle

        return listaVolontari;
    }// end of static method

    /**
     * Creazione iniziale di un volontario
     * Lo crea SOLO se non esiste già
     *
     * @param company  croce di appartenenza
     * @param manager  the EntityManager to use
     * @param listaTmp di alcune property
     *
     * @return istanza di Volontario
     */
    private static Volontario creaVolBase(WamCompany company, EntityManager manager, List listaTmp) {
        String nome = "";
        String cognome = "";
        Funzione[] funzioni = null;
        ArrayList lista = null;

        if (listaTmp.size() > 0 && listaTmp.get(0) instanceof String) {
            nome = (String) listaTmp.get(0);
        }// end of if cycle

        if (listaTmp.size() > 1 && listaTmp.get(1) instanceof String) {
            cognome = (String) listaTmp.get(1);
        }// end of if cycle

        if (listaTmp.size() > 2) {
            if (listaTmp.get(2) instanceof ArrayList) {
                lista = (ArrayList) listaTmp.get(2);
                funzioni = (Funzione[]) lista.toArray(new Funzione[lista.size()]);
            } else {
                lista = new ArrayList();
                for (int k = 2; k < listaTmp.size(); k++) {
                    if (listaTmp.get(k) instanceof Funzione) {
                        lista.add(listaTmp.get(k));
                    }// end of if cycle
                }// end of for cycle
                funzioni = (Funzione[]) lista.toArray(new Funzione[lista.size()]);
            }// end of if/else cycle
        }// end of if cycle

        return Volontario.crea(company, manager, nome, cognome, funzioni);
    }// end of static method

    /**
     * Creazione iniziale di alcuni servizi per la croce selezionata
     * Li crea SOLO se non esistono già
     *
     * @param company croce di appartenenza
     * @param manager the EntityManager to use
     * @param funz    lista delle funzioni di questa croce
     *
     * @return lista dei servizi creati
     */
    @SuppressWarnings("unchecked")
    private static ArrayList<Servizio> creaServizi(WamCompany company, EntityManager manager, ArrayList<Funzione> funz) {
        ArrayList<Servizio> listaServizi = new ArrayList<>();
        ArrayList lista = new ArrayList<>();
        int azzurro = new Color(146, 189, 255).getRGB();
        int verdino = new Color(146, 255, 189).getRGB();
        int rosa = new Color(255, 146, 211).getRGB();

        if (company == null) {
            return null;
        }// end of if cycle

        lista.add(Arrays.asList("med-mat", "Automedica mattino", 8, 12, true, azzurro, funz.get(0), funz.get(2), funz.get(5)));
        lista.add(Arrays.asList("med-pom", "Automedica pomeriggio", 12, 18, true, azzurro, funz.get(0), funz.get(2), funz.get(5)));
        lista.add(Arrays.asList("med-sera", "Automedica sera", 18, 22, true, azzurro, funz.get(0), funz.get(2), funz.get(5)));
        lista.add(Arrays.asList("amb-mat", "Ambulanza mattino", 8, 12, true, verdino, funz.get(1), funz.get(3)));
        lista.add(Arrays.asList("amb-pom", "Ambulanza pomeriggio", 12, 20, true, verdino, funz.get(1), funz.get(3)));
        lista.add(Arrays.asList("amb-notte", "Ambulanza notte", 20, 8, true, verdino, funz.get(1), funz.get(3)));
        lista.add(Arrays.asList("dim", "Dimissioni ordinarie", 0, 0, true, rosa, funz.get(1), funz.get(4)));
        lista.add(Arrays.asList("ext", "Extra", 0, 0, true, rosa, funz.get(1), funz.get(4)));
        lista.add(Arrays.asList("avis", "Avis", 0, 0, true, rosa, funz.get(1)));

        for (int k = 0; k < lista.size(); k++) {
            listaServizi.add(creaServBase(company, manager, k + 1, (List) lista.get(k)));
        }// end of for cycle

        return listaServizi;
    }// end of static method

    /**
     * Creazione iniziale di un servizio
     * La crea SOLO se non esiste già
     *
     * @param company  croce di appartenenza
     * @param manager  the EntityManager to use
     * @param ordine   di presentazione nelle liste
     * @param listaTmp di alcune property
     *
     * @return istanza di Servizio
     */
    private static Servizio creaServBase(WamCompany company, EntityManager manager, int ordine, List listaTmp) {
        String sigla = "";
        String descrizione = "";
        int oraInizio = 0;
        int oraFine = 0;
        boolean orario = false;
        int colore = 0;
        Funzione[] funzioni = null;
        ArrayList lista = null;

        if (listaTmp.size() > 0 && listaTmp.get(0) instanceof String) {
            sigla = (String) listaTmp.get(0);
        }// end of if cycle

        if (listaTmp.size() > 1 && listaTmp.get(1) instanceof String) {
            descrizione = (String) listaTmp.get(1);
        }// end of if cycle

        if (listaTmp.size() > 2 && listaTmp.get(2) instanceof Integer) {
            oraInizio = (Integer) listaTmp.get(2);
        }// end of if cycle

        if (listaTmp.size() > 3 && listaTmp.get(3) instanceof Integer) {
            oraFine = (Integer) listaTmp.get(3);
        }// end of if cycle

        if (listaTmp.size() > 4 && listaTmp.get(4) instanceof Boolean) {
            orario = (Boolean) listaTmp.get(4);
        }// end of if cycle

        if (listaTmp.size() > 5 && listaTmp.get(5) instanceof Integer) {
            colore = (Integer) listaTmp.get(5);
        }// end of if cycle

        if (listaTmp.size() > 6) {
            if (listaTmp.get(6) instanceof ArrayList) {
                lista = (ArrayList) listaTmp.get(6);
                funzioni = (Funzione[]) lista.toArray(new Funzione[lista.size()]);
            } else {
                lista = new ArrayList();
                for (int k = 6; k < listaTmp.size(); k++) {
                    if (listaTmp.get(k) instanceof Funzione) {
                        lista.add(listaTmp.get(k));
                    }// end of if cycle
                }// end of for cycle
                funzioni = (Funzione[]) lista.toArray(new Funzione[lista.size()]);
            }// end of if/else cycle
        }// end of if cycle

        return Servizio.crea(company, manager, ordine, sigla, descrizione, oraInizio, oraFine, orario, colore, funzioni);
    }// end of static method

    /**
     * Creazione iniziale di alcuni turni vuoti per la croce selezionata
     * Li crea SOLO se non esistono già
     *
     * @param company croce di appartenenza
     * @param manager the EntityManager to use
     * @param servizi lista dei servizi di questa croce
     *
     * @return lista dei turni creati
     */
    private static ArrayList<Turno> creaTurniVuoti(WamCompany company, EntityManager manager, ArrayList<Servizio> servizi) {
        ArrayList<Turno> listaTurni = new ArrayList<>();
        Date oggi = LibDate.today();

        for (int k = 0; k < 30; k++) {
            for (Servizio servizio : servizi) {
                listaTurni.add(Turno.crea(company, manager,servizio, LibDate.add(oggi, k)));
            }// end of for cycle
        }// end of for cycle

        return listaTurni;
    }// end of static method

    /**
     * Riempimento iniziale di alcuni turni vuoti per la croce selezionata
     *
     * @param company   croce di appartenenza
     * @param manager   the EntityManager to use
     * @param turni     lista dei turni di questa croce
     * @param volontari lista dei volontari di questa croce
     * @param servizi   lista dei servizi di questa croce
     */
    private static void riempieTurni(
            WamCompany company,
            EntityManager manager,
            ArrayList<Turno> turni,
            ArrayList<Volontario> volontari,
            ArrayList<Servizio> servizi) {
        Date oggi = LibDate.today();
        Servizio serv;
        Iscrizione isc;
        Turno turno;
        ArrayList<Funzione> funzioni;
//        Funzione funz;
        ArrayList<Iscrizione> iscrizioni = new ArrayList<>();

        for (int k = 0; k < 5; k++) {
//            for (int k = 0; k < turni.size(); k = k + 2) {
            turno = turni.get(k);
            serv = turno.getServizio();
            funzioni = serv.getFunzioni();
            for (Funzione funz : funzioni) {
                for (Volontario vol : volontari) {
                    if (vol.haFunzione(funz)) {
                        isc = new Iscrizione(turno, vol, new ServizioFunzione(serv, funz));
                        iscrizioni.add(isc);
                        isc.save(manager);
                    }// fine del blocco if
                } // fine del ciclo for-each
            } // fine del ciclo for-each
            turno.setIscrizioni(iscrizioni);
            turno.setAssegnato(true);
            turno.save(manager);
        } // fine del ciclo for

////        if (listaServizi != null && listaServizi.size() > 1) {
////            servizioMattina = listaServizi.get(1);
////        }// end of if cycle
//
////        for (int k = 0; k < 30; k++) {
////            Turno.crea(company, serv, LibDate.add(oggi, k));
////        }// end of for cycle
//
//        for (int k = 0; k < 30; k++) {
//            for (Servizio serv : listaServizi) {
//                iscrizioni = new ArrayList<Iscrizione>();
//                turno = Turno.find(serv, LibDate.add(oggi, k));
//                isc = new Iscrizione(turno, listaVolontari.get(3), null);
//                iscrizioni.add(isc);
//                if (turno != null) {
//                    turno.setIscrizioni(iscrizioni);
//                    turno.save();
//                }// end of if cycle
//            }// end of for cycle
//        }// end of for cycle

    }// end of static method

}// end of abstract static class
