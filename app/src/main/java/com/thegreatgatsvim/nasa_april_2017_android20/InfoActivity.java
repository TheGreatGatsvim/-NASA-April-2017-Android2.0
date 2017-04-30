package com.thegreatgatsvim.nasa_april_2017_android20;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class InfoActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info_layout);

        TextView puntos = (TextView) findViewById(R.id.textView5);

        SharedPreferences mPrefs = getApplicationContext().getSharedPreferences("label", 0);
        String pt = mPrefs.getString("puntos", "0");
        puntos.setText(puntos + " PTO.");

    }
}
