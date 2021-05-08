package ru.geekbrains.notepad;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.w3c.dom.Node;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;

import static ru.geekbrains.notepad.R.id.action_settings;
import static ru.geekbrains.notepad.R.id.drawer_layout;
import static ru.geekbrains.notepad.R.id.list_of_nodes;
import static ru.geekbrains.notepad.R.id.nav_author;
import static ru.geekbrains.notepad.R.id.nav_create_note;
import static ru.geekbrains.notepad.R.id.nav_list_notes;
import static ru.geekbrains.notepad.R.id.nav_view_main;
import static ru.geekbrains.notepad.R.layout.activity_list_nodes;
import static ru.geekbrains.notepad.R.menu.list_nodes;
import static ru.geekbrains.notepad.R.string.navigation_drawer_close;
import static ru.geekbrains.notepad.R.string.navigation_drawer_open;

public class ListNodes extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener {

    private ArrayList<Node> nodes;
    private Node node;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_list_nodes);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setTitle("Notepad");

        DrawerLayout drawer = findViewById(drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                navigation_drawer_open, navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        TextView emptyText = findViewById( R.id.emptyText );

        NavigationView navigationView = findViewById(nav_view_main);
        navigationView.setNavigationItemSelectedListener(this);

        loadData();

        NodeAdapter nodeAdapter = new NodeAdapter( this, nodes, emptyText );
        saveData();

        if(nodes.isEmpty()){
            emptyText.setVisibility(View.VISIBLE);
        }
        else{
            emptyText.setVisibility(View.GONE);
        }

        ListView listView = findViewById(list_of_nodes);
        listView.setAdapter(nodeAdapter);
        listView.setOnItemClickListener( (parent, view, position, id) -> {
            node = nodes.get(position);
            Intent i = new Intent(ListNodes.this, CreateNote.class);
            i.putExtra("nodeedit", (Serializable) node );
            i.putExtra("position", position);
            startActivity(i);
        } );
    }

    public void saveData(){
        SharedPreferences prefs = getSharedPreferences("shared preferences",MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(nodes);
        editor.putString("task list", json);
        editor.apply();
    }

    public void loadData(){
        SharedPreferences prefs = getSharedPreferences("shared preferences",MODE_PRIVATE);
        Gson gson = new Gson();
        String json = prefs.getString("task list", null);
        Type type = new TypeToken<ArrayList<Node>>() {}.getType();
        nodes = gson.fromJson(json, type);

        if(nodes==null){
            nodes = new ArrayList<>();
        }
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Выход из приложения")
                .setMessage("Вы уверены, что хотите выйти?")
                .setPositiveButton("Да", (dialog, which) -> ListNodes.super.onBackPressed() )
                .setNegativeButton("Нет", null)
                .show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate( list_nodes, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == nav_create_note) {
            Intent i = new Intent(ListNodes.this,CreateNote.class);
            startActivity(i);
        } else if (id == nav_author) {
            Intent i = new Intent(ListNodes.this,AboutAuthor.class);
            startActivity(i);
        } else if (id == nav_list_notes) {

        }
        DrawerLayout drawer = findViewById(drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
