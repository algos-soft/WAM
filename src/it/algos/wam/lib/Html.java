package it.algos.wam.lib;

import javax.persistence.metamodel.SingularAttribute;

/**
 * Created by gac on 25/02/17.
 * .
 */
public enum Html {

    plain() {
        @Override
        public String get(String testo) {
            return "";
        }// end of method
    },// end of single enumeration

    small() {
        @Override
        public String get(String testo) {
            return "<small>" + testo + "</small>";
        }// end of method
    },// end of single enumeration


    strong() {
        @Override
        public String get(String testo) {
            return "<strong>" + testo + "</strong>";
        }// end of method
    };// end of single enumeration


    /**
     * Regola il testo passato come parametro
     * DEVE essere sovrascritto (implementato)
     *
     * @param testo da regolare
     * @return testo risultante
     */
    public abstract String get(String testo);

}// end of enumeration
