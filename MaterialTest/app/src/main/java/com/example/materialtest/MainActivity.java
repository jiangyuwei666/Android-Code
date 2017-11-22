package com.example.materialtest;

import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    DrawerLayout drawerLayout ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drawerLayout = ( DrawerLayout ) findViewById( R.id.drawer_layout ) ;
        Toolbar toolbar = ( Toolbar ) findViewById( R.id.toolbar );
        setSupportActionBar( toolbar ) ;
        ActionBar actionBar = getSupportActionBar() ;
        if ( actionBar != null ) {
            actionBar.setDisplayHomeAsUpEnabled( true ) ;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch ( item.getItemId() ) {
            case android.R.id.home :
                drawerLayout.openDrawer(GravityCompat.START);
                break;
            default:
                break;
        }
        return true ;
    }
}
