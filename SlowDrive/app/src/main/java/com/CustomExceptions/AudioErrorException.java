package com.CustomExceptions;

public class AudioErrorException extends Exception {

    public AudioErrorException (String errorMsg)
    {
        super (errorMsg);
    }

    public AudioErrorException ()
    {
        super ();
    }

    public AudioErrorException (String errorMsg, Throwable err)
    {
        super (errorMsg, err);
    }
}
