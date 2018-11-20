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
            DatagramSocket c;
		try {
			c = new DatagramSocket();
			c.setBroadcast(true);
			byte[] sendData = new byte[] { 0, 2, 9, 0, 4 };
			try {
				DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length,
						InetAddress.getByName("255.255.255.255"), 7432);
				c.send(sendPacket);
			} catch (Exception e) {
				e.printStackTrace();
			}
			@SuppressWarnings("rawtypes")
			Enumeration interfaces = NetworkInterface.getNetworkInterfaces();
			while (interfaces.hasMoreElements()) {
				NetworkInterface networkInterface = (NetworkInterface) interfaces.nextElement();
				if (networkInterface.isLoopback() || !networkInterface.isUp()) {
					continue;
				}
				for (InterfaceAddress interfaceAddress : networkInterface.getInterfaceAddresses()) {
					InetAddress broadcast = interfaceAddress.getBroadcast();
					if (broadcast == null) {
						continue;
					}
					try {
						DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, broadcast, 7432);
						c.send(sendPacket);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			while (true) {
				try {
					byte[] recvBuf = new byte[160];
					DatagramPacket receivePacket = new DatagramPacket(recvBuf, recvBuf.length);
					c.receive(receivePacket);
					c.setSoTimeout(5000);
					String message = new String(receivePacket.getData()).trim();
					System.out.println(receivePacket.getAddress().toString().replace("/", ""));
				} catch (SocketTimeoutException e) {
					break;
				}
			}
			c.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
            return null;
        }
    }
}
