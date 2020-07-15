package com.UnitTests;

import org.junit.Test;

import com.Logic.Object.Driver;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

// Following TDD
public class TestDriver {
    // Test driver settings

    // Run all tests
    // Optional
    public void run ()
    {
        testToString();
    }

    @Test
    // Test that the driver class is functional
    public void testToString ()
    {
        final int TESTS = 5;
        final int STARTING_CHAR = 30;

        for (int i = STARTING_CHAR; i < TESTS * TESTS; i += TESTS) {
            // UID, FN, LN are given by toString
            String values [] = {"" + (char)i, (char)i + "" + (char) i + 1, "" + i};

            Driver temp = new Driver(values [0], values [1], values [2], "PASS", "EMAIL");

            String [] results = temp.toString().split("\n");
            for (int k = 0; k < values.length; k++)
            {
                assertEquals (results [k].split (" ")[1].trim(), values [k]);
            }
        }
    }

    @Test
    public void testField(){

        String firstname = "first";
        String lastname = "last";
        String username = "username";
        String password = "Password1!";
        String email = "email@emai.ca";

        Driver d = new Driver(firstname, lastname, username, password, email);

        assertEquals(firstname, d.getFirstName());
        assertEquals(lastname, d.getLastName());
        assertEquals(username, d.getUserName());
        assertEquals(password, d.getPassword());
        assertEquals(email, d.getEmail());

    }


}
