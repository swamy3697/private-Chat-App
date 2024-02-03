package com.swamy.calculator.Database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface MessageDeo {

    @Insert
    void insert(Message message);

    @Delete
    void delete(Message message);

    @Query("Select * from MessagesTable")
    List<Message> getAllMessages();




}
