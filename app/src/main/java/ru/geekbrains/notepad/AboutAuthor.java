package ru.geekbrains.notepad;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import static ru.geekbrains.notepad.R.id.drawer_layout;
import static ru.geekbrains.notepad.R.id.imGit;
import static ru.geekbrains.notepad.R.id.nav_author;
import static ru.geekbrains.notepad.R.id.nav_list_notes;
import static ru.geekbrains.notepad.R.id.nav_view_main;
import static ru.geekbrains.notepad.R.id.toolbar_author;
import static ru.geekbrains.notepad.R.layout.activity_about_author;
import static ru.geekbrains.notepad.R.menu.about_author;
import static ru.geekbrains.notepad.R.string.navigation_drawer_close;
import static ru.geekbrains.notepad.R.string.navigation_drawer_open;

public class AboutAuthor extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_about_author);
        Toolbar toolbar = findViewById(toolbar_author);
        setSupportActionBar(toolbar);

        toolbar.setTitle("Заметки");

        DrawerLayout drawer = findViewById(drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                navigation_drawer_open, navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(nav_view_main);
        navigationView.setNavigationItemSelectedListener(this);

        ImageView imgGit = findViewById(imGit);
        ImageView imgCodePen = findViewById(R.id.imgCodePen);

        imgGit.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://github.com/bembik08"));
            startActivity(browserIntent);
        });
        imgCodePen.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://codepen.io"));
            startActivity(browserIntent);
        });
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Выход из приложения")
                .setMessage("Вы уверены, что хотите выйти?")
                .setPositiveButton("Да", (dialog, which) -> AboutAuthor.super.onBackPressed())
                .setNegativeButton("Нет", null)
                .show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(about_author, menu);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_create_note) {
            Intent i = new Intent(AboutAuthor.this,CreateNote.class);
            startActivity(i);
        } else if (id == nav_author) {

        } else if (id == nav_list_notes) {
            Intent i = new Intent(AboutAuthor.this,ListNodes.class);
            startActivity(i);
        }

        DrawerLayout drawer = findViewById(drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
