package me.tigerapps.busbuddy;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import me.tigerapps.busbuddy.databinding.ListFragmentBinding;
import me.tigerapps.busbuddy.model.BusRoute;

/**
 * Fragment that shows a list of bus routes.
 */

public class ListFragment extends Fragment {
    private static final int REQUEST_IMPORT = 1;

    private ListFragmentBinding binding;

    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        if (requestCode == REQUEST_IMPORT) {
            if (resultCode == Activity.RESULT_OK)
                ((MainActivity) getActivity()).importRoute(data.getData());
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onAttach(final Context context) {
        super.onAttach(context);
        if (binding != null)
            binding.setRoutes(((MainActivity) context).getRoutes());
    }

    @Override
    public void onCreateOptionsMenu(final Menu menu, final MenuInflater inflater) {
        inflater.inflate(R.menu.list_fragment, menu);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             final Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        binding = ListFragmentBinding.inflate(inflater, container, false);
        binding.setRoutes(((MainActivity) getActivity()).getRoutes());
        binding.routeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent, final View view,
                                    final int position, final long id) {
                final Bundle arguments = new Bundle();
                final BusRoute route = (BusRoute) parent.getAdapter().getItem(position);
                arguments.putString(MainActivity.KEY_ROUTE, route.getName());
                final Fragment fragment = new RouteFragment();
                fragment.setArguments(arguments);
                final FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, fragment);
                transaction.addToBackStack(null);
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                transaction.commit();
            }
        });
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_import_route:
                final Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("*/*");
                startActivityForResult(intent, REQUEST_IMPORT);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (binding != null)
            binding.setRoutes(null);
    }
}
