package com.example.fixxxer.sockettest;

import android.content.Context;
import android.net.DhcpInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Arrays;
import java.util.Enumeration;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new sendUdp().execute();

            }
        });
    }

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
    private void sendDiscoveryRequest(DatagramSocket socket) throws IOException {

    }


    private void listenForResponses(DatagramSocket socket) throws IOException {

    }
    static class sendUdp extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            DatagramSocket socket = null;
            try {
                socket = new DatagramSocket(7432);
                byte[] msg = new byte[]{0,2,9,0,4};
                Log.d("sending", "Sending data " + Arrays.toString(msg));

                DatagramPacket packet = new DatagramPacket(msg, msg.length,
                        InetAddress.getByName("255.255.255.255"), 7432);
                socket.send(packet);
            } catch (SocketException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            byte[] buf = new byte[1024];
            try {
                while (true) {
                    DatagramPacket packet = new DatagramPacket(buf, buf.length);
                    assert socket != null;
                    socket.receive(packet);
                    String s = String.valueOf(packet.getAddress());
                    Log.d("response", "Received response " + s);
                }
            } catch (SocketTimeoutException e) {
                Log.d("timeout", "Receive timed out");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
