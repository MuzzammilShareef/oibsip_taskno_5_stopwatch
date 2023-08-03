package com.example.stopwatch;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private TextView stopwatchText;
    private TextView lapListTextView;
    private Button startButton, stopButton, resetButton, lapButton;
    private long startTime = 0L;
    private Handler handler = new Handler();
    private Runnable runnable;
    private ArrayList<String> lapTimes = new ArrayList<>();
    private boolean isRunning = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        stopwatchText = findViewById(R.id.stopwatchText);
        lapListTextView = findViewById(R.id.lapListTextView);
        startButton = findViewById(R.id.startButton);
        stopButton = findViewById(R.id.stopButton);
        resetButton = findViewById(R.id.resetButton);
        lapButton = findViewById(R.id.lapButton);
    }

    public void onStartButtonClick(View view) {
        if (!isRunning) {
            startTime = System.currentTimeMillis();
            handler.postDelayed(runnable = new Runnable() {
                public void run() {
                    long millis = System.currentTimeMillis() - startTime;
                    int seconds = (int) (millis / 1000);
                    int minutes = seconds / 60;
                    seconds = seconds % 60;
                    int milliseconds = (int) (millis % 1000);
                    stopwatchText.setText(String.format(Locale.getDefault(), "%02d:%02d:%02d", minutes, seconds, milliseconds));
                    handler.postDelayed(this, 10);
                }
            }, 10);
            isRunning = true;
            startButton.setEnabled(false);
            stopButton.setEnabled(true);
            resetButton.setEnabled(true);
            lapButton.setEnabled(true);
        }
    }

    public void onStopButtonClick(View view) {
        handler.removeCallbacks(runnable);
        isRunning = false;
        startButton.setEnabled(true);
        stopButton.setEnabled(false);
        resetButton.setEnabled(true);
        lapButton.setEnabled(false);
    }

    public void onResetButtonClick(View view) {
        handler.removeCallbacks(runnable);
        stopwatchText.setText("00:00:00");
        lapTimes.clear();
        updateLapTimes();
        isRunning = false;
        startButton.setEnabled(true);
        stopButton.setEnabled(false);
        resetButton.setEnabled(false);
        lapButton.setEnabled(false);
    }

    public void onLapButtonClick(View view) {
        if (isRunning) {
            String lapTime = stopwatchText.getText().toString();
            lapTimes.add(lapTime);
            updateLapTimes();
        }
    }

    private void updateLapTimes() {
        StringBuilder lapText = new StringBuilder();
        for (int i = 0; i < lapTimes.size(); i++) {
            lapText.append("Lap ").append(i + 1).append(": ").append(lapTimes.get(i)).append("\n");
        }
        lapListTextView.setText(lapText.toString());
    }
}
