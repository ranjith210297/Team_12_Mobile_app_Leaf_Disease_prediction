package com.example.loginform;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText eName;
    private EditText ePhoneNumber;
    private Button eLogin;
    private TextView eAttemptsInfo;

    private String Username = "Naveen";
    private String PhoneNumber = "9704653534";

    boolean isValid = false;
    private int counter = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        eName = findViewById(R.id.etName);
        ePhoneNumber = findViewById(R.id.etPhoneNumber);
        eLogin = findViewById(R.id.button);
        eAttemptsInfo = findViewById(R.id.tvAttemptsInfo);

        eLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String inputName = eName.getText().toString();
                String inputPassword = ePhoneNumber.getText().toString();

                if(inputName.isEmpty() || inputPassword.isEmpty()) {

                    Toast.makeText(MainActivity.this, "Please Enter all the Details Correctly!", Toast.LENGTH_SHORT).show();
                } else {

                    isValid = validate(inputName, inputPassword);

                    if(!isValid) {

                        counter--;

                        Toast.makeText(MainActivity.this, "Incorrect Credentials Entered!", Toast.LENGTH_SHORT).show();

                        eAttemptsInfo.setText("No of attempts remaining:  "  + counter);

                        if(counter == 0) {
                            eLogin.setEnabled(false);
                        }

                    } else {
                        Toast.makeText(MainActivity.this, "Registration successful", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(MainActivity.this, HomePageActivity.class);
                        startActivity(intent);
                    }




                }
            }
        });
    }


    private boolean validate(String name, String password) {

        if(name.equals(Username) && password.equals(PhoneNumber)) {
            return true;
        }

        return false;
    }
}