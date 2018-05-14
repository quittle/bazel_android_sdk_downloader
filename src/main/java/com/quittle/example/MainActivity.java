package com.quittle.example;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.google.common.io.Files;

public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        findViewById(R.id.hello_world);

        // Use Guava to prove it's bundled in
        Log.d("MainActivity", "Creating a temp dir on the main thread again. Somebody stop me: " +
                Files.createTempDir().getAbsolutePath());
    }
}
