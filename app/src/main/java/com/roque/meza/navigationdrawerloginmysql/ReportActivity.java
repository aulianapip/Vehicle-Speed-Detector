package com.roque.meza.navigationdrawerloginmysql;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class ReportActivity extends Fragment {

    public ReportActivity(){}
    LinearLayout view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = (LinearLayout) inflater.inflate(R.layout.activity_report, container, false);

        getActivity().setTitle("Laporan Kegiatan");
        Log.e("Laporan", "Laporan");

        return view;
    }
}
