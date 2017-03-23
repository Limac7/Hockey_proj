package com.example.bebe.hockey;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;

public class AllShotsActivity extends AppCompatActivity {

    private ArrayAdapter<String> allShotsArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_shots);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }
}
