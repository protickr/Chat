package com.omnisoft.chat;

import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.net.Socket;


public class MainActivity extends AppCompatActivity {

    EditText ip, port, username;
    Button connect;
    String ip_address, user_name, port_str;
    int port_num = -1;
    public static Socket clnt_socket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ip = (EditText)findViewById(R.id.ip);
        port = (EditText)findViewById(R.id.port);
        username = (EditText)findViewById(R.id.username);
        connect = (Button)findViewById(R.id.connect);

        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //user thread and handler, the following is not the best practice,
                //but for this task it is more than appropriate, synchronous connection

                int SDK_INT = android.os.Build.VERSION.SDK_INT;
                if (SDK_INT>8){
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                }

                ip_address = ip.getText().toString();
                port_str = port.getText().toString();
                user_name = username.getText().toString();

                if(port_str.trim().length() > 0){
                    port_num = Integer.valueOf(port.getText().toString());
                }else{
                    port_num = -1;
                }

                //if connect button is clicked;
                if((ip_address != null && ip_address.trim().length() > 0)
                        && (port_num >= 0)
                        && (user_name != null && user_name.trim().length() > 0)
                        ) {
                    try{

                        clnt_socket = new Socket(ip_address, port_num);
                        if(clnt_socket.isConnected()){
                            Toast.makeText(MainActivity.this, "Connected", Toast.LENGTH_LONG);

                            Intent chat = new Intent(MainActivity.this, ChatActivity.class);

                            chat.putExtra("user_name", user_name);
                            chat.putExtra("address", ip_address);
                            chat.putExtra("port", port_str);
                            clnt_socket.close();
                            startActivity(chat);
                        }
                    }catch (Exception e){
                        Toast.makeText(MainActivity.this, "Could not create socket", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }else{
                    Toast.makeText(MainActivity.this, "[*_*] Empty Fields +/- In valid port", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

}
