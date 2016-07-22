package it.algos.wam.turnigenerator;

import com.vaadin.data.Property;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import it.algos.wam.entity.servizio.Servizio;
import it.algos.wam.query.WamQuery;
import it.algos.wam.tabellone.CTabelloneEditor;
import it.algos.webbase.web.dialog.ConfirmDialog;
import it.algos.webbase.web.field.DateField;
import it.algos.webbase.web.lib.DateConvertUtils;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

/**
 * Componente per preparare il generatore/eliminatore di turni vuoti
 */
public class CTurniGenerator extends CTabelloneEditor {

    private DateField dateField1;
    private DateField dateField2;
    private ProgressBar progressBar;
    private boolean generatorRunning;
    ArrayList<Servizio> servizi;
    CheckBox[][] matrix;

    public CTurniGenerator(EntityManager entityManager) {
        super(entityManager);

        addComponent(creaCompTitolo());
        addComponent(creaCompDettaglio());

        Component panComandi=creaPanComandi();
        addComponent(panComandi);

        setComponentAlignment(panComandi, Alignment.BOTTOM_CENTER);

    }


    /**
     * Crea il componente che visualizza il titolo
     *
     * @return il componente titolo
     */
    private Component creaCompTitolo() {
        VerticalLayout layout = new VerticalLayout();
        layout.addComponent(new Label("<strong>" + "Generatore turni vuoti" + "</strong>", ContentMode.HTML));
        return layout;
    }



    /**
     * Crea il componente che visualizza i dettagli del turno
     * (nome ecc..)
     *
     * @return il componente di dettaglio
     */
    private Component creaCompDettaglio() {

        VerticalLayout layout = new VerticalLayout();
        layout.setSpacing(true);

        HorizontalLayout layoutDate = new HorizontalLayout();
        layoutDate.setSpacing(true);
        dateField1 = new DateField("Dal giorno");
        LocalDate tomorrow=LocalDate.now().plusDays(1);
        dateField1.setValue(DateConvertUtils.asUtilDate(tomorrow));
        dateField1.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent valueChangeEvent) {
                Object value = valueChangeEvent.getProperty().getValue();
                if (value != null && value instanceof Date) {
                    Date date = (Date) value;
                    LocalDate d2 = DateConvertUtils.asLocalDate(date).plusWeeks(1);
                    dateField2.setValue(DateConvertUtils.asUtilDate(d2));
                }
            }
        });


        dateField2 = new DateField("Al giorno");
        LocalDate date = DateConvertUtils.asLocalDate(dateField1.getValue());
        Date duWeek=DateConvertUtils.asUtilDate(date.plusWeeks(1));
        dateField2.setValue(duWeek);

        layoutDate.addComponent(dateField1);
        layoutDate.addComponent(dateField2);

        servizi=new ArrayList<>();
        servizi.addAll(WamQuery.queryServizi(entityManager, true));
        servizi.addAll(WamQuery.queryServizi(entityManager, false));

        GridLayout grid = new GridLayout(9,servizi.size()+2);
        grid.setSpacing(true);

        grid.addComponent(new Label("Lun"), 1, 0);
        grid.addComponent(new Label("Mar"), 2, 0);
        grid.addComponent(new Label("Mer"), 3, 0);
        grid.addComponent(new Label("Gio"), 4, 0);
        grid.addComponent(new Label("Ven"), 5, 0);
        grid.addComponent(new Label("Sab"), 6, 0);
        grid.addComponent(new Label("Dom"), 7, 0);

        int rows = servizi.size();
        int cols = 7;
        matrix = new CheckBox[rows][cols];

        int row=0;
        for(Servizio serv : servizi){

            // nome servizio
            Label label = new Label(serv.getDescrizione());
            grid.addComponent(label, 0, row+1);

            // riga orizzontale di checkboxes
            for(int col=0; col<7; col++){
                CheckBox cb = new CheckBox();
                grid.addComponent(cb, col+1, row+1);
                matrix[row][col]=cb;
            }

            // componente multiselettore per riga
            int row1=row;
            SwitchOnOffH onOff=new SwitchOnOffH(new SwitchListener() {

                @Override
                public void clickedOn() {
                    turnRow(matrix, row1, true);
                }

                @Override
                public void clickedOff() {
                    turnRow(matrix, row1, false);
                }
            });
            grid.addComponent(onOff, 8, row+1);

            row++;

        }

        // componenti multiselettore per colonna
        for(int col=1; col<=7; col++){
            int col1=col-1;
            SwitchOnOffV onOff=new SwitchOnOffV(new SwitchListener() {
                @Override
                public void clickedOn() {
                    turnColumn(matrix, col1, true);
                }

                @Override
                public void clickedOff() {
                    turnColumn(matrix, col1, false);
                }
            });
            grid.addComponent(onOff, col, row+1);
        }

        // componente multiselettore globale
        SwitchOnOffH onOffGen=new SwitchOnOffH(new SwitchListener() {
            @Override
            public void clickedOn() {
                for(int row=0; row<matrix.length; row++){
                    turnRow(matrix, row, true);
                }
            }

            @Override
            public void clickedOff() {
                for(int row=0; row<matrix.length; row++){
                    turnRow(matrix, row, false);
                }
            }
        }, FontAwesome.CHECK_SQUARE, FontAwesome.SQUARE);
        // nell' ultima cella in basso a dx
//        onOffGen.setCaption("tutto");
        grid.addComponent(onOffGen, grid.getColumns()-1, grid.getRows()-1);
        grid.setComponentAlignment(onOffGen, Alignment.BOTTOM_CENTER);


        layout.addComponent(layoutDate);
        layout.addComponent(grid);
        return layout;
    }

    private void turnRow(CheckBox[][] matrix, int row, boolean state){
        for(int i=0;i<matrix[row].length;i++){
            matrix[row][i].setValue(state);
        }
    }

    private void turnColumn(CheckBox[][] matrix, int col, boolean state){
        CheckBox[] boxes = getColumn(matrix, col);
        for(CheckBox cb : boxes){
            cb.setValue(state);
        }
    }


    private static CheckBox[] getColumn(CheckBox[][] array, int index){
        CheckBox[] column = new CheckBox[array.length];
        for(int i=0; i<column.length; i++){
            column[i] = array[i][index];
        }
        return column;
    }



    /**
     * Crea e ritorna il pannello comandi.
     *
     * @return il pannello comandi
     */
    private Component creaPanComandi() {
        HorizontalLayout layout = new HorizontalLayout();
        layout.setMargin(true);
        layout.setSpacing(true);

        progressBar = new ProgressBar();
        progressBar.setCaption("");
        progressBar.setWidth("12em");
//        progressBar.addStyleName("invisible");

        Button bAnnulla = new Button("Annulla");
        bAnnulla.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                fireDismissListeners(new DismissEvent(bAnnulla, false, false));
            }
        });

        Button bEsegui = new Button("Esegui");
        bEsegui.setWidth("8em");
        bEsegui.setClickShortcut(ShortcutAction.KeyCode.ENTER);
        bEsegui.addStyleName(ValoTheme.BUTTON_PRIMARY);    // "primary" not "default"
        bEsegui.addClickListener(new Button.ClickListener() {

            TurniGenEngine generator;

            @Override
            public void buttonClick(Button.ClickEvent event) {

                if(!generatorRunning){

                    // resetta la progress bar
                    // Attenzione! la UI deve essere @Push per usare UI.access()
                    getUI().access(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setCaption("");
                            progressBar.setValue(0f);
                        }
                    });

                    // crea i dati e controlla se sono validi
                    GeneratorData data = createGeneratorData();
                    String err= data.checkValid();
                    if(!err.equals("")){
                        Notification.show("Dati non validi", err, Notification.Type.ERROR_MESSAGE);
                        return;
                    }

                    generator = new TurniGenEngine(data);


                    // aggiunge un listener per essere informato ogni volta che un turno è fatto
                    // e aggiornare la progressbar
                    generator.addTurnoProgressListener(new TurniGenEngine.TurnoProgressListener() {

                        int quanti = 0;

                        @Override
                        public void progressUpdate(int progress) {

                            quanti =progress;

                            getUI().access(new Runnable() {
                                @Override
                                public void run() {
                                    float progress = (float) quanti / (generator.getQuantiTurni());
                                    progressBar.setCaption(quanti + "/" + generator.getQuantiTurni());
                                    progressBar.setValue(progress);
                                }
                            });


                        }
                    });

                    // aggiunge un listener per essere informato quando il lavoro è finito
                    generator.addEngineDoneListener(new TurniGenEngine.EngineDoneListener() {
                        @Override
                        public void engineDone() {
                            getUI().access(new Runnable() {
                                @Override
                                public void run() {

                                    // aspetta un attimo che il thread finisca di aggiornare la barra
                                    // prima di lanciare l'evento di fine
                                    try {
                                        Thread.sleep(200);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }

                                    fireDismissListeners(new DismissEvent(bAnnulla, false, false));
                                    generatorRunning=false;

                                }
                            });
                        }
                    });

                    // avvia il generatore in un thread separato
                    GeneratorThread thread = new GeneratorThread(generator);
                    thread.start();
                    generatorRunning=true;

                    // disabilita i bottoni
                    bAnnulla.setEnabled(false);
                    bEsegui.setCaption("Interrompi");

                }else{

                    new ConfirmDialog("", "Sei sicuro?", new ConfirmDialog.Listener() {
                        @Override
                        public void onClose(ConfirmDialog dialog, boolean confirmed) {
                            if(confirmed){
                                generator.abort();
                                bEsegui.setCaption("Esegui");
                            }
                        }
                    }).show();

                }

            }

        });

        layout.addComponent(progressBar);
        layout.addComponent(bAnnulla);
        layout.addComponent(bEsegui);

        return layout;
    }

    /**
     * Crea il wrapper dei dati per il motore di generazione
     * @return il wrapper dei dati
     */
    private GeneratorData createGeneratorData() {
        GeneratorData data = new GeneratorData(getData1(), getData2(), GeneratorData.ACTION_CREATE);

        for(int giorno=0; giorno<7; giorno++){
            ArrayList<Servizio> servChecked=new ArrayList<>();
            int row=0;
            for(Servizio serv : servizi){
                CheckBox check = matrix[row][giorno];
                if(check.getValue()){
                    servChecked.add(serv);
                }
                row++;
            }
            data.putGiorno(giorno, servChecked.toArray(new Servizio[0]));
        }
        return data;
    }



    private class SwitchOnOffH extends HorizontalLayout{

        public SwitchOnOffH(SwitchListener swListener, FontAwesome iconOn, FontAwesome iconOff) {

            setSizeUndefined();
            setSpacing(true);
            addStyleName("icon-green");

            HorizontalLayout layOn = new HorizontalLayout();
            layOn.addComponent(new Label(iconOn.getHtml() , ContentMode.HTML));
            layOn.addLayoutClickListener(layoutClickEvent -> {
                swListener.clickedOn();
            });

            HorizontalLayout layOff = new HorizontalLayout();
            layOff.addComponent(new Label(iconOff.getHtml() , ContentMode.HTML));
            layOff.addLayoutClickListener(layoutClickEvent -> {
                swListener.clickedOff();
            });

            addComponent(layOn);
            addComponent(layOff);

        }

        public SwitchOnOffH(SwitchListener swListener) {
            this(swListener, FontAwesome.CHECK_SQUARE_O, FontAwesome.SQUARE_O);
        }
    }

    interface SwitchListener{
        void clickedOn();
        void clickedOff();
    }


    private class SwitchOnOffV extends VerticalLayout{
        public SwitchOnOffV(SwitchListener swListener) {

            setSizeUndefined();
            setSpacing(false);
            addStyleName("icon-green");

            HorizontalLayout layOn = new HorizontalLayout();
            layOn.addComponent(new Label(FontAwesome.CHECK_SQUARE_O.getHtml() , ContentMode.HTML));
            layOn.addLayoutClickListener(layoutClickEvent -> {
                swListener.clickedOn();
            });

            HorizontalLayout layOff = new HorizontalLayout();
            layOff.addComponent(new Label(FontAwesome.SQUARE_O.getHtml() , ContentMode.HTML));
            layOff.addLayoutClickListener(layoutClickEvent -> {
                swListener.clickedOff();
            });


            addComponent(layOn);
            addComponent(layOff);

        }

    }


    private Date getData1(){
        return dateField1.getValue();
    }

    private Date getData2(){
        return dateField2.getValue();
    }


}
