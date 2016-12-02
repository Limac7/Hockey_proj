package com.example.bebe.hockey;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    Button btnOn, btnOff;
    TextView  txtString, txtStringLength, xgyro, ygyro, zgyro, xaccel, yaccel,zaccel;
    Handler bluetoothIn;
    MyDBHandler myDb;
    double x_gyro;
    double y_gyro;
    double z_gyro ;
    double x_accel;
    double y_accel;
    double z_accel;

    final int handlerState = 0;
    private BluetoothAdapter btAdapter = null;
    private BluetoothSocket btSocket = null;
    private StringBuilder recDataString = new StringBuilder();

    private ConnectedThread mConnectedThread;


    private static final UUID BTMODULEUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");



    private static String address;  // za MAC adresu

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        myDb = new MyDBHandler(this,null,null, 1);


        btnOn = (Button) findViewById(R.id.buttonOn);
        btnOff = (Button) findViewById(R.id.buttonOff);
        txtString = (TextView) findViewById(R.id.txtString);
        txtStringLength = (TextView) findViewById(R.id.View1);
        xgyro = (TextView) findViewById(R.id.xgyro);
        ygyro = (TextView) findViewById(R.id.ygyro);
        zgyro= (TextView) findViewById(R.id.zgyro);
        xaccel = (TextView) findViewById(R.id.xaccel);
        yaccel = (TextView) findViewById(R.id.yaccel);
        zaccel = (TextView) findViewById(R.id.zaccel);

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

                            SetPodataka setPodataka = new SetPodataka(x_gyro,y_gyro,z_gyro,x_accel,y_accel,z_accel);
                            myDb.addData(setPodataka);




                           /* String x_gyro = readings[0];
                            String y_gyro = readings[1];
                            String z_gyro= readings[2];
                            String x_accel = readings[3];
                            String y_accel = readings[4];
                            String z_accel = readings[5];


                            xgyro.setText(" Ziroskop X = " + x_gyro );
                            ygyro.setText(" Ziroskop Y  = " + y_gyro );
                            zgyro.setText(" Ziroskop Z  = " + z_gyro );
                            xaccel.setText(" Akcelerometar X  = " + x_accel );
                            yaccel.setText(" Akcelerometar Y = " + y_accel  );
                            zaccel.setText(" Akcelerometar Z = " + z_accel  );*/



                        }
                        recDataString.delete(0, recDataString.length());
                    }
                }
            }
        };

        btAdapter = BluetoothAdapter.getDefaultAdapter();
        checkBTState();


        btnOff.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                mConnectedThread.write("0");
                Toast.makeText(getBaseContext(), "Zaustavljam", Toast.LENGTH_SHORT).show();
            }
        });

        btnOn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                mConnectedThread.write("1");
                Toast.makeText(getBaseContext(), "Pokrecem", Toast.LENGTH_SHORT).show();
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

        address = intent.getStringExtra(DeviceListActivity.EXTRA_DEVICE_ADDRESS);

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
            }
        }
        mConnectedThread = new ConnectedThread(btSocket);
        mConnectedThread.start();

        //provjera povezanosti

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
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, 1);
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
            } catch (IOException e) { }

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
                    break;
                }
            }
        }
        void write(String input) {
            byte[] msgBuffer = input.getBytes();
            try {
                mmOutStream.write(msgBuffer);
            } catch (IOException e) {
                Toast.makeText(getBaseContext(), "Greska u povezivanju", Toast.LENGTH_LONG).show();
                finish();

            }
        }
    }
}