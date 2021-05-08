package ru.geekbrains.notepad;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.google.gson.Gson;

import java.util.ArrayList;

import static ru.geekbrains.notepad.R.id.dateNode;
import static ru.geekbrains.notepad.R.id.titleNode;

public class NodeAdapter extends BaseAdapter {

    private Context ctx;
    private LayoutInflater lInflater;
    private ArrayList<org.w3c.dom.Node> nodes;
    private TextView emptyText;

    public NodeAdapter(ListNodes context, ArrayList<org.w3c.dom.Node> nodes, TextView emptyText) {
        ctx = context;
        this.nodes = nodes;
        this.emptyText = emptyText;
        lInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return nodes.size();
    }

    @Override
    public Object getItem(int position) {
        return nodes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = lInflater.inflate(R.layout.list_item, parent, false);
        }

        Node p = getNode(position);

        ((TextView) view.findViewById(titleNode)).setText(p.getName());
        ((TextView) view.findViewById(dateNode)).setText(p.getDate());

        ImageView deleteButton = view.findViewById(R.id.deleteButton);
        deleteButton.setTag(position);
        deleteButton.setOnClickListener(position1 -> {
            final int pos = (int) position1.getTag();
            new AlertDialog.Builder(ctx)
                .setTitle("Удаление заметки")
                .setMessage("Вы уверены, что хотите удалить заметку?")
                .setPositiveButton("Да", (dialog, which) -> {
                    nodes.remove(pos);
                    NodeAdapter.this.notifyDataSetChanged();
                    if(nodes.isEmpty()){
                        emptyText.setVisibility(View.VISIBLE);
                    }
                    else{
                        emptyText.setVisibility(View.GONE);
                    }
                    saveData();
                    Toast toast = Toast.makeText(ctx, "Заметка удалена", Toast.LENGTH_SHORT);
                    toast.show();
                })
                .setNegativeButton("Нет", null)
                .show();

        });
        return view;
    }

    public Node getNode(int position) {
        return ((Node) getItem(position));
    }

    public void saveData(){
        SharedPreferences prefs = ctx.getSharedPreferences("shared preferences",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(nodes);
        editor.putString("task list", json);
        editor.apply();
    }
}
