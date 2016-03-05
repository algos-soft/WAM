package it.algos.wam.ui;

import com.vaadin.server.Page;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.*;
import it.algos.wam.WAMApp;
import it.algos.wam.entity.company.Company;
import it.algos.wam.entity.servizio.Servizio;
import it.algos.wam.entity.turno.Turno;
import it.algos.wam.lib.LibWam;
import it.algos.wam.tabellone.CTabellone;
import it.algos.wam.tabellone.EngineTab;
import it.algos.wam.tabellone.WRigaTab;
import it.algos.wam.tabellone.WTabellone;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by alex on 05/03/16.
 */
public class TabelloneUI extends UI {

    private Placeholder placeholder;

    @Override
    protected void init(VaadinRequest request) {

        // set theme
        String themeName;
        if (Page.getCurrent().getWebBrowser().isTouchDevice()) {
            themeName = "wam-mob";
        } else {
            themeName = "wam";
        }
        setTheme(themeName);

        // crea e aggiungi i componenti, assegna il contenuto alla UI
        VerticalLayout layout = new VerticalLayout();
        layout.addComponent(creaCompTitolo());
        placeholder=new Placeholder();
        layout.addComponent(placeholder);
        setContent(layout);

        // crea il tabellone e lo mette nel placeholder
        WTabellone wrapper = creaRighe();
        CTabellone tab = EngineTab.creaTabellone(wrapper);
        placeholder.setComponent(tab);

        // aggiunge i listener al tabellone
        tab.addClickCellListener(new CTabellone.ClickCellListener() {
            @Override
            public void cellClicked(CTabellone.ClickCellEvent e) {
                TabelloneUI.this.cellClicked(e.getData(), e.getRow());
            }
        });


    }

    /**
     * Crea il componente che visualizza il titolo, sempre presente nella UI
     *
     * @return il componente con il titolo
     */
    private Component creaCompTitolo() {
        VerticalLayout layout = new VerticalLayout();
        //layout.setMargin(true);
        Component label = new Label("Croce di prova");
        layout.addComponent(label);
        layout.addStyleName("redBg");
        return layout;
    }


    /**
     * Crea i wrapper per le righe di tabellone
     * Un wrapper per ogni servizio
     */
    private WTabellone creaRighe() {

        LocalDate d1=LocalDate.of(2016, 2, 28);
        LocalDate d2=d1.plusDays(6);
        WTabellone wtab =new WTabellone(d1, d2);

        Company company = Company.findByCode(WAMApp.TEST_COMPANY_CODE);
        ArrayList<Servizio> listaServizi = null;

        int primoGiorno = LibWam.creaChiave(d1);

        if (company != null) {
            listaServizi = Servizio.findAll(company);
        }

        if (listaServizi != null && listaServizi.size() > 0) {

            long giorni = d1.until( d2, ChronoUnit.DAYS)+1;

            for (Servizio servizio : listaServizi) {


                List<Turno> turni = new ArrayList<>();

                // todo qui fare una sola query dal... al... non un ciclo!
                for (int chiave = primoGiorno; chiave < primoGiorno+giorni; chiave++) {
                    Turno turno = Turno.find(company, servizio, chiave);
                    if (turno!=null){
                        turni.add(turno);
                    }
                }
                wtab.add(new WRigaTab(servizio, turni.toArray(new Turno[0])));
            }
        }

        return wtab;

    }

    private void cellClicked(LocalDate data, int row){
        int a = 87;
        int b = 1;
    }

    /**
     * Placeholder dei contenuti.
     * Consente di switchare tra tabellone, edit turno o altro
     */
    class Placeholder extends CustomComponent {
        public Placeholder() {
            setCompositionRoot(null);
        }

        public void setComponent(Component comp){
            setCompositionRoot(comp);
        }
    }

}
