package fr.univlyon1.mif37.dex.mapping.topDown;

import fr.univlyon1.mif37.dex.mapping.Constant;
import fr.univlyon1.mif37.dex.mapping.Literal;
import fr.univlyon1.mif37.dex.mapping.Mapping;
import fr.univlyon1.mif37.dex.mapping.Tgd;
import fr.univlyon1.mif37.dex.mapping.Value;
import fr.univlyon1.mif37.dex.mapping.Variable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdornedRules {
    Collection<AdornedTgd> adornedRules;
    Collection<AdornedAtom> adornedPredicates;

    public AdornedRules(final Mapping map) {
        adornedRules = new ArrayList<>();
        adornedPredicates = new ArrayList<>();
        init(map);
    }

    public void init(final Mapping map) {
        List<Boolean> booleans = new ArrayList<>();
        List<String> knownVars = new ArrayList<>();
        List<AdornedAtom> body = new ArrayList<>();
        for(Tgd t: map.getTgds()){
            if (t.getRight().getName().equals("query")) {
                for (Value v : t.getRight().getArgs()) {
                    if (v instanceof Variable) {
                        booleans.add(false);
                    }
                }
                AdornedAtom adornedHead = new AdornedAtom(t.getRight(), booleans);
                adornedPredicates.add(adornedHead);
                // Pour chaque literal de la query je calcule l'ornement
                Literal l;
                for(int i = 0; i < t.getLeft().size(); i++){
                    l = t.getLeft().get(i);
                    // Si c'est un EDB, je stocke le nom de variables que je connais
                    if(map.getEdbNames().contains(l.getAtom().getName())){
                        booleans.clear();
                        for (Value v : l.getAtom().getArgs()) {
                            if (v instanceof Variable) {
                                if (!knownVars.contains(((Variable) v).getName())) {
                                    knownVars.add(((Variable) v).getName());
                                }
                            }
                        }
                        body.add(new AdornedAtom(l.getAtom(), booleans));
                    }
                    // Je vérifie qu'il s'agit bien d'un IDB
                    else if(map.getIdbNames().contains(l.getAtom().getName())) {
                        // Pour chaque value d'un IDB
                        booleans.clear();
                        for (Value v : l.getAtom().getArgs()) {
                            if (v instanceof Variable) {
                                if (knownVars.contains(((Variable) v).getName())) {
                                    booleans.add(true);
                                } else {
                                    booleans.add(false);
                                    knownVars.add(((Variable) v).getName());
                                }
                            } else if( v instanceof Constant) {
                                booleans.add(true);
                            }
                        }
                        // j'ajoute l'Atom dans le body
                        AdornedAtom atom = new AdornedAtom(l.getAtom(), booleans);
                        adornedPredicates.add(atom);
                        body.add(atom);

                        for(Tgd rule : map.getTgds()){
                            if(rule.getRight().getName().equals(l.getAtom().getName())) {
                                recursiveAdornedRules(map, rule, booleans);
                            }
                        }
                    }
                }
                adornedRules.add(new AdornedTgd(adornedHead, body));
                break;
            }
        }
    }

    private void recursiveAdornedRules(final Mapping map, final Tgd rule, List<Boolean> adornement) {
        List<Boolean> booleans = new ArrayList<>();
        List<String> knownVariables = new ArrayList<>();
        List<AdornedAtom> body = new ArrayList<>();
        AdornedAtom adHead= new AdornedAtom(rule.getRight(), adornement);

        int i;
        for(i = 0; i < rule.getRight().getArgs().length; i++) {
            if(rule.getRight().getArgs()[i] instanceof Variable) {
                if (adornement.get(i)) {
                    knownVariables.add(((Variable) rule.getRight().getArgs()[i]).getName());
                }
            }
        }

        Literal l;
        for(i = 0; i < rule.getLeft().size(); i++){
            l = rule.getLeft().get(i);
            // Si c'est un EDB, je stocke le nom de variables que je connais
            if(map.getEdbNames().contains(l.getAtom().getName())){
                booleans.clear();
                for (Value v : l.getAtom().getArgs()) {
                    if (v instanceof Variable) {
                        if (!knownVariables.contains(((Variable) v).getName())) {
                            knownVariables.add(((Variable) v).getName());
                        }
                    }
                }
                body.add(new AdornedAtom(l.getAtom(), booleans));

            }
            // Je vérifie qu'il s'agit bien d'un IDB
            else if(map.getIdbNames().contains(l.getAtom().getName())) {
                // Pour chaque value d'un IDB
                booleans.clear();
                for (Value v : l.getAtom().getArgs()) {
                    if (v instanceof Variable) {
                        if (knownVariables.contains(((Variable) v).getName())) {
                            booleans.add(true);
                        } else {
                            booleans.add(false);
                            knownVariables.add(((Variable) v).getName());
                        }
                    } else if( v instanceof Constant) {
                        booleans.add(true);
                    }
                }
                // j'ajoute l'Atom dans le body
                AdornedAtom atom = new AdornedAtom(l.getAtom(), booleans);
                body.add(atom);
                if(!adornedPredicates.contains(atom)){
                    adornedPredicates.add(atom);
                    for(Tgd r : map.getTgds()){
                        if(r.getRight().getName().equals(l.getAtom().getName())) {
                            recursiveAdornedRules(map, r, booleans);
                        }
                    }
                }
            }
        }
        adornedRules.add(new AdornedTgd(adHead, body));
    }


    @Override
    public String toString() {
        String str = "";
        for (AdornedTgd adornedTgd : adornedRules) {
            str += adornedTgd.toString() + "\n";
        }
        return str;
    }

    public ArrayList<AdornedTgd> getAdornedRules() {
        return (ArrayList<AdornedTgd>) adornedRules;
    }

    public Map<AdornedAtom, List<AdornedTgd>> adornedMap() {
        Map<AdornedAtom, List<AdornedTgd>> map = new HashMap<>();
        for(AdornedAtom p: adornedPredicates) {
            ArrayList<AdornedTgd> tgd = new ArrayList<>();
            for(AdornedTgd t : adornedRules) {
                if(t.getHead().equals(p)) {
                    tgd.add(t);
                }
            }
            map.put(p, tgd);
        }

        return map;
    }
}
