package me.tigerapps.busbuddy;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.ContentResolver;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.jakewharton.threetenabp.AndroidThreeTen;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import me.tigerapps.busbuddy.bindings.ObservableSortedMap;
import me.tigerapps.busbuddy.bindings.ObservableTreeMap;
import me.tigerapps.busbuddy.model.BusRoute;

/**
 * Main class that shows a list/detail about bus routes, and allows setting a reminder.
 */

public class MainActivity extends Activity {
    public static final String KEY_ROUTE = "route";

    private final ObservableSortedMap<String, BusRoute> routes = new ObservableTreeMap<>();

    public ObservableSortedMap<String, BusRoute> getRoutes() {
        return routes;
    }

    public void importRoute(Uri uri) {
        new RouteImporter().execute(uri);
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        if (getFragmentManager().findFragmentById(R.id.fragment_container) == null) {
            final FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.add(R.id.fragment_container, new ListFragment()).commit();
        }
        AndroidThreeTen.init(getApplication().getBaseContext());
        new RouteLoader().execute(getFilesDir().listFiles(new FilenameFilter() {
            @Override
            public boolean accept(final File file, final String s) {
                return s.endsWith(".json");
            }
        }));
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_import_route:
                // Handled by a fragment.
                return false;
            case R.id.menu_settings:
                // TODO: Start the settings activity.
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private class RouteImporter extends AsyncTask<Uri, Void, List<File>> {
        @Override
        protected List<File> doInBackground(final Uri... uris) {
            final ContentResolver contentResolver = getContentResolver();
            final List<File> files = new ArrayList<>(uris.length);
            for (final Uri uri : uris) {
                String name = uri.getLastPathSegment();
                if (!name.endsWith(".json"))
                    name = name + ".json";
                final File output = new File(getFilesDir(), name);
                try (final OutputStream out = new FileOutputStream(output, false)) {
                    final InputStream in = contentResolver.openInputStream(uri);
                    if (in == null)
                        continue;
                    // FIXME: This is a rather arbitrary size.
                    final byte[] buffer = new byte[4096];
                    int bytes;
                    while ((bytes = in.read(buffer)) != -1)
                        out.write(buffer, 0, bytes);
                    files.add(output);
                } catch (final IOException e) {
                    Log.w(getClass().getSimpleName(), "Failed to import " + uri, e);
                }
                if (isCancelled())
                    break;
            }
            return files;
        }

        @Override
        protected void onPostExecute(final List<File> files) {
            new RouteLoader().execute(files.toArray(new File[files.size()]));
        }
    }

    private class RouteLoader extends AsyncTask<File, Void, List<BusRoute>> {
        @Override
        protected List<BusRoute> doInBackground(final File... files) {
            final List<BusRoute> routes = new ArrayList<>(files.length);
            for (final File file : files) {
                try {
                    routes.add(BusRoute.parseJson(new JsonReader(new FileReader(file))));
                } catch (IOException | ParseException e) {
                    Log.w(getClass().getSimpleName(), "Removing " + file.getName() +
                            " as it failed to parse", e);
                    // noinspection ResultOfMethodCallIgnored
                    file.delete();
                }
                if (isCancelled())
                    break;
            }
            return routes;
        }

        @Override
        protected void onPostExecute(final List<BusRoute> routes) {
            for (final BusRoute route : routes) {
                // FIXME: This overwrites existing routes without prompting.
                MainActivity.this.routes.put(route.getName(), route);
            }
        }
    }
}
