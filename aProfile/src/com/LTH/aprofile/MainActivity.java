package com.LTH.aprofile;
 
import java.util.ArrayList;
import java.util.HashMap;    
import java.util.List;    
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;    
import android.content.Intent;     
import android.content.IntentFilter;    
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;    
import android.os.Bundle;      
import android.view.View;    
import android.view.View.OnClickListener;     
import android.widget.Button;    
import android.widget.ListView;    
import android.widget.SimpleAdapter;    
import android.widget.TextView;    
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener
 {      
    WifiManager wifi;       
    ListView lv;
    TextView textStatus;
    Button buttonScan;
    int size = 0;
    List<ScanResult> results;

    String ITEM_KEY = "key";
    ArrayList<HashMap<String, String>> arraylist = new ArrayList<HashMap<String, String>>();
    SimpleAdapter adapter;
    
    SoundMeter soundMeter;


    /* Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textStatus = (TextView) findViewById(R.id.text_status);
        buttonScan = (Button) findViewById(R.id.button_scan);
        buttonScan.setOnClickListener(this);
        lv = (ListView)findViewById(R.id.listView);

        wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        if (wifi.isWifiEnabled() == false)
        {
            Toast.makeText(getApplicationContext(), "wifi is disabled..making it enabled", Toast.LENGTH_LONG).show();
            wifi.setWifiEnabled(true);
        }   
        int resource = android.R.layout.simple_list_item_1;
        this.adapter = new SimpleAdapter(MainActivity.this, arraylist, android.R.layout.simple_list_item_1, new String[] { ITEM_KEY }, new int[] { android.R.id.text1 });
        lv.setAdapter(this.adapter);

        registerReceiver(new BroadcastReceiver()
        {
            @Override
            public void onReceive(Context c, Intent intent) 
            {
               results = wifi.getScanResults();
               size = results.size();
            }
        }, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));  
        
        //kebab-lista
        /*
        for (int i = 0; i < 1000; i++) {
        	HashMap<String, String> item = new HashMap<String, String>();                       
            item.put(ITEM_KEY, "KEBAB");
        	arraylist.add(item);
            size--;
            adapter.notifyDataSetChanged();
            
        }
        */
        
        //sound Meter
        //soundMeter = new SoundMeter((TextView) findViewById(R.id.text_status), (ListView) findViewById(R.id.listView), this);
        //soundMeter.start();
    }

    public void onClick(View view) 
    //this is a test message
    {
        arraylist.clear();          
        wifi.startScan();

        Toast.makeText(this, "Scanning...." + size, Toast.LENGTH_SHORT).show();

        
        try 
        {
            size = size - 1;
            while (size >= 0) 
            {   
            	//Toast.makeText(this, "found " + results.get(size).SSID,  Toast.LENGTH_SHORT).show();
               HashMap<String, String> item = new HashMap<String, String>();                       
                item.put(ITEM_KEY, results.get(size).SSID + "  " + results.get(size).BSSID);

                arraylist.add(item);
                size--;
                adapter.notifyDataSetChanged();
            } 
            //Lägg till en kommentar för att se om det fungerar. /ERIK
        }
        catch (Exception e)
        { }         
    }    
}
