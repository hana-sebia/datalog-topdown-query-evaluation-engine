package fr.univlyon1.mif37.dex.mapping.topDown;

import fr.univlyon1.mif37.dex.mapping.Mapping;
import fr.univlyon1.mif37.dex.mapping.Variable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
/**
 * @juba BDD
 */
/**
 * A relation, i.e., a set of tuples  with an associated
 * attribute schema of the same arity.
 *
 */
public class Relation {
    /**
     * The tuples of this relation.
     */
    protected List<Tuple> tuples;
    /**
     * The attribute schema of this relation.
     */
    protected TermSchema attributes;

    /**
     * Constructs an empty relation
     */
    public Relation() {
        this.attributes = new TermSchema(new LinkedList<>());
        this.tuples = new ArrayList<>();
    }

    public Relation(List<Variable> variables) {
        this.attributes = new TermSchema(variables);
        this.tuples = new ArrayList<>();
    }

    public Relation(TermSchema termSchema) {
        this.attributes = termSchema;
        this.tuples = new ArrayList<>();
    }

    public Relation(List<Variable> variables, List<Tuple> tuples) {
        this.attributes = new TermSchema(variables);
        this.tuples = tuples;

    }

    public Relation (final String name, List<Variable> attributes, Mapping map) {
        this.attributes = new TermSchema(attributes);
        this.tuples = new ArrayList<>();
        for(fr.univlyon1.mif37.dex.mapping.Relation r: map.getEDB()) {
            if(r.getName().equals(name)) {
                tuples.add(new Tuple(Arrays.asList(r.getAttributes())));
            }
        }
    }

    /**
     * Add tuple to relation (this).
     * @param tuple tuple to add.
     */
    public void addTuple(Tuple tuple) {
        this.tuples.add(tuple);
    }

    /**
     * Theta joins.
     * @param otherRelation relation to join with this.
     * @return Relation (this JOIN otherRelation)
     */
    public Relation join(final Relation otherRelation) {
        List<Variable> commonVariables = new ArrayList<>();
        List<Integer> index1 = new ArrayList<>();
        List<Integer> index2 = new ArrayList<>();
        for (Variable v1 : this.attributes.attributes) {
            for (Variable v2 : otherRelation.attributes.attributes) {
                if (v1.equals(v2)) {
                    commonVariables.add(v2);
                    index1.add(this.attributes.attributes.indexOf(v1));
                    index2.add(otherRelation.attributes.attributes.indexOf(v2));
                }
            }
        }
        if (commonVariables.size() == 0) {
            return null;
        }

        List<Variable> newVariables = new ArrayList<>(this.attributes.attributes);
        for (Variable v2 : otherRelation.attributes.attributes) {
            if (!newVariables.contains(v2)) {
                newVariables.add(v2);
            }
        }
        Relation join = new Relation(new TermSchema(newVariables));
        List<String> newElements = new ArrayList<>();
        int i;
        boolean toJoin;
        for (Tuple t1 : this.tuples) {
            for (Tuple t2 : otherRelation.tuples) {
                newElements.clear();
                toJoin = true;
                for (i = 0; i < index1.size(); i++) {
                    if (!t1.elts[index1.get(i)].equals(t2.elts[index2.get(i)])) {
                        toJoin = false;
                    }
                }

                if (toJoin) {
                    for (i = 0; i < t1.elts.length; i++) {
                        newElements.add(t1.elts[i]);
                    }
                    for (i = 0; i < t2.elts.length; i++) {
                        if (!index2.contains(i)) {
                            newElements.add(t2.elts[i]);
                        }
                    }
                }
                if (!newElements.isEmpty()) {
                    join.addTuple(new Tuple(newElements));
                }
            }
        }
        return join;
    }

    public Relation projection(final List<Variable> varsRelation) {
        List<Integer> index = new ArrayList<>();
        int i;
        for(Variable var: varsRelation) {
            i = 0;
            for(Variable v: this.attributes.attributes){
                if(var.equals(v)){
                    index.add(i);
                    break;
                }
                i++;
            }
        }
        ArrayList<Tuple> newTuples = new ArrayList<>();
        for(Tuple tuple: this.tuples) {
            List<String> values = new ArrayList<>();
            for(int j = 0; j < index.size(); j++){
                values.add(tuple.elts[index.get(j)]);
            }
            Tuple newTuple = new Tuple(values);
            if(!newTuples.contains(newTuple)) {
                newTuples.add(newTuple);
            }
        }
        return new Relation(varsRelation, newTuples);
    }



    /**
     * Substraction
     * @param otherRelation to subtract to 'this'
     * Precondition : this and otherRelation have similar attributes
     */
    public Relation substract(final Relation otherRelation) {
        if (otherRelation.attributes.attributes.size() != this.attributes.attributes.size()) {
            return null;
        }
        List<Tuple> newTuples = new ArrayList<>();
        for (Tuple t1 : this.tuples) {
            if(!otherRelation.tuples.contains(t1)) {
                newTuples.add(t1);
            }
        }
        return new Relation(this.attributes.attributes, newTuples);
    }


    @Override
    public String toString() {
        String str = this.attributes.attributes.toString() + " -> ";
        for(Tuple t : tuples) {
            str += t.toString() + " ; ";
        }
        return str;
    }

    public String toStringAsResult() {
        if (this.tuples.size() == 0) {
            return "No match";
        }
        String res = "";
        if (this.tuples.size() > 1) {
            res = this.tuples.size() + " matches : \n";
        }
        else {
            res = "Single match : \n";
        }
        String str = "(";
        for (Variable v : this.attributes.attributes) {
            str += v.toString() + ",";
        }
        str = str.substring(0, str.length() - 1);
        str += ") = (";
        for (Tuple t : this.tuples) {
            res += "     " + str;
            for (String s : t.elts) {
                res += s + ",";
            }
            res = res.substring(0, res.length() - 1);
            res += ")\n";
        }
        return res;
    }
}
