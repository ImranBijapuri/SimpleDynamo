package edu.buffalo.cse.cse486586.simpledynamo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by imransay on 4/22/15.
 */
public class Complex_Object {
    public ConcurrentHashMap <String,String> map = new ConcurrentHashMap<String,String>();
    public List<String> Star_arrayList = Collections.synchronizedList(new ArrayList<String>());
    public String type;
    public AtomicInteger target;
    public AtomicInteger reached;
    public AtomicLong timestamp;
    public ConcurrentHashMap<String,String> checkmap = new ConcurrentHashMap<>();

    public Complex_Object(String type,AtomicInteger target,AtomicLong timestamp){
        this.type = type;
        this.target = target;
        this.reached = new AtomicInteger(0);
        this.timestamp = timestamp;
        checkmap.put("11108","T");checkmap.put("11112","T");checkmap.put("11116","T");checkmap.put("11120","T");checkmap.put("11124","T");
    }

}
