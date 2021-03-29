package com.example.leaf_disease_detection;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    Button btnSelect;
    Button btnSelect2;
    ImageView img;
    private int REQUEST_STORAGE = 111;
    private int REQUEST_FILE = 222;
    private Uri uri;
    private String stringPath;
    private Intent iData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnSelect = findViewById(R.id.btnSelect);
        img = findViewById(R.id.imgs);

        btnSelect.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_STORAGE);
                } else {
                    SelectImage();
                }
            }
        });
        img.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (checkReadStorageAllowed()) {
                    File imgFile = new File(stringPath);
                    if (imgFile.exists()) {
                        Uri uri;
                        uri = FileProvider.getUriForFile(MainActivity.this, MainActivity.this.getPackageName() + "." + BuildConfig.APPLICATION_ID + ".provider", imgFile);

                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        intent.setDataAndType(uri, "image/*");
                        MainActivity.this.startActivity(intent);
                    }
                } else {
                    ActivityCompat.requestPermissions((Activity) MainActivity.this, new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, REQUEST_STORAGE);
                }
            }
        });

    }

    private void SelectImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(intent,REQUEST_FILE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_FILE && resultCode == RESULT_OK){
            if(data != null){
                uri = data.getData();
                iData = data;
                getStringPath(uri);
                try{
                InputStream inputStream = getContentResolver().openInputStream(uri);
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    img.setImageBitmap(bitmap);
            }catch (FileNotFoundException e){
                    e.printStackTrace();
                }
        }
    }
}

    private String getStringPath(Uri myUri){
        String[] filePathColumn = {android.provider.MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(myUri,filePathColumn,null,null,null);
        if(cursor == null ){
            stringPath = myUri.getPath();

        }else{
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            stringPath = cursor.getString(columnIndex);
            cursor.close();
        }
        return stringPath;
    }

    public boolean checkReadStorageAllowed() {
        if(Build.VERSION.SDK_INT < 23 || ContextCompat.checkSelfPermission(MainActivity.this, "android.permission.WRITE_EXTERNAL_STORAGE") == 0) {
            return true;
        }
        return false;

    }



}
