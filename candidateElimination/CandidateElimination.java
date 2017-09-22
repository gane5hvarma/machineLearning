package candidate;

import java.util.*;
import java.io.*;

class CandidateElimination{
    Hypothesis mostGeneral = new Hypothesis(Hypothesis.GENERAL);
    Hypothesis mostSpecific = new Hypothesis(Hypothesis.SPECIFIC);
    ArrayList<Hypothesis> generalBoundary = new ArrayList<Hypothesis>();
    ArrayList<Hypothesis> specificBoundary = new ArrayList<Hypothesis>();
    CandidateElimination(int i){
        // We have to do the number of times there are number of types.
        // Just put it in a for loop
        try {
            ArrayList<TrainingData> trainingData = Reader.read("zoo.data", i);
        } catch(FileNotFoundException e){
            System.out.println("File not found!");
        }
        generalBoundary.add(mostGeneral);
        specificBoundary.add(mostSpecific);
    }
    
}