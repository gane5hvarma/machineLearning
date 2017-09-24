package decision_tree;

import java.util.*;
import java.util.Arrays;
import java.lang.*;

class TrainingData implements Comparable<TrainingData>{
    String[] attributes;
    TrainingData(String[] attributes){
        this.attributes = attributes;
    }
    @Override
    public int compareTo(TrainingData t){
        return 0;
    }
    static String[] getAcceptedValues(int i){
        String[] accepted_values;
        if (i == 1){
            accepted_values = new String[]{"Private", "Self-emp-not-inc",
                             "Self-emp-inc", "Federal-gov", "Local-gov", 
                             "State-gov","Without-pay", "Never-worked"};
        }
        else if (i == 3){
            accepted_values = new String[]{"Bachelors", "Some-college", "11th", 
             "Prof-school", "Assoc-acdm", "Assoc-voc", "9th", "7th-8th", "12th",
             "Masters", "1st-4th", "10th", "Doctorate", "5th-6th",
             "Preschool", "HS-grad",};
        }
        else if (i == 5){
            accepted_values = new String[]{"Married-civ-spouse", "Divorced",
            "Separated", "Widowed","Married-spouse-absent", "Married-AF-spouse"
            ,"Never-married"};
        }
        else if (i == 6){
            accepted_values = new String[]{"Tech-support", "Craft-repair",
             "Sales", "Exec-managerial", "Prof-specialty", "Handlers-cleaners",
             "Machine-op-inspct", "Adm-clerical", "Farming-fishing",
             "Transport-moving", "Priv-house-serv", "Protective-serv",
             "Armed-Forces", "Other-service"};
        }
        else if (i == 7){
            accepted_values = new String[]{"Wife", "Own-child", "Husband",
            "Other-relative", "Unmarried", "Not-in-family"};
        }
        else if (i == 8){
            accepted_values = new String[]{"White", "Asian-Pac-Islander",
             "Amer-Indian-Eskimo", "Other", "Black"};
        }
        else if (i == 9){
            accepted_values = new String[]{"Female", "Male"};
        }
        else if (i == 13){
            accepted_values = new String[]{"United-States", "Cambodia", "England",
             "Puerto-Rico", "Canada", "Germany", "Outlying-US(Guam-USVI-etc)",
             "India", "Japan", "Greece", "South", "China", "Cuba", "Iran",
             "Honduras", "Philippines", "Italy", "Poland", "Jamaica",
             "Vietnam", "Mexico","Portugal","Ireland", "France",
             "Dominican-Republic", "Laos", "Ecuador", "Taiwan", "Haiti",
             "Columbia", "Hungary", "Guatemala", "Nicaragua", "Scotland",
             "Thailand", "Yugoslavia", "El-Salvador", "Trinadad&Tobago", "Peru",
             "Hong", "Holand-Netherlands"};
        }
        else{
            accepted_values = null;
        }
        return accepted_values;

    }
    String getValue(){
        // <=50k => PostiveExample.
        return attributes[attributes.length - 1];
    }
}