package com.example.phonehttpserver;

import android.content.Context;
import fi.iki.elonen.NanoHTTPD;
import org.json.JSONObject;

import java.io.IOException;

public class SttHttpServer extends NanoHTTPD {
    private final Context context;


    public SttHttpServer(int port, Context ctx){
        super("0.0.0.0", port);  // Remove the port argument
        this.context = ctx;
        try {
            start(SOCKET_READ_TIMEOUT, false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Response serve(IHTTPSession session) {
        if ("/startSTT".equals(session.getUri())) {
            ((MainActivity) context).runOnUiThread(() -> {
                ((MainActivity) context).startListening();
            });
            return newFixedLengthResponse(Response.Status.OK, "text/plain", "STT started");
        } else if ("/sttResult".equals(session.getUri())) {
            JSONObject result = MainActivity.getSTTResult();
            return newFixedLengthResponse(Response.Status.OK, "application/json", result.toString());
        } else {
            return newFixedLengthResponse(Response.Status.NOT_FOUND, "text/plain", "Not Found");
        }
    }

    @Override
    public void stop() {
        super.stop(); // Calls NanoHTTPD's stop method
    }

}

