package com.rhythmpro.rhythmpro;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by jianglei660 on 2017/7/5.
 */

public class baseActivity extends AppCompatActivity {

    private static final int REQUEST_PERMISSION = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PackageManager pkg = getPackageManager();

        boolean bluetoothPermission = pkg.checkPermission(Manifest.permission.BLUETOOTH, getPackageName())
                == PackageManager.PERMISSION_GRANTED;
        boolean bluetoothAdminPermission = pkg.checkPermission(Manifest.permission.BLUETOOTH_ADMIN, getPackageName())
                == PackageManager.PERMISSION_GRANTED;

        if ((Build.VERSION.SDK_INT >= 23) && (!bluetoothPermission || !bluetoothAdminPermission)) {
            requestPermission();
        }

    }

    public void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN},
                REQUEST_PERMISSION);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_PERMISSION) {

        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
