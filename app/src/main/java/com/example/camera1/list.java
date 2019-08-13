package com.example.camera1;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EdgeEffect;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.concurrent.ExecutionException;

import static com.example.camera1.WebServiceDAO.SERVICEURL;

public class list extends AppCompatActivity {
    String ob,ob1="1";
    ArrayList arrayList  = null;
    int j=0,i=0;
    String date;
    JSONArray arr;
    String response;
    int fs=10;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);


        Button d1=(Button)findViewById(R.id.button4);
        final Button d2=(Button)findViewById(R.id.button5);
        final EditText year=(EditText)findViewById(R.id.year);
        final EditText months=(EditText)findViewById(R.id.month);
        final EditText date=(EditText)findViewById(R.id.date);
        final TextView list=(TextView)findViewById(R.id.list);
        final TextView show=(TextView)findViewById(R.id.textView5);
        final EditText month2=(EditText)findViewById(R.id.editText2);
        final TextView t1=(TextView)findViewById(R.id.textView8);

        d1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String year1 = year.getText().toString();
                String month1 = months.getText().toString();
                String date1 = date.getText().toString();
               String myDate= year1 +"/"+ month1+"/" +date1;
               System.out.println(myDate);
               show.setText("Date: " + myDate);



               GetService service = new GetService();
                try {
                    arrayList = new ArrayList();

                    String response = service.execute(SERVICEURL+"attendenceBy/"+ myDate).get();

                    System.out.println(response);
                    JSONObject jsonObj = new JSONObject(response);
                    String absent = jsonObj.getString("data");
                    JSONArray arr = new JSONArray(absent);






                    System.out.println("PRESENT LIST "+arr.toString());
                    for(int i=0;i<arr.length();i++)
                    {
                        String vis =arr.getString(i);
                        JSONArray arrin= new JSONArray(vis);
                        System.out.println("Array List Data "+arrin.toString());
                        ob=arrin.getString(1);
                        System.out.println("OB "+ob);
                        if(ob.equals(ob1))
                        {
                            arrayList.add(arrin.getString(0));
                            System.out.println(arrin.getString(0));
                        }
                    }
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                InputMethodManager imm = (InputMethodManager)getSystemService(
                        Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(date
                        .getWindowToken(), 0);
                list.setTextSize(25);
                list.setText(arrayList.toString());
                System.out.println("size is:------");
                System.out.println(arrayList.size());
                t1.setText("Total No. of present student is: "+arrayList.size());

            }


        });


        d2.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {

                String month=month2.getText().toString();


                GetService service = new GetService();
                try {
                    String response = service.execute(SERVICEURL+"attendenceByMonth/"+ month).get();
                    list.setTextSize(15);
                    list.setText(response);
                    System.out.println("here");
                    JSONObject jobj = new JSONObject(response);
                    System.out.println("there");
                    System.out.println("here");
                    System.out.println(jobj.toString());
                    Iterator<String> iter = jobj.keys();
                    System.out.println("here");
                    while (iter.hasNext()) {
                        System.out.println("in");
                        String key = iter.next();
                        try {
                            Object value = jobj.get(key);
                            System.out.println(value);
                        } catch (JSONException e) {
                            // Something went wrong!
                        }
                    }
                    System.out.println("out");

                        } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    } catch (ExecutionException e1) {
                        e1.printStackTrace();
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }
                    InputMethodManager imm = (InputMethodManager)getSystemService(
                            Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(months
                            .getWindowToken(), 0);
                   show.setText("Month: " + month);
                   t1.setText("");



            }


                });
        }



    }



