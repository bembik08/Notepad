package ru.geekbrains.notepad.ui;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import ru.geekbrains.notepad.R;
import ru.geekbrains.notepad.data.Note;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DescriptionNoteFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DescriptionNoteFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_NOTE = "Note";
    private Note note;

    public DescriptionNoteFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment DescriptionNoteFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DescriptionNoteFragment newInstance(Note note) {
        DescriptionNoteFragment fragment = new DescriptionNoteFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_NOTE, note);
        System.out.println(note + " newInstance");
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println(getArguments() + "onCreate");
        if (getArguments() != null) {
            note = getArguments().getParcelable(ARG_NOTE);
            System.out.println(note + "1111");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_description_note, container, false);
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        View view = getView();
        System.out.println(getArguments() + " preOnCreateView");
        TextView nameTextView = view.findViewById(R.id.nameOfNoteForDescription);
        TextView textDescriptionView = view.findViewById(R.id.textDescription);
        if (getArguments() != null) {
            //nothing
            nameTextView.setText(note.getName());
            textDescriptionView.setText(note.getDescription());
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.description_fragment_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.resendMenu:
                Toast.makeText(getContext(), "Re-send", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_change:
                Toast.makeText(getContext(), "Change", Toast.LENGTH_SHORT).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}