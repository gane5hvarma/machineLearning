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
    void minimalSpecialization(Hypothesis h, TrainingData t){

    }
    void minimalGeneralization(Hypothesis s, TrainingData t){
        /*
        * Find minimal generalizations of h that are consistent with t and more
        * specific than those in g. Remove from specific boundary any hypothesis
        * that is more general than another hypothesis in s
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
        
    }
    void postiveEncounter(TrainingData t){
        /*
        * Here, we need to generalize specfic boundary and remove from general
        * boundary, any hypothesis that is inconsistent with t
        */
        // Dealing with General Boundary
        for (int i = 0; i < generalBoundary.size() ; i++ ) {
            if(!generalBoundary.get(i).isConsistent(t)){
                generalBoundary.remove(i);
                i--;
            }
        }
        // Dealing with Specific Boundary
        for (int i = 0; i < specificBoundary.size() ; i++ ) {
            if(!specificBoundary.get(i).isConsistent(t)){
                Hypothesis s = specificBoundary.get(i);
                specificBoundary.remove(i);
                i--;
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
        // print general boundary and specific boundary.
    }
}