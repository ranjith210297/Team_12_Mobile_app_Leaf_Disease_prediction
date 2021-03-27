package com.example.camera;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    ImageButton btBrowse,btReset;
    ImageView imageView;
    Uri uri;



    private ImageView mimageView;
    private static final int REQUEST_IMAGE_CAPTURE=101;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mimageView = findViewById(R.id.imageView);

        btBrowse=findViewById(R.id.bt_browse);
        btReset=findViewById(R.id.bt_reset);
        imageView=findViewById(R.id.imageView);

        btBrowse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /**cropImageview.startPickImageActivity(MainActivity.this);*/
            }
        });
        btReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageView.setImageBitmap(null);
            }
        });


    }

    public void takePicture(View view){
        Intent imageTakeIntent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if(imageTakeIntent.resolveActivity(getPackageManager())!=null){
            startActivityForResult(imageTakeIntent,REQUEST_IMAGE_CAPTURE);
        }
    }

    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        if(requestCode==REQUEST_IMAGE_CAPTURE && resultCode==RESULT_OK)
        {
            Bundle extras=data.getExtras();
            Bitmap imageBitmap= (Bitmap)extras.get("data");
            mimageView.setImageBitmap(imageBitmap);
        }

    }

    private class Uri {
    }
}
