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

    private WamUI wamUi;

    /**
     * Constructor - lazy, cached
     *
     * @param mb    the MenuBar
     * @param clazz the view to diplay
     */
    public WamMenuCommand(MenuBar mb, Class clazz, WamUI wamUi) {
        super(mb, clazz);
        this.wamUi = wamUi;
    }// end of constructor

    /**
     * Constructor - heavyweight
     * Will create a heavyweight view provider
     * The view provided here will be used
     *
     * @param mb   the MenuBar
     * @param view the view to diplay
     */
    public WamMenuCommand(MenuBar mb, View view, WamUI wamUi) {
        super(mb, view);
        this.wamUi = wamUi;
    }// end of constructor

    /**
     * The item has been selected.
     * Navigate to the View and select the item in the menubar
     */
    @Override
    public void menuSelected(MenuBar.MenuItem selectedItem) {
        super.menuSelected(selectedItem);
        wamUi.menuSelected(selectedItem);
    }// end of method

}// end of class
