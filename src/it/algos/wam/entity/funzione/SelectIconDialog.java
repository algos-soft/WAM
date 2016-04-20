package it.algos.wam.entity.funzione;

import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.GridLayout;
import it.algos.webbase.web.dialog.BaseDialog;

import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;

/**
 * Dialogo di selezione di una icona
 */
class SelectIconDialog extends BaseDialog {


    public SelectIconDialog() {
        super();
        setTitle("Scegli una icona");
        addComponent(new IconGrid(this));

        Button bRemove = new Button("Rimuovi icona");
        bRemove.setIcon(FontAwesome.TRASH_O);
        getToolbar().addComponent(bRemove);
        bRemove.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                DialogEvent e = new DialogEvent(2);
                fireListeners(e);
                close();
            }
        });


        Button bChiudi = new Button("Chiudi");
        bChiudi.setIcon(FontAwesome.CLOSE);
        getToolbar().addComponent(bChiudi);
        bChiudi.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                DialogEvent e = new DialogEvent(0);
                fireListeners(e);
                close();
            }
        });
    }


    class IconGrid extends GridLayout {

        public IconGrid(SelectIconDialog dialog) {
            super();
            setColumns(4);
            setSpacing(true);
            populate();
        }

        private void populate() {
            int[] codepoints = {FontAwesome.AMBULANCE.getCodepoint(),
                    FontAwesome.WHEELCHAIR.getCodepoint(),
                    FontAwesome.CAB.getCodepoint(),
                    FontAwesome.MOTORCYCLE.getCodepoint(),
                    FontAwesome.MEDKIT.getCodepoint(),
                    FontAwesome.HEART.getCodepoint(),
                    FontAwesome.STETHOSCOPE.getCodepoint(),
                    FontAwesome.HOTEL.getCodepoint(),
                    FontAwesome.USER.getCodepoint(),
                    FontAwesome.USER_MD.getCodepoint(),
                    FontAwesome.MALE.getCodepoint(),
                    FontAwesome.FEMALE.getCodepoint(),
            };

            for (int codepoint : codepoints) {
                FontAwesome glyph = FontAwesome.fromCodepoint(codepoint);
                Button b = new Button();
                b.setHtmlContentAllowed(true);
                b.setCaption(glyph.getHtml());
                b.setWidth("3em");
                b.addStyleName("bfunzione");
                addComponent(b);

                b.addClickListener(new Button.ClickListener() {
                    @Override
                    public void buttonClick(Button.ClickEvent clickEvent) {
                        iconClicked(codepoint);
                    }
                });
            }

        }

        private void iconClicked(int codepoint) {
            DialogEvent e = new DialogEvent(1, codepoint);
            fireListeners(e);
            close();
        }


    }


    // listener list
    private List<CloseListener> closeListenerList = new ArrayList();

    public void addCloseListener(CloseListener l) {
        closeListenerList.add(l);
    }

    public interface CloseListener {
        void dialogClosed(DialogEvent event);
    }

    public class DialogEvent extends EventObject {

        private int exitcode;
        private int codepoint;

        /**
         * @param exitcode: 0=cancel, 1=confirm, 2=remove icon.
         *                  If exitcode==1, selectedCodepoint contains the codepoint of the selected icon
         */
        public DialogEvent(int exitcode, int selectedCodepoint) {
            super(SelectIconDialog.this);
            this.exitcode=exitcode;
            this.codepoint=selectedCodepoint;
        }

        public DialogEvent(int exitcode) {
            this(exitcode, 0);
        }

        public int getExitcode() {
            return exitcode;
        }

        public int getCodepoint() {
            return codepoint;
        }
    }

    private void fireListeners(DialogEvent e){
        for(CloseListener l : closeListenerList){
            l.dialogClosed(e);
        }
    }


}
