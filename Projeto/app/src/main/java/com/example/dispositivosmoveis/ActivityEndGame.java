package com.example.dispositivosmoveis;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

public class ActivityEndGame extends AppCompatActivity {

    private ImageButton btnMenu;
    private ImageButton btnRestart;
    private ImageButton btnRank;
    private TextView[] txtRanking = new TextView[5];

    SharedPreferences prefs;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_game);

        btnMenu = findViewById(R.id.menuButton_End);
        btnRestart = findViewById(R.id.restartButton_End);
        btnRank = findViewById(R.id.rankButton_End);
        TextView txtLastScore = findViewById(R.id.lastPointsText);

        prefs = getSharedPreferences("AppConfig", Context.MODE_PRIVATE);
        String lastScore = "Pontos: " + prefs.getInt("LastScore", 0);
        txtLastScore.setText(lastScore);
        for(int i = 0; i < 5; i++)
        {
            String rankID = "ranking_local_" + i;
            int resID = getResources().getIdentifier(rankID, "id", getPackageName());
            txtRanking[i] = findViewById(resID);
            String rank = (i + 1) + ". " + prefs.getInt("Rank" + i, 0);
            txtRanking[i].setText(rank);
        }
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
        if(view == btnRank)
        {
            Intent intent = new Intent(this, ActivityRanking.class);
            startActivity(intent);
        }
        this.finish();
    }
}
