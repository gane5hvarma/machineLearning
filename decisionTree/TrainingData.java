package decision_tree;

import java.util.*;
import java.util.Arrays;
import java.lang.*;

class TrainingData implements Comparable<TrainingData>{
    String[] attributes;
    public static final Map<Integer, String> features;
    static{
        Map<Integer, String> map = new HashMap<Integer, String>();
        map.put(0, "age");
        map.put(1, "workclass");
        map.put(2, "fnlwgt");
        map.put(3, "education");
        map.put(4, "education-num");
        map.put(4, "marital-status");
        map.put(6, "occupation");
        map.put(7, "relationship");
        map.put(8, "race");
        map.put(9, "sex");
        map.put(10, "capital-gain");
        map.put(11, "capital-loss");
        map.put(12, "hours-per-week");
        map.put(13, "native-country");
        features = Collections.unmodifiableMap(map);
    }

    TrainingData(String[] attributes){
        this.attributes = attributes;
    }
    @Override
    public int compareTo(TrainingData t){
        return 0;
    }
    static String[] getAcceptedValues(int i){
        String[] accepted_values;
        if(i == 0){
            accepted_values = new String[]{"aT27.0","bT27.0"};
        }
        else if (i == 1){
            accepted_values = new String[]{"Private", "Self-emp-not-inc",
                             "Self-emp-inc", "Federal-gov", "Local-gov", 
                             "State-gov","Without-pay", "Never-worked"};
        }
        else if(i == 2){
            accepted_values = new String[]{"aT209912.0","bT209912.0"};
        }
        else if (i == 3){
            accepted_values = new String[]{"Bachelors", "Some-college", "11th", 
             "Prof-school", "Assoc-acdm", "Assoc-voc", "9th", "7th-8th", "12th",
             "Masters", "1st-4th", "10th", "Doctorate", "5th-6th",
             "Preschool", "HS-grad",};
        }
        else if(i == 4){
            accepted_values = new String[]{"aT12.0", "bT12.0"};
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
        else if(i == 10){
            accepted_values = new String[]{"bT7073.5", "aT7073.5"};
        }
        else if(i == 11){
            accepted_values = new String[]{"bT1820.5", "aT1820.5"};
        }
        else if(i == 12){
            accepted_values = new String[]{"bT41.0", "aT41.0"};
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