package com.example.fujak.viciouscircle;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.bluetooth.*;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

public class Multiplayer extends Activity implements AdapterView.OnItemClickListener {

    Timer timer;
    MediaPlayer mediaPlayer;

    TextView title;
    BluetoothAdapter blueAdapter;
    BluetoothChatService bs;
    ArrayAdapter<String> pairedDevicesArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
/*
        BluetoothAdapter bluetooth = BluetoothAdapter.getDefaultAdapter();
        if(bluetooth == null)
        {
            // no bluetooth on this device
        }

        String status;
        if (!bluetooth.isEnabled()) {
            //  bluetooth is Disabled on this device
        }
        else
            {
            String mydeviceaddress = bluetooth.getAddress();
            String mydevicename = bluetooth.getName();
            status = mydevicename + " : " + mydeviceaddress;
        }

        int state = bluetooth.getState();

        Set<BluetoothDevice> pairedDevices = bluetooth.getBondedDevices();

        if (pairedDevices.size() > 0) {
            // There are paired devices. Get the name and address of each paired device.
            for (BluetoothDevice device : pairedDevices) {
                String deviceName = device.getName();
                String deviceHardwareAddress = device.getAddress(); // MAC address
            }
        }

*/

        setContentView(R.layout.activity_multiplayer);
        title = (TextView) findViewById(R.id.title1);
        title.setText("Getting paired devices...");
        ListView lv = (ListView)findViewById(R.id.listView1);

        List<String> paired_list = new ArrayList<String>();

        blueAdapter = BluetoothAdapter.getDefaultAdapter();
        Set<BluetoothDevice> pairedDevices = blueAdapter.getBondedDevices();
        for (BluetoothDevice device : pairedDevices) {
            Log.d("cancer", "kencr");
            paired_list.add(device.getName() + " - " + device.getAddress());
        }

        pairedDevicesArrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,paired_list);
        lv.setAdapter(pairedDevicesArrayAdapter);

        lv.setOnItemClickListener(this);
        title.setText("Found " + paired_list.size() + " devices:");

        MyHandler h = new MyHandler(this);
        bs = new BluetoothChatService(this,h);
        bs.start();

        //setContentView(R.layout.activity_main);
        //finish();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        blueAdapter.cancelDiscovery();
        String info = ((TextView) view).getText().toString();
        String address = info.substring(info.length() - 17);

        bs.start();
        BluetoothDevice device = blueAdapter.getRemoteDevice(address);
        bs.connect(device,true);

        String message = "Ahoj";
        byte[] send = message.getBytes();
        bs.write(send);
    }
    class MyHandler extends Handler{
        String connectedDevice;
        Multiplayer activity;
        MyHandler(Multiplayer a){
            activity = a;
        }
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what)
            {
                case Constants.MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
                        case BluetoothChatService.STATE_CONNECTED:
                            activity.title.setText("Someone Connected!");
                            pairedDevicesArrayAdapter.clear();
                            break;
                        case BluetoothChatService.STATE_CONNECTING:
                            activity.title.setText("Connecting...");
                            break;
                        case BluetoothChatService.STATE_LISTEN:
                            activity.title.setText("Listening");
                            break;
                        case BluetoothChatService.STATE_NONE:
                            activity.title.setText("Not connected");
                            break;
                    }
                    break;


                case Constants.MESSAGE_WRITE:
                {
                    byte[] writeBuf = (byte[]) msg.obj;
                    // construct a string from the buffer
                    String writeMessage = new String(writeBuf);
                    pairedDevicesArrayAdapter.add("Me:  " + writeMessage);

                    break;
                }
                case Constants.MESSAGE_READ:
                {
                    byte[] readBuf = (byte[]) msg.obj;
                    // construct a string from the valid bytes in the buffer
                    String readMessage = new String(readBuf, 0, msg.arg1);
                    pairedDevicesArrayAdapter.add("Somebody:  " + readMessage);
                    break;
                }
                case Constants.MESSAGE_DEVICE_NAME: {
                    // save the connected device's name
                    connectedDevice = msg.getData().getString(Constants.DEVICE_NAME);
                    if (null != activity) {
                        Toast.makeText(activity, "Connected to "
                                + connectedDevice, Toast.LENGTH_SHORT).show();
                        break;
                    }
                }
                case Constants.MESSAGE_TOAST: {
                    if (null != activity) {
                        Toast.makeText(activity, msg.getData().getString(Constants.TOAST),
                                Toast.LENGTH_SHORT).show();
                    }
                    break;
                }
            }
        }
    }

}
