package com.swamy.calculator.Database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "MessagesTable")
public class Message {

    @NonNull
    @PrimaryKey(autoGenerate = true)
    int messageId;
    String message;
    String time;
    boolean isMe;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public boolean isMe() {
        return isMe;
    }

    public void setMe(boolean me) {
        isMe = me;
    }

    public Message(String message, String time, boolean isMe) {
        this.message = message;
        this.time = time;
        this.isMe = isMe;
    }
    public Message(){

    }



}
