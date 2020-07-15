package com.Logic.Static;

import com.Logic.Object.Driver;

public class Profile {
    private static Driver current;
    private Profile(){

    }
    public static void updateDriver(Driver d){
        current = d;
    }
    public static Driver getCurrentDriver(){
        return current;
    }
}
