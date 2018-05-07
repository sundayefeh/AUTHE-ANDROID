package app.sunny.authe;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static OkHttpClient Http_Client ;

    Alert mAlert;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        mAlert=new Alert(this);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        final View scan=findViewById(R.id.scan);
        final View history= findViewById(R.id.history);
        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent lgn= new Intent(MainActivity.this,Login.class);
                startActivity(lgn);
            }
        });
        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckApp_Permission();

            }
        });
    }

    public void CheckApp_Permission()
    {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    Config.USE_PERMISSION_CAMERA);
        }

        else
        {
             Intent intent=new Intent(MainActivity.this,FullScannerActivity.class);
             startActivityForResult(intent,Config.USE_SCANNER);
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case Config.USE_PERMISSION_CAMERA: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    Intent intent = new Intent(MainActivity.this, FullScannerActivity.class);
                    startActivity(intent);

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

        }
        }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
/*
        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }
*/
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    void attemptVerification(final String product)
    {
        String URL= Config.BASE_URL+"product/info";
       // mAlert.Show(product);
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("product", product);
        }
        catch (Exception ex){}

        OkHttpClient.Builder builder = (new OkHttpClient.Builder()).readTimeout(60L, TimeUnit.SECONDS).writeTimeout(60L, TimeUnit.SECONDS);

        Http_Client=builder.build();

        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonObject.toString());
        Request request = new Request.Builder()
                .url(URL)
                .addHeader("Content-Type", "text/json")
                .post(body)
                .build();
        //Log.e("ID", account.Client_Id);

        Http_Client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                    }
                });

            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                try {
                    final String json=response.body().string();
                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mAlert.Dismiss();
                            try {

                                // Log.e("METERS",json);
                                JSONObject jsonobject= new JSONObject(json);
                                int code=jsonobject.getInt("code");

                                Log.e("RESPONSE",json);

                                if(code==100)
                                {
                                    String body= jsonobject.getString("body");
                                    JSONObject jsBody= new JSONObject(body);
                                    //"product_id":"19","product":"good bread","description":"hello bread","production_date":"0000-00-00 00:00:00",
                                    // "expiry_date":"0000-00-00 00:00:00","registration_code":"44782465722588986654590422574805",
                                    // "customer_id":"6","company_name":"cloo africa","company_email":"interblink@gmail.com",
                                    // "company_phone":"08179854464","company_address":"Hello Abuja","city":"Abuja","date_created":"2018-01-16 07:12:57",
                                    // "registration_no":"7897898"
                                    Product product_= new Product();
                                    product_.Name=jsBody.getString("product");
                                    product_.Company=jsBody.getString("company_name");
                                    product_.Desc=jsBody.getString("description");
                                    product_.Expiry=jsBody.getString("expiry_date");
                                    product_.Production=jsBody.getString("production_date");

                                    Intent intent= new Intent(MainActivity.this,AutheResult.class);
                                    intent.putExtra("product",product_);
                                    startActivity(intent);
                                }

                                else {
                                  //  mAlert.Dismiss();
                                    mAlert.Show("CAUTION","Product was not found. Please exercise caution.");
                                }


                                //Log.e("RESPONSE", code+"");
                            }
                            catch (Exception e){
                                mAlert.Dismiss();
                            }
                        }
                    });


                }
                catch (IOException e){
                    // Log.e("ERROR 1",e.getMessage());
                    mAlert.Show("system error occurred");
                }
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if(requestCode==Config.USE_SCANNER)
        {
            if(resultCode==RESULT_OK)
            {
                String product=data.getExtras().getString(Config.CODE_DATA_EXTRA);
                Log.e("REG: NO.",product);
                if(product.contains(":"))
                {
                    // good bread0000-00-00 00:00:000000-00-00 00:00:0044782465722588986654590422574805
                    String[] parts= product.split(":");
                    try {
                        String code = parts[4].substring(2);
                        //Log.e("CODE", code);
                        mAlert.Progress("Please wait...");
                        attemptVerification(code);
                    }catch (Exception e)
                    {}
                }


            }
        }
    }
}
