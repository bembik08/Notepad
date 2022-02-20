package ru.geekbrains.notepad;

import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.material.navigation.NavigationView;
import ru.geekbrains.notepad.bussiness_logic.GetPublisher;
import ru.geekbrains.notepad.bussiness_logic.Publisher;
import ru.geekbrains.notepad.data.Navigation;
import ru.geekbrains.notepad.ui.ContentNotesFragment;
import ru.geekbrains.notepad.ui.SettingsFragment;
import ru.geekbrains.notepad.ui.StartFragment;

public class MainActivity extends AppCompatActivity implements GetPublisher {
    private Navigation navigation;
    private boolean isLandscape;
    private final Publisher publisher = new Publisher();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        navigation = new Navigation(getSupportFragmentManager());
        this.getNavigation().addFragment(ContentNotesFragment.newInstance(), false);
        initView();
    }

    private void initView() {
        Toolbar toolbar = initToolBar();
        initDrawer(toolbar);
        isLandscape = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
    }

    private Toolbar initToolBar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        return toolbar;
    }

    private void initDrawer(Toolbar toolbar) {
        final DrawerLayout drawerLayout = findViewById(R.id.drawerLayout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.open_nav_drawer, R.string.close_navigation_toolbar);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            switch (id) {
                case R.id.action_settings_drawer:
                    getNavigation().addFragment(SettingsFragment.newInstance("string", "string2"), false);
                    drawerLayout.closeDrawer(GravityCompat.START);
                    return true;
                case R.id.action_main_drawer:
                    getNavigation().addFragment(ContentNotesFragment.newInstance(), false);
                    drawerLayout.closeDrawer(GravityCompat.START);
                    return true;
            }
            return false;
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem search = menu.findItem(R.id.action_search_menu);
        android.widget.SearchView searchView = (android.widget.SearchView) search.getActionView();
        searchView.setOnQueryTextListener(new android.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return true;
            }
        });
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.settingsMenu:
                getNavigation().addFragment(SettingsFragment.newInstance("string", "string2"), false);
                return true;
            case R.id.main_menu:
                getNavigation().addFragment(StartFragment.newInstance(), false);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Publisher getPublisher() {
        return publisher;
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public Navigation getNavigation() {
        return navigation;
    }
}