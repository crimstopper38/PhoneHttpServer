package com.example.phonehttpserver;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.Manifest;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import org.json.JSONObject;

public class MainActivity extends Activity {
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private static String sttResultText = "";
    private static boolean sttResultReady = false;

    private SttHttpServer httpServer;
    private SpeechRecognizerHelper sttHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sttHelper = new SpeechRecognizerHelper(this);
        httpServer = new SttHttpServer(8080, this);  // This auto-starts the server in the constructor
        requestMicrophonePermission();
    }

    private void requestMicrophonePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.RECORD_AUDIO},
                    REQUEST_RECORD_AUDIO_PERMISSION);
        }
    }

    public void startListening() {
        sttHelper.startListening();
    }

    public static synchronized void saveSTTResult(String result) {
        sttResultText = result;
        sttResultReady = true;
    }

    public static synchronized JSONObject getSTTResult() {
        JSONObject json = new JSONObject();
        try {
            if (sttResultReady) {
                json.put("status", "ready");
                json.put("text", sttResultText);
            } else {
                json.put("status", "waiting");
            }
        } catch (Exception ignored) {}
        return json;
    }

    public static synchronized void resetSTTResult() {
        sttResultText = "";
        sttResultReady = false;
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (httpServer != null) {
            httpServer.stop();  // Ensure the server stops gracefully
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (httpServer != null) {
            httpServer.stop();  // Ensure NanoHTTPD releases the port
        }
    }
}

