package com.IntegrationTest;

import com.Database.DriverDB;
import com.Logic.Manager.SignupManager;
import com.application.Main;
import com.utils.TestUtils;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TestSignupMNG {

    private DriverDB db;
    private File dbFile;
    private SignupManager mng;

    @Before
    public void setup() throws IOException {

        //connect to database

        // hard copies current database file
        dbFile= TestUtils.copyDB();

        // stores the copied db to db variable
        db=new DriverDB(Main.getDBPathName());

        mng=new SignupManager(db);


    }


    @Test
    public void testValid(){
        // SignupManager mng = new SignupManager();

        String firstname = "first";
        String lastname = "last";
        String username = "username";
        String password = "Password1!";
        String repassword = "Password1!";
        String email = "email@emai.ca";


        assertTrue("Passed: All Valid. " , mng.startSignup(username, password, firstname, lastname, email, repassword));


    }

    @Test
    public void testInvalid(){
        testInvalidUsername();
        testInvalidEmail();
        testPasswordNotMatch();
        testEmpty();
    }



    @Test
    public void testInvalidUsername(){

        String firstname = "first";
        String lastname = "last";
        String username = "user name";
        String password = "password";
        String repassword = "password";
        String email = "email@emai.ca";


        assertFalse("Passed: Invalid username. " + "\""+ username +"\"", mng.startSignup(username, password, firstname, lastname, email, repassword));


    }

    @Test
    public void testInvalidEmail(){

        String firstname = "first";
        String lastname = "last";
        String username = "username";
        String password = "password";
        String repassword = "password";
        String email = "wrong email";


        assertFalse("Passed: Invalid email. " + "\""+ email +"\"", mng.startSignup(username, password, firstname, lastname, email, repassword));

    }

    @Test
    public void testPasswordNotMatch(){

        String firstname = "first";
        String lastname = "last";
        String username = "username";
        String password = "password";
        String repassword = "notmatch";
        String email = "email@emai.ca";


        assertFalse("Passed: Password did not match. " + "\""+ password +"\"" + " and " + "\""+ repassword +"\"", mng.startSignup(username, password, firstname, lastname, email, repassword));


    }

    @Test
    public void testEmpty(){

        String firstname = "";
        String lastname = "";
        String username = "";
        String password = "";
        String repassword = "";
        String email = "";

        assertFalse("Passed: No input taken.", mng.startSignup(username, password, firstname, lastname, email, repassword));
    }

    @Test
    public void testInvalidPassword(){
        String password1 = "Pass1!"; // less than 8
        String password2 = "password1!"; // no uppercase
        String password3 = "Password1"; // no special character
        String password4 = "Password!"; // no number
        String password5 = "PASSWORD1!"; // no lowercase

        assertFalse("Passed: Invalid Password. Less than 8" , mng.startSignup("username1", password1, "firstname1", "lastname1", "email2@email.ca", password1));
        assertFalse("Passed: Invalid Password. No uppercase" , mng.startSignup("username2", password2, "firstname2", "lastname2", "email2@email.ca", password2));
        assertFalse("Passed: Invalid Password. No special character" , mng.startSignup("username3", password3, "firstname3", "lastname3", "email3@email.ca", password3));
        assertFalse("Passed: Invalid Password. No number" , mng.startSignup("username4", password4, "firstname4", "lastname4", "email4@email.ca", password4));
        assertFalse("Passed: Invalid Password. No number" , mng.startSignup("username5", password5, "firstname5", "lastname5", "email5@email.ca", password5));


    }


    @After
    public void clean() {
        dbFile.delete();

    }


}

