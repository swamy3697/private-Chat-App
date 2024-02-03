package com.swamy.calculator.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Entity;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Message.class},version = 1,exportSchema = false)
public abstract class AppDataBase extends RoomDatabase {
    public abstract MessageDeo messageDeo();
   static AppDataBase appDataBase;

    public static AppDataBase getInstance(Context context){

        if(appDataBase==null){

            synchronized (AppDataBase.class){
                if (appDataBase ==null){
                    appDataBase=Room.databaseBuilder(context,AppDataBase.class,"messagesDb").build();
                }
            }
        }

        return appDataBase;
    }



}
