package it.algos.wam.ui;

import com.vaadin.navigator.Navigator;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.*;
import it.algos.wam.WAMApp;
import it.algos.wam.entity.wamcompany.WamCompany;
import it.algos.wam.entity.servizio.Servizio;
import it.algos.wam.entity.turno.Turno;
import it.algos.wam.lib.LibWam;
import it.algos.wam.tabellone.*;
import it.algos.webbase.web.lib.DateConvertUtils;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by alex on 05/03/16.
 */
public class TabelloneUI extends UI {

    private Panel placeholder;
    private Navigator navigator;

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
        layout.setSizeFull();
        layout.addComponent(creaCompTitolo());

        placeholder=new Panel();
        placeholder.addStyleName("pinkBg");
        placeholder.setHeight("100%");
        placeholder.setWidth("100%");
        layout.addComponent(placeholder);
        layout.setExpandRatio(placeholder, 1);
        setContent(layout);

        // crea il tabellone
        WTabellone wrapper = creaRighe();
        CTabellone tab = EngineTab.creaTabellone(wrapper);
        tab.setSizeUndefined();


        // aggiunge un listener per la cella cliccata al tabellone
        tab.addClickCellListener(new CTabellone.ClickCellListener() {
            @Override
            public void cellClicked(CTabellone.ClickCellEvent e) {
                TabelloneUI.this.cellClicked(e.getTipo(), e.getCol(), e.getRow(), e.getCellObject());
            }
        });

        // crea un navigator e lo registra sul componente placeholder
        // il navigator sostituisce i componenti dentro al placeolder
        navigator = new Navigator(this, placeholder);
        navigator.addView("tabellone", tab);
        navigator.navigateTo("tabellone");


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

        LocalDate d1=LocalDate.of(2016, 3, 2);
        LocalDate d2=d1.plusDays(6);
        WTabellone wtab =new WTabellone(d1, d2);

        WamCompany company = WamCompany.findByCode(WAMApp.TEST_COMPANY_CODE);
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

    /**
     * E' stata cliccata una cella del tabellone
     */
    private void cellClicked(CellType tipo, int col, int row, Object cellObject){
        Turno turno=null;
        CTurnoEditor editor;
        switch (tipo){
            case TURNO:
                turno = (Turno)cellObject;
                break;
            case NO_TURNO:
                InfoNewTurnoWrap wrapper = (InfoNewTurnoWrap)cellObject;
                LocalDate dInizio=wrapper.getData();
                Servizio serv = wrapper.getServizio();
                turno = new Turno();
                turno.setInizio(DateConvertUtils.asUtilDate(dInizio));
                turno.setServizio(serv);
                break;
        }

        editor = new CTurnoEditor(turno);
        editor.addDismissListener(new CTurnoEditor.DismissListener() {
            @Override
            public void editorDismissed(CTurnoEditor.DismissEvent e) {
                navigator.navigateTo("tabellone");
            }
        });
        navigator.addView("turno",editor);
        navigator.navigateTo("turno");

    }



//    /**
//     * Placeholder dei contenuti.
//     * Consente di switchare tra tabellone, edit turno o altro
//     */
//    class Placeholder extends CustomComponent {
//        public Placeholder() {
//            setCompositionRoot(null);
//        }
//
//        public void setComponent(Component comp){
//            setCompositionRoot(comp);
//        }
//    }

}
