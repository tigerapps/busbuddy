package me.tigerapps.busbuddy;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import me.tigerapps.busbuddy.databinding.RouteFragmentBinding;

/**
 * Fragment that shows details about a bus route.
 */

public class RouteFragment extends Fragment {
    private RouteFragmentBinding binding;
    private String route;

    @Override
    public void onAttach(final Context context) {
        super.onAttach(context);
        if (binding != null)
            binding.setRoute(((MainActivity) context).getRoutes().get(route));
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             final Bundle savedInstanceState) {
        if (savedInstanceState != null)
            route = savedInstanceState.getString(MainActivity.KEY_ROUTE);
        else if (getArguments() != null)
            route = getArguments().getString(MainActivity.KEY_ROUTE);
        else
            throw new IllegalStateException();
        binding = RouteFragmentBinding.inflate(inflater, container, false);
        binding.setRoute(((MainActivity) getActivity()).getRoutes().get(route));
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (binding != null)
            binding.setRoute(null);
    }
}

