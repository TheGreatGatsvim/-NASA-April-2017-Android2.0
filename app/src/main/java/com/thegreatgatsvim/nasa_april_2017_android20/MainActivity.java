package com.thegreatgatsvim.nasa_april_2017_android20;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;

import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import android.view.View;

import android.widget.TextView;
import android.widget.Toast;

import com.thegreatgatsvim.nasa_april_2017_android20.adapter.LazyAdapter;
import com.thegreatgatsvim.nasa_april_2017_android20.models.Recycle;
import com.thegreatgatsvim.nasa_april_2017_android20.util.UtilService;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    static final int REQUEST_TAKE_PHOTO = 1;
    String mCurrentPhotoPath;
    UtilService service;
    public static LazyAdapter ADAPTER;
    private RecyclerView lv;
    private List<Recycle> listRecycle;
    MainActivity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activity = this;

        lv = (RecyclerView) findViewById(R.id.listViewRecycle);
        lv.setLayoutManager(new LinearLayoutManager(this));

        //Almacena puntuacion
        SharedPreferences mPrefs = getSharedPreferences("Points", 0);
        SharedPreferences.Editor mEditor = mPrefs.edit();
        mEditor.putString("Points", "0").commit();
        mEditor.putString("Cans", "0").commit();
        mEditor.putString("Bottles", "0").commit();

        init();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://nasa2017.jorgechato.com")
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
                listRecycle = response.body();

                ADAPTER = new LazyAdapter(activity, listRecycle);
                lv.setAdapter(ADAPTER);

                int totalScore = 0;
                for (Recycle re : listRecycle)
                    totalScore += re.getScore();

                TextView totalSC =(TextView) findViewById(R.id.totalScore);
                totalSC.setText(Integer.toString(totalScore) + "Pts");
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



    private void init() {
        FloatingActionButton addButon =(FloatingActionButton) findViewById(R.id.addButton);
        addButon.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.addButton:
                dispatchTakePictureIntent();
                break;
            default:
                break;
        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    private void postImage(final byte[] photoFile) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), photoFile);
        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("feature", "feature.jpg", requestBody);

        Call<Recycle> call = service.postImage(fileToUpload);
        call.enqueue(new Callback<Recycle>() {
            @Override
            public void onResponse(Call<Recycle> call, Response<Recycle> response) {
                if (response.code() == 200){
                    onCall();
                    sendToast("Thanks, You make the world a better place!", Toast.LENGTH_LONG);
                }
            }

            @Override
            public void onFailure(Call<Recycle> call, Throwable t) {
                sendToast("Sorry, we can't process your image", Toast.LENGTH_LONG);
            }
        });
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            Bitmap bmp = BitmapFactory.decodeFile(mCurrentPhotoPath);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, 40, bos);

            postImage(bos.toByteArray());
            sendToast("Loading image...", Toast.LENGTH_SHORT);
        }
    }

    private void sendToast(String s, int duration) {
        Context context = getApplicationContext();
        CharSequence text = s;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }
}
