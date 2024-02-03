package com.swamy.calculator.view;

import com.swamy.calculator.Database.Message;

import java.util.List;

public interface MessageLoadCallBack {
    void onLoaded(List<Message> fetchedMessages);
    void onLoadFailed(String ErrorMessage);
}
