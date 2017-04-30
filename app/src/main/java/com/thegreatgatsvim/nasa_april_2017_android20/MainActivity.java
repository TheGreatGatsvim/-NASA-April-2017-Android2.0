package com.thegreatgatsvim.nasa_april_2017_android20;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;

import com.thegreatgatsvim.nasa_april_2017_android20.adapter.LazyAdapter;
import com.thegreatgatsvim.nasa_april_2017_android20.models.Recycle;
import com.thegreatgatsvim.nasa_april_2017_android20.util.UtilService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    UtilService service;
    public static LazyAdapter ADAPTER;
    private ListView listView;
    private List<Recycle> listRecycle;
    MainActivity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.listViewRecycle);
        activity = this;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.10.11.56:8000")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(UtilService.class);

        onCall();
    }

    private void onCall() {
        Call<List<Recycle>> call = service.getPictures();
        call.enqueue(new Callback<List<Recycle>>() {
            @Override
            public void onResponse(Call<List<Recycle>> call, Response<List<Recycle>> response) {
                int statusCode = response.code();
                System.out.println(statusCode);
                System.out.println("*********");

                listRecycle = response.body();

                ADAPTER = new LazyAdapter(activity,R.layout.activity_recycle_adapter, listRecycle);
                listView.setAdapter(ADAPTER);
            }

            @Override
            public void onFailure(Call<List<Recycle>> call, Throwable t) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.about:
                Intent myIntent = new Intent(MainActivity.this, InfoActivity.class);
                //myIntent.putExtra("key", value); //Optional parameters
                MainActivity.this.startActivity(myIntent);
                return true;

            case R.id.refresh:
                final ProgressDialog progress = new ProgressDialog(this);
                progress.setTitle("Connecting");
                progress.setMessage("Please wait while we refresh...");
                progress.show();

                Runnable progressRunnable = new Runnable() {

                    @Override
                    public void run() {
                        onCall();
                        progress.cancel();
                    }
                };

                Handler pdCanceller = new Handler();
                pdCanceller.postDelayed(progressRunnable, 3000);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
