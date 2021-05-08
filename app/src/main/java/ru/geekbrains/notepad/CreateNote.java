package ru.geekbrains.notepad;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

import static ru.geekbrains.notepad.R.id.drawer_layout;
import static ru.geekbrains.notepad.R.id.nav_view_main;
import static ru.geekbrains.notepad.R.id.toolbar_main;
import static ru.geekbrains.notepad.R.layout.activity_create_note;
import static ru.geekbrains.notepad.R.menu.create_note;
import static ru.geekbrains.notepad.R.string.navigation_drawer_close;
import static ru.geekbrains.notepad.R.string.navigation_drawer_open;

public class CreateNote extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener {

    private TextInputEditText textInput;

    private Node node;
    private ArrayList<Node> nodes;

    private SharedPreferences prefs;

    private Intent intentOfActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_create_note);
        Toolbar toolbar = findViewById(toolbar_main);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                navigation_drawer_open, navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(nav_view_main);
        navigationView.setNavigationItemSelectedListener(this);

        loadData();

        textInput = findViewById(R.id.textInput);

        intentOfActivity = getIntent();

        if(getIntent().hasExtra("nodeedit")) {
            Node editNode = (Node) getIntent().getSerializableExtra("nodeedit");
            textInput.setText(editNode.getText());
        }
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Выход из приложения")
                .setMessage("Вы уверены, что хотите выйти?")
                .setPositiveButton("Да", (dialog, which) -> {
                    if(intentOfActivity.hasExtra("position") &&
                            intentOfActivity.hasExtra("nodeedit")){
                        intentOfActivity.removeExtra("nodeedit");
                        intentOfActivity.removeExtra("position");
                    }
                    CreateNote.super.onBackPressed();
                })
                .setNegativeButton("Нет", null)
                .show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(create_note, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.save_button) {
            if(!(Objects.requireNonNull(textInput.getText()).length()==0)){
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Сохранение");

                final EditText input = new EditText(this);
                builder.setView(input);

                if(intentOfActivity.hasExtra("nodeedit")) {
                    Node editNode = (Node) getIntent().getSerializableExtra("nodeedit");
                    input.setText(editNode.getName());
                }

                builder.setPositiveButton("OK", (dialog, which) -> {
                    if (input.getText().toString().length() != 0) {
                        node = new Node(input.getText().toString(),
                                textInput.getText().toString(),
                                new SimpleDateFormat("dd.MM.yyyy; HH:mm:ss").format(Calendar.getInstance().getTime()));
                        nodes.add(node);

                        if(intentOfActivity.hasExtra("position")){
                            int pos = (int) getIntent().getSerializableExtra("position");
                            nodes.remove(pos);

                            intentOfActivity.removeExtra("nodeedit");
                            intentOfActivity.removeExtra("position");
                        }

                        saveData();
                        Toast toast = Toast.makeText(getApplicationContext(), "Заметка сохранена", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                    else {
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(CreateNote.this);
                        builder1.setTitle("Пустой текст!").setMessage("Введите название заметки, а потом сохраните!").setCancelable(false).setNegativeButton("OK", (dialog1, id1) -> dialog1.cancel());
                        AlertDialog alert = builder1.create();
                        alert.show();
                    }
                });
                builder.setNegativeButton("Закрыть", (dialog, which) -> dialog.cancel());

                builder.show();
            }
            else{
                AlertDialog.Builder builder = new AlertDialog.Builder(CreateNote.this);
                builder.setTitle("Пустой текст!")
                        .setMessage("Введите текст заметки, а потом сохраните!")
                        .setCancelable(false)
                        .setNegativeButton("OK",
                                (dialog, id12) -> dialog.cancel());
                AlertDialog alert = builder.create();
                alert.show();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_create_note) {

        } else if (id == R.id.nav_author) {
            if(intentOfActivity.hasExtra("position") && intentOfActivity.hasExtra("nodeedit")){
                intentOfActivity.removeExtra("nodeedit");
                intentOfActivity.removeExtra("position");
            }
            Intent intent = new Intent(CreateNote.this,AboutAuthor.class);
            startActivity(intent);
        } else if (id == R.id.nav_list_notes) {
            if(intentOfActivity.hasExtra("position") && intentOfActivity.hasExtra("nodeedit")){
                intentOfActivity.removeExtra("nodeedit");
                intentOfActivity.removeExtra("position");
            }
            Intent intent = new Intent(CreateNote.this, ListNodes.class);
            startActivity(intent);
        }

        DrawerLayout drawer = findViewById(drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void saveData(){
        prefs = getSharedPreferences("shared preferences",MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(nodes);
        editor.putString("task list", json);
        editor.apply();
    }

    public void loadData(){
        prefs = getSharedPreferences("shared preferences",MODE_PRIVATE);
        Gson gson = new Gson();
        String json = prefs.getString("task list", null);
        Type type = new TypeToken<ArrayList<Node>>() {}.getType();
        nodes = gson.fromJson(json, type);

        if(nodes==null){
            nodes = new ArrayList<>();
        }
    }
}
