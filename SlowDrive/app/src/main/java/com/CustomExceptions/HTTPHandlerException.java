package com.CustomExceptions;

import android.util.Log;

import androidx.annotation.Nullable;

public class HTTPHandlerException extends Exception{

    private static final String TAG = "HTTPHandler";
    private static final String MESSAGE = "refused to make request";

    public HTTPHandlerException(Exception e){
        super(e);
    }

    @Override
    public void printStackTrace() {
        Log.e(TAG, MESSAGE);
        super.printStackTrace();
    }

    @Nullable
    @Override
    public String getMessage() {
        String message = TAG + " " + MESSAGE + "\n" + super.getMessage();
        return message;
    }
}
