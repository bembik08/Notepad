package ru.geekbrains.notepad.data;

import android.content.res.Resources;
import ru.geekbrains.notepad.R;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class NoteSourceImpl implements NoteSource {
    private final List<Note> noteList;
    private final Resources resources;

    public NoteSourceImpl(Resources resources) {
        this.resources = resources;
        noteList = new ArrayList<>();
    }

    public NoteSource init(NoteSourceResponse noteSourceResponse) {
        String[] titles = resources.getStringArray(R.array.noteName);
        String[] descriptions = resources.getStringArray(R.array.noteDescription);
        for (int i = 0; i < titles.length; i++) {
            noteList.add(new Note(titles[i], descriptions[i], i, Calendar.getInstance().getTime()));
        }
        if (noteSourceResponse != null){
            noteSourceResponse.initialized(this);
        }
        return this;
    }

    @Override
    public Note getNote(int position) {
        return noteList.get(position);
    }

    @Override
    public int size() {
        return noteList.size();
    }

    @Override
    public void updateNote(Note note, int position) {
        noteList.set(position, note);
    }

    @Override
    public void addNote(Note note) {
        noteList.add(note);
    }

    @Override
    public void removeNote(int position) {
          noteList.remove(position);
    }
}
