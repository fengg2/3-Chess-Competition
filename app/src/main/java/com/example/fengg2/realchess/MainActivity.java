package com.example.fengg2.realchess;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private Intent intent;
    private Button login;
    private EditText user;
    private EditText phone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uidesign);
        intent = new Intent(this, UIdesign.class);
        login = (Button) findViewById(R.id.gameStart);
        user = (EditText) findViewById(R.id.user);
        phone = (EditText) findViewById(R.id.phone);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = user.getText().toString();
                String num = phone.getText().toString();
                if (num == null || num.trim().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "phone number cannot be empty.", Toast.LENGTH_SHORT).show();
                    return;
                } else if (name == null || name.trim().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "username cannot be empty.", Toast.LENGTH_SHORT).show();
                    return;
                } else if (num.trim().length() != 11 || !num.startsWith("1")) {
                    Toast.makeText(getApplicationContext(), "phone number is wrong.", Toast.LENGTH_SHORT).show();
                    return;
                }
                startActivity(intent);
            }
        });
    }

}
