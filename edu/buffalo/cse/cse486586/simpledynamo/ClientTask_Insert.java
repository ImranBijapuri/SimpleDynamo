package edu.buffalo.cse.cse486586.simpledynamo;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by imransay on 4/21/15.
 */
public class ClientTask_Insert extends AsyncTask<Data_Insert_Object, Void, Void> {

    @Override
    protected Void doInBackground(Data_Insert_Object... objects) {
        try {
            Log.d("Logger", "Started a new ClientTask and flushing it to " + objects[1].toString());
            Socket socket = new Socket(InetAddress.getByAddress(new byte[]{10, 0, 2, 2}), Integer.parseInt(objects[1].toString()));
            ObjectOutputStream outputstream = new ObjectOutputStream(socket.getOutputStream());
            Data_Insert_Object obj = (Data_Insert_Object) objects[0];
            outputstream.writeObject(obj);
            outputstream.flush();
            socket.close();
        } catch (UnknownHostException e) {
            Log.e("E", "ClientTask UnknownHostException");
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("E", "ClientTask socket IOException " + e.getLocalizedMessage());
        }

        return null;
    }
}