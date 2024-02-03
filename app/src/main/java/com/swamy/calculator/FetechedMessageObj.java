package com.swamy.calculator;

public class FetechedMessageObj {
    public String getMessage() {
        return message;
    }

    public String getTime() {
        return time;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setTime(String time) {
        this.time = time;
    }

    String message;
    String time;
    public FetechedMessageObj(){}

    public FetechedMessageObj(String message, String time) {
        this.message = message;
        this.time = time;
    }
}
