package candidate;

import java.util.*;
import java.io.*;

class CandidateElimination{
    Hypothesis mostGeneral = new Hypothesis(Hypothesis.GENERAL);
    Hypothesis mostSpecific = new Hypothesis(Hypothesis.SPECIFIC);
    ArrayList<Hypothesis> generalBoundary = new ArrayList<Hypothesis>();
    ArrayList<Hypothesis> specificBoundary = new ArrayList<Hypothesis>();
    ArrayList<TrainingData> trainingData;
    CandidateElimination(int i){
        // We have to do the number of times there are number of types.
        // Just put it in a for loop
        try {
            trainingData = Reader.read("zoo.data", i);
        } catch(FileNotFoundException e){
            System.out.println("File not found!");
        }
        generalBoundary.add(mostGeneral);
        specificBoundary.add(mostSpecific);
    }
    void minifyGeneralBoundary(){
        for (int i = generalBoundary.size() - 1; i > 0; i-- ) {
            for (int j = generalBoundary.size() - 1; j > 0; j--) {
                try{
                    if(!(generalBoundary.get(i).isMoreGeneral(generalBoundary.get(j)))){
                        if (i != j){
                            System.out.print("removing from generalBoundary");
                            generalBoundary.get(i).printHypothesis();
                            generalBoundary.get(j).printHypothesis();
                            generalBoundary.remove(i);                          
                        }
                    }
                }catch(java.lang.IndexOutOfBoundsException e){
                    continue;
                }
            }
        }
    }
    boolean acceptableSpecialization(Hypothesis h){
        for (Hypothesis s: specificBoundary ) {
            if(h.isMoreGeneral(s)){
                return true;
            }
        }
        return false;
    }
    void minimalSpecialization(Hypothesis g, TrainingData t){
        /*
        * find minimal specialization of g that are consistent with t and more 
        * general than those in S. Remove from General Boundary any hypothesis
        * that is not more general than those in S. 
        */
        ArrayList<Hypothesis> minSpec = new ArrayList<Hypothesis>();
        for (int i = 0; i < g.attributes.length; i++) {
            if (g.isEqual(g.attributes[i], t.attributes[i])) {
                if(g.attributes[i] == Hypothesis.ALL){
                    int[] acceptable_values = g.acceptableValues(i);
                    int flag = 0;
                    for (int acceptable: acceptable_values ) {
                        int[] new_attributes = new int[g.attributes.length];
                        int l = 0;
                        for ( int k: g.attributes) {
                            new_attributes[l++] = k;
                        }
                        new_attributes[i] = acceptable;
                        Hypothesis h = new Hypothesis(new_attributes);
                        if(h.isConsistent(t)){
                            if (acceptableSpecialization(h)) {
                                minSpec.add(h);   
                            }
                        }else{
                            flag++;
                        }                
                    }
                    if (flag == acceptable_values.length) {
                        return;    
                    }
                }
                else if (g.attributes[i] == Hypothesis.NONE) {
                    System.out.println("Can't be specialized more");
                }
                else{
                    Hypothesis h;
                    int l = 0;
                    int[] new_attributes = new int[g.attributes.length];
                    for ( int k: g.attributes) {
                        new_attributes[l++] = k;
                    }
                    new_attributes[i] = Hypothesis.NONE;
                    h = new Hypothesis(new_attributes);
                    if (h.isConsistent(t)) {
                        if (acceptableSpecialization(h)) {
                            minSpec.add(h);   
                        }
                    }
                }
            }
        }
        for (Hypothesis h: minSpec) {
            System.out.print("adding to generalBoundary:");
            h.printHypothesis();
            generalBoundary.add(h);
        }
        minifyGeneralBoundary();
    }
    void minimalGeneralization(Hypothesis s, TrainingData t){
        /*
        * Find minimal generalizations of s that are consistent with t and more
        * specific than those in G. Remove from specific boundary any hypothesis
        * that is more general than another hypothesis in G
        */
        for (int i = 0; i < s.attributes.length ; i++) {
            if(!s.isEqual(s.attributes[i], t.attributes[i])){
                if(s.attributes[i] == Hypothesis.NONE){
                    s.attributes[i] = t.attributes[i];
                }else{
                    s.attributes[i] = Hypothesis.ALL;
                }
            }
        }
        System.out.println(generalBoundary.size());
        for (int i = 0; i < generalBoundary.size() ; i++ ) {
            Hypothesis gen = generalBoundary.get(i);
            if(gen.isMoreGeneral(s)){
                specificBoundary.add(s);
                return;
            }
        }
        return;
    }
    void negativeEncounter(TrainingData t){
        /*
        * We need to specialize the general boundary and remove from specific 
        * boundary any hypothesis that is inconsistent with t.
        */
        //dealing with Specific Boundary
        for (int i = specificBoundary.size() - 1; i >= 0 ; i-- ) {
            if(!specificBoundary.get(i).isConsistent(t)){
                specificBoundary.remove(i);
            }
        }
        // Dealing with General Boundary
        for (int i = 0; i < generalBoundary.size() ; i++ ) {
            // Test for inconsistency with negative examples once.
            if(!generalBoundary.get(i).isConsistent(t)){
                Hypothesis g = generalBoundary.get(i);
                System.out.println("remoing from generalBoundary");
                g.printHypothesis();
                t.printTrainingData();
                generalBoundary.remove(i);
                minimalSpecialization(g, t);
            }
        }
    }
    void postiveEncounter(TrainingData t){
        /*
        * Here, we need to generalize specfic boundary and remove from general
        * boundary any hypothesis that is inconsistent with t.
        */
        // Dealing with General Boundary
        for (int i = generalBoundary.size() - 1; i >= 0 ; i-- ) {
            if(!generalBoundary.get(i).isConsistent(t)){
                generalBoundary.remove(i);
            }
        }
        // Dealing with Specific Boundary
        for (int i = specificBoundary.size() - 1; i >=0  ; i-- ) {
            if(!specificBoundary.get(i).isConsistent(t)){
                Hypothesis s = specificBoundary.get(i);
                specificBoundary.remove(i);
                minimalGeneralization(s, t);
            }
        }
    }
    void candidateElimination(){
        /*
        * This function should be called within the afore mentioned for loop.
        * Within the for loop, we create a candidateElimination object with
        * different 'types' and run obj.candidateElimination() to print G and S
        */
        for (int i = 0; i < this.trainingData.size() ; i++) {
            TrainingData t = this.trainingData.get(i);
            if(t.attributes[Hypothesis.TYPE] == 1){
                postiveEncounter(t);
            }else{
                System.out.println("negativeEncounter");
                negativeEncounter(t);
            }
        }
        System.out.print("Specific boundary is:  ");
        for (int i = 0; i < specificBoundary.size() ; i ++ ) {
            Hypothesis h = specificBoundary.get(i);
            h.printHypothesis();
        }
        System.out.println();
        System.out.print("General boundary is:  " + generalBoundary.size());
        for (int i = 0; i < generalBoundary.size() ; i ++ ) {
            Hypothesis h = generalBoundary.get(i);
            h.printHypothesis();
        }
        return;
    }
    public static void main(String[] args) {
        CandidateElimination ce = new CandidateElimination(1);
        ce.candidateElimination();
    }
}