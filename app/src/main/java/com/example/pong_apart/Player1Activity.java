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

public class Player1Activity extends BasePluginActivity {

    private Game game;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("Player1.java", "onCreate()");
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        //Set Content View to game, so that object in the game class can be rendered
        game = new Game(this);
        setContentView(game);
    }
    @Override
    protected void onStart() {
        Log.d("Player1.java", "onStart()");
        super.onStart();
    }

    @Override
    protected void onResume() {
        Log.d("Player1.java", "onResume()");
        super.onResume();
    }

    @Override
    protected void onPause() {
        Log.d("Player1.java", "onPause()");
        game.pause();
        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.d("Player1.java", "onStop()");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.d("Player1.java", "onDestroy()");
        super.onDestroy();
    }

    @Override
    protected void onMessageReceived(@NonNull BroadcastMessage message) {

    }
}
