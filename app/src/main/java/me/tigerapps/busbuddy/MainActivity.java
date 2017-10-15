package me.tigerapps.busbuddy;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.os.Bundle;

import com.jakewharton.threetenabp.AndroidThreeTen;

import me.tigerapps.busbuddy.databinding.MainActivityBinding;
import me.tigerapps.busbuddy.model.BusRoute;

public class MainActivity extends Activity {
    private final ObservableList<BusRoute> routes = new ObservableArrayList<>();

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidThreeTen.init(getApplication().getBaseContext());
        final MainActivityBinding binding =
                DataBindingUtil.setContentView(this, R.layout.main_activity);
        binding.setRoutes(routes);
    }
}
