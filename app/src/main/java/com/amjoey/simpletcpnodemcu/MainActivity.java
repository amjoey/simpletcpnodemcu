package com.amjoey.simpletcpnodemcu;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import simpletcp.*;

public class MainActivity extends AppCompatActivity {

    public static final int TCP_PORT = 21111;

    private SimpleTcpServer simpleTcpServer;

    private ToggleButton tButton;
    private TextView textViewIpAddress;
    private EditText editTextMessage, editTextIpAddress;
    private Button buttonSend;
    private Button buttonRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tButton = (ToggleButton) findViewById(R.id.toggleButton1);
        textViewIpAddress = findViewById(R.id.textViewIpAddress);
        editTextMessage = findViewById(R.id.editTextMessage);
        editTextIpAddress = findViewById(R.id.editTextIpAddress);
        buttonSend = findViewById(R.id.buttonSend);
        buttonRefresh = findViewById(R.id.buttonRefresh);

        SimpleTcpClient.send("UPDATE\r\n", "192.168.1.42", TCP_PORT, new SimpleTcpClient.SendCallback() {
            public void onUpdated(String tag) {
                Toast.makeText(getApplicationContext(), tag , Toast.LENGTH_SHORT).show();
                String[] arr_state = tag.split(",");
                if(arr_state[1].equals("1")){
                    tButton.setChecked(true);

                }else if(arr_state[1].equals("0")){
                    tButton.setChecked(false);

                }
            }
            public void onSuccess(String tag) {
                Toast.makeText(getApplicationContext(), "onSuccess", Toast.LENGTH_SHORT).show();
            }
            public void onFailed(String tag) {
                Toast.makeText(getApplicationContext(), "onFailed", Toast.LENGTH_SHORT).show();
            }
        }, "TAG");

      //  tButton.setChecked(false);

        TcpUtils.forceInputIp(editTextIpAddress);

        textViewIpAddress.setText(TcpUtils.getIpAddress(this) + ":" + TCP_PORT);

        tButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // Send message and waiting for callback
                    SimpleTcpClient.send("LEDON\r\n", "192.168.1.42", TCP_PORT, new SimpleTcpClient.SendCallback() {
                        public void onUpdated(String tag) {
                            Toast.makeText(getApplicationContext(), "onUpdated", Toast.LENGTH_SHORT).show();
                        }
                        public void onSuccess(String tag) {
                            Toast.makeText(getApplicationContext(), "onSuccess", Toast.LENGTH_SHORT).show();
                        }
                        public void onFailed(String tag) {
                            Toast.makeText(getApplicationContext(), "onFailed", Toast.LENGTH_SHORT).show();
                        }
                    }, "TAG");
                } else {
                    // Send message and waiting for callback
                    SimpleTcpClient.send("LEDOFF\r\n", "192.168.1.42", TCP_PORT, new SimpleTcpClient.SendCallback() {
                        public void onUpdated(String tag) {
                            Toast.makeText(getApplicationContext(), "onUpdated", Toast.LENGTH_SHORT).show();
                        }
                        public void onSuccess(String tag) {
                            Toast.makeText(getApplicationContext(), "onSuccess", Toast.LENGTH_SHORT).show();
                        }
                        public void onFailed(String tag) {
                            Toast.makeText(getApplicationContext(), "onFailed", Toast.LENGTH_SHORT).show();
                        }
                    }, "TAG");
                }
            }
        });

        buttonSend.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (editTextMessage.getText().length() > 0) {
                    String message = editTextMessage.getText().toString();
                    String ip = editTextIpAddress.getText().toString();

                    // Send message without callback
                    //SimpleTcpClient.send(message, ip, TCP_PORT);

                    // Send message and waiting for callback
                    SimpleTcpClient.send(message+"\r\n", ip, TCP_PORT, new SimpleTcpClient.SendCallback() {
                        public void onUpdated(String tag) {
                            Toast.makeText(getApplicationContext(), "onUpdated", Toast.LENGTH_SHORT).show();
                        }
                        public void onSuccess(String tag) {
							Toast.makeText(getApplicationContext(), "onSuccess", Toast.LENGTH_SHORT).show();
						}
						public void onFailed(String tag) {
							Toast.makeText(getApplicationContext(), "onFailed", Toast.LENGTH_SHORT).show();
						}
				}, "TAG");
                }
            }
        });

        buttonRefresh.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //String ip = editTextIP.getText().toString();
                //SimpleTcpClient.send("UPDATE\r\n", "192.168.1.42", TCP_PORT);
                SimpleTcpClient.send("UPDATE\r\n", "192.168.1.42", TCP_PORT, new SimpleTcpClient.SendCallback() {
                    public void onUpdated(String tag) {
                        Toast.makeText(getApplicationContext(), tag , Toast.LENGTH_SHORT).show();
                        String[] arr_state = tag.split(",");
                        if(arr_state[1].equals("1")){
                            tButton.setChecked(true);

                        }else if(arr_state[1].equals("0")){
                            tButton.setChecked(false);

                        }
                    }
                    public void onSuccess(String tag) {
                        Toast.makeText(getApplicationContext(), "onSuccess", Toast.LENGTH_SHORT).show();
                    }
                    public void onFailed(String tag) {
                        Toast.makeText(getApplicationContext(), "onFailed", Toast.LENGTH_SHORT).show();
                    }
                }, "TAG");
            }
        });

        simpleTcpServer = new SimpleTcpServer(TCP_PORT);
        simpleTcpServer.setOnDataReceivedListener(new SimpleTcpServer.OnDataReceivedListener() {
            public void onDataReceived(String message, String ip) {

                if(message.equals("1")){
                    tButton.setChecked(true);

                }else if(message.equals("0")){
                    tButton.setChecked(false);

                }
                Toast.makeText(getApplicationContext(), "Updated", Toast.LENGTH_SHORT).show();

            }
        });

    }
    public void onResume() {
        super.onResume();
        simpleTcpServer.start();
    }

    public void onStop() {
        super.onStop();
        simpleTcpServer.stop();
    }
}
