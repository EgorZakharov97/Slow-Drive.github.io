package com.UnitTests;


import com.Logic.Utils.PasswordUtils;

import org.junit.Test;


import java.util.ArrayList;
import java.util.LinkedHashSet;




import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertNotEquals;


public class TestPasswordUtils {

    // NOTE: Mockito is not suitable for static methods

    @Test
    public void testGetSalt(){

        //Testing Randomness
        // generate 100 different salt values
        // store in arraylist, overriding duplicates
        // compare size

        final int COUNT = 100;
        ArrayList<String> salts = new ArrayList<String>();

        for(int i = 0; i < COUNT; i++){
            salts.add(PasswordUtils.getSalt());
        }

        //Removes duplicates
        LinkedHashSet<String> noDupSalts = new LinkedHashSet<>(salts);

        assertEquals(noDupSalts.size(), salts.size());

    }

    @Test
    public void testGenSecurePass(){
        testSameSaltValue();
        testDifferentSaltValue();
    }

    @Test
    public void testSameSaltValue(){

        String salt = PasswordUtils.getSalt();

        String secure1 = PasswordUtils.generateSecurePassword("password", salt);
        String secure2 = PasswordUtils.generateSecurePassword("password", salt);

        assertNotNull(secure1);
        assertNotNull(secure2);
        assertEquals(secure1, secure2);
    }

    @Test
    public void testDifferentSaltValue(){

        String salt1 = PasswordUtils.getSalt();
        String salt2 = PasswordUtils.getSalt();

        String secure1 = PasswordUtils.generateSecurePassword("Password1!", salt1);
        String secure2 = PasswordUtils.generateSecurePassword("Password1!", salt2);

        System.out.println(secure1 + " " + secure2);

        assertNotNull(secure1);
        assertNotNull(secure2);
        assertNotEquals(secure1, secure2);

    }


    @Test
    public void TestGeneratorUniqueness(){



        final int COUNT = 100;
        final String TEST_PASSWORD = "password";
        ArrayList<String> securePasswords = new ArrayList<String>();

        for(int i = 0; i < COUNT; i++){

            String salt = PasswordUtils.getSalt();
            String securePassword = PasswordUtils.generateSecurePassword(TEST_PASSWORD, salt);

            securePasswords.add(securePassword);


        }

        //Removes duplicates
        LinkedHashSet<String> noDup = new LinkedHashSet<>(securePasswords);

        assertEquals(noDup.size(), securePasswords.size());

    }





}
