package com.omnisoft.chat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.*;
import java.net.Socket;

public class ChatActivity extends AppCompatActivity {

    private TextView message_view;
    private EditText sendBox;
    private Button send,exit;

    private Socket clnt_socket;

    public String clnt_msg, send_buff,resp_string,user_name, address, port_str;
    int port_num;

    PrintWriter out;
    BufferedReader inb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        //message <- received message from server;
        //sendBox -> Message to be sent to server;
        message_view = (TextView)findViewById(R.id.message);
        sendBox = (EditText)findViewById(R.id.sendbox);
        //buttons
        send = (Button)findViewById(R.id.send);
        exit = (Button)findViewById(R.id.exit);

        //receiving values from previous activity;
        user_name = getIntent().getExtras().getString("user_name");
        address = getIntent().getExtras().getString("address");
        port_str = getIntent().getExtras().getString("port");
        port_num = Integer.valueOf(port_str);

        try{
            clnt_socket = new Socket(address, port_num);
            out = new PrintWriter(clnt_socket.getOutputStream());
            inb = new BufferedReader(new InputStreamReader(clnt_socket.getInputStream()));

        }catch(Exception e){
            Toast.makeText(ChatActivity.this, "Could not create socket", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

        //send click
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clnt_msg = sendBox.getText().toString();

                if(clnt_msg != null && clnt_msg.trim().length() > 0){
                    send_buff = user_name+": "+clnt_msg;

                    try{
                        if((clnt_socket.isConnected())){
                            out.printf("%s", send_buff);
                            out.flush();

                            if((resp_string = inb.readLine()) != null){
                                Log.d("resp_string: ", resp_string);
                            }
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    Log.d("resp_str_out_of_catch", resp_string);
                    message_view.setText(resp_string);
                    sendBox.setText("");
                }else{
                    Toast.makeText(ChatActivity.this, "Message is empty", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //exit click
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try{
                    inb.close();
                    out.close();
                    clnt_socket.close();
                    Intent mainActivity = new Intent(ChatActivity.this, MainActivity.class);
                    startActivity(mainActivity);

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }
}
