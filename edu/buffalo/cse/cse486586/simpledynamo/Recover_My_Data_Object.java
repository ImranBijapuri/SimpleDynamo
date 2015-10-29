package edu.buffalo.cse.cse486586.simpledynamo;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by imransay on 4/23/15.
 */
public class Recover_My_Data_Object implements Serializable {
    public ArrayList<String> arrayList = new ArrayList<String>();

    public Recover_My_Data_Object(ArrayList<String> arrayList){
        this.arrayList = arrayList;
    }
}
