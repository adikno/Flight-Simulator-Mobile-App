package com.example.ex4;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void connect(View view){
        EditText ip = (EditText)findViewById(R.id.ip);
        EditText port = (EditText)findViewById(R.id.port);
        Intent intent = new Intent(this, JoystickActivity.class);
        intent.putExtra("ip",ip.getText().toString());
        intent.putExtra("port",port.getText().toString());
        startActivity(intent);

    }
}
