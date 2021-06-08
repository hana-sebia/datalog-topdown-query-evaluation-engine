package fr.univlyon1.mif37.dex.mapping;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableSortedMap;
import com.google.common.collect.Multimap;
/*
import com.sun.org.apache.xpath.internal.operations.Mult;
import com.sun.org.apache.xpath.internal.SourceTree;
*/

import java.util.*;

public class Mapping {

    private Collection<Relation> edb;

    private Collection<AbstractRelation> idb;

    private Collection<Tgd> tgds;

    final private List<String> edbNames = new ArrayList<>();

    final private List<String> idbNames = new ArrayList<>();


    public Mapping() {
        this.edb = new ArrayList<>();
        this.idb = new ArrayList<>();
        this.tgds = new ArrayList<>();
    }


    public Mapping(Mapping mapping) {
        this.edb = new ArrayList<>(mapping.getEDB());
        this.idb = new ArrayList<>(mapping.getIDB());
        this.tgds = new ArrayList<>(mapping.getTgds());
        setEdbNames();
        setIdbNames();
    }

    public Mapping(Collection<Relation> edb, Collection<AbstractRelation> idb, Collection<Tgd> tgds) {
        this.edb = edb;
        this.idb = idb;
        this.tgds = tgds;
        setEdbNames();
        setIdbNames();
    }

    private void setEdbNames(){
        for(Relation r: edb){
            edbNames.add(r.getName());
        }
    }

    private void setIdbNames(){
        for(AbstractRelation r: idb){
            idbNames.add(r.getName());
        }
    }

    public Collection<Relation> getEDB() {
        return edb;
    }

    public Collection<AbstractRelation> getIDB() {
        return idb;
    }

    public Collection<Tgd> getTgds() {
        return tgds;
    }

    public List<String> getEdbNames() {
        return edbNames;
    }

    public List<String> getIdbNames() {
        return idbNames;
    }



/**
*   Instiate the Datalog Top Down Engine in this part
*
**/


    public String toString() {
        String result = "";
        result += "EDB"+ "\n";
        for(Relation r : this.getEDB())
            result += r+ "\n";
        result +=""+ "\n";
        result +="IDB"+ "\n";
        for(AbstractRelation relation : this.getIDB())
            result +=relation+ "\n";
        result +=""+ "\n";;
        result +="TGD"+ "\n";
        for(Tgd tgd : this.getTgds())
            result +=tgd + "\n";
        return result;
    }

}
