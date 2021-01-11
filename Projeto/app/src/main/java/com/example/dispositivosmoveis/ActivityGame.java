package com.example.dispositivosmoveis;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Random;

public class ActivityGame extends AppCompatActivity implements View.OnClickListener {

    private int count;
    private CountDownTimer countDownTimer;

    private int points = 0;
    private TextView pointsText;
    private TextView timerText;

    private final ArrayList<ImageButton> moles = new ArrayList<>();
    private int maxSize;

    private FirebaseDatabase database;
    private DatabaseReference reference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        count = 21;
        maxSize = 6;

        for(int i = 0; i < maxSize; i++)
        {
            String moleID = "mole_" + i;
            int resID = getResources().getIdentifier(moleID, "id", getPackageName());
            moles.add(findViewById(resID));
        }

        pointsText = findViewById(R.id.pointsText);
        updatePoints(0);
        timerText = findViewById(R.id.timerText);
        updateTimer();

        changeMolesState();
    }

    private void startTimer()
    {
        countDownTimer = new CountDownTimer(count * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                count--;
                updateTimer();
                changeMolesState();
            }

            @Override
            public void onFinish()
            {
                database = FirebaseDatabase.getInstance();
                reference = database.getReference("Rank");

                SharedPreferences prefs = getSharedPreferences("AppConfig", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putInt("LastScore", points);

                for(int i = 0; i < 5; i++)
                {
                    int rankPoint = prefs.getInt("Rank" + i, 0);
                    if(points > rankPoint)
                    {
                        for(int j = i; j < 5; j++)
                        {
                            int lastRankPoint = prefs.getInt("Rank" + j, 0);
                            if(lastRankPoint >= prefs.getInt("Rank" + (j + 1), 0))
                            {
                                editor.putInt("Rank" + (j + 1), lastRankPoint);
                            }
                        }
                        editor.putInt("Rank" + i, points);
                        break;
                    }
                }
                editor.apply();
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(int i = 0; i < 5; i++)
                        {
                            String scoreSaved = snapshot.child(String.valueOf(i)).child("score").getValue(String.class);
                            if(points > Integer.parseInt(scoreSaved))
                            {
                                String nickname = prefs.getString("Nickname", "?");
                                String score = String.valueOf(points);
                                UserHelperClass helperClass;

                                for(int j = i; j < 5; j++)
                                {
                                    if(j + 1 < 5)
                                    {
                                        int lastRankPoint = Integer.parseInt(snapshot.child(String.valueOf(j)).child("score").getValue(String.class));
                                        int plusOne = Integer.parseInt(snapshot.child(String.valueOf(j + 1)).child("score").getValue(String.class));
                                        if(lastRankPoint >= plusOne)
                                        {
                                            String lastRankNickname =  snapshot.child(String.valueOf(j)).child("nickname").getValue(String.class);
                                            helperClass = new UserHelperClass(lastRankNickname, String.valueOf(lastRankPoint));
                                            reference.child(String.valueOf(j + 1)).setValue(helperClass);
                                        }
                                    }
                                }
                                helperClass = new UserHelperClass(nickname, score);
                                reference.child(String.valueOf(i)).setValue(helperClass);
                                break;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        System.out.println("Foi cancelado.");
                    }
                });

                changeActivity(ActivityEndGame.class);
            }
        }.start();
    }

    private void stopTimer()
    {
        countDownTimer.cancel();
    }

    @Override
    public void onClick(View v) {
        smashIt(v);
    }

    private void smashIt(View v)
    {
        updatePoints(1);
        for(int i = 0; i < maxSize; i++)
        {
            if(moles.get(i).getId() == v.getId())
            {
                notShow(i);
            }
        }
    }

    private void updatePoints(int newPoint)
    {
        points += newPoint;
        String text = points + " pontos";
        pointsText.setText(text);
    }

    private void updateTimer()
    {
        String timerTextToShow = " " + count;
        timerText.setText(timerTextToShow);
    }

    private void changeMolesState()
    {
        for(int i = 0; i < maxSize; i++)
        {
            if(moles.get(i).getTag().equals("NotShowing"))
            {
                if(getRandomInt(0, 1) == 0)
                {
                    show(i);
                }
            }
            else
            {
                notShow(i);
            }
        }
    }

    private void notShow(int i)
    {
        moles.get(i).setImageAlpha(0);
        moles.get(i).setTag("NotShowing");
        moles.get(i).setOnClickListener(null);
    }

    private void show(int i)
    {
        moles.get(i).setImageAlpha(255);
        moles.get(i).setTag("Showing");
        moles.get(i).setOnClickListener(this);
    }

    private int getRandomInt(int min, int max)
    {
        Random random = new Random();
        return random.nextInt((max - min) + 1);
    }

    private void changeActivity(Class c)
    {
        Intent intent = new Intent(this, c);
        startActivity(intent);
        this.finish();
    }

    public void restartGame(View view)
    {
        changeActivity(ActivityGame.class);
    }

    private Context getContext()
    {
        return this;
    }

    @Override
    protected void onResume() {
        super.onResume();
        startTimer();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopTimer();
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopTimer();
    }
}
