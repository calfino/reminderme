package com.example.reminderme;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import java.util.List;

public class DeviceActivity extends AppCompatActivity {


    String[] TEST = {"test1","test2","test3"};
    String title[] = {"testboi","testboi2","tesboi3"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device);



        ListView getAllDevices = (ListView) findViewById(R.id.getAllDevices);
        DeviceAdapter ad = new DeviceAdapter(this, title);
        getAllDevices.setAdapter(ad);
    }

    class DeviceAdapter extends ArrayAdapter<String> {
        Context context;
        String title[];
        Switch aSwitch;

        DeviceAdapter (Context c, String title[]){
            super(c,R.layout.devices_layout,R.id.textView, title);
            this.context=c;
            this.title=title;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View devices_layout = layoutInflater.inflate(R.layout.devices_layout, parent, false);
            TextView myTitle= devices_layout.findViewById(R.id.textView);
            myTitle.setText(title[position]);
            return devices_layout;
        }
    }
}
