package com.ionshield.planner.fragments.list;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.ionshield.planner.database.entities.Note;
import com.ionshield.planner.databinding.FragmentNoteBinding;

import java.util.Objects;

public class NoteRecyclerViewAdapter extends ListAdapter<Note, NoteRecyclerViewAdapter.ViewHolder> {
    private final NoteRecyclerViewAdapter that = this;
    private Mode mMode;
    private SelectionListener<Note> listener;

    public static final DiffUtil.ItemCallback<Note> DIFF_CALLBACK = new DiffUtil.ItemCallback<Note>() {
        @Override
        public boolean areItemsTheSame(@NonNull Note oldItem, @NonNull Note newItem) {
            return oldItem.noteId == newItem.noteId;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Note oldItem, @NonNull Note newItem) {
            return Objects.equals(oldItem, newItem);
        }
    };

    public NoteRecyclerViewAdapter() {
        super(DIFF_CALLBACK);
    }


    public void setMode(Mode mode) {
        mMode = mode;
    }

    public void setOnSelectionListener(SelectionListener<Note> l) {
        listener = l;
    }

    public void notifyListeners(Note select, SelectionMode mode) {
        if (listener != null) {
            listener.onSelect(select, mode);
        }
    }

    @NonNull
    @Override
    public NoteRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewNote) {
        return new NoteRecyclerViewAdapter.ViewHolder(FragmentNoteBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }


    @Override
    public void onBindViewHolder(final NoteRecyclerViewAdapter.ViewHolder holder, int position) {
        holder.bindTo(getItem(position), position + 1, mMode);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public Mode mMode;

        public final View mRoot;
        public final TextView mNumberView;
        public final TextView mNameView;

        public final Button mEditButton;
        public final Button mDeleteButton;
        public final Button mSelectButton;

        public Note mNote;
        public int mPosition;

        public ViewHolder(FragmentNoteBinding binding) {
            super(binding.getRoot());
            mRoot = binding.getRoot();
            mNumberView = binding.itemNumber;
            mNameView = binding.name;

            mEditButton = binding.editButton;
            mDeleteButton = binding.deleteButton;
            mSelectButton = binding.selectButton;

            setMode(that.mMode);
        }

        public void bindTo(Note note, int position, Mode mode) {
            mNote = note;
            mPosition = position;

            mNumberView.setText(String.valueOf(position));
            mNameView.setText(note.name);


            mEditButton.setText("");
            mDeleteButton.setText("");

            if (mode != mMode) setMode(mode);

        }

        public void setMode(Mode mode) {
            mMode = mode;
            switch (mMode) {
                case NONE:
                    mSelectButton.setVisibility(View.GONE);
                    mSelectButton.setOnClickListener(null);
                    mEditButton.setVisibility(View.GONE);
                    mEditButton.setOnClickListener(null);
                    mDeleteButton.setVisibility(View.GONE);
                    mDeleteButton.setOnClickListener(null);
                    break;
                case SELECT:
                    mSelectButton.setVisibility(View.VISIBLE);
                    mSelectButton.setOnClickListener(l -> notifyListeners(mNote, SelectionMode.SELECT));
                    mEditButton.setVisibility(View.GONE);
                    mEditButton.setOnClickListener(null);
                    mDeleteButton.setVisibility(View.GONE);
                    mDeleteButton.setOnClickListener(null);
                    break;
                case EDIT:
                    mSelectButton.setVisibility(View.GONE);
                    mSelectButton.setOnClickListener(null);
                    mEditButton.setVisibility(View.VISIBLE);
                    mEditButton.setOnClickListener(l -> notifyListeners(mNote, SelectionMode.EDIT));
                    mDeleteButton.setVisibility(View.VISIBLE);
                    mDeleteButton.setOnClickListener(l -> notifyListeners(mNote, SelectionMode.DELETE));
                    break;
                case SELECT_EDIT:
                    mSelectButton.setVisibility(View.VISIBLE);
                    mSelectButton.setOnClickListener(l -> notifyListeners(mNote, SelectionMode.SELECT));
                    mEditButton.setVisibility(View.VISIBLE);
                    mEditButton.setOnClickListener(l -> notifyListeners(mNote, SelectionMode.EDIT));
                    mDeleteButton.setVisibility(View.VISIBLE);
                    mDeleteButton.setOnClickListener(l -> notifyListeners(mNote, SelectionMode.DELETE));
                    break;
            }
        }

        @NonNull
        @Override
        public String toString() {
            return super.toString() + " '" + mNameView.getText() + "'";
        }
    }
}
