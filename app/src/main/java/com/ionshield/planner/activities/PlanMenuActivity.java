package com.ionshield.planner.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.ionshield.planner.R;

public class PlanMenuActivity extends AppCompatActivity {
    private int planId;
    private String planName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_menu);

        Intent intent = getIntent();

        planId = intent.getIntExtra("planId", -1);
        planName = intent.getStringExtra("planName");

        updateText();
    }

    public void updateText() {
        TextView currentPlanTextView = findViewById(R.id.current_plan_name);
        if (planId <= 0) {
            currentPlanTextView.setText(R.string.current_plan_none);
        }
        else {
            if (planName != null && !planName.isEmpty()) {
                currentPlanTextView.setText(planName);
            }
            else {
                currentPlanTextView.setText(R.string.current_plan_none);
            }
        }
    }
}