package com.example.braintrainer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.TextView;

public class EndGameActivity extends AppCompatActivity {

    public static final String RECORD = "record";
    private TextView textViewResult;
    private static final String APP_HISTORY = "application history";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_game);
        Intent intent = getIntent();
        int score = intent.getIntExtra(MainActivity.SCORE, 0);
        textViewResult = findViewById(R.id.textViewResult);
        int record;
        SharedPreferences preferences = getSharedPreferences(APP_HISTORY, Context.MODE_PRIVATE);
        record = preferences.getInt(RECORD, 0);
        if(score > record) {
            preferences.edit().putInt(RECORD, score).apply();
            record = score;
        }
        String result = String.format("Ваш результат: %d\nМаксимальный результат: %d", score, record);
        textViewResult.setText(result);
    }

    public void onClickPlayAgain(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
