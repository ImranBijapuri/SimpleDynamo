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
public class Delete_From_All {
    public static void deleting_from_all(){

        Data_Delete_Object data_delete_object = new Data_Delete_Object("*");
        new Inside_Use_Delete_ClientTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, data_delete_object, 11108);
        new Inside_Use_Delete_ClientTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, data_delete_object, 11112);
        new Inside_Use_Delete_ClientTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, data_delete_object, 11116);
        new Inside_Use_Delete_ClientTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, data_delete_object, 11120);
        new Inside_Use_Delete_ClientTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, data_delete_object, 11124);
    }

    public static void delete_selection(String selection){
        Log.d("Logger", "In delete_selection " + selection);
        Data_Delete_Object data_delete_object = new Data_Delete_Object(selection);
        SimpleDynamoProvider simpleDynamoProvider = new SimpleDynamoProvider();
        String port = simpleDynamoProvider.find_my_position_to_insert(selection);
        simpleDynamoProvider.delete_all(data_delete_object);
        ArrayList<String> arrayList = SimpleDynamoProvider.neighbour.get(port);
        new Inside_Use_Delete_ClientTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, data_delete_object, Integer.parseInt(arrayList.get(0)));
    }



    private static class Inside_Use_Delete_ClientTask extends AsyncTask<Object, Void, Void> {
        @Override
        protected Void doInBackground(Object... objects) {
            try {
                Log.d("Logger", "Started a new Inside_Use_Delete_ClientTask and flushing it to " + objects[1].toString() + objects[0].getClass());
                Socket socket = new Socket(InetAddress.getByAddress(new byte[]{10, 0, 2, 2}), Integer.parseInt(objects[1].toString()));
                ObjectOutputStream outputstream = new ObjectOutputStream(socket.getOutputStream());
                outputstream.writeObject(objects[0]);
                outputstream.flush();
                socket.close();
            } catch (UnknownHostException e) {
                Log.e("E", "Inside_Use_Delete_ClientTask UnknownHostException");
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("E", "Inside_Use_Delete_ClientTask socket IOException " +e.getLocalizedMessage());
            }

            return null;
        }


    }

}
