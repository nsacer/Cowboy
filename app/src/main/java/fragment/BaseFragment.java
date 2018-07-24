package fragment;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.xutils.x;

/**
 * A simple {@link Fragment} subclass.
 */
public class BaseFragment extends Fragment {

    private boolean injected = false;
    protected Context context;
    protected Activity activity;
    protected AppCompatActivity appCompatActivity;

    public BaseFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        injected = true;
        return x.view().inject(this, inflater, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        context = getContext();
        activity = getActivity();

        if(!injected) {
            x.view().inject(this, this.getView());
        }
        setHasOptionsMenu(true);
    }

    public Toolbar initToolbar(int toolbarId, int title) {

        appCompatActivity = (AppCompatActivity) activity;
        Toolbar toolbar = (Toolbar) appCompatActivity.findViewById(toolbarId);
        appCompatActivity.setSupportActionBar(toolbar);
        toolbar.setTitle(title);

        ActionBar actionBar = appCompatActivity.getSupportActionBar();
        if(actionBar != null) {

            actionBar.setDisplayHomeAsUpEnabled(false);
        }

        return toolbar;
    }

    public Toolbar initToolbar(int toolbarId, CharSequence title) {

        appCompatActivity = (AppCompatActivity) activity;
        Toolbar toolbar = (Toolbar) appCompatActivity.findViewById(toolbarId);
        appCompatActivity.setSupportActionBar(toolbar);
        toolbar.setTitle(title);

        ActionBar actionBar = appCompatActivity.getSupportActionBar();
        if(actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(false);

        return toolbar;
    }
}
