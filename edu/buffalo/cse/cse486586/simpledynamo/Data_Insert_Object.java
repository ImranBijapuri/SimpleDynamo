package edu.buffalo.cse.cse486586.simpledynamo;

import java.io.Serializable;

/**
 * Created by imransay on 4/21/15.
 */
public class Data_Insert_Object implements Serializable {
    public String key;
    public String value;
    public String origin;

    public Data_Insert_Object(String key,String value,String origin){
        this.key = key;
        this.value = value;
        this.origin = origin;
    }
}
