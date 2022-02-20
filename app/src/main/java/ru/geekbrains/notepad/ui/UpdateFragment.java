package ru.geekbrains.notepad.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import com.google.android.material.textfield.TextInputEditText;
import java.util.Calendar;
import java.util.Date;
import ru.geekbrains.notepad.MainActivity;
import ru.geekbrains.notepad.R;
import ru.geekbrains.notepad.bussiness_logic.Publisher;
import ru.geekbrains.notepad.data.Navigation;
import ru.geekbrains.notepad.data.Note;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UpdateFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UpdateFragment extends DialogFragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_NOTE = "Note";
    private static final String ARG_POSITION = "position";

    // TODO: Rename and change types of parameters
    private Note note;
    private  Navigation navigation;
    private Publisher publisher;

    private TextInputEditText nameTextInput;
    private TextInputEditText descriptionTextInput;
    private DatePicker datePicker;

    public UpdateFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param note     Parameter 1.
     * @param position Parameter 2.
     * @return A new instance of fragment UpdateFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UpdateFragment newInstance(Note note, int position) {
        UpdateFragment fragment = new UpdateFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_NOTE, note);
        args.putInt(ARG_POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }

    public static UpdateFragment newInstance(){
        UpdateFragment fragment = new UpdateFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            note = getArguments().getParcelable(ARG_NOTE);
            int position = getArguments().getInt(ARG_POSITION);
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        MainActivity mainActivity = (MainActivity) context;
        publisher = mainActivity.getPublisher();
    }

    @Override
    public void onDetach() {
        publisher = null;
        super.onDetach();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_update, container, false);
        initView(view);
        if (note != null){
            setNoteView();
        }
        return view;
    }

    private void initView(View view) {
        datePicker = view.findViewById(R.id.dateUpdateNote);
        nameTextInput = view.findViewById(R.id.updateInputNameNote);
        descriptionTextInput = view.findViewById(R.id.updateInputDescriptionNote);
        Button updateBtn = view.findViewById(R.id.updateBtn);
        updateBtn.setOnClickListener(view1 -> requireActivity().onBackPressed());
    }

    private void setNoteView() {
        initDatePicker(note.getDate());
        nameTextInput.setText(note.getName());
        descriptionTextInput.setText(note.getDescription());
    }

    private void initDatePicker(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        this.datePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), null);
    }

    @Override
    public void onStop() {
        super.onStop();
        note = collectNote();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        publisher.notify(note);
    }

    private Note collectNote() {
        String name = this.nameTextInput.getText().toString();
        String description = this.descriptionTextInput.getText().toString();
        Date date = getDateFromDatePicker();
        if(note != null){
            Note answer;
            answer = new Note(name, description, note.getPosition(), date);
            answer.setId(note.getId());
            return answer;
        }
        return new Note(name, description, 0, date);
    }

    private Date getDateFromDatePicker() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, this.datePicker.getYear());
        calendar.set(Calendar.MONTH, this.datePicker.getMonth());
        calendar.set(Calendar.DAY_OF_MONTH, this.datePicker.getDayOfMonth());
        return  calendar.getTime();
    }
}