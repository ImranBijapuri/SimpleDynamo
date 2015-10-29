package edu.buffalo.cse.cse486586.simpledynamo;

import java.io.Serializable;

/**
 * Created by imransay on 4/24/15.
 */
public class Dummy_Recover_Replication_Data implements Serializable{
    public String origin;

    public Dummy_Recover_Replication_Data(String origin){
        this.origin = origin;
    }

}
