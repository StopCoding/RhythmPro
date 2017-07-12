package com.rhythmpro.rhythmpro;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class MainActivity extends baseActivity {

    private Button button;
    private BluetoothAdapter adapter;
    private ListView deveiceListView;
    //private ArrayAdapter listAdapter;
    private DeviceAdapter listAdapter;
    private List<String> deviceNames;
    private List<BluetoothDevice> deviceList;

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                String deviceName = (device.getName() != null)? device.getName(): "";
                String deviceAddress = device.getAddress();
                if (Build.VERSION.SDK_INT >= 18) {
                    int type = device.getType();
                }
                BluetoothClass deviceClass = device.getBluetoothClass();
                int major = deviceClass.getDeviceClass();
                int manor = deviceClass.getMajorDeviceClass();
                int hasCode = device.hashCode();

                boolean hasContain = false;
                for (String name: deviceNames) {
                    if (name.equals(deviceName+"--"+deviceAddress)) {
                        hasContain = true;
                        break;
                    }
                }
                if (!hasContain) {
                    listAdapter.add(deviceName+"--"+deviceAddress);
                }
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

        deviceNames = new ArrayList<String>();
        deveiceListView = (ListView) findViewById(R.id.deviceList);

        listAdapter = new DeviceAdapter(this, R.layout.devicelist, deviceNames);
        deveiceListView.setAdapter(listAdapter);

        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(receiver, filter);
    }

    public void onButtonClick() {
        adapter = BluetoothAdapter.getDefaultAdapter();
        boolean bDiscovery = adapter.isDiscovering();
//
//        deviceNames.clear();
//        deviceList.clear();

        Toast.makeText(this, "isDiscovery: "+ String.valueOf(bDiscovery), Toast.LENGTH_SHORT).show();
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


//                try {
//
//                    BluetoothSocket socket = device.createInsecureRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
//
//                    while(!socket.isConnected()) {
//                        socket.connect();
//
//                    }
//                    boolean connected = socket.isConnected();
//
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
            }

            adapter.startDiscovery();


//            if (Build.VERSION.SDK_INT >= 21) {
//                final BluetoothLeScanner scanner = adapter.getBluetoothLeScanner();
//                scanner.startScan(new ScanCallback() {
//                    @Override
//                    public void onScanResult(int callbackType, ScanResult result) {
//                        super.onScanResult(callbackType, result);
//
//                        BluetoothDevice device = result.getDevice();
//                        if (device != null) {
//                            String deviceName = device.getName();
//                            String deviceAddress = device.getAddress();
//                            int type = device.getType();
//                            BluetoothClass deviceClass = device.getBluetoothClass();
//                            int major = deviceClass.getDeviceClass();
//                            int manor = deviceClass.getMajorDeviceClass();
//                            int hasCode = device.hashCode();
//
//                            if (deviceName!=null && deviceName.contains("Pebble")) {
//                                ParcelUuid[] uuids = device.getUuids();
//                                UUID uuid = uuids[0].getUuid();
//                                try {
//                                    BluetoothServerSocket socket = adapter.listenUsingRfcommWithServiceRecord(deviceName, uuid);
//                                }catch (IOException e) {
//
//                                }
//                            }
//                        }
//                    }
//
//                    @Override
//                    public void onBatchScanResults(List<ScanResult> results) {
//                        super.onBatchScanResults(results);
//
//                        for (int i=0; i<results.size(); i++) {
//                            BluetoothDevice device = results.get(i).getDevice();
//                            if (device != null) {
//                                String deviceName = device.getName();
//                                String deviceAddress = device.getAddress();
//                                int type = device.getType();
//                            }
//                        }
//                    }
//
//                    @Override
//                    public void onScanFailed(int errorCode) {
//                        super.onScanFailed(errorCode);
//                        Toast.makeText(null, "扫描失败", Toast.LENGTH_SHORT).show();
//                    }
//                });
//            } else {
//
//            }

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


    class DeviceAdapter extends ArrayAdapter {

        private int resourceId;
        private Context context;
        private List dataSource;

        public DeviceAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List objects) {
            super(context, resource, objects);
            this.resourceId = resource;
            this.context = context;
            this.dataSource = objects;
        }

        public DeviceAdapter(@NonNull Context context, @LayoutRes int resource) {
            super(context, resource);
            this.resourceId = resource;
            this.context = context;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            String deviceName = getItem(position).toString();

            LayoutInflater layout = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layout.inflate(this.resourceId, null);

//            LayoutInflater layout = getLayoutInflater();
//            View view = layout.inflate(this.resourceId, null);

            final TextView deviceNameView = (TextView) view.findViewById(R.id.deviceName);
            TextView deviceAddressView = (TextView) view.findViewById(R.id.deviceAddress);

            deviceNameView.setText(deviceName);
            deviceNameView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context, "toast", Toast.LENGTH_SHORT).show();
                    adapter.cancelDiscovery();

                    String address = ((TextView) view).getText().toString().split("--")[1];
                    BluetoothDevice device = adapter.getRemoteDevice(address);

                    try {

                        BluetoothSocket socket = device.createInsecureRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));

                        if (socket.isConnected()) {
                            OutputStream output = socket.getOutputStream();
                        } else {
                            socket.connect();
                            boolean connected = socket.isConnected();
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                }
            });


            return  view;
        }
    }
}
