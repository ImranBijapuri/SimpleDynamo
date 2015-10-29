package edu.buffalo.cse.cse486586.simpledynamo;

import android.database.Cursor;
import android.database.MatrixCursor;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by imransay on 4/22/15.
 */
public class Fetch_From_All {

    public static Cursor fetching_from_all(){

        String guid = java.util.UUID.randomUUID().toString();
        Query_Originator obj = new Query_Originator(SimpleDynamoProvider.myport,guid);

        new Inside_Use_ClientTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, obj, 11108);
        new Inside_Use_ClientTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, obj, 11112);
        new Inside_Use_ClientTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, obj, 11116);
        new Inside_Use_ClientTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, obj, 11120);
        new Inside_Use_ClientTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, obj, 11124);

        Complex_Object complex_object = new Complex_Object("*",new AtomicInteger(5),new AtomicLong(System.currentTimeMillis()));
        SimpleDynamoProvider.Big_Boss.put(guid,complex_object);

      /*  Fetch_From_All fetch_from_all = new Fetch_From_All();
        fetch_from_all.flush_out_to_check(guid,"11108");
        fetch_from_all.flush_out_to_check(guid,"11112");
        fetch_from_all.flush_out_to_check(guid,"11116");
        fetch_from_all.flush_out_to_check(guid,"11120");
        fetch_from_all.flush_out_to_check(guid,"11124");*/

     //   while(SimpleDynamoProvider.Big_Boss.get(guid).target.get() != SimpleDynamoProvider.Big_Boss.get(guid).reached.get()){
/*            if(SimpleDynamoProvider.Big_Boss.get(guid).target.get()-SimpleDynamoProvider.Big_Boss.get(guid).reached.get() == 1){
                if(System.currentTimeMillis() - SimpleDynamoProvider.Big_Boss.get(guid).timestamp.get() > 2000){
                    SimpleDynamoProvider.Big_Boss.get(obj.guid).target.set(SimpleDynamoProvider.Big_Boss.get(obj.guid).target.decrementAndGet());
                }
            }
*/
       // }

        try {
            Thread.sleep(2000);                 //1000 milliseconds is one second.
        } catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        }


        Log.d("Logger", "The while for star has ended and size of star arraylist is " + SimpleDynamoProvider.Big_Boss.get(guid).Star_arrayList.size());


        //Here we have to return the cursor;
        List<String> arrayList = SimpleDynamoProvider.Big_Boss.get(guid).Star_arrayList;
        MatrixCursor cursor = new MatrixCursor(new String[]{"key","value"});
        String[]array  = null;
        for(int i = 0 ; i < arrayList.size() ; i ++){
            array = arrayList.get(i).split("\\|");
            cursor.addRow(new String[]{array[0],array[1]});
            Log.d("Logger",arrayList.get(i));
        }
        return cursor;
    }

    public static String[] Fetch_for_selection(String selection){
        Log.d("Logger","I am in fetch for selection ");
        String guid = java.util.UUID.randomUUID().toString();
        Complex_Object complex_object = new Complex_Object("selection",new AtomicInteger(3),new AtomicLong(System.currentTimeMillis()));

        SimpleDynamoProvider simpleDynamoProvider = new SimpleDynamoProvider();
        String port = simpleDynamoProvider.find_my_position_to_insert(selection);
        ArrayList arrayList = SimpleDynamoProvider.neighbour.get(port);
        Single_Query_Originator single_query_originator = new Single_Query_Originator(SimpleDynamoProvider.myport,selection,guid);

        new Inside_Use_ClientTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, single_query_originator, arrayList.get(0));
        new Inside_Use_ClientTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, single_query_originator, arrayList.get(1));
        new Inside_Use_ClientTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, single_query_originator, arrayList.get(2));

        SimpleDynamoProvider.Big_Boss.put(guid,complex_object);


/*        Fetch_From_All fetch_from_all = new Fetch_From_All();
        fetch_from_all.flush_out_to_check(guid,arrayList.get(0).toString());
        fetch_from_all.flush_out_to_check(guid,arrayList.get(1).toString());
        fetch_from_all.flush_out_to_check(guid,arrayList.get(2).toString());

        while(SimpleDynamoProvider.Big_Boss.get(guid).target.get() != SimpleDynamoProvider.Big_Boss.get(guid).reached.get()){
/*            if(SimpleDynamoProvider.Big_Boss.get(guid).target.get()-SimpleDynamoProvider.Big_Boss.get(guid).reached.get() == 1){
                if(System.currentTimeMillis() - SimpleDynamoProvider.Big_Boss.get(guid).timestamp.get() > 2000){
                    SimpleDynamoProvider.Big_Boss.get(single_query_originator.guid).target.set(SimpleDynamoProvider.Big_Boss.get(single_query_originator.guid).target.decrementAndGet());
                }
            }
          //    && System.currentTimeMillis() - SimpleDynamoProvider.Big_Boss.get(guid).timestamp.get() < 500 ){
*/
    /*    }*/

        try {
            Thread.sleep(2000);                 //1000 milliseconds is one second.
        } catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        }

        String[] array = new String[]{selection,SimpleDynamoProvider.Big_Boss.get(guid).map.get(selection)};
        Log.d("Logger","The while has ended and now and the key and value we have is " +selection + "  " + SimpleDynamoProvider.Big_Boss.get(guid).map.get(selection) );
        return array;
    }

    public void flush_out_to_check(String guid,String port) {
        try {
            Log.d("Logger", "I am here in flush out");
            Socket socket = new Socket(InetAddress.getByAddress(new byte[]{10, 0, 2, 2}), Integer.parseInt(port));
            ObjectOutputStream outputstream = new ObjectOutputStream(socket.getOutputStream());
            outputstream.writeObject("dummy");
            outputstream.flush();
            socket.close();

        } catch (UnknownHostException e) {
            Log.d("Logger", "Inside_Use_ClientTask UnknownHostException");
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("Logger", "we caught him in flush_out_to_check");
            if (SimpleDynamoProvider.Big_Boss.get(guid).checkmap.get(port).equals("T")) {
                SimpleDynamoProvider.Big_Boss.get(guid).target.set(SimpleDynamoProvider.Big_Boss.get(guid).target.decrementAndGet());
                SimpleDynamoProvider.Big_Boss.get(guid).checkmap.put(port, "F");
            } else {

            }

        }
    }

    private static class Inside_Use_ClientTask extends AsyncTask<Object, Void, Void> {
        @Override
        protected Void doInBackground(Object... objects) {
            try {
                Log.d("Logger", "Started a new Inside_Use_ClientTask and flushing it to " + objects[1] + objects[0].getClass());
                Socket socket = new Socket(InetAddress.getByAddress(new byte[]{10, 0, 2, 2}), Integer.parseInt(objects[1].toString()));
                ObjectOutputStream outputstream = new ObjectOutputStream(socket.getOutputStream());
                outputstream.writeObject(objects[0]);
                outputstream.flush();
                socket.close();
                this.cancel(false);
            } catch (UnknownHostException e) {
                Log.d("Logger", "Inside_Use_ClientTask UnknownHostException");
            } catch (IOException e) {
                e.printStackTrace();
                if(objects[0] instanceof Query_Originator){
                    Query_Originator obj = (Query_Originator) objects[0];
                    SimpleDynamoProvider.Big_Boss.get(obj.guid).target.set(SimpleDynamoProvider.Big_Boss.get(obj.guid).target.decrementAndGet());
                    SimpleDynamoProvider.Big_Boss.get(obj.guid).checkmap.put(objects[1].toString(),"F");
                    Log.d("Logger", " decremented target to " +SimpleDynamoProvider.Big_Boss.get(obj.guid).target);
                }else if(objects[0] instanceof Single_Query_Originator){
                    Single_Query_Originator obj = (Single_Query_Originator) objects[0];
                    SimpleDynamoProvider.Big_Boss.get(obj.guid).target.set(SimpleDynamoProvider.Big_Boss.get(obj.guid).target.decrementAndGet());
                    Log.d("Logger", "Inside_Use_ClientTask socket IOException and decremented target to " +SimpleDynamoProvider.Big_Boss.get(obj.guid).target);
                }

                Log.e("E", "Inside_Use_ClientTask socket IOException and decremented reached to " +e.getLocalizedMessage());
            }

            return null;
        }




    }
}
