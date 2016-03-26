package com.uberlyftcomp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.uberlyftcomp.util.Loc;

import java.util.ArrayList;

public class Comp_activity extends AppCompatActivity {

    Loc origin;
    Loc dest;
    ArrayList<Loc> l;
    TextView tv_origin;
    TextView tv_dest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comp_activity);
        l = (ArrayList<Loc>) getIntent().getSerializableExtra("Loc");
        origin = l.get(0);
        dest = l.get(1);

        tv_origin=(TextView)findViewById(R.id.tv_comp_origin);
        tv_dest=(TextView)findViewById(R.id.tv_comp_dest);

        tv_origin.setText(" Origin:  "+ origin.getAddress()+" longitude: "+origin.getLon()+" latitude: "+origin.getLat() );
        tv_dest.setText(" Origin:  "+ dest.getAddress()+" longitude: "+dest.getLon()+" latitude: "+dest.getLat() );

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_comp_activity, menu);
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
}
