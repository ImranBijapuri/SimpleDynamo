package edu.buffalo.cse.cse486586.simpledynamo;

import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.ContactsContract;
import android.util.Log;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by imransay on 4/20/15.
 */
public class MySQLiteHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Messenger.db";


    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.d("Logger","I am in Mysqllitehelper constructor after super call");
    }

    public MySQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("Logger", "call to oncreate from constructor");
        String CREATE_MESSENGER_TABLE = "CREATE TABLE messenger (key varchar(500) UNIQUE,value varchar(500),version INTEGER, origin varchar(100))";
        db.execSQL(CREATE_MESSENGER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public Cursor query(String selection){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor ;

        if(selection.equals("\"@\"")){
            cursor = db.rawQuery("SELECT key,value FROM messenger", null);
            Log.d("Logger","The cursor ret in @ " + cursor.getCount());
            return cursor;
        }else if(selection.equals("\"*\"")){
            Log.d("Logger","The call is going to fetch for star selection ");
            cursor = Fetch_From_All.fetching_from_all();
            return cursor;
        }else {
            Log.d("Logger","The call is going to fetch for selection ");
            String[] arr = Fetch_From_All.Fetch_for_selection(selection);
            //cursor = db.rawQuery("SELECT key,value FROM messenger where key = '" + selection + "'", null);
            Log.d("Logger","Added to the cursor " + arr[0] + " " + arr[1]);
            MatrixCursor cursor1 = new MatrixCursor(new String[]{"key","value"});
            cursor1.addRow(new String[]{arr[0],arr[1]});

            return cursor1;
        }

    }

    public void insert(Data_Insert_Object obj){
        try {
            Log.d("Logger", "Inserting into db at key " + obj.key + " values " + obj.value);
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor;
            int version = 0;
            cursor = db.rawQuery("SELECT key,value FROM messenger where key = '" + obj.key + "'", null);

            if (cursor.getCount() == 0) {
                db.execSQL("INSERT INTO messenger(key,value,version,origin) " +
                        "VALUES('" + obj.key + "','" + obj.value + "'," + 1 + ",'" + obj.origin + "' );");
                } else {
                cursor = db.rawQuery("select version from messenger where key = '" + obj.key + "'", null);
                if (cursor.moveToFirst()) {
                    do {
                        version = cursor.getInt(0);
                    } while (cursor.moveToNext());
                }
                version++;
                db.execSQL("update messenger " +
                        "set value = '" + obj.value + "',version = " + version + ",origin = '" + obj.origin + "' where key ='" + obj.key + "';");

            }
        }catch (Exception e){
            Log.d("Logger", "Error in insert " + e.getLocalizedMessage());
        }
    }

    public boolean Recovery_Agent(){
        Log.d("Logger","I am in Recovery Agent");
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor;
        cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='messenger';", null);
        if(cursor.getCount()==0){
            Log.d("Logger","I am in 0");
            return false;
        }else{
            db.execSQL("Delete from messenger");
            Log.d("Logger","I am in 1 and I emptied it");
            return true;
        }
    }


    public void data_for_star(Query_Originator query_originator){
        Log.d("Logger","I am in data_for_star");
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor;
        cursor = db.rawQuery("SELECT key,value,version FROM messenger", null);
        ArrayList<String> arrayList = new ArrayList<String>();
        String data = "";

        if(cursor.moveToFirst()){
            do{
                data = cursor.getString(0) + "|"  + cursor.getString(1) + "|" + cursor.getString(2);
                arrayList.add(data);
            }while(cursor.moveToNext());
        }
        Log.d("Logger","Cursor count in  data_for_star " + cursor.getCount());
        Data_Query_Object data_query_object = new Data_Query_Object(arrayList,SimpleDynamoProvider.myport,query_originator.guid);
        try {
            Socket socket = new Socket(InetAddress.getByAddress(new byte[]{10, 0, 2, 2}), Integer.parseInt(query_originator.myport));
            ObjectOutputStream outputstream = new ObjectOutputStream(socket.getOutputStream());
            outputstream.writeObject(data_query_object);
            outputstream.flush();
            socket.close();
        } catch (UnknownHostException e) {
            Log.e("E", "ClientTask UnknownHostException");
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("E", "ClientTask socket IOException " +e.getLocalizedMessage());
        }

    }

    public void data_for_selection(Single_Query_Originator single_Query_Originator){
        Log.d("Logger","I am in data_for_selection");
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor;
        cursor = db.rawQuery("SELECT key,value,version FROM messenger where key = '"+single_Query_Originator.selection+"'", null);
        ArrayList<String> arrayList = new ArrayList<String>();
        String data = "";
        Log.d("Logger","Cursor count in  data_for_selection " + cursor.getCount());
        if(cursor.moveToFirst()){
            do{
                Log.d("Logger","Pipe separated data contains " + cursor.getString(0) + "|"  + cursor.getString(1) + "|" + cursor.getString(2));
                data = data + cursor.getString(0) + "|"  + cursor.getString(1) + "|" + cursor.getString(2);
                arrayList.add(data);
            }while(cursor.moveToNext());
        }

        Data_Query_Object_Selection data_query_object_selection = new Data_Query_Object_Selection(arrayList,SimpleDynamoProvider.myport,single_Query_Originator.guid);
        try {
            Log.d("Logger","Flushing data query object to  " + single_Query_Originator.myport);
            Socket socket = new Socket(InetAddress.getByAddress(new byte[]{10, 0, 2, 2}), Integer.parseInt(single_Query_Originator.myport));
            ObjectOutputStream outputstream = new ObjectOutputStream(socket.getOutputStream());
            outputstream.writeObject(data_query_object_selection);
            outputstream.flush();
            socket.close();
        } catch (UnknownHostException e) {
            Log.e("E", "ClientTask UnknownHostException");
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("E", "ClientTask socket IOException " +e.getLocalizedMessage());
        }

    }


    public void delete(String selection){
        SQLiteDatabase db = this.getWritableDatabase();
        if(selection.equals("\"@\"")){
            Log.d("Logger","Deleting from @");
            db.execSQL("delete from messenger");
        }else if(selection.equals("\"*\"")){
            Delete_From_All.deleting_from_all();
        }else {
            Delete_From_All.delete_selection(selection);
        }
    }

    public void delete_all_inside(Data_Delete_Object data_delete_object){
        Log.d("Logger","Inside delete_all_inside");
        SQLiteDatabase db = this.getWritableDatabase();
        if(data_delete_object.selection.equals("*")){
            db.execSQL("delete from messenger");
        }else {
            db.execSQL("delete from messenger where key = '" + data_delete_object.selection +"'");
        }
    }

    public void recover_my_data(Dummy_My_Recovery_Data dummy_my_recovery_data){
        Log.d("Logger","I am in recover_my_data in helper");
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor;
        cursor = db.rawQuery("SELECT key,value,version,origin FROM messenger where origin = '"+dummy_my_recovery_data.origin+"'", null);
        ArrayList<String> arrayList = new ArrayList<String>();
        String data = "";
        Log.d("Logger","I am in recover_my_data and cursor count is " + cursor.getCount());
        if(cursor.moveToFirst()){
            do{
                Log.d("Logger",cursor.getString(0) + "|"  + cursor.getString(1) + "|" + cursor.getString(2) + "|" + cursor.getString(3));
                data = cursor.getString(0) + "|"  + cursor.getString(1) + "|" + cursor.getString(2) + "|" + cursor.getString(3);
                arrayList.add(data);
            }while(cursor.moveToNext());
        }
        Recover_My_Data_Object recover_my_data_object = new Recover_My_Data_Object(arrayList);

        try {
            Log.d("Logger","Flushing recover_my_data_object to  " + dummy_my_recovery_data.origin);
            Socket socket = new Socket(InetAddress.getByAddress(new byte[]{10, 0, 2, 2}), Integer.parseInt(dummy_my_recovery_data.origin));
            ObjectOutputStream outputstream = new ObjectOutputStream(socket.getOutputStream());
            outputstream.writeObject(recover_my_data_object);
            outputstream.flush();
            socket.close();
        } catch (UnknownHostException e) {
            Log.e("E", "ClientTask UnknownHostException");
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("E", "ClientTask socket IOException " +e.getLocalizedMessage());
        }


    }

    public void updating_my_recovered_data(Recover_My_Data_Object recover_my_data_object){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor;
        ArrayList<String> arrayList = recover_my_data_object.arrayList;
        Log.d("Logger", " Bought" + arrayList.size());
        for(int i = 0 ; i < arrayList.size() ; i++){
            //Log.d("Logger", " I am in updating_my_recovered_data without split " + arrayList.get(i));
            String[]array = arrayList.get(i).split("\\|");
            cursor = db.rawQuery("select version from messenger where key = '" + array[0] + "'", null);

            if(cursor.moveToFirst()) {
                if (Integer.parseInt(array[2]) > cursor.getInt(0)) {
                    Log.d("Logger", " I updated in updating_my_recovered_data without split " + arrayList.get(i));
                    db.execSQL("update messenger set value = '" + array[1] + "',version = " + Integer.parseInt(array[2]) + " where key ='" + array[0] + "';");
                }
            }else{
                Log.d("Logger", " I am inserting in recovery " + arrayList.get(i));
                db.execSQL("INSERT INTO messenger(key,value,version,origin) " +
                        "VALUES('" + array[0] + "','" + array[1] + "'," + 1 + ",'" + array[3] + "' );");
            }


        }
    }

    public void recover_replication_data(Dummy_Recover_Replication_Data dummy_recover_replication_data){
        Log.d("Logger","I am in recover_replication_data in helper");
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor;
        cursor = db.rawQuery("SELECT key,value,version,origin FROM messenger where origin = '"+SimpleDynamoProvider.myport+"'", null);
        ArrayList<String> arrayList = new ArrayList<String>();
        String data = "";
        Log.d("Logger","I am in recover_replication_data and cursor count is " + cursor.getCount());

        if(cursor.moveToFirst()){
            do{
                Log.d("Logger",cursor.getString(0) + "|"  + cursor.getString(1) + "|" + cursor.getString(2) + "|" + cursor.getString(3));
                data = cursor.getString(0) + "|"  + cursor.getString(1) + "|" + cursor.getString(2) + "|" + cursor.getString(3);
                arrayList.add(data);
            }while(cursor.moveToNext());
        }
        Recover_Replication_Data_Object recover_replication_data_object = new Recover_Replication_Data_Object(arrayList);

        try {
            Log.d("Logger","Flushing Recover_Replication_Data_Object to  " + dummy_recover_replication_data.origin);
            Socket socket = new Socket(InetAddress.getByAddress(new byte[]{10, 0, 2, 2}), Integer.parseInt(dummy_recover_replication_data.origin));
            ObjectOutputStream outputstream = new ObjectOutputStream(socket.getOutputStream());
            outputstream.writeObject(recover_replication_data_object);
            outputstream.flush();
            socket.close();
        } catch (UnknownHostException e) {
            Log.e("E", "ClientTask UnknownHostException");
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("E", "ClientTask socket IOException " +e.getLocalizedMessage());
        }

    }


    public void update_recover_replication_data(Recover_Replication_Data_Object recover_replication_data_object){
        Log.d("Logger","I am in update_recover_replication_data");
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor;
        ArrayList<String> arrayList = recover_replication_data_object.arrayList;
        Log.d("Logger", " Bought" + arrayList.size());

        for(int i = 0 ; i < arrayList.size() ; i++){
            //Log.d("Logger", " I am in updating_my_recovered_data without split " + arrayList.get(i));
            String[]array = arrayList.get(i).split("\\|");
            cursor = db.rawQuery("select version from messenger where key = '" + array[0] + "'", null);

            if(cursor.moveToFirst()) {
                if (Integer.parseInt(array[2]) > cursor.getInt(0)) {
                    Log.d("Logger", " I updated in update_recover_replication_data without split " + arrayList.get(i));
                    db.execSQL("update messenger set value = '" + array[1] + "',version = " + Integer.parseInt(array[2]) + " where key ='" + array[0] + "';");
                }
            }else{
                Log.d("Logger", " I am inserting in update_recover_replication_data " + arrayList.get(i));
                db.execSQL("INSERT INTO messenger(key,value,version,origin) " +
                        "VALUES('" + array[0] + "','" + array[1] + "'," + 1 + ",'" + array[3] + "' );");
            }


        }
    }

}
