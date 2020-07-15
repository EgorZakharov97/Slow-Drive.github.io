package com.CustomExceptions;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class InterfaceMissingException extends Exception {
    private String message = "";

    public InterfaceMissingException(AppCompatActivity context, String Interface) {
        super();
        message = context.getClass().getSimpleName() + " class has to implement " + Interface;
    }

    @Nullable
    @Override
    public String getMessage() {
        return message;
    }

    @NonNull
    @Override
    public String toString() {
        return message;
    }
}
