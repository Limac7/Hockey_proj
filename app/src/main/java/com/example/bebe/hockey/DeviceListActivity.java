package com.example.bebe.hockey;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import java.util.Set;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class DeviceListActivity extends AppCompatActivity {


    private static final String TAG = "DeviceListActivity";


    TextView tvConnect;

    public static String EXTRA_DEVICE_ADDRESS = "device_address";


    private BluetoothAdapter BA;
    private ArrayAdapter<String> mPairedDevicesArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_list);
    }

    @Override
    public void onResume()
    {
        super.onResume();

        checkBTState();

        tvConnect = (TextView) findViewById(R.id.connecting);
        tvConnect.setTextSize(50);
        tvConnect.setText(" ");


        mPairedDevicesArrayAdapter = new ArrayAdapter<>(this, R.layout.ime);

        ListView pairedListView = (ListView) findViewById(R.id.paired_devices);

        pairedListView.setAdapter(mPairedDevicesArrayAdapter);

        pairedListView.setOnItemClickListener(mDeviceClickListener);

        BA = BluetoothAdapter.getDefaultAdapter();

        Set<BluetoothDevice> pairedDevices = BA.getBondedDevices();

        if (pairedDevices.size() > 0) {
            findViewById(R.id.name_paired_devices).setVisibility(View.VISIBLE);
            for (BluetoothDevice device : pairedDevices) {
                mPairedDevicesArrayAdapter.add(device.getName() + "\n" + device.getAddress());
            }
        } else {
            mPairedDevicesArrayAdapter.add("No connected devices!");
        }
    }

    private OnItemClickListener mDeviceClickListener = new OnItemClickListener() {
        public void onItemClick(AdapterView<?> av, View v, int arg2, long arg3) {

            tvConnect.setText("Connecting...");
            String info = ((TextView) v).getText().toString();
            String address = info.substring(info.length() - 17);


            Intent myintent = new Intent(DeviceListActivity.this, MainActivity.class);
            myintent.putExtra(EXTRA_DEVICE_ADDRESS, address);
            startActivity(myintent);
        }
    };

    private void checkBTState() {

        BA =BluetoothAdapter.getDefaultAdapter();
        if(BA ==null) {
            Toast.makeText(getApplicationContext(), "Bluetooth not supported! Buy new phone please!", Toast.LENGTH_SHORT).show();
        } else {
            if (BA.isEnabled()) {
                Log.d(TAG, "...Bluetooth ON...");
            } else {

                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, 1);

            }
        }
    }
}
