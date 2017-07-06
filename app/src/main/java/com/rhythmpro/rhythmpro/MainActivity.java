package com.rhythmpro.rhythmpro;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelUuid;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;

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


        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(receiver, filter);
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

            //adapter.startDiscovery();
            if (Build.VERSION.SDK_INT >= 21) {
                final BluetoothLeScanner scanner = adapter.getBluetoothLeScanner();
                scanner.startScan(new ScanCallback() {
                    @Override
                    public void onScanResult(int callbackType, ScanResult result) {
                        super.onScanResult(callbackType, result);

                        BluetoothDevice device = result.getDevice();
                        if (device != null) {
                            String deviceName = device.getName();
                            String deviceAddress = device.getAddress();
                            int type = device.getType();
                            BluetoothClass deviceClass = device.getBluetoothClass();
                            int major = deviceClass.getDeviceClass();
                            int manor = deviceClass.getMajorDeviceClass();
                            int hasCode = device.hashCode();

                            if (deviceName!=null && deviceName.contains("Pebble")) {
                                ParcelUuid[] uuids = device.getUuids();
                                UUID uuid = uuids[0].getUuid();
                                try {
                                    BluetoothServerSocket socket = adapter.listenUsingRfcommWithServiceRecord(deviceName, uuid);
                                }catch (IOException e) {

                                }
                            }
                        }
                    }

                    @Override
                    public void onBatchScanResults(List<ScanResult> results) {
                        super.onBatchScanResults(results);

                        for (int i=0; i<results.size(); i++) {
                            BluetoothDevice device = results.get(i).getDevice();
                            if (device != null) {
                                String deviceName = device.getName();
                                String deviceAddress = device.getAddress();
                                int type = device.getType();
                            }
                        }
                    }

                    @Override
                    public void onScanFailed(int errorCode) {
                        super.onScanFailed(errorCode);
                        Toast.makeText(null, "扫描失败", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {

            }

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
