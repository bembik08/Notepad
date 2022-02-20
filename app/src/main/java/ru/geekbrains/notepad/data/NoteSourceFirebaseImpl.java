package ru.geekbrains.notepad.data;

import android.annotation.SuppressLint;
import android.util.Log;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import androidx.annotation.NonNull;

public class NoteSourceFirebaseImpl implements  NoteSource {
    private final   static String NOTE_COLLECTION = "notes";
    private final static String TAG = "[NoteSourceFirebaseImpl]";

    private FirebaseFirestore store = FirebaseFirestore.getInstance();

    private CollectionReference collectionReference = store.collection(NOTE_COLLECTION);

    private List<Note> noteList = new ArrayList<Note>();

    @Override
    public NoteSource init(NoteSourceResponse noteSourceResponse) {
        collectionReference.orderBy(NoteDataMapping.FIELDS.DATE, Query.Direction.DESCENDING).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @SuppressLint("LongLogTag")
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            noteList = new ArrayList<Note>();
                            for (QueryDocumentSnapshot documentSnapshots : Objects.requireNonNull(task.getResult())){
                                Map<String, Object> document = documentSnapshots.getData();
                                String id = documentSnapshots.getId();
                                Note note = NoteDataMapping.toNoteData(id, document);
                                noteList.add(note);
                            }
                            Log.d(TAG, "success" + noteList.size() + "qnt");
                            noteSourceResponse.initialized(NoteSourceFirebaseImpl.this);
                        } else {
                            Log.d(TAG, "get failed with " + task.getException());
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "get failed with " + e);
            }
        });
        return this;
    }

    @Override
    public Note getNote(int position) {
        return  noteList.get(position);
    }

    @Override
    public int size() {
        if (noteList == null){
            return 0;
        }
        return noteList.size();
    }

    @Override
    public void updateNote(Note note, int position) {
        String id = note.getId();
        collectionReference.document(id).set(NoteDataMapping.toDocument(note));
    }

    @Override
    public void addNote(Note note) {
     collectionReference.add(NoteDataMapping.toDocument(note)).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
         @Override
         public void onSuccess(DocumentReference documentReference) {
             note.setId(documentReference.getId());
         }
     });
    }

    @Override
    public void removeNote(int position) {
        collectionReference.document(noteList.get(position).getId()).delete();
    }
}
