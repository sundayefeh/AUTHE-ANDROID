package app.sunny.authe;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class AutheResult extends AppCompatActivity {

    Alert mAlert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authe_result);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mAlert= new Alert(this);

        Intent intent= getIntent();
        Product product_= (Product) intent.getSerializableExtra("product");
        mAlert.Show("PRODUCT","Name: "+product_.Name+"\nManufacturer: "+product_.Company+"\nProduced: "+product_.Production+"\nExpires: "+product_.Expiry);

    }

}
