package it.algos.wam.tabellone;

import com.vaadin.ui.Label;
import it.algos.webbase.web.lib.LibDate;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

/**
 * Created by alex on 21/02/16.
 */
public class CGiorno extends Label {
    public CGiorno(LocalDate date) {
        super();

        setWidth(GridTabellone.W_COLONNE_TURNI);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE d MMM");
        String text = date.format(formatter);
        setValue(text);

        addStyleName("cgiorno");


        // se è oggi ha un colore dedicato,
        // altrimenti colora se festivo

        // colore se è oggi
        if(date.equals(LocalDate.now())) {  // oggi
            addStyleName("cgiorno-today");
        }else{
            if(isFestivo(date)){
                addStyleName("cgiorno-festivo");
            }else{
                addStyleName("cgiorno");
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
