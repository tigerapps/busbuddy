package me.tigerapps.busbuddy;

import android.app.Activity;
import android.os.Bundle;

import com.jakewharton.threetenabp.AndroidThreeTen;

public class MainActivity extends Activity {
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidThreeTen.init(getApplication().getBaseContext());
    }
}
