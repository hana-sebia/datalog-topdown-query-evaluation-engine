package fr.univlyon1.mif37.dex.mapping.topDown;

import fr.univlyon1.mif37.dex.mapping.*;
import fr.univlyon1.mif37.dex.mapping.Relation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class AdornedRules {
    Collection<AdornedTgd> adornedRules;

    public AdornedRules(final Mapping map) {
        adornedRules = new ArrayList<>();
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
                System.out.println(adornedHead);
                // Pour chaque literal de la query je calcule l'ornement
                for(Literal l :t.getLeft()) {
                    // Si c'est un EDB, je stocke le nom de variables que je connais
                    if(map.getEdbNames().contains(l.getAtom().getName())){
                        for (Value v : l.getAtom().getArgs()) {
                            if (v instanceof Variable) {
                                if (!knownVars.contains(((Variable) v).getName())) {
                                    knownVars.add(((Variable) v).getName());
                                }
                            }
                        }
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
                        System.out.println(atom);
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

        for(Literal l :rule.getLeft()) {
            // Si c'est un EDB, je stocke le nom de variables que je connais
            if(map.getEdbNames().contains(l.getAtom().getName())){
                for (Value v : l.getAtom().getArgs()) {
                    if (v instanceof Variable) {
                        if (!knownVariables.contains(((Variable) v).getName())) {
                            knownVariables.add(((Variable) v).getName());
                        }
                    }
                }
            }
            // Je vérifie qu'il s'agit bien d'un IDB
            if(map.getIdbNames().contains(l.getAtom().getName())) {
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
                body.add(new AdornedAtom(l.getAtom(), booleans));
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
}
