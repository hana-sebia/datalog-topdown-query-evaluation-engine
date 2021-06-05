package fr.univlyon1.mif37.dex;

import fr.univlyon1.mif37.dex.mapping.Literal;
import fr.univlyon1.mif37.dex.mapping.Mapping;


import fr.univlyon1.mif37.dex.mapping.Tgd;
import fr.univlyon1.mif37.dex.mapping.topDown.AdornedRules;
import fr.univlyon1.mif37.dex.parser.MappingParser;
import fr.univlyon1.mif37.dex.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class App {
    private static final Logger LOG = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) throws Exception {
        MappingParser mp = new MappingParser(App.class.getResourceAsStream("/exemple2.txt"));
        Mapping mapping = mp.mapping();
        LOG.info("\n"+mapping.toString());
        LOG.info("Parsed {} edb(s), {} idb(s) and {} tgd(s).",
                mapping.getEDB().size(),
                mapping.getIDB().size(),
                mapping.getTgds().size());
        Collection<Tgd> tgd = new ArrayList<>(mapping.getTgds());
        List<Literal> list = new ArrayList<>();
        for(Tgd t: tgd){
            list.clear();
            for(Literal s: t.getLeft()) {
                list.add(s);
            }
            for (int i = list.size() - 1; i >= 0; i--) {
                System.out.print(list.get(i).getAtom().getName());
            }
            System.out.println(" ");
        }

        AdornedRules adornedRules = new AdornedRules(mapping);
        //System.out.println(adornedRules.toString());
    }
}
