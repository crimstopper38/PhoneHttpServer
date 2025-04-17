package com.example.phonehttpserver;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;

import java.util.ArrayList;

public class SpeechRecognizerHelper {
    private final SpeechRecognizer recognizer;
    private final Intent recognizerIntent;
    private final Object lock = new Object();

    public SpeechRecognizerHelper(Context context) {
        recognizer = SpeechRecognizer.createSpeechRecognizer(context);

        recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);

        recognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onResults(Bundle results) {
                ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                String result = (matches != null && !matches.isEmpty()) ? matches.get(0) : "No speech recognized";
                Log.d("STT", "Result: " + result);
                MainActivity.saveSTTResult(result);
                synchronized (lock) {
                    lock.notify();
                }
            }

            @Override
            public void onError(int error) {
                Log.e("STT", "Error: " + error);
                MainActivity.saveSTTResult("STT Error Code: " + error);
                synchronized (lock) {
                    lock.notify();
                }
            }

            // Required stubs
            public void onReadyForSpeech(Bundle params) {
            }

            public void onBeginningOfSpeech() {
            }

            public void onRmsChanged(float rmsdB) {
            }

            public void onBufferReceived(byte[] buffer) {
            }

            public void onEndOfSpeech() {
            }

            public void onPartialResults(Bundle partialResults) {
            }

            public void onEvent(int eventType, Bundle params) {
            }
        });
    }

    public void startListening() {
        recognizer.startListening(recognizerIntent);
    }

    public void destroy() {
        recognizer.destroy();
    }
}

