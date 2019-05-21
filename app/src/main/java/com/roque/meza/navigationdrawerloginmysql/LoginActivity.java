package com.roque.meza.navigationdrawerloginmysql;

import android.graphics.drawable.AnimationDrawable;
import android.support.v7.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.roque.meza.navigationdrawerloginmysql.Utils.UserParcelable;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Maycol Meza on 14/11/2017.
 * Modified by Hafizh Anggit on 27/12/2017
 */
public class LoginActivity extends AppCompatActivity {
    private Button login;
    private TextView register;
    private EditText email;
    private EditText password;
    private ProgressDialog progress;
    private RequestQueue requestQueue;
    StringRequest stringRequest;
    Animation frombottom, fromtop;
    TextView textTitle, textDesc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        LinearLayout linearLayout = findViewById(R.id.layout_login);
        //animasi gradient_list bergerak
        AnimationDrawable animationDrawable = (AnimationDrawable) linearLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();

        //animasi fadeTop dan fadeBottom formLogin
        frombottom = AnimationUtils.loadAnimation(this,R.anim.frombottom);
        fromtop = AnimationUtils.loadAnimation(this,R.anim.fromtop);

        textTitle = (TextView) findViewById(R.id.txtTitle);
        textDesc = (TextView) findViewById(R.id.txtDesc);
        email = (EditText)findViewById(R.id.etusuario);
        password = (EditText)findViewById(R.id.etpass);
        login = (Button)findViewById(R.id.btn_login);
        register = (TextView)findViewById(R.id.signup);
        requestQueue = Volley.newRequestQueue(this);

        //animasi transisi fadeTop dan fadeBottom formLogin
        textTitle.startAnimation(fromtop);
        textDesc.startAnimation(fromtop);
        email.startAnimation(fromtop);
        password.startAnimation(fromtop);
        login.startAnimation(frombottom);
        register.startAnimation(frombottom);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(getApplicationContext(),SignupActivity.class);
                startActivity(intent);
                //animasi transisi perpindahan formLogin dan formSignup
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                finish();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
    }

    private void login() {

        if (!validation()) return;

        progress = new ProgressDialog(this);
        progress.setMessage("Verifikasi...");
        progress.show();
        String url = "http://androinotech.com/movil/login_movil.php?";

        stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                UserParcelable userParcelable = new UserParcelable();;
                Log.i("JSON REQUEST: ",""+response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if(jsonObject.names().get(0).equals("success")){
                        email.setText("");
                        password.setText("");

                        userParcelable.setId(jsonObject.getJSONArray("user").getJSONObject(0).getInt("iduser_"));
                        userParcelable.setEmail(jsonObject.getJSONArray("user").getJSONObject(0).getString("email"));
                        userParcelable.setName(jsonObject.getJSONArray("user").getJSONObject(0).getString("names"));
                        userParcelable.setImage(jsonObject.getJSONArray("user").getJSONObject(0).getString("photo"));

                        Toast.makeText(getApplicationContext(),jsonObject.getString("success"),Toast.LENGTH_SHORT).show();
                        progress.dismiss();

                        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                        intent.putExtra("DATA_USER",userParcelable);
                        startActivity(intent);
                        finish();
                    }else{
                        Toast.makeText(getApplicationContext(),jsonObject.getString("error"),Toast.LENGTH_SHORT).show();
                        Log.i("JSON REQUEST: ",""+jsonObject.getString("error"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                progress.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"Tidak dapat terhubung ke database",Toast.LENGTH_SHORT).show();
                progress.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {//mengirim data melalui method POST
                String sEmail = email.getText().toString();
                String sPassword =  password.getText().toString();

                Map<String,String> parameter = new HashMap<>();
                parameter.put("email",sEmail);
                parameter.put("password",sPassword);
                //parameter mengirim ke web server

                return parameter;
            }
        };

        requestQueue.add(stringRequest);
    }

    private boolean validation() {
        boolean valid = true;

        String sEmail = email.getText().toString();
        String sPassword = password.getText().toString();

        if (sEmail.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(sEmail).matches()) {
            email.setError("Alamat email tidak valid!");
            valid = false;
        } else {
            email.setError(null);
        }

        if (sPassword.isEmpty() || password.length() < 4 || password.length() > 10) {
            password.setError("Inputan 4 sampai 10 karakter alfanumerik");
            valid = false;
        } else {
            password.setError(null);
        }

        return valid;
    }

}
