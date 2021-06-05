package fr.univlyon1.mif37.dex.mapping.topDown;

import fr.univlyon1.mif37.dex.mapping.Atom;

import java.util.*;
/**
 * @juba BDD
 */
/**
 * A Datalog evaluation engine that uses a recursive version of the
 * query-subquery top-down technique.
 *
 */
public class RecursiveQsqEngine {
    /**
     * A container for tracking global information passed back and forth between
     * recursion frames.
     *
     */
    private class QSQRState {
        /**
         * Tracks the answer tuples generated for each adorned predicate.
         */
        private Map<Object,Object> ans;
        /**
         * Tracks which input tuples have been used for each rule.
         */
        private Map<Object,Object> inputByRule;
        /**
         * Holds all the adorned rules for a given adorned predicate.
         */
        private Map<Object,Object> adornedRules;
        /**
         * Holds all the unadorned rules for a given predicate.
         */
        private final Map<Object,Object>  unadornedRules;


        /**
         * Initializes state with a set of all unadorned rules for the program.
         *
         * @param unadornedRules
         *            set of unadorned rules
         */
        public QSQRState(Map<Object,Object> unadornedRules) {
            this.ans = new LinkedHashMap<>();
            this.inputByRule = new LinkedHashMap<>();
            this.adornedRules = new LinkedHashMap<>();
            this.unadornedRules = unadornedRules;

        }


    }

    /**
     *
     *
     * Preparation of query q and retun the obtained result
     *
     *
    **/

    public Set<Object> query(Atom q) {
        Set<Object> result = new LinkedHashSet<>();
        return result;
    }

    /**
     * Evaluates the query represented by the adorned predicate p and the
     * relation newInput.
     *
     * @param p
     *            adorned predicate of query
     * @param newInput
     *            input tuples
     * @param state
     *            current state of evaluation-wide variables
     */
    private void qsqr(AdornedAtom p, Relation newInput, QSQRState state) {

    }

    /**
     * Evaluates the supplied rule using the input tuples newInput.
     *
     * @param rule
     *            rule
     * @param newInput
     *            input tuples
     * @param state
     *            current state of evaluation-wide variables
     */
    private void qsqrSubroutine(AdornedTgd rule, Relation newInput, QSQRState state) {


    }

}
