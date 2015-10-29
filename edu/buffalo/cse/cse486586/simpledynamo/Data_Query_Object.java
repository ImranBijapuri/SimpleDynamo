package edu.buffalo.cse.cse486586.simpledynamo;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by imransay on 4/23/15.
 */
public class Data_Query_Object implements Serializable{

    public ArrayList<String> arrayList = new ArrayList<String>();
    public String coming_from;
    public String guid;

    public Data_Query_Object(ArrayList<String> arrayList,String coming_from,String guid){
        this.arrayList = arrayList;
        this.coming_from = coming_from;
        this.guid = guid;
    }
}
