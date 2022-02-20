package ru.geekbrains.notepad.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Date;

import ru.geekbrains.notepad.MainActivity;
import ru.geekbrains.notepad.R;
import ru.geekbrains.notepad.bussiness_logic.Observer;
import ru.geekbrains.notepad.bussiness_logic.Publisher;
import ru.geekbrains.notepad.data.Navigation;
import ru.geekbrains.notepad.data.Note;
import ru.geekbrains.notepad.data.NoteSource;
import ru.geekbrains.notepad.data.NoteSourceFirebaseImpl;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ContentNotesFragment#} factory method to
 * create an instance of this fragment.
 */
public class ContentNotesFragment extends Fragment {

    private static final String ARG_NOTE = "Note";
    public static Note currentNote;
    private boolean isLandscape;
    private NoteSource noteSource;
    private Navigation navigation;
    private Publisher publisher;
    private ContentNotesAdapter contentNotesAdapter;
    private static final String ARG_POSITION = "position";
    private boolean moveToFirstPosition;

    public ContentNotesFragment() {
        // Required empty public constructor
    }

    public static ContentNotesFragment newInstance(){
        return new ContentNotesFragment();
    }

    public  static ContentNotesFragment newInstance(Note currentNote, int position){
        ContentNotesFragment fragment = new ContentNotesFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_NOTE, currentNote);
        args.putInt(ARG_POSITION, position);
        return fragment;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_content_notes, container, false);
        setHasOptionsMenu(true);

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        MainActivity activity = (MainActivity) context;
        navigation = activity.getNavigation();
        publisher = activity.getPublisher();
    }

    @Override
    public void onDetach() {
        navigation = null;
        publisher = null;
        super.onDetach();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initList(view);
        noteSource = new NoteSourceFirebaseImpl().init(noteData -> contentNotesAdapter.notifyDataSetChanged());
        contentNotesAdapter.setNoteSource(noteSource);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void initList(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.recycle_view_layout);
        //    recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
         contentNotesAdapter = new ContentNotesAdapter(this);
        recyclerView.setAdapter(contentNotesAdapter);

        DividerItemDecoration itemDecoration = new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL);
        itemDecoration.setDrawable(getResources().getDrawable(R.drawable.common_google_signin_btn_text_dark_normal_background, null));
        recyclerView.addItemDecoration(itemDecoration);

        DefaultItemAnimator animator = new DefaultItemAnimator();
        animator.setAddDuration(1000);
        animator.setRemoveDuration(1000);
        recyclerView.setItemAnimator(animator);
        if(moveToFirstPosition && noteSource.size() > 0){
            recyclerView.smoothScrollToPosition(0);
            moveToFirstPosition = false;
        }


        contentNotesAdapter.setItemClickListener((view1, position, resources) -> {
            if (resources == R.id.noteDate) {
                initPopup(view1, noteSource.getNote(position), position);
             } else if(resources == R.id.deleteBtn){
                currentNote = noteSource.getNote(position);
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle(R.string.titleDeleteDialog)
                        .setMessage(R.string.deleteTxtBtn)
                        .setNegativeButton("No", null);
                builder.setPositiveButton("Yes", (dialogInterface, i) -> {
                    noteSource.removeNote(position);
                    navigation.addFragment(ContentNotesFragment.newInstance(), true);
                    contentNotesAdapter.notifyDataSetChanged();
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
            else {
                currentNote = noteSource.getNote(position);
                showDescriptionNote(currentNote);
            }
        });

    }

    private void initPopup(View view1, Note note, int position) {
        Activity activity = requireActivity();
        PopupMenu popupMenu = new PopupMenu(activity, view1);
        activity.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(menuItem -> {
            int id = menuItem.getItemId();
            switch (id) {
                case R.id.changePopup:
                    showUpdateFragment(note, position);
                    return true;
                case R.id.donePopup:
                    Toast.makeText(getContext(), "Done", Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.deletePopup:
                     noteSource.removeNote(position);
                     contentNotesAdapter.notifyDataSetChanged();
                     moveToFirstPosition = true;
                    return true;
            }
            return true;
        });
        popupMenu.show();

    }

    private void showUpdateFragment(Note note, int position) {
        navigation.addFragment(UpdateFragment.newInstance(note, position), true);
        publisher.subscribe(new Observer() {
            @Override
            public void updateNoteSource(Note note) {
                noteSource.updateNote(note, position);
                contentNotesAdapter.notifyItemChanged(position);
                moveToFirstPosition  = true;
            }
        });
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelable(ARG_NOTE, currentNote);
        super.onSaveInstanceState(outState);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        isLandscape = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
        if (savedInstanceState != null) {
            currentNote = savedInstanceState.getParcelable(ARG_NOTE);
        } else {
            currentNote = new Note(getResources().getStringArray(R.array.noteName)[0], getResources().getStringArray(R.array.noteDescription)[0], 0, new Date());
        }
    }

    private void showDescriptionNote(Note currentNote) {
        navigation.addFragment(DescriptionNoteFragment.newInstance(currentNote), true, isLandscape);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.content_fragment_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_add:
                addFragment();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public  void  addFragment(){
        navigation.addFragment(UpdateFragment.newInstance(), true);
        publisher.subscribe(note -> {
            noteSource.addNote(note);
            contentNotesAdapter.notifyItemInserted(noteSource.size() - 1);
            moveToFirstPosition= true;
        });
    }

    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater menuInflater = requireActivity().getMenuInflater();
        menuInflater.inflate(R.menu.context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        int position = contentNotesAdapter.getMenuPosition();
        switch (item.getItemId()){
            case R.id.action_add_context:
                addFragment();
                Toast.makeText(getContext(), "ADD", Toast.LENGTH_SHORT).show();
                return  true;
            case R.id.action_update_context:
                showUpdateFragment(currentNote, position);
                Toast.makeText(getContext(), "Update", Toast.LENGTH_SHORT).show();
                return true;
        }
        return super.onContextItemSelected(item);
    }
}