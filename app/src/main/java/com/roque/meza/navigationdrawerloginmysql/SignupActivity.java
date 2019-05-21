package com.roque.meza.navigationdrawerloginmysql;

import android.graphics.drawable.AnimationDrawable;
import android.support.v7.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.roque.meza.navigationdrawerloginmysql.Utils.UserParcelable;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Maycol Meza on 14/11/2017.
 * Modified by Hafizh Anggit on 27/12/2017
 */

public class SignupActivity extends AppCompatActivity {
    private TextView loginLink;
    private ImageView imagePhoto;
    private EditText password;
    private EditText name;
    private EditText email;
    private Button register;
    private int request_code = 1;
    private Bitmap bitmap;
    private ProgressDialog progreso;
    RequestQueue requestQueue; //izinkan koneksi langsung dari layanan web
    StringRequest stringRequest;
    Animation frombottom, fromtop;
    TextView textChange;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        LinearLayout linearLayout = findViewById(R.id.layout_logout);
        //animasi gradient_list bergerak
        AnimationDrawable animationDrawable = (AnimationDrawable) linearLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();

        //animasi fadeTop dan fadeBottom formLogin
        frombottom = AnimationUtils.loadAnimation(this,R.anim.frombottom);
        fromtop = AnimationUtils.loadAnimation(this,R.anim.fromtop);

        imagePhoto = (ImageView) findViewById(R.id.user_image_register);
        textChange = (TextView) findViewById(R.id.txtCange);
        name = (EditText)findViewById(R.id.name_registration);
        email = (EditText)findViewById(R.id.correo_registro);
        password = (EditText)findViewById(R.id.password_registro);
        register = (Button)findViewById(R.id.btn_register_user);
        loginLink = (TextView)findViewById(R.id.link_login);

        //animasi transisi fadeTop dan fadeBottom formLogin
        imagePhoto.startAnimation(fromtop);
        textChange.startAnimation(fromtop);
        name.startAnimation(fromtop);
        email.startAnimation(fromtop);
        password.startAnimation(fromtop);
        register.startAnimation(frombottom);
        loginLink.startAnimation(frombottom);

        requestQueue = Volley.newRequestQueue(this);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Register();
            }
        });

        imagePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = null;
                //Verifikasi versi platform
                if(Build.VERSION.SDK_INT < 19){
                    //Android 4.3 dan sebelumnya
                    i = new Intent();
                    i.setAction(Intent.ACTION_GET_CONTENT);
                }else {
                    //Android 4.4 dan lebih tinggi
                    i = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                    i.addCategory(Intent.CATEGORY_OPENABLE);
                }
                i.setType("image/*");
                startActivityForResult(i, request_code);
            }
        });

        loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                finish();
            }
        });
    }

    private void Register() {

        if (!validar()) return;

        progreso = new ProgressDialog(this);
        progreso.setMessage("Registrasi akun...");
        progreso.show();
        String url = "http://androinotech.com/movil/register_movil.php?";

        stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                UserParcelable userParcelable = new UserParcelable();;
                Log.i("JSON REQUEST: ",""+response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if(jsonObject.names().get(0).equals("success")){
                        email.setText("");
                        name.setText("");
                        password.setText("");
                        userParcelable.setId(jsonObject.getJSONArray("user").getJSONObject(0).getInt("iduser_"));
                        userParcelable.setEmail(jsonObject.getJSONArray("user").getJSONObject(0).getString("email"));
                        userParcelable.setName(jsonObject.getJSONArray("user").getJSONObject(0).getString("names"));
                        userParcelable.setImage(jsonObject.getJSONArray("user").getJSONObject(0).getString("photo"));

                        Toast.makeText(getApplicationContext(),jsonObject.getString("success"),Toast.LENGTH_SHORT).show();
                        progreso.dismiss();

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

                progreso.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"Tidak dapat terhubung ke database",Toast.LENGTH_SHORT).show();
                Log.i("ERROR: ",""+error.toString());
                progreso.dismiss();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {//mengirim data menggunakan method POST
                String sEmail = email.getText().toString();
                String sPassword =  password.getText().toString();
                String sName = name.getText().toString();
                String  sImagePhoto = convertirImgString(bitmap);

                Map<String,String> parameter = new HashMap<>();
                parameter.put("email",sEmail);
                parameter.put("password",sPassword);
                parameter.put("photo",sImagePhoto);
                parameter.put("names",sName);
                //kirim ke layanan web service

                return parameter;
            }
        };

        requestQueue.add(stringRequest);
    }

    private String convertirImgString(Bitmap bitmap) {

        String imagenString;
        ByteArrayOutputStream array=new ByteArrayOutputStream();
        if(bitmap!=null){
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,array);
            byte[] imagenByte=array.toByteArray();
            imagenString= Base64.encodeToString(imagenByte,Base64.DEFAULT);
        }else{
            imagenString = "Tidak ada gambar"; //string ini akan dikirim jika tidak ada gambar
        }

        return imagenString;
    }

    private boolean validar() {
        boolean valid = true;

        String sName = name.getText().toString();
        String sPassword = password.getText().toString();
        String sEmail = email.getText().toString();

        if (sName.isEmpty() || sName.length() < 3) {
            name.setError("Masukkan min 3 karakter");
            valid = false;
        } else {
            name.setError(null);
        }

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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if(resultCode == RESULT_OK && requestCode == request_code){
            imagePhoto.setImageURI(data.getData());

            try{
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),data.getData());
                imagePhoto.setImageBitmap(bitmap);
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
