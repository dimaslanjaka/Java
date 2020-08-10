package com.dimaslanjaka.tools;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.dimaslanjaka.tools.helper.SharedPref;
import com.dimaslanjaka.tools.ytdE.Launcher;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private SharedPref spref = null;
    private SharedPreferences pref = null;
    private String id = null;
    private FloatingActionButton fab;
    private static FloatingActionButton fabs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.spref = new SharedPref(getApplicationContext());
        id = this.spref.id;
        this.pref = this.spref.getPref();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new
                    StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        fab = findViewById(R.id.fab);
        fabs = fab;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MainActivity.this.pref != null) {
                    Intent myIntent = new Intent(MainActivity.this, AdmobStart.class);
                    MainActivity.this.startActivity(myIntent);
                } else {
                    Snackbar.make(view, "Admob Not Properly Configured", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }
            }
        });
        if (this.pref != null) {
            Snackbar.make(fab, "Welcome", Snackbar.LENGTH_LONG).setAction("Action", null).show();
        } else {
            fab.setVisibility(View.INVISIBLE);
            Snackbar.make(fab, "Preference Load Error", Snackbar.LENGTH_LONG).setAction("Action", null).show();
        }
        if (this.pref.getString("appid", "").isEmpty()) {
            fab.setVisibility(View.INVISIBLE);
        } else {
            fab.setVisibility(View.VISIBLE);
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_ytmp3, R.id.nav_settings
        ).setDrawerLayout(drawer).build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    public static void enableFab() {
        fabs.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        super.onOptionsItemSelected(menuItem);
        int itemId = menuItem.getItemId();
        if (itemId == R.id.action_settings) {
            Toast.makeText(getApplicationContext(), "Settings", Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration) || super.onSupportNavigateUp();
    }
}
