package com.ionshield.planner.fragments.edit;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.common.util.concurrent.ListenableFuture;
import com.ionshield.planner.database.PlannerRepository;
import com.ionshield.planner.database.dao.NoteDao;
import com.ionshield.planner.database.entities.Note;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class NoteEditDialogViewModel extends ViewModel {
    private final MutableLiveData<Note> note = new MutableLiveData<>();
    private final Executor executor = Executors.newSingleThreadExecutor();

    public NoteEditDialogViewModel() {
        note.setValue(new Note());
    }

    public void setNoteById(long id, Long planId) {
        ListenableFuture<List<Note>> res = PlannerRepository.getInstance().getNoteDao().queryNotes(id);
        res.addListener(() -> {
            try {
                List<Note> list = res.get();
                if (!list.isEmpty() && list.get(0) != null) {
                    synchronized (note) {
                        if (planId != null) {
                            list.get(0).planId = planId;
                        }
                        note.postValue(list.get(0));
                    }
                }
                else {
                    Note n = new Note();
                    if (planId != null) {
                        n.planId = planId;
                    }
                    note.postValue(n);

                }
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }, executor);
    }

    public void writeNote() {
        NoteDao dao = PlannerRepository.getInstance().getNoteDao();
        Note value = note.getValue();
        if (value != null) {
            if (value.noteId == 0) {
                dao.insertAll(value);
            }
            else {
                dao.updateAll(value);
            }
        }
    }

    public LiveData<Note> getNote() {
        return note;
    }

    public void setName(String name) {
        if (note.getValue() != null) {
            synchronized (note) {
                note.getValue().name = name;
                note.setValue(note.getValue());
            }
        }
    }

    public void setContent(String content) {
        if (note.getValue() != null) {
            synchronized (note) {
                note.getValue().content = content;
                note.setValue(note.getValue());
            }
        }
    }

}
