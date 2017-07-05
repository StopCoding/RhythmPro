package com.rhythmpro.rhythmpro;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

public class MainActivity extends baseActivity {

    private Button button;
    private BluetoothAdapter adapter;
    private ArrayList mArrayAdapter;

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                mArrayAdapter.add(device.getName() + "\n" + device.getAddress());
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onButtonClick();
            }
        });
    }

    public void onButtonClick() {
        adapter = BluetoothAdapter.getDefaultAdapter();
        if (adapter.isEnabled()) {
            Toast.makeText(this, "蓝牙已打开", Toast.LENGTH_SHORT).show();
            String name = adapter.getName();
            String address = adapter.getAddress();

            Set<BluetoothDevice> devices = adapter.getBondedDevices();
            Iterator<BluetoothDevice> iterator = devices.iterator();

            while (iterator.hasNext()) {
                BluetoothDevice device = iterator.next();

                String deviceName = device.getName();
                String deviceAddress = device.getAddress();
                int deviceType = device.getType();
            }

            adapter.startDiscovery();

            IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            registerReceiver(receiver, filter);
        } else {
            Toast.makeText(this, "蓝牙未连接", Toast.LENGTH_SHORT).show();
            //adapter.enable();
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent, 0x1);
        }
    }

    public void registerFoundAction() {

    }

    @Override
    protected void onDestroy() {



        super.onDestroy();
    }
}
