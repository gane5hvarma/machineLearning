package candidate;

import java.util.*;
import java.io.*;
import java.util.Arrays;

class CandidateElimination{
    /*
    * Actual Class that performs Candidate Elimination. It's attributes are self
    * explanatory. 
    * mostGeneral is the mostGeneral Hypothesis, containing ALL for all of its
    * attributes
    * mostSpecific is the mostSpecifc Hypothesis, contiaing NONE for all of its
    * attributes.
    */
    ArrayList<Hypothesis> generalBoundary = new ArrayList<Hypothesis>();
    ArrayList<Hypothesis> specificBoundary = new ArrayList<Hypothesis>();
    ArrayList<TrainingData> trainingData;
    CandidateElimination(int i){
        try {
            trainingData = Reader.read("zoo.data", i);
        } catch(FileNotFoundException e){
            System.out.println("File not found!");
        }
        int[] General = new int[]{999, 999, 999, 999, 999, 999, 999,
                                   999, 999, 999, 999, 999, 999, 999, 999, 999};
        int[] Specific = new int[]{111, 111, 111, 111, 111, 111, 111, 111, 111,
                                             111, 111, 111, 111, 111, 111, 111};
        Hypothesis mostGeneral = new Hypothesis(General);
        Hypothesis mostSpecific = new Hypothesis(Specific);
        generalBoundary.add(mostGeneral);
        specificBoundary.add(mostSpecific);
    }
    void minifyGeneralBoundary(){
        /*
        * We have to remove from GeneralBoundary any Hypothesis that is less 
        * general than another hypothesis in GenearlBoundary. This function
        * takes care of it.
        */
        ArrayList <Hypothesis> remove = new ArrayList<> ();
        for(int i = 0; i < generalBoundary.size(); i++){
            for(int j = 0; j < generalBoundary.size(); j++){
                if(i == j){
                    continue;
                }
                Hypothesis a = generalBoundary.get(i);
                Hypothesis b = generalBoundary.get(j);
                if (a.isMoreGeneral(b)){
                    remove.add(b);
                }
            }
        }
        for(Hypothesis h: remove){
            generalBoundary.remove(h);
        }
    }
    boolean acceptableSpecialization(Hypothesis h){
        /*
        * For a hypothesis to be added to general boundary, there shoudl be at-
        * least one hypothesis in specific boundary that is more specific than
        * the hypothesis under consideration.
        */
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
                        }               
                    }
                }
                else if (g.attributes[i] == Hypothesis.NONE) {
                    System.out.println("Can't be specialized more");
                    return;
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
            if(!generalBoundary.get(i).isConsistent(t)){
                Hypothesis g = generalBoundary.get(i);
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
                // System.out.println("here");
                postiveEncounter(t);
            }else{
                // System.out.println("there");
                negativeEncounter(t);
            }
        }
        if(specificBoundary.size() == 0){
            System.out.println("concept can't be learned.");
            return;
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
}
