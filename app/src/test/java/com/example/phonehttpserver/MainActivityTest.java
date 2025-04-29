package com.example.phonehttpserver;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class MainActivityTest {

    @Test
    public void testResetSTTResultClearsData() {
        // Use setter methods instead of direct field access
        MainActivity.saveSTTResult("Old result");

        // Reset STT result
        MainActivity.resetSTTResult();

        // Validate that result is cleared
        assertEquals("", MainActivity.getSTTResultText());
        assertFalse(MainActivity.isSTTResultReady());
    }

    @Test
    public void testGetSTTResultReturnsCorrectJSON() throws JSONException {
        MainActivity.saveSTTResult("Recognized speech");

        JSONObject result = MainActivity.getSTTResult();

        assertEquals("ready", result.getString("status"));
        assertEquals("Recognized speech", result.getString("text"));
    }
}
