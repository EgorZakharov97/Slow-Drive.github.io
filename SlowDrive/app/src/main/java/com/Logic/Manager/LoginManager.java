package com.Logic.Manager;

import com.Database.Interface.IDriverDB;
import com.Logic.Interface.ILoginMng;
import com.Logic.Object.Driver;
import com.Logic.Static.Profile;
import com.Logic.Utils.PasswordUtils;
import com.application.Services;

public class LoginManager implements ILoginMng {
    private IDriverDB db;

    public LoginManager(){
        this.db= Services.getDriverDB();
    }
    public LoginManager(IDriverDB db){
        this.db=db;
    }

    @Override
    public boolean startLogin(String username, String password) {

        boolean result = validateLogin(username,password);

        if(result)
            Profile.updateDriver(db.getDriver(username));
        return result;
    }

    private boolean validateLogin(String username,String password){

        Driver d = db.getDriver(username);

        String salt = db.getDriverSalt(username);

        boolean result = false;

        if(d!=null){ //match username
            if(PasswordUtils.verifyUserPassword(password, d.getPassword(), salt)){
                result=true; //match password
            }
        }
        return result;

    }
}
