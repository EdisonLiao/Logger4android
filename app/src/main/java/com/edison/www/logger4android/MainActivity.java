package com.edison.www.logger4android;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    @Override
    protected void onStart() {
        super.onStart();
        Loggger loggger = new Loggger.LogBuilder().savePath(Environment.getExternalStorageDirectory()
                + File.separator + "haha").logByDay().build();
        loggger.writeLog("真的可以了");
        loggger.close();
    }
}
