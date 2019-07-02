package com.example.shiva.alarmtut;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.content.Intent;

import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;

import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class MyAlarmActivity extends Activity implements TextToSpeech.OnInitListener {
    MediaPlayer ring;
    TextToSpeech textToSpeech;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Button btnStopAlarm;
        String language =  "it_IT";

        textToSpeech = new TextToSpeech(this, this);
        textToSpeech.setLanguage(Locale.ITALY);

        ring= MediaPlayer.create(this,R.raw.audio);
        ring.start();

        setContentView(R.layout.alarm);


        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,language);
        startActivityForResult(intent, 30);

        btnStopAlarm = (Button)findViewById(R.id.btnStopAlarm);

        btnStopAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               stopPlaying();
            }
        });

    }

    @Override
    public void onInit(int i) {

        textToSpeech.setLanguage(Locale.ITALY);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {
            char operatorFound = getOperatorFromResult(data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS));
            if (operatorFound != '0') {
                stopPlaying();
            } else {
               // Toast.makeText(getApplicationContext(), "Sorry, I didn't catch that! Please try again", Toast.LENGTH_LONG).show();
                textToSpeech.speak("che hai detto? Ma sei sveglio? Daiiiii svegliati", TextToSpeech.QUEUE_ADD, null);
            }
        } else {
            //Toast.makeText(getApplicationContext(), "Failed to recognize speech!", Toast.LENGTH_LONG).show();
            textToSpeech.speak("che cosa? Mica ti capisco !! Ripeti!!!", TextToSpeech.QUEUE_ADD, null);
        }
    }

    // method to loop through results trying to find an operator
    private char getOperatorFromResult(ArrayList<String> results) {
        for (String str : results) {
            if (getCharOperatorFromText(str) != '0') {
                return getCharOperatorFromText(str);
            }
        }
        return '0';
    }
    // method to convert string operator to char
    private char getCharOperatorFromText(String strOper) {
        switch (strOper) {
            case "stop":
            case "basta":
                return '1';
        }
        return '0';
    }

    private void stopPlaying(){
        ring.stop();
        textToSpeech.speak("OK, It was time Alessandra", TextToSpeech.QUEUE_ADD, null);
    }


}


