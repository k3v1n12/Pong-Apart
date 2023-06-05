package com.example.pong_apart;

import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.Objects;

import ac.robinson.bettertogether.api.BasePluginActivity;
import ac.robinson.bettertogether.api.messaging.BroadcastMessage;

public class Player2Activity extends BasePluginActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("Player2Activity.java", "onCreate()");
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        //Set Content View to game, so that object in the game class can be rendered
        try {
            control = new Control(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
        setContentView(control);
    }

    @Override
    protected void onMessageReceived(@NonNull BroadcastMessage message) {

    }
}
