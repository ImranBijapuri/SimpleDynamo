package edu.buffalo.cse.cse486586.simpledynamo;

import java.io.IOException;
import java.net.ServerSocket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.telephony.TelephonyManager;
import android.util.Log;

public class SimpleDynamoProvider extends ContentProvider {

    private static final String AUTHORITY = "edu.buffalo.cse.cse486586.simpledynamo.SimpleDynamoProvider";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static MySQLiteHelper db = null;
    public Context context = this.getContext();
    public static HashMap<String, String> mapper = new HashMap<String, String>();
    public static HashMap<String, ArrayList<String>> neighbour = new HashMap<String, ArrayList<String>>();
    public static String myport;
    public static ConcurrentHashMap<String,Complex_Object> Big_Boss = new ConcurrentHashMap<String,Complex_Object>();

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // TODO Auto-generated method stub
        db.delete(selection);
        Log.d("Logger","I am in delete");
        return 0;
    }

    @Override
    public String getType(Uri uri) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        String target_port = find_my_position_to_insert(values.getAsString("key"));
        ArrayList arr = neighbour.get(target_port);
        Podcast_for_insert(arr,values);
        return null;
    }

    public void insert_data(Data_Insert_Object obj){
        db.insert(obj);
    }

    @Override
    public boolean onCreate() {
        myport = get_my_port();
        Hash_Table_Generator();
        Neighbour_Generator(myport);
        db = new MySQLiteHelper(getContext());
        db.getWritableDatabase();

        if(isRecovering()){
            Log.d("Logger","I am in isrecovering mode");
            Recover_my_data();
            Recover_replication_data();
        }


        try {
            Log.d("Logger","I am in serverSocket");
            ServerSocket serverSocket = new ServerSocket(10000);
            new ServerTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, serverSocket);
        } catch (IOException e) {
            Log.e("E", "Can't create a ServerSocket");
            return false;
        }

        return false;
    }

    public void Recover_my_data(){
        Log.d("Logger","I am in Recover_my_data in provider");
        Recover_Data.Recover_my_data();
    }

    public void Recover_replication_data(){
        Log.d("Logger","I am in Recover_replication_data in provider");
        String[] array = new String[2];
        if(myport.equals("11108")){array[0]="11124";array[1]="11112";}
        else if(myport.equals("11112")){array[0]="11124";array[1]="11120";}
        else if(myport.equals("11116")){array[0]="11108";array[1]="11112";}
        else if(myport.equals("11120")){array[0]="11108";array[1]="11116";}
        else if(myport.equals("11124")){array[0]="11120";array[1]="11116";}
        Recover_Data.Recover_replication_data(array);
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        // TODO Auto-generated method stub
        Cursor cursor = db.query(selection);
        //Log.d("Logger","The cursor i got is of size " + cursor.getCount());
        return cursor;
    }

    public boolean isRecovering(){
        return db.Recovery_Agent();
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO Auto-generated method stub
        return 0;
    }

    private String genHash(String input) {
        try {
            MessageDigest sha1 = MessageDigest.getInstance("SHA-1");
            byte[] sha1Hash = sha1.digest(input.getBytes());
            Formatter formatter = new Formatter();
            for (byte b : sha1Hash) {
                formatter.format("%02x", b);
            }
            return formatter.toString();
        } catch (NoSuchAlgorithmException e) {
            Log.e("Logger", "in genHash NoSuchAlgorithmException");
            return null;
        }
    }

    public void Hash_Table_Generator() {
            mapper.put("5554", genHash("5554"));
            mapper.put("5556", genHash("5556"));
            mapper.put("5558", genHash("5558"));
            mapper.put("5560", genHash("5560"));
            mapper.put("5562", genHash("5562"));
    }

    public String get_my_port() {
        TelephonyManager tel = (TelephonyManager) this.getContext().getSystemService(Context.TELEPHONY_SERVICE);
        String portStr = tel.getLine1Number().substring(tel.getLine1Number().length() - 4);
        return String.valueOf((Integer.parseInt(portStr) * 2));
    }

    public void Neighbour_Generator(String myport) {
        ArrayList<String> arrayList = null;

        arrayList = new ArrayList<String>();
        arrayList.add("11124");arrayList.add("11112");arrayList.add("11108");
        neighbour.put("5562",arrayList);

        arrayList = new ArrayList<String>();
        arrayList.add("11112");arrayList.add("11108");arrayList.add("11116");
        neighbour.put("5556",arrayList);

        arrayList = new ArrayList<String>();
        arrayList.add("11108");arrayList.add("11116");arrayList.add("11120");
        neighbour.put("5554",arrayList);

        arrayList = new ArrayList<String>();
        arrayList.add("11116");arrayList.add("11120");arrayList.add("11124");
        neighbour.put("5558",arrayList);

        arrayList = new ArrayList<String>();
        arrayList.add("11120");arrayList.add("11124");arrayList.add("11112");
        neighbour.put("5560",arrayList);


    }

    public static String Make_it_Half(String str) {
        return Integer.toString(Integer.parseInt(str) / 2);
    }

    public String find_my_position_to_insert(String data) {
        String data_genhash = genHash(data);
        if(data_genhash.compareTo(mapper.get("5562")) < 0 || data_genhash.compareTo(mapper.get("5560")) > 0){
            return "5562";
        }
        else if(data_genhash.compareTo(mapper.get("5556")) < 0 && data_genhash.compareTo(mapper.get("5562")) > 0){
            return "5556";
        }
        else if(data_genhash.compareTo(mapper.get("5554")) < 0 && data_genhash.compareTo(mapper.get("5556")) > 0){
            return "5554";
        }
        else if(data_genhash.compareTo(mapper.get("5558")) < 0 && data_genhash.compareTo(mapper.get("5554")) > 0){
            return "5558";
        }
        else if(data_genhash.compareTo(mapper.get("5560")) < 0 && data_genhash.compareTo(mapper.get("5558")) > 0){
            return "5560";
        }

        return "";
    }

    public void Podcast_for_insert(ArrayList arrayList,ContentValues contentValues){
        Data_Insert_Object obj = new Data_Insert_Object(contentValues.getAsString("key"),contentValues.getAsString("value"),arrayList.get(0).toString());

        new ClientTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, obj, arrayList.get(0).toString());
        new ClientTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, obj, arrayList.get(1).toString());
        new ClientTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, obj, arrayList.get(2).toString());


    }

    public void result_for_star(Query_Originator query_originator){
        Log.d("Logger","I am in result_for_star");
        db.data_for_star(query_originator);
    }


    public void result_for_selection(Single_Query_Originator single_Query_Originator){
        Log.d("Logger","I am in result_for_selection");
        db.data_for_selection(single_Query_Originator);
    }

    public void delete_all(Data_Delete_Object data_delete_object){
        Log.d("Logger","I am in delete_all");
        db.delete_all_inside(data_delete_object);
    }

    public void recover_my_data(Dummy_My_Recovery_Data dummy_my_recovery_data){
        Log.d("Logger","I am in recover_my_data");
        db.recover_my_data(dummy_my_recovery_data);
    }

    public void update_my_recovered_data(Recover_My_Data_Object recover_my_data_object){
        Log.d("Logger","I am in update_my_recovered_data");
        db.updating_my_recovered_data(recover_my_data_object);
    }

    public void recover_replication_data(Dummy_Recover_Replication_Data dummy_recover_replication_data){
        Log.d("Logger","I am in recover_replication_data");
        db.recover_replication_data(dummy_recover_replication_data);
    }
    public void update_recover_replication_data(Recover_Replication_Data_Object recover_replication_data_object){
        Log.d("Logger","I am in update_recover_replication_data");
        db.update_recover_replication_data(recover_replication_data_object);
    }


}
