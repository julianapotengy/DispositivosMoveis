package com.example.dispositivosmoveis;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Console;

public class ActivityRanking extends AppCompatActivity {

    private ImageButton btnMenu;
    private TextView[] txtRanking = new TextView[5];

    private FirebaseDatabase database;
    private DatabaseReference reference;
    private ValueEventListener valueEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);

        btnMenu = findViewById(R.id.menuButton_Rank);

        database = FirebaseDatabase.getInstance();
        reference = database.getReference("Rank");

        for(int i = 0; i < 5; i++)
        {
            String rankID = "ranking_global_" + i;
            int resID = getResources().getIdentifier(rankID, "id", getPackageName());
            txtRanking[i] = findViewById(resID);
        }
        getRank();

        TextView txtLastScore = findViewById(R.id.lastPointsTextRanking);
        SharedPreferences prefs = getSharedPreferences("AppConfig", Context.MODE_PRIVATE);
        String lastScore = "Pontos: " + prefs.getInt("LastScore", 0);
        txtLastScore.setText(lastScore);
    }

    private void getRank()
    {
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(int i = 0; i < 5; i++)
                {
                    String searchFor = String.valueOf(i);
                    String name = snapshot.child(searchFor).child("nickname").getValue(String.class);
                    String score = snapshot.child(searchFor).child("score").getValue(String.class);

                    String rankingText = (i + 1) + ". " + name + ": " + score;
                    txtRanking[i].setText(rankingText);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("Foi cancelado.");
            }
        });
    }

    public void changeActivity(View view)
    {
        if(view == btnMenu)
        {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        this.finish();
    }
}