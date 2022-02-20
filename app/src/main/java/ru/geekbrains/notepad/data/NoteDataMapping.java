package ru.geekbrains.notepad.data;

import com.google.firebase.Timestamp;
import java.util.HashMap;
import java.util.Map;

public class NoteDataMapping {
    public static class FIELDS{
        public final static String DATE = "date";
        public final static String NAME = "name";
        public final static String DESCRIPTION = "description";
        public final static String POSITION = "position";
        public final static String DONE = "done";
    }
    public static Note toNoteData(String id, Map<String, Object> doc){
        Timestamp timestamp = (Timestamp) doc.get(FIELDS.DATE);
        assert timestamp != null;
        Note answer = new Note(
                (String) doc.get(FIELDS.NAME),
                (String) doc.get(FIELDS.DESCRIPTION),
                Integer.parseInt(String.valueOf(doc.get(FIELDS.POSITION))),
                timestamp.toDate()
        );
        answer.setId(id);
        return answer;
    }
    public  static  Map<String, Object> toDocument(Note note){
        Map<String, Object> answer = new HashMap<>();
        answer.put(FIELDS.NAME, note.getName());
        answer.put(FIELDS.DESCRIPTION, note.getDescription());
        answer.put(FIELDS.POSITION, note.getPosition());
        answer.put(FIELDS.DATE, note.getDate());
        return answer;
    }
}
