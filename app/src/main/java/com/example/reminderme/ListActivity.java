package com.example.reminderme;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ListActivity extends AppCompatActivity {

    ListView listDevices;
    TextView pairedDevicesView ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        listDevices = findViewById(R.id.listDevices);
        pairedDevicesView = findViewById(R.id.pairedDevicesView);
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(bluetoothAdapter.isEnabled()){
                    pairedDevicesView.setText("Paired Devices");
                    Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();

                    List<String> s = new ArrayList<String>();
                    for(BluetoothDevice device : pairedDevices){
                        s.add(device.getName());
                    }

            ArrayAdapter<String> itemAdapter= new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,s);
                    ListView listView = (ListView) findViewById(R.id.listDevices);
                    listView.setAdapter(itemAdapter);

                }else{
                    showToast("Turn on bluetooth to get paired devices");
                }
    }

    private void showToast(String str){
        Toast.makeText(this, str,  Toast.LENGTH_SHORT);
    }
}
