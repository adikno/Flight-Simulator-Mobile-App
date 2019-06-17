package com.example.ex4;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class JoystickActivity extends AppCompatActivity {
    Socket socket;
    private Joystick joystick;
    private PrintWriter printWriter;
    private String elevator;
    private String aileron;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joystick);
        joystick = findViewById(R.id.joystick);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String ip = getIntent().getSerializableExtra("ip").toString();
                    String port = getIntent().getSerializableExtra("port").toString();
                    socket = new Socket(ip, Integer.parseInt(port));

                } catch (Exception e) {
                    System.out.println(e);
                    Log.e("TCP", "C: Error", e);
                }
            }
        });
        thread.start();


        joystick.setOnJoystickMoveListener(new Joystick.OnJoystickMoveListener() {

            @Override
            public void onValueChanged(int angle, int direction) {
                // TODO Auto-generated method stub
                elevator = "set /controls/flight/elevator ";
                aileron = "set /controls/flight/aileron ";
                double ail;
                double ele;
                try {
                    printWriter = new PrintWriter(socket.getOutputStream(),true);
                    switch (direction) {
                        case Joystick.FRONT:
                            elevator = elevator + 1;
                            aileron =  aileron + 0;
                            break;
                        case Joystick.FRONT_RIGHT:
                            ele = (double)(90 - angle) / 90.0;
                            elevator = elevator + ele;
                            ail = (double)angle / 90.0;
                            aileron = aileron + ail;
                            break;
                        case Joystick.RIGHT:
                            elevator = elevator + "0";
                            aileron =  aileron + "1";
                            break;
                        case Joystick.RIGHT_BOTTOM:
                            ele = -((double)(angle - 90) / 90.0);
                            elevator = elevator + ele;
                            ail = (double)(180 - angle) / 90.0;
                            aileron = aileron + ail;
                            break;
                        case Joystick.BOTTOM:
                            elevator = elevator + "-1";
                            aileron =  aileron + "0";
                            break;
                        case Joystick.BOTTOM_LEFT:
                            ele = (double)(90 + angle) / 90.0;
                            elevator = elevator + ele;
                            ail = -1.0 * ((double)(180 + angle) / 90.0);
                            aileron = aileron + ail;
                            break;
                        case Joystick.LEFT:
                            elevator = elevator + "0";
                            aileron =  aileron + "-1";
                            break;
                        case Joystick.LEFT_FRONT:
                            ele = (double)(angle + 90) / 90.0;
                            elevator = elevator + ele;
                            ail = (double)angle / 90.0;
                            aileron = aileron + ail;
                            break;
                        default:
                            break;
                    }
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                if (printWriter != null) {
                                    printWriter.write(elevator);
                                    printWriter.flush();
                                    printWriter.write(aileron);
                                    printWriter.flush();
                                }

                            } catch (Exception e) {
                                Log.e("TCP", "C: Error", e);
                            }
                        }
                    });
                    thread.start();
                } catch (Exception e) {
                    Log.e("TCP", "S: Error", e);
                }
            }

        });
    }
    @Override
    protected void onDestroy(){
            super.onDestroy();
            if(socket != null) {
                printWriter.close();
                try {
                    socket.close();
                } catch (Exception e ) {
                    Log.e("TCP", "C: Error", e);
                }
            }
            finish();
        }
}
