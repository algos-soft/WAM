package it.algos.wam.tabellone;

import com.vaadin.event.LayoutEvents;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import it.algos.wam.entity.servizio.Servizio;
import it.algos.wam.entity.serviziofunzione.ServizioFunzione;
import it.algos.wam.entity.turno.Turno;
import it.algos.wam.entity.iscrizione.Iscrizione;
import it.algos.wam.settings.CompanyPrefs;
import it.algos.webbase.web.lib.LibSession;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * Componente grafico che rappresenta la cella relativa a un Turno visualizzata
 * all'interno del Tabellone.
 * Ospita un titolo (facoltativo) e un'area con le iscrizioni.
 * Se non è definito il turno l'area iscrizioni è tratteggiata, se il turno è definito
 * l'area iscrizioni riporta l'elenco delle iscrizioni.
 * Se il turno è definito l'area iscrizioni si colora in funzione dell'urgenza di completamento.
 * Cliccando sull'area iscrizioni si apre l'editor del turno.
 * Ci sono due costruttori, uno da usare quando il turno esiste, e l'altro quando il turno non è definito.
 * Created by alex on 20/02/16.
 */
public class CTurnoDisplay extends VerticalLayout implements TabelloneCell {

//    private static int ORE_WARNING = 96; // turno vicino (giallo)
//    private static int ORE_ALERT = 24;  // turno molto vicino (rosso)

    private GridTabellone tabellone;
    private int x;
    private int y;
    private Turno turno;
    private Servizio serv;
    private LocalDate dataInizio;

    private GridLayout gridIscrizioni;

    /**
     * Costruttore per celle con turno
     *
     * @param tabellone il tabellone di riferimento
     * @param turno     il turno di riferimento
     */
    public CTurnoDisplay(GridTabellone tabellone, Turno turno) {
        super();
        this.tabellone = tabellone;
        this.turno = turno;
        this.serv = turno.getServizio();

        // inizializzazioni comuni turno e no-turno
        init();

        // numero di righe di iscrizione pari al numero delle funzioni previste
        int rows = turno.getServizio().getNumFunzioni();

        if (rows==0) {
            rows++;
        }// end of if cycle

        // crea la griglia delle iscrizioni
        gridIscrizioni = new GridLayout(1, rows);
        gridIscrizioni.setSpacing(false);
        gridIscrizioni.setWidth("100%");
        gridIscrizioni.setHeight("100%");
        gridIscrizioni.addStyleName("cturno");

        // listener quando viene cliccata l'area iscrizioni
        gridIscrizioni.addLayoutClickListener(new LayoutEvents.LayoutClickListener() {
            @Override
            public void layoutClick(LayoutEvents.LayoutClickEvent event) {
                tabellone.cellClicked(CellType.TURNO, x, y, turno);
            }
        });

        // inizializzazioni specifiche se il turno è presente
        initTurno();

        // aggiunge graficamente la griglia iscrizioni
        this.addComponent(gridIscrizioni);
        setExpandRatio(gridIscrizioni, 1);

    }


    /**
     * Costruttore per celle vuote
     *
     * @param tabellone il tabellone di riferimento
     * @param serv      il servizio di riferimento
     */
    public CTurnoDisplay(GridTabellone tabellone, Servizio serv, LocalDate dataInizio) {
        this.tabellone = tabellone;
        this.serv = serv;
        this.dataInizio = dataInizio;

        // inizializzazioni comuni turno e no-turno
        init();

        setHeight("100%");


        // componente blank visualizzato nell'area iscrizioni
        VerticalLayout blank = new VerticalLayout();
        blank.setWidth("100%");
        blank.setHeight("100%");
        blank.addStyleName("cnoturno");
        if(LibSession.isAdmin()){
            blank.addStyleName("cursor-pointer");
        }

        // solo admin: listener quando viene cliccata l'area iscrizioni
        if(LibSession.isAdmin()){
            blank.addLayoutClickListener(new LayoutEvents.LayoutClickListener() {
                @Override
                public void layoutClick(LayoutEvents.LayoutClickEvent event) {
                    InfoNewTurnoWrap wrapper = new InfoNewTurnoWrap(serv, dataInizio);
                    tabellone.cellClicked(CellType.NO_TURNO, x, y, wrapper);
                }
            });
        }

        // aggiunge graficamente il componente
        this.addComponent(blank);
        setExpandRatio(blank, 1);

    }


    /**
     * Inizializzazioni comuni ai casi turno-no turno
     */
    private void init() {
        setWidth(GridTabellone.W_COLONNE_TURNI);
        //setHeight("100%");

        if (!serv.isOrario()) {

            String note=null;
            if(turno!=null){
                note=turno.getNote();
            }
            if(note==null || note.isEmpty()){
                note = "&nbsp;";
            }
            Label label = new Label(note, ContentMode.HTML);
//            label.setWidthUndefined();
            label.setWidth("100%");
            label.addStyleName("cturno-title");
            this.addComponent(label);
        }

    }


    /**
     * Inizializzazioni quando esiste il turno.
     * Colora lo sfondo
     * Aggiunge le iscrizioni
     */
    private void initTurno() {

        // Colora lo sfondo del turno
        // se è nel passato, non colora
        // se è oggi, colora di azzurro
        // se è un po' nel futuro, colora in base all'urgenza di completamento
        // se è molto nel futuro, non colora
        String bgStyle = null;
        String fgStyle = null;
        LocalDate dataTurno = turno.getData1();
        if (dataTurno.equals(LocalDate.now())) {  // è oggi
            bgStyle = "cturno-today";
            fgStyle = "ciscrizione-light";
        } else {
            if (LocalDate.now().isBefore(dataTurno)) {   // è nel futuro
                LocalDateTime now = LocalDateTime.now();
                LocalDateTime inizioTurno = turno.getStartTime();
                long oreMancanti = ChronoUnit.HOURS.between(now,inizioTurno);
//                long ggMancanti = ChronoUnit.DAYS.between(LocalDate.now(), turno.getData1());
                if (oreMancanti < CompanyPrefs.turnoWarningOrePrima.getInt()) {
                    String[] styles = coloraTurnoUrgenza(turno);
                    bgStyle = styles[0];
                    fgStyle = styles[1];
                }
            }
        }
        // background del turno
        if (bgStyle != null) {
            gridIscrizioni.addStyleName(bgStyle);
        }


        // aggiunge le iscrizioni
        int row = 0;
        for (ServizioFunzione sf : serv.getServizioFunzioni()) {
            Iscrizione iscr = turno.getIscrizione(sf);
            FontAwesome icon = sf.getFunzione().getIcon();
            Component ci;
            if (iscr != null) {
                String nome = iscr.getVolontario().toString();
                FontAwesome fa = null;
                if(iscr.hasNota()){
                    fa=FontAwesome.ASTERISK;
                }
                ci = new CIscrizione(nome, icon, fa);
            } else {
                ci = new CIscrizione("", icon, null);
            }
            // foreground dell'iscrizione
            if (bgStyle != null) {
                ci.addStyleName(fgStyle);
            }

            gridIscrizioni.addComponent(ci, 0, row);
            row++;
        }
    }


    /**
     * Colorazione di un turno futuro in funzione dell'urgenza di completamento
     * - se è valido è verde
     * - se non è valido:
     *    - se è vicino è giallo
     *    - se è molto vicino è rosso
     *
     * @param turno il turno da esaminare
     * @return una coppia di stringhe con lo stile di background e lo stile di foreground
     */
    private String[] coloraTurnoUrgenza(Turno turno) {

        String bgStyle;
        String fgStyle;

        // controlla se il turno è valido
        boolean valido = turno.isValido();

        // se il turno è valido è in ogni caso verde
        if (valido) {
            bgStyle = "cturno-ok";
            fgStyle = "ciscrizione-light";
        } else {  // turno non valido

            bgStyle = "cturno-warning";
            fgStyle = "ciscrizione-dark";

            // se il turno è vicino e giallo, se è vicinissimo è rosso, se è lontano resta com'è
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime inizioTurno = turno.getStartTime();
            long hMancanti = ChronoUnit.HOURS.between(now, inizioTurno);
//            long ggMancanti = ChronoUnit.DAYS.between(LocalDate.now(), turno.getData1());
            if (hMancanti < CompanyPrefs.turnoAlertOrePrima.getInt()) {
                bgStyle = "cturno-alert";
                fgStyle = "ciscrizione-light";
            }
        }

        return new String[]{bgStyle, fgStyle};

    }



    /**
     * @return x la colonna del tabellone in cui è posizionato questo componente
     */
    public int getX() {
        return x;
    }

    /**
     * @param x la colonna del tabellone in cui è posizionato questo componente
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * @return la riga del tabellone in cui è posizionato questo componente
     */
    public int getY() {
        return y;
    }

    /**
     * @return la colonna del tabellone in cui è posizionato questo componente
     */
    public void setY(int y) {
        this.y = y;
    }



}// end of class

