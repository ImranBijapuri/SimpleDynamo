package edu.buffalo.cse.cse486586.simpledynamo;

import java.io.Serializable;

/**
 * Created by imransay on 4/23/15.
 */
public class Query_Originator implements Serializable{
    public String myport;
    public String guid;


    public Query_Originator(String myport,String guid){
        this.myport = myport;
        this.guid = guid;
    }
}
