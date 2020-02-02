package com.example.androidcrud;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.androidcrud.config.konfigurasi;
import com.example.androidcrud.modul.RequestHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class FormActivity extends AppCompatActivity implements View.OnClickListener{

    EditText txtNama, txtKelas, txtJurusan;
    Button btnInput,btnHapus;

    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        txtNama = (EditText)findViewById(R.id.txtNama);
        txtKelas = (EditText)findViewById(R.id.txtKelas);
        txtJurusan = (EditText)findViewById(R.id.txtJurusan);
        btnInput = (Button)findViewById(R.id.btnInput);
        btnHapus = (Button)findViewById(R.id.btnHapus);
        btnInput.setOnClickListener(this);
        btnHapus.setOnClickListener(this);

        btnHapus.setVisibility(View.GONE);

        Bundle bundle = getIntent().getExtras();

        if(bundle != null){
            id = bundle.getString("id");

            if(!id.equals("")){
                btnInput.setText("Ubah");
                btnHapus.setVisibility(View.VISIBLE);
                getSiswa();
            }
        }

    }

    public void tambahSiswa(){
        final String nama = txtNama.getText().toString().trim();
        final String kelas = txtKelas.getText().toString().trim();
        final String jurusan = txtJurusan.getText().toString().trim();

        class TambahSiswa extends AsyncTask<Void, Void, String>{

            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(FormActivity.this, "Menambahkan Data","Mohon tunggu...",false,false);
            }

            @Override
            protected String doInBackground(Void... voids) {
                HashMap<String, String> params = new HashMap<>();
                params.put(konfigurasi.KEY_NAMA, nama);
                params.put(konfigurasi.KEY_KELAS, kelas);
                params.put(konfigurasi.KEY_JURUSAN, jurusan);

                RequestHandler rh = new RequestHandler();
                String res = rh.sendPostRequest(konfigurasi.URL_INSERT, params);

                return res;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(FormActivity.this, s, Toast.LENGTH_SHORT).show();
            }
        }

        TambahSiswa ts = new TambahSiswa();
        ts.execute();
    }

    public void getSiswa(){
        class GetSiswa extends AsyncTask<Void, Void, String>{
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(FormActivity.this, "Memuat", "Sedang mengambil..", false,false);
            }

            @Override
            protected String doInBackground(Void... voids) {
                RequestHandler rh = new RequestHandler();
                String res = rh.sendGetRequestParam(konfigurasi.URL_SHOWONE, id);
                return res;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                tampilSiswa(s);
            }
        }

        GetSiswa gs = new GetSiswa();
        gs.execute();

    }

    public void tampilSiswa(String json){
        try{
            JSONObject jsonObject = new JSONObject(json);
            JSONArray result = jsonObject.getJSONArray(konfigurasi.TAG_JSON_ARRAY);

            JSONObject c = result.getJSONObject(0);
            txtNama.setText(c.getString(konfigurasi.KEY_NAMA));
            txtKelas.setText(c.getString(konfigurasi.KEY_KELAS));
            txtJurusan.setText(c.getString(konfigurasi.KEY_JURUSAN));

        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    public void ubahSiswa(){
        final String nama = txtNama.getText().toString().trim();
        final String kelas = txtKelas.getText().toString().trim();
        final String jurusan = txtJurusan.getText().toString().trim();

        class UbahSiswa extends AsyncTask<Void, Void, String>{
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(FormActivity.this, "Mengubah","Mohon Tunggu..",false,false);
            }

            @Override
            protected String doInBackground(Void... voids) {
                HashMap<String, String> params = new HashMap<>();
                params.put(konfigurasi.KEY_ID, id);
                params.put(konfigurasi.KEY_NAMA, nama);
                params.put(konfigurasi.KEY_KELAS, kelas);
                params.put(konfigurasi.KEY_JURUSAN, jurusan);

                RequestHandler rh = new RequestHandler();
                String res = rh.sendPostRequest(konfigurasi.URL_UPDATE, params);

                return res;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(FormActivity.this, s, Toast.LENGTH_SHORT).show();
            }
        }

        UbahSiswa us = new UbahSiswa();
        us.execute();
    }

    public void hapusSiswa(){
        class HapusSiswa extends AsyncTask<Void, Void, String>{
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(FormActivity.this, "Menghapus", "Mohon tunggu..", false, false);
            }

            @Override
            protected String doInBackground(Void... voids) {
                RequestHandler rh = new RequestHandler();
                String res = rh.sendGetRequestParam(konfigurasi.URL_DELETE, id);
                return res;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(FormActivity.this, s, Toast.LENGTH_SHORT).show();
            }
        }

        HapusSiswa hs = new HapusSiswa();
        hs.execute();
    }

    @Override
    public void onClick(View v) {
        if(v == btnInput){
            String txtbutton = btnInput.getText().toString().trim();
            if(txtbutton.equals("Tambah")){
                tambahSiswa();
            }else if(txtbutton.equals("Ubah")){
                ubahSiswa();
            }
        }else if(v == btnHapus){
            hapusSiswa();
        }
    }

}
