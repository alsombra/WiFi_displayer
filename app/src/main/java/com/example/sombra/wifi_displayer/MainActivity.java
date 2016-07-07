package com.example.sombra.wifi_displayer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    Switch aSwitch;
    WifiManager wifiManager;
    TextView textView;
    Button btn;
    MyBroadCastReceiver myBroadCastReceiver = new MyBroadCastReceiver() ;
    StringBuilder sb = new StringBuilder();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        aSwitch = (Switch) findViewById(R.id.myswitch);
        textView = (TextView)findViewById(R.id.textView);
        btn=(Button)findViewById(R.id.btn);


        wifiManager = (WifiManager)getSystemService(WIFI_SERVICE);
        //register the switch for event handling
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked && !wifiManager.isWifiEnabled()) {
                    //to switch on WiFi
                    wifiManager.setWifiEnabled(true);
                }
                //to switch off WiFi
                else if (!isChecked && wifiManager.isWifiEnabled()) {
                    wifiManager.setWifiEnabled(false);
                }
            }
        });

        btn.setOnClickListener(new View.OnClickListener(){

            //melhorar verificação com o swtich do wifi
            @Override
            //On click function
            public void onClick(View view) {
                if(!wifiManager.isWifiEnabled()){
                    Toast.makeText(getApplicationContext(), "Turn On Wifi", Toast.LENGTH_SHORT).show();
                }
                else {
                wifiManager.startScan();
                textView.setText("Starting Scan...");
                }
            }
        });

        //register the broadcast receiver
        // Broacast receiver will automatically call when number of wifi connections changed
        registerReceiver(myBroadCastReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));


    }

    class MyBroadCastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            List<ScanResult> wifilist = wifiManager.getScanResults();
            sb.append("\n Number of WiFi connections:"+wifilist.size()+"\n\n");
            for(int i=0; i<wifilist.size(); i++){
                sb.append(new Integer (i+1).toString()+ ".");
                sb.append((wifilist.get(i)).toString());
                sb.append("\n\n");
            }
            textView.setText(sb);
        }
    }
}
