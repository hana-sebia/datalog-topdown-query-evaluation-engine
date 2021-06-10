package fr.univlyon1.mif37.dex.mapping.topDown;

import fr.univlyon1.mif37.dex.mapping.Mapping;
import fr.univlyon1.mif37.dex.mapping.Variable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
         * Tracks the input tuples used for each rule.
         */
        private Map<AdornedAtom,Relation> inputByRule;
        /**
         * Holds all the adorned rules for a given adorned predicate.
         */
        private Map<AdornedAtom, List<AdornedTgd>> adornedRules;
        /**
         * Holds the total number of tuples in the output_relations
         */
        private int ansCount;
        /**
         * Holds the total number of tuples in the input_relations
         */
        private int inputCount;



        /**
         * Initializes state with a set of all adorned rules for the program.
         *
         * @param adornedRules
         *            set of adorned rules
         */
        public QSQRState(Map<AdornedAtom,List<AdornedTgd>> adornedRules) {
            this.adornedRules = adornedRules;
            this.ans = new HashMap<>();
            this.inputByRule = new HashMap<>();
            this.ansCount = 0;
            this.inputCount = 0;

            // Generate an empty input and output relation for each adorned atom
            for(Map.Entry<AdornedAtom, List<AdornedTgd>> map: adornedRules.entrySet()) {
                ans.put(map.getKey(), new Relation((List<Variable>) map.getKey().getAtom().getVars()));
                inputByRule.put(map.getKey(), new Relation(map.getKey().getBoundVariables()));
            }
        }
    }

    /**
     * Constructor.
     * @param mapping
     *          contains the datalog mapping (EDB, IDB, TGD)
     */
    public RecursiveQsqEngine(Mapping mapping) {
        AdornedRules adornedRules = new AdornedRules(mapping);
        QSQRState state = new QSQRState(adornedRules.adornedMap());


        // Identify the adorned rule with the head "query"
        AdornedTgd ruleQuery = null;
        AdornedAtom query = null;
        for(Map.Entry<AdornedAtom, List<AdornedTgd>> rule : state.adornedRules.entrySet()) {
            if(rule.getKey().getAtom().getName().equals("query")) {
                ruleQuery = rule.getValue().get(0);
                query = rule.getKey();
            }
        }

        // Do the recursive call until there is no new tuples added to the input or output relation
        int inputCountCopy;
        int ansCountCopy;
        do {
            inputCountCopy = state.inputCount;
            ansCountCopy = state.ansCount;

            // Evaluate the query giving an input relation and the current state of the QSQR engine
            qsqrSubroutine(ruleQuery, state.inputByRule.get(query), state, mapping);

        } while (ansCountCopy != state.ansCount || inputCountCopy != state.inputCount);

        // Report the query's output_relation in the result variable
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
        // STEP 1 :
        // Initialize auxiliary relations to empty sets
        QsqTemplate qsqTemplate = new QsqTemplate(rule);
        List<Relation> sup = new ArrayList<>();
        for(TermSchema t: qsqTemplate.getSchemata()) {
            sup.add(new Relation(t));
        }

        // STEP 2 :
        // Compute sup0
        sup.get(0).tuples.addAll(newInput.tuples);
        int i = 1;

        // STEP 3 :
        // Compute sup1 - supn
        for(AdornedAtom atom: rule.getBody()){
            // Case EDB
            if(map.getEdbNames().contains(atom.getAtom().getName())) {
                // Compute all the matches for the atom in the mapped program
                Relation edb = new Relation(atom.getAtom().getName(), (List<Variable>) atom.getAtom().getVars(), map);
                // Compute edb join sup i-1, projected on sup i
                Relation result = edb.linkRelations(sup.get(i-1)).projection(sup.get(i).attributes.attributes);
                // add the result to sup i
                sup.get(i).tuples.addAll(result.tuples);
            }
            // Case IDB
            else {
                // Compute sup i-1 projected on BoundVars(Atom) - Input(Atom)
                Relation s = sup.get(i-1).projection(atom.getBoundVariables()).substract(state.inputByRule.get(atom));

                // If the Input(Atom) changes, call recursively this method on all rules with head predicate Atom
                if(!s.tuples.isEmpty() || state.inputByRule.get(atom).tuples.isEmpty()) {
                    // Add the new tuples to the Input(Atom)
                    state.inputByRule.get(atom).tuples.addAll(s.tuples);
                    // Increment the input's counter
                    state.inputCount += s.tuples.size();

                    for(AdornedTgd r:state.adornedRules.get(atom)){
                        qsqrSubroutine(r, state.inputByRule.get(atom), state, map);
                    }
                }

                Relation rename_output = new Relation((List<Variable>) atom.getAtom().getVars(), state.ans.get(atom).tuples);
                // Compute sup i as sup i-1 join output(Atom), projected on sup i
                Relation sup_i = sup.get(i-1).linkRelations(rename_output).projection(sup.get(i).attributes.attributes);
                sup.get(i).tuples.addAll(sup_i.tuples);
            }
            i++;
        }

        // STEP 4 :
        // Add the result of sup n to the output relation of the adorned predicate
        state.ansCount -= state.ans.get(rule.getHead()).tuples.size();
        for(Tuple t : sup.get(sup.size()-1).tuples) {
            if(!state.ans.get(rule.getHead()).tuples.contains(t)) {
                state.ans.get(rule.getHead()).tuples.add(t);
            }
        }
        // Increment the answer's counter
        state.ansCount += state.ans.get(rule.getHead()).tuples.size();
    }
}
