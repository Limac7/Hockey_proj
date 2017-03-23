package com.example.bebe.hockey;

import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    Button btnOn, btnOff, btnAllShots, btnBestShot, btnTips;
    TextView  txtString, txtStringLength, textView3, textView2;
    ImageView image;
    Handler bluetoothIn;
    MyDBHandler myDb;
    double x_gyro,y_gyro,z_gyro,x_accel,y_accel,z_accel,force,spin,acc;
    final int handlerState = 0;
    private BluetoothAdapter btAdapter = null;
    private BluetoothSocket btSocket = null;
    private StringBuilder recDataString = new StringBuilder();
    private ConnectedThread mConnectedThread;
    private static final UUID BTMODULEUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private static final double GFORCE = 9.81;
    private static final double WEIGHT = 0.2;
    public int tempID;
    final Context context = this;




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        myDb = new MyDBHandler(this,null,null, 1);

        btnOn = (Button) findViewById(R.id.buttonOn);
        btnOff = (Button) findViewById(R.id.buttonOff);
        btnAllShots = (Button) findViewById(R.id.buttonAllShots);
        btnBestShot = (Button) findViewById(R.id.buttonBestShot);
        btnTips = (Button) findViewById(R.id.buttonTips);

        txtString = (TextView) findViewById(R.id.txtString);
        txtStringLength = (TextView) findViewById(R.id.View1);


        btAdapter = BluetoothAdapter.getDefaultAdapter();
        checkBTState();


        btnOff.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                mConnectedThread.write("0");
                stopHandler();
                Toast.makeText(getApplicationContext(), "Zaustavljam", Toast.LENGTH_SHORT).show();
            }
        });

        btnOn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                mConnectedThread.write("1");
                startHandler();
                tempID++;
                Toast.makeText(getApplicationContext(), "Pokrecem", Toast.LENGTH_SHORT).show();
            }
        });

        btnAllShots.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {



                mConnectedThread.cancel();
                Intent i = new Intent(MainActivity.this, AllShotsActivity.class);
                startActivity(i);





            }
        });



        btnBestShot.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mConnectedThread.cancel();



                    }
                });

        btnTips.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {


                    // custom dialog
                    final Dialog dialog = new Dialog(context);
                    dialog.setContentView(R.layout.activity_tips);
                    dialog.setTitle("Title...");

                    // set the custom dialog components - text, image and button
                     textView3 = (TextView) dialog.findViewById(R.id.textView3);
                     textView2 = (TextView) dialog.findViewById(R.id.textView2);
                     image = (ImageView) dialog.findViewById(R.id.imageView2);

                    dialog.show();



            }
        });
    }

    private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {

        return  device.createRfcommSocketToServiceRecord(BTMODULEUUID);
    }

    @Override
    public void onResume() {
        super.onResume();

        Intent intent = getIntent();

        String address = intent.getStringExtra(DeviceListActivity.EXTRA_DEVICE_ADDRESS);

        BluetoothDevice device = btAdapter.getRemoteDevice(address);

        try {
            btSocket = createBluetoothSocket(device);
        } catch (IOException e) {
            Toast.makeText(getBaseContext(), "Socket Error", Toast.LENGTH_LONG).show();
        }
        try
        {
            btSocket.connect();
        } catch (IOException e) {
            try
            {
                btSocket.close();
            } catch (IOException e2)
            {
                Toast.makeText(getBaseContext(),"Closing socket,error occured.",Toast.LENGTH_LONG).show();
            }
        }
        mConnectedThread = new ConnectedThread(btSocket);

        mConnectedThread.start();

        mConnectedThread.write("a");
    }

    @Override
    public void onPause()
    {
        super.onPause();
        try
        {
            btSocket.close();
        } catch (IOException e2) {
        }
    }


    private void checkBTState() {

        if(btAdapter==null) {
            Toast.makeText(getBaseContext(), "Bluetooth not supported! Buy new phone please!", Toast.LENGTH_LONG).show();
        } else {
            if (btAdapter.isEnabled()) {
            } else {
                Intent turnOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(turnOn, 1);
            }
        }
    }

    private class ConnectedThread extends Thread {
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;


         ConnectedThread(BluetoothSocket socket) {
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
                Toast.makeText(MainActivity.this, "Problem getting Input and Output streams", Toast.LENGTH_SHORT).show();
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            byte[] buffer = new byte[256];
            int bytes;


            while (true) {
                try {
                    bytes = mmInStream.read(buffer);
                    String readMessage = new String(buffer, 0, bytes);
                    bluetoothIn.obtainMessage(handlerState, bytes, -1, readMessage).sendToTarget();
                } catch (IOException e) {
                    Toast.makeText(MainActivity.this, "Problem receiving data", Toast.LENGTH_SHORT).show();
                    break;
                }
            }
        }
        void write(String input) {
            byte[] msgBuffer = input.getBytes();
            try {
                mmOutStream.write(msgBuffer);
            } catch (IOException e) {
                Toast.makeText(getBaseContext(), "Message transmission failed.", Toast.LENGTH_LONG).show();
                finish();

            }
        }
        public void cancel(){
            try {
                btSocket.close();
            }
                    catch (IOException e)
            {
                Toast.makeText(MainActivity.this, "Problem with closing socket", Toast.LENGTH_SHORT).show();

            }
        }
    }
    private void startHandler() {

        bluetoothIn = new Handler() {
            public void handleMessage(Message msg) {
                if (msg.what == handlerState) {
                    String readMessage = (String) msg.obj;
                    recDataString.append(readMessage);
                    int endOfLineIndex = recDataString.indexOf("*");
                    if (endOfLineIndex > 0) {
                        String dataInPrint = recDataString.substring(0, endOfLineIndex);
                        txtString.setText("Primljeni podatci = " + dataInPrint);
                        int dataLength = dataInPrint.length();
                        txtStringLength.setText("Duzina Poruke = " + String.valueOf(dataLength));

                        if (recDataString.charAt(0) == '#')
                        {
                            String[] readings = recDataString.toString().split("\\+");

                            readings[0] = readings[0].substring(1);

                            x_gyro = Double.parseDouble(readings[0]);
                            y_gyro = Double.parseDouble(readings[1]);
                            z_gyro = Double.parseDouble(readings[2]);
                            x_accel = Double.parseDouble(readings[3]);
                            y_accel = Double.parseDouble(readings[4]);
                            z_accel = Double.parseDouble(readings[5]);
                            acc = Math.sqrt((x_accel * x_accel) + (y_accel * y_accel) + (z_accel * z_accel)); //  2g
                            force =acc*GFORCE*WEIGHT;
                            spin = Math.sqrt((x_gyro * x_gyro) + (y_gyro * y_gyro) + (z_gyro * z_gyro)); //  250°/s
                            if ((acc>2) || (acc<-2)) {
                                SetPodataka setPodataka = new SetPodataka(x_gyro, y_gyro, z_gyro, x_accel, y_accel, z_accel, force, spin, acc, tempID);
                                boolean isAdded = myDb.addData(setPodataka);
                                if (isAdded == true)
                                {
                                    Toast.makeText(MainActivity.this,"Data is added",Toast.LENGTH_LONG).show();
                                }
                                else
                                {
                                    Toast.makeText(MainActivity.this,"Data is not added",Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                        recDataString.delete(0, recDataString.length());
                    }
                }
            }
        };
    }
    public void stopHandler() {
        bluetoothIn.removeMessages(0);
    }

}