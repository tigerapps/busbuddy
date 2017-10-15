package me.tigerapps.busbuddy;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.jakewharton.threetenabp.AndroidThreeTen;

import me.tigerapps.busbuddy.databinding.MainActivityBinding;

public class MainActivity extends Activity {
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidThreeTen.init(getApplication().getBaseContext());
        final MainActivityBinding binding =
                DataBindingUtil.setContentView(this, R.layout.main_activity);
    }
}
