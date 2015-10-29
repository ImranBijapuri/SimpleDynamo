package edu.buffalo.cse.cse486586.simpledynamo;

import java.io.Serializable;

/**
 * Created by imransay on 4/23/15.
 */
public class Data_Delete_Object implements Serializable {

    public String selection;
    public Data_Delete_Object(String selection){
        this.selection = selection;
    }

}
