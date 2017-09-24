package decision_tree;

imort java.util.*;

class Attribute{
    String name;
    ArrayList<String> accepted_values = new ArrayList<String>();
    double range;
    boolean continuous;
    double minimum_value;
    int index;
    Attribute(String name, String accepted_values){
        this.name = name;
        this.accepted_values = accepted_values;
    }
    boolean equal(Attribute a){
        if (a.name.equalsIgnoreCase(this.name) && a.accepted_values) {
            
        }
    }
}