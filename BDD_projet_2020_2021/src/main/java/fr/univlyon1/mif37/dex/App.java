package fr.univlyon1.mif37.dex;

import fr.univlyon1.mif37.dex.mapping.Mapping;


import fr.univlyon1.mif37.dex.mapping.Variable;
import fr.univlyon1.mif37.dex.mapping.topDown.*;
import fr.univlyon1.mif37.dex.parser.MappingParser;
import fr.univlyon1.mif37.dex.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
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


        /*AdornedRules adornedRules = new AdornedRules(mapping);
        System.out.println(adornedRules.toString());
        ArrayList<AdornedTgd> rules = adornedRules.getAdornedRules();
        QsqTemplate qsqTemplate = new QsqTemplate(rules.get(1));
        System.out.println(qsqTemplate.toString());*/


        String[] e1 = new String[3];
        e1[0] = "a";
        e1[1] = "b";
        e1[2] = "c";

        String[] e2 = new String[3];
        e2[0] = "a";
        e2[1] = "c";
        e2[2] = "d";

        String[] e3 = new String[3];
        e3[0] = "a";
        e3[1] = "c";
        e3[2] = "d";

        String[] e4 = new String[3];
        e4[0] = "a";
        e4[1] = "b";
        e4[2] = "b";


        Variable x = new Variable("x");
        Variable y = new Variable("y");
        Variable z = new Variable("z");
        Variable t = new Variable("t");
        Variable w = new Variable("w");

        List<Variable> variables1 = new ArrayList<>();
        variables1.add(x);
        variables1.add(y);
        variables1.add(z);

        List<Variable> variables2 = new ArrayList<>();
        variables2.add(x);
        variables2.add(w);
        variables2.add(z);

        List<Tuple> t1 = new ArrayList<>();
        t1.add(new Tuple(Arrays.asList(e1)));
        t1.add(new Tuple(Arrays.asList(e3)));

        List<Tuple> t2 = new ArrayList<>();
        t2.add(new Tuple(Arrays.asList(e2)));
        t2.add(new Tuple(Arrays.asList(e4)));

        fr.univlyon1.mif37.dex.mapping.topDown.Relation r1 = new fr.univlyon1.mif37.dex.mapping.topDown.Relation(variables1, t1);
        fr.univlyon1.mif37.dex.mapping.topDown.Relation r2 = new fr.univlyon1.mif37.dex.mapping.topDown.Relation(variables2, t2);

        Relation join = r1.join(r2);
        System.out.println(join.toString());


    }
}
