package com.example.androidcrud;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.androidcrud.config.konfigurasi;
import com.example.androidcrud.modul.RequestHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    ListView listView;

    public String JSON_STRING;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView)findViewById(R.id.listView);

        getSiswaAll();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent i = new Intent(MainActivity.this, FormActivity.class);
                    HashMap<String, String> map = (HashMap)parent.getItemAtPosition(position);
                    String siswaid = map.get(konfigurasi.KEY_ID).toString();
                    i.putExtra(konfigurasi.KEY_ID,siswaid);
                    startActivity(i);
            }
        });

    }

    public void getSiswaAll(){
        class GetSiswaAll extends AsyncTask<Void, Void, String>{
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(MainActivity.this, "Memuat", "Mohon tunggu..", false, false);
            }

            @Override
            protected String doInBackground(Void... voids) {
                RequestHandler rh = new RequestHandler();
                String res = rh.sendGetRequest(konfigurasi.URL_SHOWALL);

                return res;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                JSON_STRING = s;
                System.out.println(s);
                tampilSiswaAll();
            }
        }

        GetSiswaAll gs = new GetSiswaAll();
        gs.execute();
    }

    public void tampilSiswaAll(){
        JSONObject jsonObject = null;
        ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
        try{
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result = jsonObject.getJSONArray(konfigurasi.TAG_JSON_ARRAY);
            for (int i=0;i<result.length();i++){
                JSONObject c = result.getJSONObject(i);
                String id = c.getString(konfigurasi.KEY_ID);
                String nama = c.getString(konfigurasi.KEY_NAMA);

                HashMap<String, String> siswa = new HashMap<>();
                siswa.put(konfigurasi.KEY_ID, id);
                siswa.put(konfigurasi.KEY_NAMA,nama);
                list.add(siswa);
            }

        }catch (JSONException e){
            e.printStackTrace();
        }

        ListAdapter adapter = new SimpleAdapter(MainActivity.this, list, R.layout.list_row,
                new String[]{konfigurasi.KEY_ID, konfigurasi.KEY_NAMA},
                new int[]{R.id.txtId,R.id.txtNama});

        listView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menudash, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId){
            case R.id.menuAdd:
                Intent i = new Intent(MainActivity.this, FormActivity.class);
                startActivity(i);
                break;
        }


        return super.onOptionsItemSelected(item);
    }
}
