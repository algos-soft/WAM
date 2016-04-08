package it.algos.wam.entity.servizio;

import com.vaadin.ui.ColorPicker;

/**
 * Created by alex on 8-04-2016.
 */
public class ServizioColorPicker extends ColorPicker{

    public ServizioColorPicker() {
        super("Scegli il colore");
        setCaption("colore");
        setRGBVisibility(false);
        setHSVVisibility(false);
        setCaptionAsHtml(true);
        setPosition(20,20);
    }
}
