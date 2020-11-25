package com.example.dispositivosmoveis;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {

    ImageButton btnPlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnPlay = findViewById(R.id.playButton);
    }

    public void startGame(View view)
    {
        if(view == btnPlay)
        {
            Intent intent = new Intent(this, ActivityGame.class);
            startActivity(intent);
        }
    }
}