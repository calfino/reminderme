package com.example.reminderme;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothHeadset;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class HomeActivity extends AppCompatActivity {
    private static final String TAG = "HomeActivity";
    private static final int REQUEST_ENABLE_BT = 0;
    private static final int REQUEST_DISCOVER_BT = 2;
    TextView statusTextView, pairedDevicesView;
    ImageView imageView;
    Button turnOnBtn, turnOffBtn, getDiscoverableBtn, getPairedBtn;
    BluetoothAdapter bluetoothAdapter;
    ListView lvNewDevices;
    public List<BluetoothDevice> BTDevices;
    public ArrayAdapter<String> deviceListAdapter;
    ArrayList<String> bluetoothDevices = new ArrayList<>();
    ArrayList<String> addresses = new ArrayList<>();
    private BroadcastReceiver BroadcastReceiver1 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            Log.d(TAG, "onReceive: ACTION FOUND.");
            if (action.equals(BluetoothDevice.ACTION_FOUND)){
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                BTDevices.add(device);
                Log.i("list", BTDevices.toString());
                String name = device.getName();
                String address = device.getAddress();
                String rssi = Integer.toString(intent.getShortExtra(BluetoothDevice.EXTRA_RSSI, Short.MIN_VALUE));
//                Log.d(TAG, "onReceive: " + name + ": " + address+ "RSSI :" + rssi);
                deviceListAdapter = new ArrayAdapter<String>(context,android.R.layout.simple_list_item_1,bluetoothDevices);
                lvNewDevices.setAdapter(deviceListAdapter);

                if(!addresses.contains(address)){
                    addresses.add(address);
                    String deviceString = "";
                    if(name==null || name.equals("")){
                        deviceString = address + " - RSSI " + rssi + "dBm";
                    }else{
                        deviceString = name + " - RSSI " + rssi + "dBm";
                    }

                    bluetoothDevices.add(deviceString);
                    deviceListAdapter.notifyDataSetChanged();
                }





            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        statusTextView = findViewById(R.id.statusTextView);
        turnOffBtn = findViewById(R.id.turnOff);
        turnOnBtn = findViewById(R.id.turnOn);
        getDiscoverableBtn = findViewById(R.id.discoverableDevices);
        getPairedBtn = findViewById(R.id.pairedDevices);
        pairedDevicesView = findViewById(R.id.pairedDevicesView);
        imageView= findViewById(R.id.imageView);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        BTDevices= new ArrayList<>();
        lvNewDevices = (ListView) findViewById(R.id.lvNewDevices);

        if (bluetoothAdapter == null){
            statusTextView.setText("Bluetooth is not Available");
        }else{
            statusTextView.setText("Bluetooth is Available");
        }

        if (bluetoothAdapter.isEnabled()){
            imageView.setImageResource(R.drawable.reminderme_logo);
        }
        else {
            imageView.setImageResource(R.drawable.ic_action_name);
        }

        turnOnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!bluetoothAdapter.isEnabled()) {

                    showToast("Turning On Bluetooth. . .");
                    Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(intent,REQUEST_ENABLE_BT);
                }else{
                    showToast("Bluetooth is already on");
                }
            }
        });

        turnOffBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bluetoothAdapter.isEnabled()){
                    bluetoothAdapter.disable();
                    showToast("Disabling Bluetooth Connection!");
                    imageView.setImageResource(R.drawable.ic_action_name);
                }
                else{
                    showToast("Bluetooth is already off");
                }
            }
        });


        getDiscoverableBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!bluetoothAdapter.isDiscovering()){
                    showToast("Making Your Device Discoverable");
                    Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                    startActivityForResult(intent, REQUEST_DISCOVER_BT);
                }
            }
        });

        getPairedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, ListActivity.class);
                startActivity(intent);
//
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch(requestCode){
            case REQUEST_ENABLE_BT:
                if(resultCode == RESULT_OK){
                    //bluetooth is on
                    imageView.setImageResource(R.drawable.reminderme_logo);
                    showToast("bluetooth is on");
                }else{
                    showToast("couldn't on bluetooth");
                }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void showToast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT ).show();
    }
    public void scanDevicesOnClick(View view) {
        showToast("Scanning for devices");
        if(bluetoothAdapter.isDiscovering()){
            bluetoothAdapter.cancelDiscovery();
            showToast("Cancel Scanning");
            if(Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                bluetoothAdapter.startDiscovery();
                IntentFilter discoverDevicesIntent = new IntentFilter(BluetoothDevice.ACTION_FOUND);
                registerReceiver(BroadcastReceiver1, discoverDevicesIntent);
            }
        }
        if(!bluetoothAdapter.isDiscovering()){
            if(Build.VERSION.SDK_INT>Build.VERSION_CODES.LOLLIPOP){
                bluetoothAdapter.startDiscovery();
                IntentFilter discoverDevicesIntent = new IntentFilter(BluetoothDevice.ACTION_FOUND);
                registerReceiver(BroadcastReceiver1, discoverDevicesIntent);
            }
        }
    }


}
