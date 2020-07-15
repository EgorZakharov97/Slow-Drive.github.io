package com.Logic.Manager;

import com.Database.Interface.IDriverDB;
import com.Logic.Interface.ISignupInterface;
import com.Logic.Object.Driver;
import com.Logic.Static.Profile;
import com.UI.SignupActivity;
import com.application.Services;

import java.util.regex.Pattern;

public class SignupManager implements ISignupInterface {
    
    private IDriverDB db;
    private int PASSWORD_MIN_LENGTH = 8;

    public SignupManager(){
        db = Services.getDriverDB();

    }

    public SignupManager(IDriverDB db){
        this.db =db;


    }


    // true = sign-up successful
    // false = sign-up unsuccessful
    @Override
    public boolean startSignup(String username, String password, String firstName, String lastName, String email, String repassword){


        boolean result = false;

        if(!isFieldEmpty(firstName,email,username,password)){

            if(validateAllInput(username, password, firstName, lastName, email, repassword)){

                if(isUnique(username)){

                    Driver newD = new Driver(firstName, lastName,username, password, email); //unencrypted password
                    db.insert(newD);
                    Profile.updateDriver(newD);
                    result = true;
                } // We have a duplicate

            } // One of the inputs are in the wring format


        } // One of the inputs are empty
        return result;

    }




    private boolean isFieldEmpty(String f, String e, String u, String p){

        int emptyCounter = 0;

        if(f.equals("")){
            SignupActivity.requirements += "\n\t\tFirst name is required.";
        }

        if(e.equals("")){
            SignupActivity.requirements += "\n\t\tEmail address is required.";
        }

        if(u.equals("")){
            SignupActivity.requirements += "\n\t\tUsername is required.";
        }

        if(p.equals("")){
            SignupActivity.requirements += "\n\t\tPassword is required.";
        }

        return emptyCounter > 0;

    }


    private boolean validateAllInput(String u, String p, String f, String l, String e, String rp){
        int invalidCounter = 0;
        if(!validateUsername(u)){
            invalidCounter++;
        }
        if(!validatePassword(p, rp)){
            invalidCounter++;
        }
        if(!validateEmailAddress(e)){
            invalidCounter++;
        }

        return invalidCounter == 0;
    }

    private boolean validateUsername(String u){
        if(!u.contains(" ")){
            return true;
        } else {
            SignupActivity.requirements += "\n\t\t- Username must not contain black spaces.";
            return false;
        }
    }

    private boolean validatePassword(String p, String rp){

        boolean result = true;

        if( p != "") {

            if (p.length() < PASSWORD_MIN_LENGTH) { // less than min length
                SignupActivity.requirements += "\n\t\t- Password length must be greater than 8.";
                result = false;
            }

            if (!p.matches(".*\\d.*")) { // no number
                SignupActivity.requirements += "\n\t\t- Password must have at least one number.";
                result = false;
            }

            if(!p.matches(".*\\W.*")){ // no special char
                SignupActivity.requirements += "\n\t\t- Password must contain at least one special character.";
                result = false;
            }

            if (p.equals(p.toLowerCase())) { //has no UpperCase
                SignupActivity.requirements += "\n\t\t- Password must contain at least one upper case letter.";
                result = false;
            }

            if (p.equals(p.toUpperCase())) { //has no LowerCase
                SignupActivity.requirements += "\n\t\t- Password must contain at least one lower case letter.";
                result = false;
            }

            if (!p.equals(rp)) { // does not match
                SignupActivity.requirements += "\n\t\t- Password does not match.";
                result = false;
            }
        } else {
            result = false;
            SignupActivity.requirements += "\n\t\t- Enter a password.";
        }

        return result;
    }

    private boolean validateEmailAddress(String e){

        //reference: //from https://www.geeksforgeeks.org/check-email-address-valid-not-java/
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        if (e == null) {
            return false;
        }

        if(pat.matcher(e).matches()){
            return true;
        } else {
            SignupActivity.requirements += "\n\t\t- Email must be in w@x.yz format.";
            return false;
        }

    }

    private boolean isUnique(String username){
        if(db.getDriver(username) != null){
            SignupActivity.requirements += "\n\t\tUsername already exists.";
            return false;
        }
        return true;
    }







}
