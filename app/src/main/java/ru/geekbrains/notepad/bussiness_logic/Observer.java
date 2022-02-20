package ru.geekbrains.notepad.bussiness_logic;

import ru.geekbrains.notepad.data.Note;

public interface Observer {
    void updateNoteSource(Note note);
}
