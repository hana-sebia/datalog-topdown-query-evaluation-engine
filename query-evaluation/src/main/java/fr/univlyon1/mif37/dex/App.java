package fr.univlyon1.mif37.dex;

import fr.univlyon1.mif37.dex.mapping.Mapping;
import fr.univlyon1.mif37.dex.mapping.topDown.RecursiveQsqEngine;
import fr.univlyon1.mif37.dex.parser.MappingParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class App {
    private static final Logger LOG = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) throws Exception {
        MappingParser mp = new MappingParser(App.class.getResourceAsStream("/qsqrEngine/exemple8.txt"));
        Mapping mapping = mp.mapping();

        LOG.info("\n"+mapping.toString());
        LOG.info("\nParsed {} edb(s), {} idb(s) and {} tgd(s).",
                mapping.getEDB().size(),
                mapping.getIDB().size(),
                mapping.getTgds().size());

        long begin = System.currentTimeMillis();
        RecursiveQsqEngine rs = new RecursiveQsqEngine(mapping);
        long end = System.currentTimeMillis();
        System.out.println("\n" + rs.result.toStringAsResult(end - begin) + "\n");
    }
}
