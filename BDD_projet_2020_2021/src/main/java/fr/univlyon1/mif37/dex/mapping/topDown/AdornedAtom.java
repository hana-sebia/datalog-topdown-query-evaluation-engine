package fr.univlyon1.mif37.dex.mapping.topDown;
import fr.univlyon1.mif37.dex.mapping.Atom;

import java.util.ArrayList;
import java.util.List;
/**
 * @juba BDD
 */

/**
 * An adorned atom. Each argument of any atom formed from this
 * predicate symbol will be marked as bound or free in accordance with the
 * predicate's adornment.
 *
 */


public class AdornedAtom {

    private Atom atom;
    /**
     * The adornment of the atom. A true value means that the
     * corresponding term in the arguments of an atom
     * is considered bound; a false value implies that the argument is
     * free.
     */
    private List<Boolean> adornment;
    /**
     * Total number of bound terms in the adornment.
     */
    private int bound;
    /**
     * Constructs an adorned Atom from a atom and an
     * adornment.
     *
     * @param atom
     *            atom
     * @param adornment
     *            adornment
     */
    public AdornedAtom(Atom atom, List<Boolean> adornment) {
        //DONE-begin
        this.atom = atom;
        this.adornment = new ArrayList<>();
        this.bound = 0;
        for (Boolean a : adornment) {
            this.adornment.add(a);
            if (a) {
                this.bound++;
            }
        }
        //DONE-end
    }

    public Atom getAtom() {
        return atom;
    }

    public List<Boolean> getAdornment() {
        return adornment;
    }

    public int getBound() {
        return bound;
    }

    @Override
    public boolean equals (Object o) {
        if(o instanceof AdornedAtom){
            boolean b;
            return (((AdornedAtom) o).adornment.equals(adornment) && ((AdornedAtom) o).atom.equals(atom));
        }
        return false;
    }

    @Override
    public String toString() {
        String str = "";
        for (Boolean a : adornment) {
            if (a) {
                str += "b";
            } else {
                str += "f";
            }
        }
        str = atom.toString(str);
        return str;
    }

}
