package com.example.reminderme;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class scanDevice extends AppCompatActivity {


    private static final String TAG = "scandevice";
    BluetoothAdapter bluetoothAdapter;
    public List<BluetoothDevice> BTDevices;
    public ArrayAdapter<String> deviceListAdapter;
    ArrayList<String> bluetoothDevices = new ArrayList<>();
    ArrayList<String> addresses = new ArrayList<>();
    ListView lvNewDevices;


    private BroadcastReceiver BroadcastReceiver2 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action= intent.getAction();
            if(action.equals(BluetoothDevice.ACTION_BOND_STATE_CHANGED)){
                BluetoothDevice device1 = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                if(device1.getBondState()==BluetoothDevice.BOND_BONDED){
                    showToast("Bond is Formed");
                }else if(device1.getBondState()==BluetoothDevice.BOND_BONDING){
                    showToast("Connecting to device");
                }else{
                    showToast("Bond is break");
                }
            }
        }
    };

    private BroadcastReceiver BroadcastReceiver1 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            Log.d("intent.getAction", intent.getAction());
            Log.d(TAG, "onReceive: ACTION FOUND.");
            if (action.equals(BluetoothDevice.ACTION_FOUND)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                Log.d("intent.getAction", intent.getAction());
                Log.d("Bluetooth Devices", device.toString());
                BTDevices.add(device);
                Log.i("list", BTDevices.toString());
                String name = device.getName();
                String address = device.getAddress();
                String rssi = Integer.toString(intent.getShortExtra(BluetoothDevice.EXTRA_RSSI, Short.MIN_VALUE));
                Log.d(TAG, "onReceive: " + name + ": " + address+ "RSSI :" + rssi);
                Log.d("test", context.toString());
                deviceListAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, bluetoothDevices);
                lvNewDevices.setAdapter(deviceListAdapter);

                if (!addresses.contains(address)) {
                    addresses.add(address);
                    String deviceString = "";
                    if (name == null || name.equals("")) {
                        deviceString = address + " - RSSI " + rssi + "dBm";
                    } else {
                        deviceString = name + " - RSSI " + rssi + "dBm";
                    }
                    Log.d("test", deviceString);
                    bluetoothDevices.add(deviceString);
                    deviceListAdapter.notifyDataSetChanged();
                }


            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scandevice);

        BTDevices = new ArrayList<>();
        lvNewDevices = (ListView) findViewById(R.id.lvNewDevices);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        showToast("Scanning for devices");
        if(bluetoothAdapter.isDiscovering()){
            bluetoothAdapter.cancelDiscovery();
            showToast("Cancel Scanning");
            if(Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                bluetoothAdapter.startDiscovery();
                IntentFilter discoverDevicesIntent = new IntentFilter(BluetoothDevice.ACTION_FOUND);
                registerReceiver(BroadcastReceiver1, discoverDevicesIntent);
                IntentFilter checkBondState = new IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
                registerReceiver(BroadcastReceiver2, checkBondState);
            }
        }
        if(!bluetoothAdapter.isDiscovering()){
            if(Build.VERSION.SDK_INT>Build.VERSION_CODES.LOLLIPOP){
                bluetoothAdapter.startDiscovery();
                IntentFilter discoverDevicesIntent = new IntentFilter(BluetoothDevice.ACTION_FOUND);
                registerReceiver(BroadcastReceiver1, discoverDevicesIntent);
                IntentFilter checkBondState = new IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
                registerReceiver(BroadcastReceiver2, checkBondState);
            }
        }




        lvNewDevices.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


                bluetoothAdapter.cancelDiscovery();
                Log.d(TAG,"You clicked on a device!");
                String deviceName = BTDevices.get(i).getName();
                String deviceAddress = BTDevices.get(i).getAddress();

                Log.d(TAG,"onItemClicked: device Name " + deviceName);
                Log.d(TAG,"onItemClicked: device Address " + deviceAddress);
                BTDevices.get(i).createBond();
            }
        });

    }

    public void showToast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_LONG ).show();
    }

    @Override
    protected void onDestroy() {
        Log.v("scanDevice", "onDestroy");
        unregisterReceiver(BroadcastReceiver1);
        unregisterReceiver(BroadcastReceiver2);
        super.onDestroy();
    }


}
