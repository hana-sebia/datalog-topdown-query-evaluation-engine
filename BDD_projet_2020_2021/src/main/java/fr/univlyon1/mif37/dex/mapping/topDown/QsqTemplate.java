package fr.univlyon1.mif37.dex.mapping.topDown;

import fr.univlyon1.mif37.dex.mapping.Variable;

import java.util.ArrayList;
import java.util.List;
/**
 * @juba BDD
 */
/**
 * A template containing the attribute schemata of the supplementary relations
 * for a given rule in QSQ evaluation.
 *
 */
public class QsqTemplate {
    /**
     * The attribute schemata for the supplementary relations.
     */
    private  List<TermSchema> schemata;
    /**
     * Constructs a template from an adorned rule.
     *
     * @param rule
     *            the adorned rule
     */
    public QsqTemplate(AdornedTgd rule) {
        this.schemata = new ArrayList<>();
        List<Variable> boundVarsHead = new ArrayList<>();
        // supp0 : add bound variables only
        ArrayList<Variable> headVars = (ArrayList<Variable>) rule.getHead().getAtom().getVars();
        for (int i = 0; i < rule.getHead().getAdornment().size(); i++){
            if(rule.getHead().getAdornment().get(i)) {
                boundVarsHead.add(new Variable(headVars.get(i).getName()));
            }
        }
        this.schemata.add(new TermSchema(boundVarsHead));

        // supp1 - supp n-1 : add variables bound before and needed later
        ArrayList<AdornedAtom> body = (ArrayList<AdornedAtom>) rule.getBody();
        ArrayList<Variable> seenHeadVars = new ArrayList<>();
        ArrayList<Variable> vars = new ArrayList<>();
        for (int i = 0; i < body.size() - 1; i++){
            vars.clear();
            ArrayList<Variable> atomVariables = (ArrayList<Variable>) body.get(i).getAtom().getVars();
            // We add all seen Head Variables
            vars.addAll(seenHeadVars);
            for(Variable v : atomVariables){

                if(headVars.contains(v)) {
                    if(!vars.contains(v))
                        vars.add(new Variable(v.getName()));
                    if(!seenHeadVars.contains(v))
                        seenHeadVars.add(v);

                } else {
                    for(int j = i+1; j < body.size(); j++){
                        for(Variable vnext: body.get(j).getAtom().getVars()) {
                            if(vnext.equals(v)) {
                                vars.add(v);
                                break;
                            }
                        }
                    }
                }
            }
            this.schemata.add(new TermSchema(vars));
        }

        // suppn : add all head variables
        this.schemata.add(new TermSchema(headVars));
    }

    @Override
    public String toString() {
        String str = "";
        int i = 0;
        for (TermSchema t : schemata) {
            str += t.toString(i);
            if(i  != schemata.size() - 1)
                str += ",";
            ++i;
        }
        return str;
    }

    public List<TermSchema> getSchemata() {
        return schemata;
    }
}
