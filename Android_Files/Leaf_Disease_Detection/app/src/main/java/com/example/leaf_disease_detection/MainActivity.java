package com.example.leaf_disease_detection;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.renderscript.ScriptGroup;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.datavec.image.loader.NativeImageLoader;
import org.tensorflow.lite.Interpreter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;



//Dataset --> keras --> .h5
//data --> farmers-image(Validation set) -    (Trianed_model).h5 -->disease_name+remedy



public class MainActivity extends AppCompatActivity {
    private static int RESULT_LOAD_IMAGE = 1;

    Button btnSelect;
    Button btnSelect2;
    ImageView img;
    private int REQUEST_STORAGE = 111;
    private int REQUEST_FILE = 222;
    private Uri uri;
    private String stringPath;
    private Intent iData;
    Interpreter tflite;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btnSelect = (Button) findViewById(R.id.btnSelect);

        Button detectButton = (Button) findViewById(R.id.btnSelect2);
        TextView res = (TextView)findViewById(R.id.result_text);
        img = findViewById(R.id.imgs);

        tflite = new Interpreter(loadModelFile(activity));




        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }

        btnSelect.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                Intent i = new Intent(Intent.ACTION_PICK);
                i.setType("image/*");
                i.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(i,"Select Picture"),RESULT_LOAD_IMAGE);

            }
        });

        try {
            tflite = new Interpreter(loadModelFile());
        } catch(Exception e){
            e.printStackTrace();
        }
        detectButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                float prediction = inference(ScriptGroup.Input.getText().toString());
                res.setText(Float.toString(prediction));

            }
        });


    }

    public NativeImageLoader(long height, long width, long channels) {
        this.height = height;
        this.width = width;
        this.channels = channels;
    }
    public float inference(String s) {
        int height = 224;
        int width = 224;
        int channels = 3;

        String absolutePath;
        File f = new File(absolutePath,img);
        NativeImageLoader loader = new NativeImageLoader(height, width, channels);

        float [] inputValue = new float[1];
        inputValue[0] = Float.valueOf(s);

        float[][] outputValue = new float[1][1];
        tflite.run(inputValue,outputValue);
        float inferenceValue = outputValue[0][0];
        return inferenceValue;
    }






    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null){
                uri = data.getData();

                try{
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
                    img.setImageBitmap(bitmap);
            }catch (FileNotFoundException e){
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
        }
    }
}

    private MappedByteBuffer loadModelFile(Activity activity) throws IOException {
        AssetFileDescriptor fileDescriptor = activity.getAssets().openFd(getModelPath());


        FileInputStream inputStream = new  FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }

    private String getModelPath() {

        return "model_inception.tflite";
    }


}
