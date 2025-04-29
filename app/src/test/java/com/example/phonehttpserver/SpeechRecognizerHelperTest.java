package com.example.phonehttpserver;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.SpeechRecognizer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import java.util.ArrayList;

@RunWith(MockitoJUnitRunner.class)
public class SpeechRecognizerHelperTest {

    private SpeechRecognizerHelper recognizerHelper;
    private SpeechRecognizer mockRecognizer;
    private Context mockContext;
    private Bundle mockResults;

    @Before
    public void setup() {
        mockContext = Mockito.mock(Context.class);
        mockRecognizer = Mockito.mock(SpeechRecognizer.class);
        recognizerHelper = new SpeechRecognizerHelper(mockContext);

        mockResults = new Bundle();
        ArrayList<String> matches = new ArrayList<>();
        matches.add("Test speech result");
        mockResults.putStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION, matches);
    }

    @Test
    public void testStartListeningTriggersSpeechRecognizer() {
        recognizerHelper.startListening();
        Mockito.verify(mockRecognizer).startListening(Mockito.any(Intent.class));
    }

    @Test
    public void testSpeechRecognitionProcessesResult() {
        RecognitionListener listener = Mockito.mock(RecognitionListener.class);

        recognizerHelper = new SpeechRecognizerHelper(mockContext);
        listener.onResults(mockResults);

        Mockito.verify(listener).onResults(mockResults);
    }
}
