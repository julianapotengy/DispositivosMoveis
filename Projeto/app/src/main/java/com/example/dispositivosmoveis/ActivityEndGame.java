package com.example.dispositivosmoveis;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ActivityEndGame extends AppCompatActivity {

    ImageButton btnMenu;
    ImageButton btnRestart;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_game);

        btnMenu = findViewById(R.id.menuButton_End);
        btnRestart = findViewById(R.id.restartButton_End);
    }

    public void changeActivity(View view)
    {
        if(view == btnMenu)
        {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        if(view == btnRestart)
        {
            Intent intent = new Intent(this, ActivityGame.class);
            startActivity(intent);
        }
        this.finish();
    }
}
