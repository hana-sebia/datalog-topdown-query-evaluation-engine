package fr.univlyon1.mif37.dex.mapping.topDown;
import fr.univlyon1.mif37.dex.mapping.Atom;
import fr.univlyon1.mif37.dex.mapping.Variable;

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
        this.atom = atom;
        this.adornment = new ArrayList<>();
        this.bound = 0;
        for (Boolean a : adornment) {
            this.adornment.add(a);
            if (a) {
                this.bound++;
            }
        }
    }

    /**
     * Get.
     * @return AdornedAtom's Atom
     */
    public Atom getAtom() {
        return atom;
    }

    /**
     * Get.
     * @return List<Boolean>: True if the variable is 'bound', False for free variable
     */
    public List<Boolean> getAdornment() {
        return adornment;
    }

    /**
     * Get bound Variables.
     * @return List<Variable> bound variables only.
     */
    public List<Variable> getBoundVariables() {
        List<Variable> boundVars = new ArrayList<>();
        ArrayList<Variable> vars = (ArrayList<Variable>) atom.getVars();
        for(int i = 0; i < vars.size(); i++){
            if(adornment.get(i)){
                boundVars.add(vars.get(i));
            }
        }
        return boundVars;
    }

    @Override
    public boolean equals (Object o) {
        if(o instanceof AdornedAtom){
            if (((AdornedAtom) o).atom.equals(atom)) {
                for (int i = 0; i < this.adornment.size(); i++) {
                    if (!this.adornment.get(i).equals(((AdornedAtom) o).adornment.get(i))) {
                        return false;
                    }
                }
                return true;
            }
            else {
                return false;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        if(this.adornment == null)
            return 0;
        int sum = 0;
        for(boolean b : this.adornment){
            if(b)
                sum++;
        }
        return 31+sum;
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
