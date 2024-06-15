package com.example.stop_watch;


import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private Button startButton, pauseButton, resetButton, lapButton;
    private Chronometer chronometer;
    private TextView lapTimesTextView;

    private boolean isRunning = false;
    private long pauseOffset;
    private long lastLapTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startButton = findViewById(R.id.start);
        pauseButton = findViewById(R.id.pause);
        resetButton = findViewById(R.id.reset);
        lapButton = findViewById(R.id.lap);
        chronometer = findViewById(R.id.chronometer);
        lapTimesTextView = findViewById(R.id.lapTimesTextView);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isRunning) {
                    chronometer.setBase(SystemClock.elapsedRealtime() - pauseOffset);
                    chronometer.start();
                    startButton.setVisibility(View.GONE);
                    pauseButton.setVisibility(View.VISIBLE);
                    isRunning = true;
                    lastLapTime = SystemClock.elapsedRealtime();
                }
            }
        });

        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isRunning) {
                    chronometer.stop();
                    pauseOffset = SystemClock.elapsedRealtime() - chronometer.getBase();
                    startButton.setVisibility(View.VISIBLE);
                    pauseButton.setVisibility(View.GONE);
                    isRunning = false;
                }
            }
        });

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chronometer.stop();
                chronometer.setBase(SystemClock.elapsedRealtime());
                pauseOffset = 0;
                startButton.setVisibility(View.VISIBLE);
                pauseButton.setVisibility(View.GONE);
                isRunning = false;
                lapTimesTextView.setText("");
            }
        });

        lapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isRunning) {
                    long currentElapsedTime = SystemClock.elapsedRealtime();
                    long lapTime = currentElapsedTime - lastLapTime;
                    lastLapTime = currentElapsedTime;

                    int hours = (int) (lapTime / 3600000);
                    int minutes = (int) (lapTime - hours * 3600000) / 60000;
                    int seconds = (int) (lapTime - hours * 3600000 - minutes * 60000) / 1000;
                    int millis = (int) (lapTime - hours * 3600000 - minutes * 60000 - seconds * 1000);

                    String lapTimeString = String.format("%02d:%02d:%02d.%03d\n", hours, minutes, seconds, millis);
                    lapTimesTextView.append(lapTimeString);
                } else {
                    Toast.makeText(MainActivity.this, "Start the stopwatch first!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}

