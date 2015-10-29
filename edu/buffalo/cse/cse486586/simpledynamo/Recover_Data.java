package edu.buffalo.cse.cse486586.simpledynamo;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

/**
 * Created by imransay on 4/23/15.
 */
public class Recover_Data {

    //public static int count = 0;

    public static void Recover_my_data(){
        Log.d("Logger","I am in Recover_my_data");
        ArrayList<String> arrayList = SimpleDynamoProvider.neighbour.get(SimpleDynamoProvider.Make_it_Half(SimpleDynamoProvider.myport));
        Log.d("Logger" , "The arraylist i got while recovering my data " + arrayList);
        Dummy_My_Recovery_Data dummy_my_recovery_data = new Dummy_My_Recovery_Data(SimpleDynamoProvider.myport);

        new Inside_Use_ClientTask_Recover_Data().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, dummy_my_recovery_data, arrayList.get(1));
        new Inside_Use_ClientTask_Recover_Data().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, dummy_my_recovery_data, arrayList.get(2));

       // while(count <= 1){

       // }


    }

    public static void Recover_replication_data(String[] array){
        Log.d("Logger","I am in Recover_replication_data");

        Dummy_Recover_Replication_Data dummy_recover_replication_data = new Dummy_Recover_Replication_Data(SimpleDynamoProvider.myport);

        new Inside_Use_ClientTask_Recover_Data().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, dummy_recover_replication_data, array[0]);
        new Inside_Use_ClientTask_Recover_Data().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, dummy_recover_replication_data, array[1]);

    }

    private static class Inside_Use_ClientTask_Recover_Data extends AsyncTask<Object, Void, Void> {
        @Override
        protected Void doInBackground(Object... objects) {
            try {
                Log.d("Logger", "Started a new Inside_Use_ClientTask_Recover_Data and flushing it to " + objects[1] + objects[0].getClass());
                Socket socket = new Socket(InetAddress.getByAddress(new byte[]{10, 0, 2, 2}), Integer.parseInt(objects[1].toString()));
                ObjectOutputStream outputstream = new ObjectOutputStream(socket.getOutputStream());
                outputstream.writeObject(objects[0]);
                outputstream.flush();
                socket.close();
            } catch (UnknownHostException e) {
                Log.e("E", "Inside_Use_ClientTask_Recover_Data UnknownHostException");
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("E", "Inside_Use_ClientTask_Recover_Data socket IOException " +e.getLocalizedMessage());
            }

            return null;
        }


    }

}
