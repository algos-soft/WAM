package it.algos.wam.menu;

import com.vaadin.navigator.View;
import com.vaadin.ui.MenuBar;
import it.algos.wam.ui.WamUI;
import it.algos.webbase.web.navigator.MenuCommand;

/**
 * Created by gac on 01 lug 2016.
 * Sovrascrive la classe base per mantenere i riferimenti a diverse MenuBar, invece che solo ad una
 * Quando si seleziona il comando del menu, deve disabilitare anche gli items delle altre (eventuali) MenuBar
 */
public class WamMenuCommand extends MenuCommand {

    private WamUI wamUI;

    /**
     * Constructor - lazy, cached
     *
     * @param clazz the view to diplay
     * @param wamUI l'interfaccia di riferimento
     */
    public WamMenuCommand(Class clazz, WamUI wamUI) {
        super(null, clazz);
        this.wamUI = wamUI;
    }// end of constructor

    /**
     * Constructor - heavyweight
     * Will create a heavyweight view provider
     * The view provided here will be used
     *
     * @param view  the view to diplay
     * @param wamUI l'interfaccia di riferimento
     */
    public WamMenuCommand(View view, WamUI wamUI) {
        super(null, view);
        this.wamUI = wamUI;
    }// end of constructor

    /**
     * The item has been selected.
     * Navigate to the View and select the item in the menubar
     */
    @Override
    public void menuSelected(MenuBar.MenuItem selectedItem) {
        super.menuSelected(selectedItem);
        wamUI.menuSelected(selectedItem);
    }// end of method

}// end of class
