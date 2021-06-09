package fr.univlyon1.mif37.dex.parser;

import fr.univlyon1.mif37.dex.App;
import fr.univlyon1.mif37.dex.mapping.Mapping;
import fr.univlyon1.mif37.dex.mapping.Variable;
import fr.univlyon1.mif37.dex.mapping.topDown.RecursiveQsqEngine;
import fr.univlyon1.mif37.dex.mapping.topDown.Relation;
import fr.univlyon1.mif37.dex.mapping.topDown.Tuple;
import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.*;

public class DatalogTest {

    static final int NB_EXAMPLES = 10;

    public DatalogTest() throws ParseException {
    }

    @Test
    public void test() throws ParseException {
        MappingParser mp;
        Mapping mapping;
        RecursiveQsqEngine rs;

        List<Relation> results = new ArrayList<>();

        List<Variable> tempVariables = new ArrayList<>();
        List<String> elmts = new ArrayList<>();
        List<Tuple> tempTuples = new ArrayList<>();

        /************ exemple1.txt *************/
        tempVariables.add(new Variable("$x"));
        elmts.add("b");
        tempTuples.add(new Tuple(elmts));
        elmts.clear();
        elmts.add("a");
        tempTuples.add(new Tuple(elmts));
        results.add(new Relation(tempVariables, tempTuples));

        /************ exemple2.txt *************/
        tempVariables = new ArrayList<>();
        tempTuples = new ArrayList<>();
        elmts = new ArrayList<>();
        tempVariables.add(new Variable("$y"));
        elmts.add("Perrache");
        tempTuples.add(new Tuple(elmts));
        results.add(new Relation(tempVariables, tempTuples));

        /************ exemple3.txt *************/
        tempVariables = new ArrayList<>();
        tempTuples = new ArrayList<>();
        elmts = new ArrayList<>();
        tempVariables.add(new Variable("$y"));
        elmts.add("Charpennes");
        tempTuples.add(new Tuple(elmts));
        elmts.clear();
        elmts.add("Debourg");
        tempTuples.add(new Tuple(elmts));
        elmts.clear();
        elmts.add("PartDieu");
        tempTuples.add(new Tuple(elmts));
        elmts.clear();
        elmts.add("Perrache");
        tempTuples.add(new Tuple(elmts));
        results.add(new Relation(tempVariables, tempTuples));


        /************ exemple4.txt *************/
        tempVariables = new ArrayList<>();
        tempTuples = new ArrayList<>();
        elmts = new ArrayList<>();
        tempVariables.add(new Variable("$y"));
        results.add(new Relation(tempVariables, tempTuples));

        /************ exemple5.txt *************/
        tempVariables = new ArrayList<>();
        tempTuples = new ArrayList<>();
        elmts = new ArrayList<>();
        tempVariables.add(new Variable("$y"));
        elmts.add("Charpennes");
        tempTuples.add(new Tuple(elmts));
        elmts.clear();
        elmts.add("Debourg");
        tempTuples.add(new Tuple(elmts));
        elmts.clear();
        elmts.add("PartDieu");
        tempTuples.add(new Tuple(elmts));
        elmts.clear();
        elmts.add("Perrache");
        tempTuples.add(new Tuple(elmts));
        results.add(new Relation(tempVariables, tempTuples));

        /************ exemple6.txt *************/
        tempVariables = new ArrayList<>();
        tempTuples = new ArrayList<>();
        elmts = new ArrayList<>();
        tempVariables.add(new Variable("$y"));
        elmts.add("b");
        tempTuples.add(new Tuple(elmts));
        elmts.clear();
        elmts.add("c");
        tempTuples.add(new Tuple(elmts));
        elmts.clear();
        elmts.add("d");
        tempTuples.add(new Tuple(elmts));
        results.add(new Relation(tempVariables, tempTuples));

        /************ exemple7.txt *************/
        tempVariables = new ArrayList<>();
        tempTuples = new ArrayList<>();
        elmts = new ArrayList<>();
        tempVariables.add(new Variable("$x"));
        tempVariables.add(new Variable("$y"));
        elmts.add("t1");
        elmts.add("t2");
        tempTuples.add(new Tuple(elmts));
        elmts.clear();
        elmts.add("t1");
        elmts.add("t3");
        tempTuples.add(new Tuple(elmts));
        elmts.clear();
        elmts.add("t1");
        elmts.add("t4");
        tempTuples.add(new Tuple(elmts));
        elmts.clear();
        elmts.add("t2");
        elmts.add("t3");
        tempTuples.add(new Tuple(elmts));
        elmts.clear();
        elmts.add("t2");
        elmts.add("t4");
        tempTuples.add(new Tuple(elmts));
        elmts.clear();
        elmts.add("t3");
        elmts.add("t4");
        tempTuples.add(new Tuple(elmts));
        elmts.clear();
        elmts.add("t5");
        elmts.add("t6");
        tempTuples.add(new Tuple(elmts));
        elmts.clear();
        elmts.add("t5");
        elmts.add("t7");
        tempTuples.add(new Tuple(elmts));
        elmts.clear();
        elmts.add("t5");
        elmts.add("t8");
        tempTuples.add(new Tuple(elmts));
        elmts.clear();
        elmts.add("t6");
        elmts.add("t7");
        tempTuples.add(new Tuple(elmts));
        elmts.clear();
        elmts.add("t6");
        elmts.add("t8");
        tempTuples.add(new Tuple(elmts));
        elmts.clear();
        elmts.add("t7");
        elmts.add("t8");
        tempTuples.add(new Tuple(elmts));
        results.add(new Relation(tempVariables, tempTuples));

        /************ exemple8.txt *************/
        tempVariables = new ArrayList<>();
        tempTuples = new ArrayList<>();
        elmts = new ArrayList<>();
        tempVariables.add(new Variable("$x"));
        elmts.add("t1");
        tempTuples.add(new Tuple(elmts));
        elmts.clear();
        elmts.add("t2");
        tempTuples.add(new Tuple(elmts));
        elmts.clear();
        elmts.add("t3");
        tempTuples.add(new Tuple(elmts));
        elmts.clear();
        elmts.add("t5");
        tempTuples.add(new Tuple(elmts));
        elmts.clear();
        elmts.add("t6");
        tempTuples.add(new Tuple(elmts));
        elmts.clear();
        elmts.add("t7");
        tempTuples.add(new Tuple(elmts));
        results.add(new Relation(tempVariables, tempTuples));

        /************ exemple9.txt *************/
        tempVariables = new ArrayList<>();
        tempTuples = new ArrayList<>();
        elmts = new ArrayList<>();
        tempVariables.add(new Variable("$x"));
        elmts.add("charlotte");
        tempTuples.add(new Tuple(elmts));
        elmts.clear();
        elmts.add("jules");
        tempTuples.add(new Tuple(elmts));
        elmts.clear();
        elmts.add("kevin");
        tempTuples.add(new Tuple(elmts));
        elmts.clear();
        elmts.add("zoe");
        tempTuples.add(new Tuple(elmts));
        results.add(new Relation(tempVariables, tempTuples));

        /************ exemple10.txt *************/
        tempVariables = new ArrayList<>();
        tempTuples = new ArrayList<>();
        elmts = new ArrayList<>();
        tempVariables.add(new Variable("$x"));
        elmts.add("daniela");
        tempTuples.add(new Tuple(elmts));
        elmts.clear();
        elmts.add("stephanie");
        tempTuples.add(new Tuple(elmts));
        results.add(new Relation(tempVariables, tempTuples));

        for (int i = 0; i < NB_EXAMPLES; i++) {
            mp = new MappingParser(App.class.getResourceAsStream("/exemple" + (i + 1) + ".txt"));
            mapping = mp.mapping();
            rs = new RecursiveQsqEngine(mapping);
            if (!rs.result.equals(results.get(i))) {
                System.out.println("\'exemple" + (i + 1) + ".txt\' is wrong!");
            }
            assertThat(rs.result.equals(results.get(i)), is(true));

        }
    }
}
