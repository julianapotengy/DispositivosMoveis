package com.example.dispositivosmoveis;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {

    private ImageButton btnPlay;
    private EditText nickname;
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnPlay = findViewById(R.id.playButton);
        nickname = findViewById(R.id.nicknameID);

        prefs = getSharedPreferences("AppConfig", Context.MODE_PRIVATE);
        setNicknameText();
    }

    public void startGame(View view)
    {
        if(view == btnPlay)
        {
            Intent intent = new Intent(this, ActivityGame.class);
            startActivity(intent);
        }
    }

    public void goToGlobalRank(View view)
    {
        Intent intent = new Intent(this, ActivityRanking.class);
        startActivity(intent);
    }

    public void changeNickname(View view)
    {
        SharedPreferences.Editor editor = prefs.edit();
        String newNickname = String.valueOf(nickname.getText());
        System.out.println(newNickname);
        editor.putString("Nickname", newNickname);
        editor.apply();
        setNicknameText();
    }

    private void setNicknameText()
    {
        String nicknameText = prefs.getString("Nickname", "?");
        nickname.setText(nicknameText);
    }
}