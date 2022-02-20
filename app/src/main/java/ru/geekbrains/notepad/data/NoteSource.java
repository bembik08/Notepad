package ru.geekbrains.notepad.data;

public interface NoteSource {
    Note getNote(int position);
    NoteSource init(NoteSourceResponse noteSourceResponse);

    int size();

    void updateNote(Note note, int position);
    void addNote(Note note);
    void removeNote(int position);
}