package com.IntegrationTest;

import com.Logic.Manager.LoginManager;
import com.Logic.Object.Driver;
import com.application.Main;
import com.utils.TestUtils;
import com.Database.DriverDB;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

public class TestLoginMNG {
    private DriverDB db;
    private File dbFile;
    private LoginManager mng;

    @Before
    public void setup() throws IOException {
        dbFile= TestUtils.copyDB();
        //db= new DriverDB(dbFile.getAbsolutePath().replace(".script",""));
        db=new DriverDB(Main.getDBPathName());
        mng=new LoginManager(db);



    }
    @Test
    public void testStartLogin(){
        //null test
        assertFalse(mng.startLogin("test","123"));

        createFake();
        //invalid test
        //invalid userName
        assertFalse(mng.startLogin("SomeRandomPeople","i dont really care"));
        //invalid password
        assertFalse(mng.startLogin("tester1","2"));

        //valid test
        for(int i=0;i<10;i++){
            assertTrue(mng.startLogin("tester"+i,""+i));
        }
    }
    @After
    public void clean() {
        dbFile.delete();

    }

    private void createFake(){
        for(int i=0;i<10;i++){
            String userName="tester";
            String firstName= userName;
            userName+=""+i;
            String password=""+i;
            String lastName=""+i;
            String email= userName+"@email.com";
            Driver d= new Driver(firstName,lastName,userName,password,email);
            db.insert(d);
        }
    }
}
