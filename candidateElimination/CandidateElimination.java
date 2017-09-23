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
    ArrayList<Hypothesis> minimalSpecialization(Hypothesis g, TrainingData t){
        /*
        * find minimal specialization of g that are consistent with t and more 
        * general than those in S. Remove from General Boundary any hypothesis
        * that is not more general than those in S. 
        */
        System.out.println("\t  in minimalSpecialization");
        t.printTrainingData();
        ArrayList<Hypothesis> minSpec = new ArrayList<Hypothesis>();
        int flag1 = 0;
        for (int i = 0; i < g.attributes.length ; i++ ) {
            if(g.isEqual(g.attributes[i], t.attributes[i])){
                if(g.attributes[i] == Hypothesis.NONE){
                    continue;
                }else if(!(g.attributes[i] == Hypothesis.ALL)){
                    int[] new_attributes = g.attributes;
                    new_attributes[i] = Hypothesis.NONE;
                    Hypothesis h = new Hypothesis(new_attributes);
                    if(h.isConsistent(t)){
                        minSpec.add(h);
                        System.out.println("\t Adding to minSpec");
                        h.printHypothesis();
                        t.printTrainingData();
                        flag1++;
                    }
                }else{
                    System.out.println("In else:");
                    int[] acceptable = g.acceptableValues(i);
                    int[] new_attributes = new int[g.attributes.length];
                    int m = 0;
                    for (int attr:g.attributes) {
                        new_attributes[m++] = attr;
                    }
                    int flag = 0;
                    for (int j = 0; j < acceptable.length; j++ ) {
                        new_attributes[i] = acceptable[j];
                        Hypothesis h = new Hypothesis(new_attributes);
                        if(h.isConsistent(t)){
                            System.out.println("adding to minSpec");
                            h.printHypothesis();
                            t.printTrainingData();
                            minSpec.add(h);
                            flag--;
                        }else{
                            System.out.println("Not adding to minSpec");
                            h.printHypothesis();
                            t.printTrainingData();
                            flag++;
                        }
                    }
                    if (flag == acceptable.length) {
                        System.out.println("In recursive shit");
                        for (int j = 0; j < acceptable.length ; j++ ) {
                            new_attributes[i] = acceptable[j];
                            Hypothesis h = new Hypothesis(new_attributes);
                            ArrayList<Hypothesis> recur = minimalSpecialization(h,t);
                            for(int k = 0; k < recur.size(); k++){
                                if(recur.get(k).isConsistent(t)){
                                    recur.get(k).printHypothesis();
                                    System.out.println("\t adding to minSpec");
                                    t.printTrainingData();
                                    minSpec.add(recur.get(k));
                                }
                            }
                        }
                    }
                }
            }
        }
        if(!g.isConsistent(t) && minSpec.size() == 0){
            return null;
        }
        for (int i = minSpec.size() - 1; i >= 0; ) {
            for (int j = 0; j < i; j++ ) {
                if(minSpec.get(i).isMoreGeneral(minSpec.get(j))&& i!=j){
                    System.out.println("Removing from minSpec");
                    minSpec.get(i).printHypothesis();
                    minSpec.get(j).printHypothesis();
                    minSpec.remove(i--);
                    j = 0;
                }
            }
            i--;
        }
        if(minSpec.size() != 0){
            for (int i = minSpec.size() - 1; i >= 0 ; i-- ) {
                for (int j = 0; j < generalBoundary.size(); j++ ) {
                    if(minSpec.get(i).isMoreGeneral(generalBoundary.get(j))){
                        minSpec.remove(i--);
                    }
                }
            }
        }
        return minSpec;
    }
    void minimalGeneralization(Hypothesis s, TrainingData t){
        /*
        * Find minimal generalizations of s that are consistent with t and more
        * specific than those in G. Remove from specific boundary any hypothesis
        * that is more general than another hypothesis in G
        */
        System.out.println("\t  in minimalGeneralization");
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
                System.out.println("/t  adding to specificBoundary");
                gen.printHypothesis();
                t.printTrainingData();
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
        System.out.println("In negativeEncounter");
        for (int i = specificBoundary.size() - 1; i >= 0 ; i-- ) {
            if(!specificBoundary.get(i).isConsistent(t)){
                System.out.println("\t Removing from specificBoundary");
                specificBoundary.get(i).printHypothesis();
                t.printTrainingData();
                specificBoundary.remove(i);
            }
        }
        // Dealing with General Boundary
        for (int i = 0; i < generalBoundary.size() ; i++ ) {
            // Test for inconsistency with negative examples once.
            System.out.println("\tSize of generalBoundary " + generalBoundary.size());
            if(!generalBoundary.get(i).isConsistent(t)){
                Hypothesis g = generalBoundary.get(i);
                System.out.println("\tremoving from generalBoundary");
                generalBoundary.get(i).printHypothesis();
                t.printTrainingData();
                generalBoundary.remove(i);
                ArrayList<Hypothesis> minSpec = minimalSpecialization(g, t);
                for (int j = 0; j < minSpec.size(); j++ ) {
                    System.out.println("\tadding to general boundary " + j);
                    minSpec.get(i).printHypothesis();
                    t.printTrainingData();
                    generalBoundary.add(minSpec.get(j));
                } 
            }
        }
    }
    void postiveEncounter(TrainingData t){
        /*
        * Here, we need to generalize specfic boundary and remove from general
        * boundary any hypothesis that is inconsistent with t.
        */
        // Dealing with General Boundary
        System.out.println("In positive Encounter" + generalBoundary.size());
        for (int i = generalBoundary.size() - 1; i >= 0 ; i-- ) {
            if(!generalBoundary.get(i).isConsistent(t)){
                generalBoundary.get(i).printHypothesis();
                t.printTrainingData();
                System.out.println("\tRemoving from General Boundary");
                generalBoundary.remove(i);
            }
        }
        // Dealing with Specific Boundary
        for (int i = specificBoundary.size() - 1; i >=0  ; i-- ) {
            if(!specificBoundary.get(i).isConsistent(t)){
                Hypothesis s = specificBoundary.get(i);
                System.out.println("\tRemoving from specfic Boundary");
                specificBoundary.get(i).printHypothesis();
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
                System.out.println("postiveEncounter: ");
                postiveEncounter(t);
            }else{
                System.out.println("negativeEncounter: ");
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
        CandidateElimination ce = new CandidateElimination(2);
        ce.candidateElimination();
    }
}