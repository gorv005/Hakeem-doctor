package com.app.hakeem.util;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.app.hakeem.db.DatabaseManager;
import com.app.hakeem.pojo.ChatMessage;

import java.util.ArrayList;

/**
 * Created by aditya.singh on 9/13/2016.
 */
public class ChatRepo {

    private ChatMessage register;

    public ChatRepo() {

        register = new ChatMessage();

    }


    public static String createTable() {
        return "CREATE TABLE " + ChatMessage.TABLE + "("
                + ChatMessage.KEY_Id + " TEXT  PRIMARY KEY, "
                + ChatMessage.KEY_SENDER + " TEXT,"
                + ChatMessage.KEY_RECIEVER + " TEXT,"
                + ChatMessage.KEY_MESSAGE + " TEXT,"
                + ChatMessage.KEY_MESSAGE_STATUS + " TEXT,"
                + ChatMessage.KEY_IS_DELIVER + " TEXT )";
    }


    public int insert(ChatMessage chepter) {
        int majorId;
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        ContentValues values = new ContentValues();
        values.put(ChatMessage.KEY_Id, chepter.getId());
        values.put(ChatMessage.KEY_SENDER, chepter.getSender());
        values.put(ChatMessage.KEY_RECIEVER, chepter.getReceiver());
        values.put(ChatMessage.KEY_MESSAGE, chepter.getMessage());
        values.put(ChatMessage.KEY_MESSAGE_STATUS, chepter.getMessageStatus());
        values.put(ChatMessage.KEY_IS_DELIVER, chepter.isDeliver());
        // Inserting Row
        majorId = (int) db.insert(ChatMessage.TABLE, null, values);
        DatabaseManager.getInstance().closeDatabase();
        return majorId;

    }


    public void delete() {
        try {
            SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
            db.delete(ChatMessage.TABLE, null, null);
            DatabaseManager.getInstance().closeDatabase();
        } catch (Exception e) {

        }
    }

    public void deleteChatMessage(String id) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        db.delete(ChatMessage.TABLE, ChatMessage.KEY_Id + " = " + id, null);
        DatabaseManager.getInstance().closeDatabase();
    }


    public ArrayList<ChatMessage> getChatMessages(String sender,String reciever) {
        ArrayList<ChatMessage> subjects = new ArrayList<>();

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        String selectQuery = " SELECT *"
                + " FROM " + ChatMessage.TABLE + " WHERE "+ ChatMessage.KEY_SENDER+"=\""+sender+"\" AND "+ ChatMessage.KEY_RECIEVER+"=\""+reciever+"\";";

        Log.d("Query ", selectQuery);
        try {
            Cursor cursor = db.rawQuery(selectQuery, null);
            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {

                do {
                    ChatMessage subject = new ChatMessage();
                    subject.setId(cursor.getLong(cursor.getColumnIndex(ChatMessage.KEY_Id)));
                    subject.setMessage(cursor.getString(cursor.getColumnIndex(ChatMessage.KEY_MESSAGE)));
                    subject.setMessageStatus(cursor.getInt(cursor.getColumnIndex(ChatMessage.KEY_MESSAGE_STATUS)));
                    subject.setReceiver(cursor.getString(cursor.getColumnIndex(ChatMessage.KEY_RECIEVER)));
                    subject.setSender(cursor.getString(cursor.getColumnIndex(ChatMessage.KEY_SENDER)));
                    subject.setDelivered(cursor.getString(cursor.getColumnIndex(ChatMessage.KEY_IS_DELIVER)));
                    subjects.add(subject);
                } while (cursor.moveToNext());
            }
            cursor.close();
            DatabaseManager.getInstance().closeDatabase();
        } catch (Exception e) {
            return null;
        }
        return subjects;
    }

    public boolean updateMsgStatus(ChatMessage usedPermissionInfo) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        ContentValues args = new ContentValues();
        args.put(ChatMessage.KEY_MESSAGE_STATUS, usedPermissionInfo.getMessageStatus());
//
        int result = db.update(ChatMessage.TABLE, args, ChatMessage.KEY_Id + "=" + usedPermissionInfo.getId(), null);
        DatabaseManager.getInstance().closeDatabase();
        return result > 0;
    }

}
