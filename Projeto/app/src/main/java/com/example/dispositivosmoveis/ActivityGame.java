package com.example.dispositivosmoveis;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Random;

public class ActivityGame extends AppCompatActivity implements View.OnClickListener {

    private int count;
    private CountDownTimer countDownTimer;

    private int points = 0;
    private TextView pointsText;
    private TextView timerText;
    private String timerTextToShow;

    private final ArrayList<ImageButton> moles = new ArrayList<>();
    private int maxSize;

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
            public void onFinish() {
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
        timerTextToShow = " " + count;
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
