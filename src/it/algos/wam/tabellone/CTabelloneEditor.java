package it.algos.wam.tabellone;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.VerticalLayout;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.EventObject;

/**
 * Superclasse astratta degli editor di Tabellone
 * Created by alex on 18-03-2016.
 */
public abstract class CTabelloneEditor extends VerticalLayout implements View {

    private ArrayList<DismissListener> dismissListeners = new ArrayList();
    protected EntityManager entityManager;

    public CTabelloneEditor(EntityManager entityManager) {
        this.entityManager=entityManager;
        setSizeUndefined();
        setSpacing(true);
    }


    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
    }


    protected void fireDismissListeners(DismissEvent e) {
        for (DismissListener l : dismissListeners) {
            l.editorDismissed(e);
        }
    }

    public void addDismissListener(DismissListener l) {
        dismissListeners.add(l);
    }

    public void removeAllDismissListeners(){
        dismissListeners.clear();
    }


    /**
     * Listener per editor dismissed
     */
    public interface DismissListener {
        public void editorDismissed(DismissEvent e);
    }


    /**
     * Evento editor dismissed
     */
    public class DismissEvent extends EventObject {
        private boolean saved;
        private boolean deleted;

        public DismissEvent(Object source, boolean saved, boolean deleted) {
            super(source);
            this.saved = saved;
            this.deleted=deleted;
        }

        public boolean isSaved() {
            return saved;
        }

        public boolean isDeleted() {
            return deleted;
        }
    }



}
