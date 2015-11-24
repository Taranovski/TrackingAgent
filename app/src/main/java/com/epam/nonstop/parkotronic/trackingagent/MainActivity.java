package com.epam.nonstop.parkotronic.trackingagent;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.epam.nonstop.parkotronic.trackingagent.asynctask.BluetoothRestCallTask;
import com.epam.nonstop.parkotronic.trackingagent.asynctask.RestCallTask;
import com.epam.nonstop.parkotronic.trackingagent.dto.Dto;

public class MainActivity extends AppCompatActivity {
    private Button button;
    private EditText serverIpInputField;
    private EditText xCoordinateInputField;
    private EditText yCoordinateInputField;
    private TextView editableResult;

    private String prefix = "http://";
    private String suffixForSimpleCall = ":8080/nonstop/welcome";
    private String suffixForSpotInfoCall = ":8080/nonstop/spotInfo";

    private Button startScanButton;
    private Button endScanButton;

    private TextView discoveryResultEditableText;

    private BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
    private TextView editableDeviceId;

    private volatile boolean discoveryRunning;
    private volatile boolean broadcastReceiverRegistered;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        button = (Button) findViewById(R.id.application_action_send_state);
        serverIpInputField = (EditText) findViewById(R.id.application_input_server_ip);
        xCoordinateInputField = (EditText) findViewById(R.id.application_input_x_coordinate);
        yCoordinateInputField = (EditText) findViewById(R.id.application_input_y_coordinate);
        editableResult = (TextView) findViewById(R.id.application_text_editable_result_of_send);
        editableDeviceId = (TextView) findViewById(R.id.application_text_editable_device_id);

        button.setOnClickListener(new SimpleOnClickListener());

        startScanButton = (Button) findViewById(R.id.application_action_start_bluetooth_scan);
        endScanButton = (Button) findViewById(R.id.application_action_end_bluetooth_scan);

        discoveryResultEditableText = (TextView) findViewById(R.id.application_text_editable_result_of_discovery);


        startScanButton.setOnClickListener(new StartBluetoothScanOnClickListener());

        endScanButton.setOnClickListener(new StopBluetoothScanOnClickListener());

    }

    private final BroadcastReceiver mReceiver = new BluetoothTrackingBroadcastReceiver();

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy() {
        if (broadcastReceiverRegistered) {
            unregisterReceiver(mReceiver);
        }
        super.onDestroy();
    }

    private class SimpleOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            String serverAddress = serverIpInputField.getText().toString();
            String xCoordinate = xCoordinateInputField.getText().toString();
            String yCoordinate = yCoordinateInputField.getText().toString();
            String deviceId = editableDeviceId.getText().toString();
            String url = prefix + serverAddress + suffixForSimpleCall +
                    "?name=x:" + xCoordinate + ";y:" + yCoordinate + ";id:" + deviceId;

            AsyncTask result = new RestCallTask(editableResult).execute(url);

        }
    }

    private class StartBluetoothScanOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (!discoveryRunning && !broadcastReceiverRegistered) {
                IntentFilter filter = new IntentFilter();

                filter.addAction(BluetoothDevice.ACTION_FOUND);
                filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
                filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);

                registerReceiver(mReceiver, filter);
                broadcastReceiverRegistered = true;
                adapter.startDiscovery();
                discoveryRunning = true;
            }
        }
    }

    private class StopBluetoothScanOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (discoveryRunning) {
                adapter.cancelDiscovery();
            }
            if (broadcastReceiverRegistered) {
                unregisterReceiver(mReceiver);
            }

        }
    }

    private class BluetoothTrackingBroadcastReceiver extends BroadcastReceiver {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
                //discovery starts, we can show progress dialog or perform other tasks
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                //discovery finishes, dismis progress dialog
            } else if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                //bluetooth device found
                BluetoothDevice device = (BluetoothDevice) intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                short rssi = intent.getShortExtra(BluetoothDevice.EXTRA_RSSI, Short.MIN_VALUE);
                // Add the name and address to an array adapter to show in a ListView
                String name = device.getName();
                String address = device.getAddress();

                String xCoordinate = xCoordinateInputField.getText().toString();
                String yCoordinate = yCoordinateInputField.getText().toString();
                String deviceId = editableDeviceId.getText().toString();
                String serverAddress = serverIpInputField.getText().toString();

                String url = prefix + serverAddress + suffixForSpotInfoCall;

                AsyncTask<String, Integer, Dto> asyncTask =
                        new BluetoothRestCallTask(discoveryResultEditableText, deviceId,
                                xCoordinate, yCoordinate, name, address, rssi)
                                .execute(url);
            }
        }
    }
}
