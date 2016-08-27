package it.algos.wam.migration;

import it.algos.wam.entity.wamcompany.WamCompany;

/**
 * Created by Gac on 25 ago 2016.
 * Importa i dati -on line- della vecchia versione del programma (webambulanze)
 * Il nome della croce (company) può essere diverso
 */
public class Import {

    private final static String DB_OLD_LOCAL = "";
    private final static String DB_OLD_SERVER = "";
    private final static String DB_NEW_LOCAL = "";
    private final static String DB_NEW_SERVER = "";

    private String dbOld = DB_OLD_LOCAL; // cambiare alla fine dei test
    private String dbNew = DB_OLD_LOCAL; // cambiare alla fine dei test

    private String siglaCroceOld = "";
    private String siglaCroceNew = "";

    /**
     * Costruttore
     *
     * @param siglaCroceOld nome della company usata in webambulanze
     */
    public Import(String siglaCroceOld) {
        this(siglaCroceOld, siglaCroceOld);
    }// end of constructor

    /**
     * Costruttore
     *
     * @param siglaCroceOld nome della company usata in webambulanze
     * @param siglaCroceNew nome della company usata in wam
     */
    public Import(String siglaCroceOld, String siglaCroceNew) {
        this.siglaCroceOld = siglaCroceOld;
        this.siglaCroceNew = siglaCroceNew;
        inizia();
    }// end of constructor

    /**
     * Cerca una company con la sigla siglaCroceNew
     * La cancella, con tutti i dati
     * Cerca una company con la sigla siglaCroceOld
     * Importa i dati
     */
    private void inizia() {
        WamCompany companyNew = WamCompany.findByCode(siglaCroceNew);

        if (companyNew != null) {
            companyNew.delete();
        }// fine del blocco if

        importCroce();
    }// end of method

    /**
     * Importa la croce
     */
    private void importCroce() {
        Croce companyOld = Croce.findByCode(siglaCroceOld);
        WamCompany companyNew;

        if (companyOld == null) {
            return;
        }// fine del blocco if

        companyNew = new WamCompany();
        companyNew.setCompanyCode(companyOld.getSigla());
        companyNew.setName(companyOld.getDescrizione());
        companyNew.save();
    }// end of method

}// end of class
