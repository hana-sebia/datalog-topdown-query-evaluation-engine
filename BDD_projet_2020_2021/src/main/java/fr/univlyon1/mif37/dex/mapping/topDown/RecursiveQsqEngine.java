package fr.univlyon1.mif37.dex.mapping.topDown;

import fr.univlyon1.mif37.dex.mapping.Mapping;
import fr.univlyon1.mif37.dex.mapping.Variable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @juba BDD
 */
/**
 * A Datalog evaluation engine that uses a recursive version of the
 * query-subquery top-down technique.
 *
 */
public class RecursiveQsqEngine {
    public Relation result;
    /**
     * A container for tracking global information passed back and forth between
     * recursion frames.
     *
     */
    private class QSQRState {
        /**
         * Tracks the answer tuples generated for each adorned predicate.
         */
        private Map<AdornedAtom,Relation> ans;
        /**
         * Tracks which input tuples have been used for each rule.
         */
        private Map<AdornedAtom,Relation> inputByRule;
        /**
         * Holds all the adorned rules for a given adorned predicate.
         */
        private Map<AdornedAtom, List<AdornedTgd>> adornedRules;
        /**
         * Holds all the unadorned rules for a given predicate.
         */
        private int ansCount;

        private int inputCount;



        /**
         * Initializes state with a set of all unadorned rules for the program.
         *
         * @param adornedRules
         *            set of unadorned rules
         */
        public QSQRState(Map<AdornedAtom,List<AdornedTgd>> adornedRules) {
            this.adornedRules = adornedRules;
            this.ans = new HashMap<>();
            this.inputByRule = new HashMap<>();
            this.ansCount = 0;
            this.inputCount = 0;

            for(Map.Entry<AdornedAtom, List<AdornedTgd>> map: adornedRules.entrySet()) {
                ans.put(map.getKey(), new Relation((List<Variable>) map.getKey().getAtom().getVars()));
                inputByRule.put(map.getKey(), new Relation(map.getKey().getBoundVariables()));
            }
        }
    }

    /**
     * Constructor.
     * @param mapping
     */
    public RecursiveQsqEngine(Mapping mapping) {
        AdornedRules adornedRules = new AdornedRules(mapping);
        QSQRState state = new QSQRState(adornedRules.adornedMap());

        // Step 1 :
        // Identify the adorned rules with relevant P predicate in the body of the query
        AdornedTgd ruleQuery = null;
        AdornedAtom query = null;
        for(Map.Entry<AdornedAtom, List<AdornedTgd>> rule : state.adornedRules.entrySet()) {
            if(rule.getKey().getAtom().getName().equals("query")) {
                ruleQuery = rule.getValue().get(0);
                query = rule.getKey();
            }
        }
        int inputCountCopy;
        int ansCountCopy;
        do {
            inputCountCopy = state.inputCount;
            ansCountCopy = state.ansCount;
            qsqrSubroutine(ruleQuery, state.inputByRule.get(query), state, mapping);
        } while (ansCountCopy != state.ansCount || inputCountCopy != state.inputCount);
        // Projection of Output_predicate on query's variables
        result = state.ans.get(query);
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
    private void qsqrSubroutine(AdornedTgd rule, Relation newInput, QSQRState state, Mapping map) {
        QsqTemplate qsqTemplate = new QsqTemplate(rule);
        List<Relation> sup = new ArrayList<>();
        for(TermSchema t: qsqTemplate.getSchemata()) {
            sup.add(new Relation(t));
        }

        //sup0
        sup.get(0).tuples.addAll(newInput.tuples);
        int i = 1;
        //sup1 - supn
        for(AdornedAtom atom: rule.getBody()){
            // Case EDB
            if(map.getEdbNames().contains(atom.getAtom().getName())) {
                Relation edb = new Relation(atom.getAtom().getName(), (List<Variable>) atom.getAtom().getVars(), map);
                //reste la linkRelationsture entre sup_i-1 et edb puis projection sur les variables de sup_i
                Relation result = edb.linkRelations(sup.get(i-1)).projection(sup.get(i).attributes.attributes);
                sup.get(i).tuples.addAll(result.tuples);
            }
            // Case IDB
            else {
                // faut faire une projection de sup_i-1 sur boundVars de IDB - Input_IDB = S
                Relation s = sup.get(i-1).projection(atom.getBoundVariables()).substract(state.inputByRule.get(atom));
                // faut faire Input_IDB = Input_IDB U S
                if(!s.tuples.isEmpty() || state.inputByRule.get(atom).tuples.isEmpty()) {
                    state.inputByRule.get(atom).tuples.addAll(s.tuples);
                    state.inputCount += s.tuples.size();
                    for(AdornedTgd r:state.adornedRules.get(atom)){
                        qsqrSubroutine(r, state.inputByRule.get(atom), state, map);
                    }
                }
                // sup_i = sup_i join output_IDB
                Relation rename_output = new Relation((List<Variable>) atom.getAtom().getVars(), state.ans.get(atom).tuples);
                Relation sup_i = sup.get(i-1).linkRelations(rename_output).projection(sup.get(i).attributes.attributes);
                sup.get(i).tuples.addAll(sup_i.tuples);
            }
            i++;
        }

        //supn
        state.ansCount -= state.ans.get(rule.getHead()).tuples.size();
        for(Tuple t : sup.get(sup.size()-1).tuples) {
            if(!state.ans.get(rule.getHead()).tuples.contains(t)) {
                state.ans.get(rule.getHead()).tuples.add(t);
            }
        }
        state.ansCount += state.ans.get(rule.getHead()).tuples.size();
    }
}
