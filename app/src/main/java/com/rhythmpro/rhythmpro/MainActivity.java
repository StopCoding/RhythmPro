package com.rhythmpro.rhythmpro;

import android.bluetooth.BluetoothAdapter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends baseActivity {

    private Button button;
    private BluetoothAdapter adapter;

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

            if (adapter.isDiscovering()) {
                adapter.cancelDiscovery();
            }
            adapter.startDiscovery();

        } else {
            Toast.makeText(this, "蓝牙未连接", Toast.LENGTH_SHORT).show();
        }
    }
}
