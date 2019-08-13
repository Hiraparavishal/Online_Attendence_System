package com.example.camera1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class login extends AppCompatActivity implements WebServiceDAO{

    private String username;
    private String password;
    public static  final  String SHRED_PREFS="shredpref";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        final EditText user=(EditText)findViewById(R.id.user);
        final EditText pass=(EditText)findViewById(R.id.pass);
        Button log=(Button)findViewById(R.id.blog);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("LOGIN");



        SharedPreferences sp1=getSharedPreferences(SHRED_PREFS,MODE_PRIVATE);
        final String username1 =sp1.getString("username","");

          if(!username1.equals(""))
          {
              Toast.makeText(getApplicationContext(),"welcome "+username1+"sir",Toast.LENGTH_LONG).show();
              Intent i2=new Intent(login.this,MainActivity.class);
              startActivity(i2);
              finish();
          }


        log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = user.getText().toString();
                password = pass.getText().toString();
                System.out.println(username+" - "+password);

                if(username!="" && password!= "")
                {
                    GetService service = new GetService();
                    try {
                        System.out.println(SERVICEURL+"authenticate/"+username+"/"+password);
                        String response = service.execute(SERVICEURL+"authenticate/"+user.getText().toString()+"/"+password).get();


                        if (response != null) {
                            try {
                                JSONObject jsonObj = new JSONObject(response);
                                String isAuth = jsonObj.getString("msg");
                                if(!isAuth.equals("False"))
                                {
                                    SharedPreferences sp1=getSharedPreferences(SHRED_PREFS,MODE_PRIVATE);
                                    SharedPreferences.Editor e1=sp1.edit();
                                    e1.putString("username",username);
                                    e1.apply();
                                    Toast.makeText(getApplicationContext(),"login success",Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(login.this,MainActivity.class));
                                    finish();
                                }else
                                {
                                    Toast.makeText(getApplicationContext(),"INVALID USERNAME / PASSWORD",Toast.LENGTH_SHORT).show();
                                }

                            } catch (JSONException e) {

                                Toast.makeText(getApplicationContext(),"INTERNET NOT CONNECTED...",Toast.LENGTH_LONG).show();
                            }
                        }else{
                            Toast.makeText(getApplicationContext(),"INTERNET NOT CONNECTED...",Toast.LENGTH_LONG).show();
                        }



                        //Toast.makeText(getApplicationContext(),"sucssess",Toast.LENGTH_SHORT).show();

                    } catch (ExecutionException e) {
                        Toast.makeText(getApplicationContext(),"INTERNET NOT CONNECTED...",Toast.LENGTH_LONG).show();
                    } catch (InterruptedException e) {
                        Toast.makeText(getApplicationContext(),"INTERNET NOT CONNECTED...",Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(getApplicationContext(),"INVALID USERNAME / PASSWORD",Toast.LENGTH_LONG).show();
                }

            }
        });





    }
}
