package com.roque.meza.navigationdrawerloginmysql;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.roque.meza.navigationdrawerloginmysql.Utils.UserParcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

//import com.ebanx.swipebtn.SwipeButton;

public class RootActivity extends Fragment {

    //SwipeButton swipeButton;

    public RootActivity(){
    }
    View rootView;
    TextView textLokasi,textWarn, textKecepatan;
    private RequestQueue requestQueue;
    private Context globalContext = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Context context = getActivity();
        Context mContext = getActivity().getApplicationContext();
        globalContext = this.getActivity();
        rootView = (LinearLayout) inflater.inflate(R.layout.activity_root, container, false);
        textLokasi = (TextView) rootView.findViewById(R.id.txtLokasi);
        textKecepatan = (TextView) rootView.findViewById (R.id.txtKecepatan);

        getData();
        final Thread t = new Thread(new RepeatingThread());
        t.start();


        getActivity().setTitle("Dashboard");
        Log.e("Dashbord", "Dashboard");

        return rootView;
    }

    public void getData (){
        textKecepatan = (TextView) rootView.findViewById (R.id.txtKecepatan);
        textLokasi = (TextView) rootView.findViewById(R.id.txtLokasi);

        Context mContext = getActivity();
        RequestQueue req = Volley.newRequestQueue(globalContext);
        String url = "http://androinotech.com/movil/show_speedUser.php";

        StringRequest strreq = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("TEST", "" + response);

                try {

                    JSONArray job = new JSONArray(response);
                    JSONObject obj = job.getJSONObject(0);

                    textLokasi.setText(obj.getString("lokasi"));
                    textKecepatan.setText(obj.getString("kecepatan"));
                } catch (JSONException ex) {
                    ex.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        req.add(strreq);
    }
    public class RepeatingThread implements Runnable {

        private final Handler mHandler = new Handler();

        public RepeatingThread() {

        }

        @Override
        public void run() {
            mHandler.postDelayed(new Runnable() {
                public void run() {
                    getData();
                    Log.d("testing","yes");
                    mHandler.postDelayed(this, 1000);
                }
            }, 1000);
        }
    }


}