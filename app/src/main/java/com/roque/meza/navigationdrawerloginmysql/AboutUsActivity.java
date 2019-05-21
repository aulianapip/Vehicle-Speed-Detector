package com.roque.meza.navigationdrawerloginmysql;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class AboutUsActivity extends Fragment {

    public AboutUsActivity(){}
    LinearLayout view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = (LinearLayout) inflater.inflate(R.layout.activity_about_us, container, false);

        getActivity().setTitle("About Us");
        Log.e("About Us", "About Us");

        return view;
    }
}
