package com.example.reminderme;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Handler;
import android.os.ParcelUuid;
import android.os.Parcelable;
import android.os.VibrationEffect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.os.Vibrator;

import java.io.IOException;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class scanDevice extends AppCompatActivity {

    final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
    private static final String TAG = "scandevice";
    BluetoothAdapter bluetoothAdapter;
    public List<BluetoothDevice> BTDevices;
    public ArrayAdapter<String> deviceListAdapter;
    ArrayList<String> bluetoothDevices = new ArrayList<>();
    ArrayList<String> addresses = new ArrayList<>();
    ListView lvNewDevices;
    Parcelable[] uuidExtra;

//    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            String action = intent.getAction();
//            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
//
//            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
//           showToast("device found!");//Device found
//            }
//            else if (BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)) {
//           showToast("Device is connected"); //Device is now connected
//            }
//            else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
//           showToast("done searching");//Done searching
//            }
//            else if (BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED.equals(action)) {
//           showToast("device is about to be disconnect"); //Device is about to disconnect
//            }
//            else if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)) {
//           showToast("device has disconnected"); //Device has disconnected
//            }
//        }
//    };

//    private BroadcastReceiver BroadcastReceiver2 = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            String action = intent.getAction();
//            if (action.equals(BluetoothDevice.ACTION_BOND_STATE_CHANGED)) {
//                BluetoothDevice device1 = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
//                uuidExtra = intent.getParcelableArrayExtra(BluetoothDevice.EXTRA_UUID);
//                log.i(TAG, "UUID = " + uuidExtra);
//                if (device1.getBondState() == BluetoothDevice.BOND_BONDED) {
//                    showToast("Bond is Formed");
//                } else if (device1.getBondState() == BluetoothDevice.BOND_BONDING) {
//                    showToast("Connecting to device");
//                } else {
//                    showToast("Bond is break");
//                }
//            }
//        }
//    };


    private BroadcastReceiver BroadcastReceiver1 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
//            Log.i("intent.getAction", intent.getAction());
//            Log.i(TAG, "onReceive: ACTION FOUND.");
            if (action.equals(BluetoothDevice.ACTION_FOUND)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
//                uuidExtra = intent.getParcelableArrayExtra(BluetoothDevice.EXTRA_UUID);
//                Log.i("intent.getAction", intent.getAction());
//                Log.i("Bluetooth Devices", device.toString());



                if (device.getName()!=null) {
                    String name = device.getName();
                    if(name.contains("I7") || name.contains("S530") || name.contains("FLY")){
//                    Log.i("devices name: ", name);
                        String address = device.getAddress();
                        String rssi = Integer.toString(intent.getShortExtra(BluetoothDevice.EXTRA_RSSI, Short.MIN_VALUE));
                        int rssivalue = intent.getShortExtra(BluetoothDevice.EXTRA_RSSI, Short.MIN_VALUE);
                    Log.i(TAG, "onReceive: " + name + ": " + address + "RSSI :" + rssi);
                    Log.i("test", context.toString());
                        deviceListAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, bluetoothDevices);
                        lvNewDevices.setAdapter(deviceListAdapter);
                        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                        if (rssivalue < -70) {
                            showToast("Detected Devices:" + device.getName());
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                BTDevices.add(device);
                                Log.i("isi dari BTDevices: ",  BTDevices.toString());
                                v.vibrate(VibrationEffect.createOneShot(2500, VibrationEffect.DEFAULT_AMPLITUDE));
                                Intent intents = new Intent(scanDevice.this, HomeActivity.class);
                                intents.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intents);
                                finish();
                            } else {
                                BTDevices.add(device);
                                Log.i("isi dari BTDevices: ",  BTDevices.toString());
                                //deprecated in API 26
                                v.vibrate(2500);
                                Intent intents = new Intent(scanDevice.this, HomeActivity.class);
                                intents.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intents);
                                finish();
                            }
                        } else if (rssivalue > -70) {

//                            Intent intents = new Intent(scanDevice.this, scanDevice.class);
//                            intents.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                            startActivity(intents);

                            if (!addresses.contains(address)) {
                                addresses.add(address);
                                String deviceString = "";
                                deviceString = name + " - RSSI " + rssi + "dBm";
                                Log.i("test", deviceString);
                                BTDevices.add(device);
                                Log.i("isi dari BTDevices: ",  BTDevices.toString());
                                bluetoothDevices.add(deviceString);
                                deviceListAdapter.notifyDataSetChanged();
                            } else{
                                bluetoothDevices.clear();
                                addresses.clear();
                                deviceListAdapter.notifyDataSetChanged();
                                BTDevices.clear();
                            }
                        } else {
                            Log.i("Ada device lain", "Nearby device other than chip");
                        }


                    }else{
                        showToast("No Registered Bluetooth Chip In Range!");
                    }
                }else{

                }

            } else {
                showToast("No Device Found!");
                Log.i("error", "device not found");
            }


        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scandevice);
        BTDevices = new ArrayList<>();
        lvNewDevices = (ListView) findViewById(R.id.lvNewDevices);
        showToast("Scanning for devices");

        executorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                bluetoothAdapter.cancelDiscovery();
                bluetoothAdapter.startDiscovery();

                IntentFilter discoverDevicesIntent = new IntentFilter(BluetoothDevice.ACTION_FOUND);
                registerReceiver(BroadcastReceiver1, discoverDevicesIntent);
            }
        }, 0, 8, TimeUnit.SECONDS);





//        IntentFilter checkBondState = new IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
//        registerReceiver(BroadcastReceiver2, checkBondState);
//        IntentFilter filter = new IntentFilter();
//        filter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
//        filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED);
//        filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
//        this.registerReceiver(mReceiver, filter);
//        IntentFilter discoverDevicesIntents = new IntentFilter(BluetoothDevice.ACTION_FOUND);
//        bluetoothAdapter.startDiscovery();
//        registerReceiver(BroadcastReceiver1, discoverDevicesIntents);
//        if(bluetoothAdapter.isDiscovering()){
//            bluetoothAdapter.cancelDiscovery();
//            showToast("Cancel Scanning");
//                bluetoothAdapter.startDiscovery();
//                IntentFilter discoverDevicesIntent = new IntentFilter(BluetoothDevice.ACTION_FOUND);
//                registerReceiver(BroadcastReceiver1, discoverDevicesIntent);
//                IntentFilter checkBondState = new IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
//                registerReceiver(BroadcastReceiver2, checkBondState);
//
//        }
//        if(!bluetoothAdapter.isDiscovering()){
//
//                bluetoothAdapter.startDiscovery();
//                IntentFilter discoverDevicesIntent = new IntentFilter(BluetoothDevice.ACTION_FOUND);
//                registerReceiver(BroadcastReceiver1, discoverDevicesIntent);
//                IntentFilter checkBondState = new IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
//                registerReceiver(BroadcastReceiver2, checkBondState);
//
//        }


        lvNewDevices.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


                bluetoothAdapter.cancelDiscovery();
                Log.i(TAG, "You clicked on a device!");
                Log.i(TAG, bluetoothDevices.get(i));
                String deviceName = BTDevices.get(i).getName();
                String deviceAddress = BTDevices.get(i).getAddress();

                Log.i(TAG, "onItemClicked: device Name " + deviceName);
                Log.i(TAG, "onItemClicked: device Address " + deviceAddress);
//                BTDevices.get(i).createBond();
            }
        });

    }

    public void showToast(String msg) {
        try {
            Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
        }catch(Exception e){
            System.out.println("Error: " + e);
        }
    }

    @Override
    protected void onDestroy() {
        Log.v("scanDevice", "onDestroy");
        unregisterReceiver(BroadcastReceiver1);
        executorService.shutdown();
//        unregisterReceiver(BroadcastReceiver2);
//        unregisterReceiver(mReceiver);
        super.onDestroy();
    }


}
