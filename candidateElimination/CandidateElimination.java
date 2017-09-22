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
    void minimalSpecialization(Hypothesis g, TrainingData t){
        /*
        * find minimal specialization of g that are consistent with t and more 
        * general than those in S. Remove from General Boundary any hypothesis
        * that is not more general than those in S. 
        */
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
        for (int i = specificBoundary.size(); i >= 0 ; i-- ) {
            if(!specificBoundary.get(i).isConsistent(t)){
                specificBoundary.remove(i);
            }
        }
        // Dealing with General Boundary
        for (int i = generalBoundary.size(); i >=0 ; i-- ) {
            // Test for inconsistency with negative examples once.
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
        for (int i = generalBoundary.size(); i >= 0 ; i-- ) {
            if(!generalBoundary.get(i).isConsistent(t)){
                generalBoundary.remove(i);
            }
        }
        // Dealing with Specific Boundary
        for (int i = specificBoundary.size(); i >=0  ; i-- ) {
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
                negativeEncounter(t);
            }
        }
        System.out.print("Specific boundary is:  ");
        for (int i = 0; i < specificBoundary.size() ; i ++ ) {
            Hypothesis h = specificBoundary.get(i);
            for (int j = 0; j < h.attributes.length ; j++ ) {
                System.out.print("  "+ h.attributes[j] + "");
            }
        }
        System.out.println();
        System.out.print("General boundary is:  ");
        for (int i = 0; i < generalBoundary.size() ; i ++ ) {
            Hypothesis h = generalBoundary.get(i);
            System.out.print("<");
            for (int j = 0; j < h.attributes.length ; j++ ) {
                System.out.print(" "+ h.attributes[j] + " ");
            }
            System.out.println(">");
        }
        return;
    }
    public static void main(String[] args) {
        CandidateElimination ce = new CandidateElimination(4);
        ce.candidateElimination();
    }
}