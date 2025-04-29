package com.example.phonehttpserver;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import android.content.Context;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockedConstruction;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;

import fi.iki.elonen.NanoHTTPD;

@RunWith(MockitoJUnitRunner.class)
public class SttHttpServerTest {

    private SttHttpServer server;
    private MockedConstruction.Context mockContext;

    @Before
    public void setup() throws IOException {
        mockContext = Mockito.mock(MockedConstruction.Context.class);
        server = new SttHttpServer(5001, (Context) mockContext); // Use test port
        server.start();
    }

    @Test
    public void testServerStartsSuccessfully() {
        assertTrue(server.isAlive());
    }

    @Test
    public void testHandleStartSTTRequest() {
        NanoHTTPD.IHTTPSession mockSession = Mockito.mock(NanoHTTPD.IHTTPSession.class);
        Mockito.when(mockSession.getUri()).thenReturn("/startSTT");

        NanoHTTPD.Response response = server.serve(mockSession);
        assertEquals(NanoHTTPD.Response.Status.OK, response.getStatus());
        assertEquals("STT started", response.getMimeType());
    }

    @After
    public void tearDown() {
        server.stop();
    }
}
