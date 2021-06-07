package fr.univlyon1.mif37.dex.mapping.topDown;

import fr.univlyon1.mif37.dex.mapping.Value;

import java.util.Arrays;
import java.util.List;
/**
 * @juba BDD
 */

/**
 * A tuple of terms, i.e., an ordered list of fixed arity.
 *
 */
public class Tuple {
    /**
     * The value in this tuple.
     */
    public  String[] elts;

    /**
     * Constructs a tuple from a list of values.
     *
     * @param elts
     *            the list of values
     */
    public Tuple(List<String> elts) {
        String[] tmp = new String[elts.size()];
        this.elts = elts.toArray(tmp);

    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Tuple){
           return Arrays.equals(elts, ((Tuple) obj).elts);
        }
        return false;
    }

    @Override
    public String toString() {
        return Arrays.toString(elts);
    }
}
