package ru.geekbrains.notepad.bussiness_logic;

import ru.geekbrains.notepad.data.Note;
import java.util.ArrayList;
import java.util.List;

public class Publisher {
    private final List<Observer> observers;
    public Publisher(){
        observers = new ArrayList<>();
    }
    public void subscribe(Observer observer){
        observers.add(observer);
    }
    public void unSubscribe(Observer observer){
        observers.remove(observer);
    }
    public void notify(Note note){
        for (Observer observer : observers) {
            observer.updateNoteSource(note);
            unSubscribe(observer);
        }
    }
}
