package com.example.reminderme;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class ListActivity extends AppCompatActivity {

    ListView listDevices;
    TextView pairedDevicesView ;
    Button btnStartConnection;
    Button btnSend;
    private static final String TAG ="ListActivity";

    private static final UUID MY_UUID_INSECURE = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    BluetoothConnectionService mBluetoothConnection;

    BluetoothDevice mBTDevice;
    List<BluetoothDevice> a;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        listDevices = findViewById(R.id.listDevices);
        pairedDevicesView = findViewById(R.id.pairedDevicesView);
        btnStartConnection = findViewById(R.id.btnStartConnection);
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(bluetoothAdapter.isEnabled()){
                    pairedDevicesView.setText("Paired Devices");
                    Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
                    a = new ArrayList<>(pairedDevices);
//            String rssi = Integer.toString(intent.getShortExtra(BluetoothDevice.EXTRA_RSSI, Short.MIN_VALUE));
                    List<String> s = new ArrayList<String>();
                    for(BluetoothDevice device : pairedDevices){
                        s.add(device.getName());
//                        s.add(device.getAddress());

                    }

            ArrayAdapter<String> itemAdapter= new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,s);
                    ListView listView = (ListView) findViewById(R.id.listDevices);
                    listView.setAdapter(itemAdapter);


                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            Log.d("item clicked", "item clickedd");
                            String asd = String.valueOf(a.get(i).getBondState());
                           Log.d("bond state", asd);
                           mBluetoothConnection= new BluetoothConnectionService(ListActivity.this);
                        }
                    });
                }else{
                    showToast("Turn on bluetooth to get paired devices");
                }


        btnStartConnection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startConnection();
            }
        });
    }

    //Create method to start connection
    //App will crash if havent paired first
    public void startConnection(){
        startBTConnection(mBTDevice, MY_UUID_INSECURE);
    }

    public void startBTConnection(BluetoothDevice device, UUID uuid){
        Log.d(TAG, "Start Bluetooth Connection: Initializing RFCOM Bluetooth connection" );

        mBluetoothConnection.startClient(device, uuid);
    }


    private void showToast(String str){
        Toast.makeText(this, str,  Toast.LENGTH_SHORT);
    }


}
