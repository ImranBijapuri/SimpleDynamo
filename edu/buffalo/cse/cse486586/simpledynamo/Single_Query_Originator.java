package edu.buffalo.cse.cse486586.simpledynamo;

import java.io.Serializable;

/**
 * Created by imransay on 4/23/15.
 */
public class Single_Query_Originator implements Serializable{
    public String myport;
    public String selection;
    public String guid;

    public Single_Query_Originator(String myport,String selection,String guid){
        this.myport = myport;
        this.selection = selection;
        this.guid = guid;
    }
}
