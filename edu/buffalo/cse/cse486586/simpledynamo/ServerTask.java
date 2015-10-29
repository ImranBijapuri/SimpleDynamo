package edu.buffalo.cse.cse486586.simpledynamo;

import android.os.AsyncTask;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by imransay on 4/21/15.
 */
public class ServerTask extends AsyncTask<ServerSocket, String, Void> {

    @Override
    protected Void doInBackground(ServerSocket... sockets) {
        ServerSocket serverSocket = sockets[0];

        Socket socket = null;
        String message = null;
        boolean status = true;

        while (status) {
            try {

                socket = serverSocket.accept();
                ObjectInputStream inputstream = new ObjectInputStream(socket.getInputStream());
                Object o = inputstream.readObject();
                Log.d("Logger", " I am in doInBackground and instance is " + o.getClass());
                if (o instanceof Data_Insert_Object) {
                    Log.d("Logger", " I am instance of Data_Insert_Object");
                    SimpleDynamoProvider obj = new SimpleDynamoProvider();
                    obj.insert_data((Data_Insert_Object) o);
                }

                else if(o instanceof Query_Originator){
                    Log.d("Logger", " I am instance of Query_Originator");
                    Query_Originator query_originator = (Query_Originator) o ;
                    SimpleDynamoProvider simpleDynamoProvider = new SimpleDynamoProvider();
                    simpleDynamoProvider.result_for_star(query_originator);
                }
                else if (o instanceof Data_Query_Object_Selection){

                    Data_Query_Object_Selection data_query_object_selection = (Data_Query_Object_Selection) o ;
                    Log.d("Logger", " I am instance of Data_Query_Object and size is "  + data_query_object_selection.arrayList.size());
                    ArrayList<String> arrayList = data_query_object_selection.arrayList;
                    for(int i = 0 ; i < arrayList.size() ; i++){
                        Log.d("Logger", " I am in Data_Query_Object without split " + arrayList.get(i));
                        String[]array = arrayList.get(i).split("\\|");
                        Log.d("Logger", " I am in Data_Query_Object and I bought with me " + array[0] + " xxx " + array[1] + " xxx " + data_query_object_selection.coming_from);


                        if(SimpleDynamoProvider.Big_Boss.get(data_query_object_selection.guid).map.containsKey(array[0])){
                            //here object versioning will come into play
                            Log.d("Logger", " I am in Data_Query_Object object vers");

                        }else{

                            SimpleDynamoProvider.Big_Boss.get(data_query_object_selection.guid).map.put(array[0],array[1]);

                        }
                    }

                    Log.d("Logger" , data_query_object_selection.coming_from + " is incre reached" );
                    SimpleDynamoProvider.Big_Boss.get(data_query_object_selection.guid).reached.set(SimpleDynamoProvider.Big_Boss.get(data_query_object_selection.guid).reached.incrementAndGet());



                    Log.d("Logger", " Value of reached is " + SimpleDynamoProvider.Big_Boss.get(data_query_object_selection.guid).reached + " and target is " +  SimpleDynamoProvider.Big_Boss.get(data_query_object_selection.guid).target);
                }

                else if (o instanceof Data_Query_Object){

                    Data_Query_Object data_query_object = (Data_Query_Object) o ;
                    Log.d("Logger", " I am instance of Data_Query_Object and size is "  + data_query_object.arrayList.size());
                    ArrayList<String> arrayList = data_query_object.arrayList;
                    for(int i = 0 ; i < arrayList.size() ; i++){
                        Log.d("Logger", " I am in Data_Query_Object without split " + arrayList.get(i));
                        String[]array = arrayList.get(i).split("\\|");
                        Log.d("Logger", " I am in Data_Query_Object and I bought with me " + array[0] + " xxx " + array[1] + " xxx " + data_query_object.coming_from);
                        SimpleDynamoProvider.Big_Boss.get(data_query_object.guid).Star_arrayList.add(array[0] + "|" + array[1]);
                    }

                    SimpleDynamoProvider.Big_Boss.get(data_query_object.guid).reached.set(SimpleDynamoProvider.Big_Boss.get(data_query_object.guid).reached.incrementAndGet());



                    Log.d("Logger", " Size of star arraylist " + SimpleDynamoProvider.Big_Boss.get(data_query_object.guid).Star_arrayList.size());
                    Log.d("Logger", " Value of reached is " + SimpleDynamoProvider.Big_Boss.get(data_query_object.guid).reached + " and target is " +  SimpleDynamoProvider.Big_Boss.get(data_query_object.guid).target);
                }

                else if (o instanceof Single_Query_Originator){
                    Log.d("Logger", " I am instance of Single_Query_Originator");
                    Single_Query_Originator single_Query_Originator = (Single_Query_Originator) o ;
                    SimpleDynamoProvider simpleDynamoProvider = new SimpleDynamoProvider();
                    simpleDynamoProvider.result_for_selection(single_Query_Originator);

                }
                else if (o instanceof Data_Delete_Object){
                    Log.d("Logger", " I am instance of Data_Delete_Object");
                    Data_Delete_Object data_delete_object = (Data_Delete_Object) o ;
                    SimpleDynamoProvider simpleDynamoProvider = new SimpleDynamoProvider();
                    simpleDynamoProvider.delete_all(data_delete_object);

                }
                else if(o instanceof Dummy_My_Recovery_Data){
                    Log.d("Logger", " I am instance of Dummy_My_Recovery_Data");
                    Dummy_My_Recovery_Data dummy_my_recovery_data = (Dummy_My_Recovery_Data) o;
                    SimpleDynamoProvider simpleDynamoProvider = new SimpleDynamoProvider();
                    simpleDynamoProvider.recover_my_data(dummy_my_recovery_data);
                }
                else if(o instanceof Recover_My_Data_Object){
                    Log.d("Logger", " I am instance of Recover_My_Data_Object");
                    Recover_My_Data_Object recover_my_data_object = (Recover_My_Data_Object) o;
                    SimpleDynamoProvider simpleDynamoProvider = new SimpleDynamoProvider();
                    simpleDynamoProvider.update_my_recovered_data(recover_my_data_object);
                }
                else if(o instanceof Dummy_Recover_Replication_Data){
                    Log.d("Logger", " I am instance of Dummy_Recover_Replication_Data");
                    Dummy_Recover_Replication_Data dummy_recover_replication_data = (Dummy_Recover_Replication_Data) o;
                    SimpleDynamoProvider simpleDynamoProvider = new SimpleDynamoProvider();
                    simpleDynamoProvider.recover_replication_data(dummy_recover_replication_data);
                }
                else if(o instanceof Recover_Replication_Data_Object){
                    Log.d("Logger", " I am instance of Recover_Replication_Data_Object");
                    Recover_Replication_Data_Object recover_replication_data_object = (Recover_Replication_Data_Object) o;
                    SimpleDynamoProvider simpleDynamoProvider = new SimpleDynamoProvider();
                    simpleDynamoProvider.update_recover_replication_data(recover_replication_data_object);
                }



        }catch(IOException e){
            e.printStackTrace();
        }catch(ClassNotFoundException e){
            e.printStackTrace();
        }
    }
        return null;
    }
}
