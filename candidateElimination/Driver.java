package candidate;

import java.util.*;

class Driver{
    public static void main(String[] args) {
        for(int i = 1; i < 8; i++){
            System.out.println("running candidate elimination for class:" + i);
            CandidateElimination ce = new CandidateElimination(i);
            ce.candidateElimination();
            System.out.println("---------------------------------------------");
        }
    }
}