package com.example.camera1;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.Telephony;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import in.ac.adit.hackathon.GetAbsentList;

import static com.example.camera1.WebServiceDAO.SERVICEURL;

public class MainActivity extends AppCompatActivity {
    private StorageReference mStorageref;
    private DatabaseReference mdatabaseref;
    private EditText txtimagename;
    private ImageView imageView;
    private Uri imguri;


    public static final String FB_STORAGE_PATH = "image/";
    public static final String FB_DATABASE_PATH = "image";
    public static final int REQUEST_CODE = 1234;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mStorageref = FirebaseStorage.getInstance().getReference();
        mdatabaseref = FirebaseDatabase.getInstance().getReference(FB_DATABASE_PATH);
        Button b1 = (Button) findViewById(R.id.button);
       // txtimagename = (EditText) findViewById(R.id.editText);
        Button bt2=(Button)findViewById(R.id.button2);
        imageView =(ImageView)findViewById(R.id.imageView);
        Button b3=(Button)findViewById(R.id.button3);
        Button email=(Button)findViewById(R.id.button6) ;
       /* ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("HOME");*/


      email.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              GetService nserver = new GetService();
              nserver.execute("http://192.168.137.1:8081/mail");
              Toast.makeText(getApplicationContext(),"EMAIL SEND SUCCESSFUL",Toast.LENGTH_LONG).show();
          }
      });



      b3.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {



              Intent i1=new Intent(MainActivity.this,list.class);
              startActivity(i1);


          }
      });

       /* b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getApplicationContext(),"hiiii",Toast.LENGTH_SHORT).show();

                Intent i1=new Intent(MainActivity.this,list.class);
                startActivity(i1);
            /*  GetAbsentList getAbsentList =new GetAbsentList();
                String json = null;
                        try{
                            json = getAbsentList.execute().get();
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                System.out.println(json);
                  //jsn.setText(json);


            }
        });*/


        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "select image"), REQUEST_CODE);
            }
        });
        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnupload_click();

            }
        });


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {

            imguri = data.getData();
            try {

                Bitmap bm = MediaStore.Images.Media.getBitmap(getContentResolver(), imguri);

                imageView.setImageBitmap(bm);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public String getimageext(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mineTypeMap = MimeTypeMap.getSingleton();
        return mineTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));

    }

    public void btnupload_click() {
        if (imguri != null) {
            SimpleDateFormat formatter = new SimpleDateFormat("dd_MM_yyyy HH:mm:ss");
            Date date = new Date();
            String strDate = formatter.format(date);
            //System.out.println(formatter.format(date));

            final ProgressDialog dialog = new ProgressDialog(this);
            dialog.setTitle("uploading image");
            dialog.show();
            StorageReference ref = mStorageref.child(strDate);
             ref.putFile(imguri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                 @Override
                 public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                     dialog.dismiss();


                     Toast.makeText(getApplicationContext(),"uploaded sucsses..",Toast.LENGTH_LONG).show();
                     imageUpload imageupload= new imageUpload("vishal",FB_STORAGE_PATH + System.currentTimeMillis() + "." + getimageext(imguri));
                     String uploadid=mdatabaseref.push().getKey();
                     mdatabaseref.child(uploadid).setValue(imageupload);

                 }
             }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                 @Override
                 public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                     dialog.dismiss();
                     Toast.makeText(getApplicationContext(),"uploading......",Toast.LENGTH_SHORT).show();

                 }
             });
        }
        else
        {

            Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_SHORT).show();
        }


    }



}