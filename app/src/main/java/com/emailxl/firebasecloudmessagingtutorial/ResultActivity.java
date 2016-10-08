package com.emailxl.firebasecloudmessagingtutorial;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class ResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstatnceState) {
        super.onCreate(savedInstatnceState);
        setContentView(R.layout.activity_main);
        TextView textView = (TextView) findViewById(R.id.textView1);
        textView.setText(getResources().getString(R.string.result_text));
    }
}
