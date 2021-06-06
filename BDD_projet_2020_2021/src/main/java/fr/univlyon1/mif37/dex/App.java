package fr.univlyon1.mif37.dex;

import fr.univlyon1.mif37.dex.mapping.Mapping;


import fr.univlyon1.mif37.dex.mapping.topDown.AdornedRules;
import fr.univlyon1.mif37.dex.mapping.topDown.AdornedTgd;
import fr.univlyon1.mif37.dex.mapping.topDown.QsqTemplate;
import fr.univlyon1.mif37.dex.parser.MappingParser;
import fr.univlyon1.mif37.dex.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;


public class App {
    private static final Logger LOG = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) throws Exception {
        MappingParser mp = new MappingParser(App.class.getResourceAsStream("/exemple3.txt"));
        Mapping mapping = mp.mapping();
        LOG.info("\n"+mapping.toString());
        LOG.info("Parsed {} edb(s), {} idb(s) and {} tgd(s).",
                mapping.getEDB().size(),
                mapping.getIDB().size(),
                mapping.getTgds().size());


        AdornedRules adornedRules = new AdornedRules(mapping);
        System.out.println(adornedRules.toString());
        ArrayList<AdornedTgd> rules = adornedRules.getAdornedRules();
        QsqTemplate qsqTemplate = new QsqTemplate(rules.get(4));
        System.out.println(qsqTemplate.toString());
    }
}
