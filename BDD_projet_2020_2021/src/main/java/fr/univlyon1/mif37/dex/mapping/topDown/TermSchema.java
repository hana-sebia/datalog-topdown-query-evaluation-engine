package fr.univlyon1.mif37.dex.mapping.topDown;
import fr.univlyon1.mif37.dex.mapping.Value;
import fr.univlyon1.mif37.dex.mapping.Variable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
/**
 * @juba BDD
 */
/**
 * A list of terms of fixed arity representing the attribute schema for a
 * relation of the same arity (i.e., the 2nd term in the list is the attribute
 * for the 2nd "column" in the relation).
 *
 */
public class TermSchema {
    /**
     * The attributes of this schema.
     */
    public final List<Variable> attributes;

    /**
     * Constructs a schema from a list of terms.
     *
     * @param terms
     *            the terms
     */
    public TermSchema(List<Variable> terms) {
        this.attributes = new ArrayList<>();
        attributes.addAll(terms);
    }


    public String toString(int i) {
        String str = "";
        str += "sup" + i + "(";
        int j = 0;
        for(Variable v :attributes) {
            str += v;
            if(j  != attributes.size() - 1)
            str += ",";
            ++j;
        }
        str += ")";
        return str;
    }

}
