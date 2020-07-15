package com.UnitTests;

import com.Logic.Static.HTTPSHandler;

import static junit.framework.TestCase.*;

import org.junit.Test;

import java.net.URL;

public class TestHTTPHandler {

    @Test
    public void testRequest() throws Exception {
        URL url = new URL("http://www.google.com");
        String response = HTTPSHandler.sendGet(url);
        assertNotSame(response, "");
    }

    @Test
    public void testResponse() throws Exception {
        String expected = "{  \"userId\": 1,  \"id\": 1,  \"title\": \"delectus aut autem\",  \"completed\": false}";
        URL url = new URL("https://jsonplaceholder.typicode.com/todos/1");
        String response = HTTPSHandler.sendGet(url);
        assertEquals(expected, response);
    }
}
