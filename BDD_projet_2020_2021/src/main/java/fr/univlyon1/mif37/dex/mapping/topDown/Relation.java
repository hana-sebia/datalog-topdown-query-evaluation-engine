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
            if(r.getName().equals(name)) {
                tuples.add(new Tuple(Arrays.asList(r.getAttributes())));
            }
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
            for (Variable v2 : relation.attributes.attributes) {
                if (v1.equals(v2)) {
                    commonVariables.add(v2);
                    index1.add(this.attributes.attributes.indexOf(v1));
                    index2.add(relation.attributes.attributes.indexOf(v2));
                }
            }
        }
        if (commonVariables.size() == 0) {
            return null;
        }

        List<Variable> newVariables = new ArrayList<>();
        for (Variable v1 : this.attributes.attributes) {
            newVariables.add(v1);
        }
        for (Variable v2 : relation.attributes.attributes) {
            if (!newVariables.contains(v2)) {
                newVariables.add(v2);
            }
        }
        Relation join = new Relation(new TermSchema(newVariables));
        List<String> newElements = new ArrayList<>();
        int i;
        List<Integer> toRemove = new ArrayList<>();
        boolean toJoin;
        for (Tuple t1 : this.tuples) {
            for (Tuple t2 : relation.tuples) {
                newElements.clear();
                toRemove.clear();
                toJoin = true;
                for (i = 0; i < index1.size(); i++) {
                    if (!t1.elts[index1.get(i)].equals(t2.elts[index2.get(i)])) {
                        toJoin = false;
                    }
                }

                if (toJoin) {
                    System.out.println("joining  " + t1 + " & " + t2);
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
     * substraction
     * @param otherRelation to subtract to 'this'
     * Precondition : this and otherRelation have similar attributes
     */
    public Relation substract(final Relation otherRelation) {
        if (otherRelation.attributes.attributes.size() != this.attributes.attributes.size()) {
            return null;
        }
        int i;
        boolean toAdd;
        List<Tuple> newTuples = new ArrayList<>();
        for (Tuple t1 : this.tuples) {
            /*toAdd = false;
            for (Tuple t2 : otherRelation.tuples) {
                for  (i = 0; i < this.attributes.attributes.size(); i++) {
                    if (!t1.elts[i].equals(t2.elts[i])) {
                        toAdd = true;
                    }
                }
            }*/
            if(!otherRelation.tuples.contains(t1)) {
                newTuples.add(t1);
            }
        }
        return new Relation(this.attributes.attributes, newTuples);
    }

    public Relation cartesianProduct(Relation otherRelation) {
        if (otherRelation == null) {
            return new Relation(this.attributes.attributes, this.tuples);
        }
        if (this.tuples.size() == 0) {
            if (otherRelation.tuples.size() == 0) {
                return null;
            }
            else {
                return otherRelation;
            }
        }
        else if (otherRelation.tuples.size() == 0) {
            return new Relation(this.attributes.attributes, this.tuples);
        }

        List<Variable> newVariables = new ArrayList<>();
        List<Tuple> newTuples = new ArrayList<>();
        List<String> e1 = new ArrayList<>();
        List<String> e2 = new ArrayList<>();
        int i;

        newVariables.addAll(this.attributes.attributes);
        newVariables.addAll(otherRelation.attributes.attributes);

        for (Tuple t1 : this.tuples) {
            e1.clear();
            for (i = 0; i < t1.elts.length; i++) {
                e1.add(t1.elts[i]);
            }
            for (Tuple t2 : otherRelation.tuples) {
                e2.clear();
                for (i = 0; i < t1.elts.length; i++) {
                    e2.add(t1.elts[i]);
                }
                for (i = 0; i < t2.elts.length; i++) {
                    e2.add(t2.elts[i]);
                }
                newTuples.add(new Tuple(e2));
            }
        }
        return new Relation(newVariables, newTuples);
    }

    @Override
    public String toString() {
        String str = this.attributes.attributes.toString() + " -> ";
        for(Tuple t : tuples) {
            str += t.toString() + " ; ";
        }
        return str;
    }
}
