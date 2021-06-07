package fr.univlyon1.mif37.dex.mapping.topDown;

import fr.univlyon1.mif37.dex.mapping.Variable;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
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
}
