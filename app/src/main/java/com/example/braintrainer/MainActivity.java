package com.example.braintrainer;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private static final int MAX = 100;
    private static final int MAX_SUM = 200;
    private static final int COUNT_OF_VARIANTS = 4;
    private static final int TIME_FOR_GAME_IN_MILLISECONDS = 6_000;
    private static final int CRITICAL_TIME_IN_MILLISECONDS = 3_000;
    public static final int MILLISECONDS_IN_SECOND = 1_000;
    public static final String SCORE = "score";

    private Random random;
    private int rightVariant;
    private int score;
    private TextView textViewTask;
    private TextView textViewTimer;
    private TextView textViewScore;
    private Button buttonVariantOne;
    private Button buttonVariantTwo;
    private Button buttonVariantThree;
    private Button buttonVariantFour;
    private CountDownTimer timer;
    private int countOfGamesPlayed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textViewTask = findViewById(R.id.textViewTask);
        textViewTimer = findViewById(R.id.textViewTimer);
        textViewScore = findViewById(R.id.textViewScore);
        buttonVariantOne = findViewById(R.id.buttonVariantOne);
        buttonVariantTwo = findViewById(R.id.buttonVariantTwo);
        buttonVariantThree = findViewById(R.id.buttonVariantThree);
        buttonVariantFour = findViewById(R.id.buttonVariantFour);
        random = new Random(System.currentTimeMillis());
        score = 0;
        countOfGamesPlayed = 0;
        setScoreToTextView(score, countOfGamesPlayed);
        timer = new CountDownTimer(TIME_FOR_GAME_IN_MILLISECONDS, MILLISECONDS_IN_SECOND) {
            @Override
            public void onTick(long millisUntilFinished) {
                int secondsLeft = (int) millisUntilFinished / MILLISECONDS_IN_SECOND;
                secondsLeft++;
                textViewTimer.setText(getStringSeconds(secondsLeft));
                if (millisUntilFinished < CRITICAL_TIME_IN_MILLISECONDS)
                    textViewTimer.setTextColor(getResources().getColor(R.color.red));
                else
                    textViewTimer.setTextColor(getResources().getColor(android.R.color.holo_green_light));
            }

            @Override
            public void onFinish() {
                textViewTimer.setText(getStringSeconds(0));
                Intent intent = new Intent(getApplicationContext(), EndGameActivity.class);
                intent.putExtra(SCORE, score);
                startActivity(intent);
            }
        };
        timer.start();
        setVariantsAndTask();
    }

    private void setVariantsAndTask() {
        int first = random.nextInt(MAX) - MAX / 2;
        int second = random.nextInt(MAX) - MAX / 2;
        String sign = null;
        if (second >= 0)
            sign = " + ";
        else
            sign = " - ";
        textViewTask.setText(String.format("%d%s%d", first, sign, Math.abs(second)));
        rightVariant = first + second;
        ArrayList<Integer> variants = getVariants(rightVariant);
        buttonVariantOne.setText(variants.get(0).toString());
        buttonVariantTwo.setText(variants.get(1).toString());
        buttonVariantThree.setText(variants.get(2).toString());
        buttonVariantFour.setText(variants.get(3).toString());
    }

    private ArrayList<Integer> getVariants(int rightVariant) {
        ArrayList<Integer> variants = new ArrayList<>();
        variants.add(rightVariant);
        for (int i = 0; i < COUNT_OF_VARIANTS - 1; i++) {
            int wrongVariant = getWrongVariant();
            while (wrongVariant == rightVariant)
                wrongVariant = getWrongVariant();
            variants.add(wrongVariant);
        }
        Collections.shuffle(variants);
        return variants;
    }

    private int getWrongVariant() {
        return random.nextInt(MAX_SUM) - MAX_SUM / 2;
    }

    private String getStringSeconds(int seconds) {
        return String.format("00:%02d", seconds);
    }

    public void onClickChooseVariant(View view) {
        countOfGamesPlayed++;
        Button button = (Button) view;
        if (Integer.parseInt(button.getText().toString()) == rightVariant) {
            Toast.makeText(this, getString(R.string.right_answer), Toast.LENGTH_SHORT).show();
            score++;
        } else
            Toast.makeText(this, getString(R.string.wrong_answer) + " " + rightVariant,
                    Toast.LENGTH_SHORT).show();
        setVariantsAndTask();
        setScoreToTextView(score, countOfGamesPlayed);
        timer.start();
    }


    private void setScoreToTextView(int score, int gamesPlayed) {
        String scoreInfo = String.format("%d / %d", score, gamesPlayed);
        textViewScore.setText(scoreInfo);
    }
}
