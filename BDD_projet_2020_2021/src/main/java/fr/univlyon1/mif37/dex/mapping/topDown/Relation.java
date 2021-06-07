package fr.univlyon1.mif37.dex.mapping.topDown;

import fr.univlyon1.mif37.dex.mapping.Mapping;
import fr.univlyon1.mif37.dex.mapping.Variable;

import java.util.*;
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
            tuples.add(new Tuple(Arrays.asList(r.getAttributes())));
        }
    }

    public void addTuple(Tuple tuple) {
        this.tuples.add(tuple);
    }

    public Relation join(final Relation relation) {
        List<Variable> commonVariables = new ArrayList<>();
        List<Integer> index1 = new ArrayList<>();
        List<Integer> index2 = new ArrayList<>();
        for (Variable v1 : this.attributes.attributes) {
            commonVariables.add(v1);
            for (Variable v2 : relation.attributes.attributes) {
                if (!this.attributes.attributes.contains(v2)) {
                    commonVariables.add(v2);
                }
                if (v1.equals(v2)) {
                    index1.add(this.attributes.attributes.indexOf(v1));
                    index2.add(relation.attributes.attributes.indexOf(v2));
                }
            }
        }
        if (commonVariables.size() == 0) {
            return null;
        }
        Relation join = new Relation(new TermSchema(commonVariables));
        List<String> newElements = new ArrayList<>();
        int i;
        List<Integer> toRemove = new ArrayList<>();
        for (Tuple t1 : this.tuples) {
            for (Tuple t2 : relation.tuples) {
                newElements.clear();
                toRemove.clear();
                for (Integer i1 : index1) {
                    for (Integer i2 : index2) {
                        if (t1.elts[i1].equals(t2.elts[i2])) {
                            toRemove.add(i2);
                            System.out.println(t1.elts[i1]);
                        }
                        System.out.println();
                    }
                }
                if (toRemove.size() > 0 && toRemove.size() == commonVariables.size()) {
                    for (i = 0; i < t1.elts.length; i++) {
                        newElements.add(t1.elts[i]);
                    }
                    for (i = 0; i < t2.elts.length; i++) {
                        if (!toRemove.contains(i)) {
                            newElements.add(t2.elts[i]);
                        }
                    }
                }
                join.addTuple(new Tuple(newElements));
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



    @Override
    public String toString() {
        String str = "";
        for(Tuple t : tuples) {
            str += t.toString() + "\n";
        }
        return str;
    }
}
