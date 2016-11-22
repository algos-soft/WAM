package it.algos.wam.tabellone;

import com.vaadin.ui.Label;
import it.algos.wam.lib.LibWam;
import it.algos.webbase.web.lib.LibDate;
import org.apache.commons.lang.LocaleUtils;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Locale;

/**
 * Created by alex on 21/02/16.
 */
public class CGiorno extends Label {
    public CGiorno(LocalDate date) {
        super();

        setWidth(GridTabellone.W_COLONNE_TURNI);


        Locale locale = LibWam.getCurrentLocale();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE d MMM", locale);
        String text = date.format(formatter);
        setValue(text);

        // stile di base
        addStyleName("cgiorno");


        // se è oggi ha un colore dedicato,
        // altrimenti colora se festivo

        // colore se è oggi
        if(date.equals(LocalDate.now())) {  // oggi
            addStyleName("cgiorno-today");
        }else{
            if(isFestivo(date)){
                addStyleName("cgiorno-festivo");
            }
        }

    }



    private boolean isFestivo(LocalDate data){
        boolean festivo=false;
        DayOfWeek dow = data.getDayOfWeek();
        if(dow.equals(DayOfWeek.SATURDAY) || dow.equals(DayOfWeek.SUNDAY)){
            festivo=true;
        }
        return festivo;
    }


}
