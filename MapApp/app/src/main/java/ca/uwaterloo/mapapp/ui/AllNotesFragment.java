package ca.uwaterloo.mapapp.ui;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.melnykov.fab.FloatingActionButton;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import ca.uwaterloo.mapapp.R;
import ca.uwaterloo.mapapp.data.DatabaseHelper;
import ca.uwaterloo.mapapp.objects.Note;
import ca.uwaterloo.mapapp.shared.data.DataManager;
import ca.uwaterloo.mapapp.ui.adapters.NoteAdapter;

public class AllNotesFragment extends Fragment {

    @InjectView(R.id.note_list)
    protected ListView noteList;
    @InjectView(R.id.fab_new_note)
    protected FloatingActionButton fab;

    private List<Note> mNotes;
    private NoteAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_all_notes, container, false);
        // This has to come after setContentView
        ButterKnife.inject(this, view);

        DatabaseHelper databaseHelper = DatabaseHelper.getDatabaseHelper();
        DataManager<Note, Long> dataManager = databaseHelper.getDataManager(Note.class);

        mNotes = dataManager.getAll(Note.COLUMN_LAST_MODIFIED, false);
        mAdapter = new NoteAdapter(getActivity(), mNotes);

        noteList.setAdapter(mAdapter);
        noteList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), ViewNoteActivity.class);
                intent.putExtra(NewEditNoteActivity.ARG_NOTE_ID, id);
                startActivityForResult(intent, ViewNoteActivity.REQUEST_UPDATE);
                getActivity().overridePendingTransition(R.anim.slide_up, R.anim.nothing);
            }
        });
        noteList.setEmptyView(view.findViewById(R.id.empty_list_state));

        fab.attachToListView(noteList);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), NewEditNoteActivity.class);
                startActivityForResult(intent, NewEditNoteActivity.REQUEST_INSERT);
                getActivity().overridePendingTransition(R.anim.slide_up, R.anim.nothing);
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_CANCELED) return;

        DatabaseHelper databaseHelper = DatabaseHelper.getDatabaseHelper();
        DataManager<Note, Long> dataManager = databaseHelper.getDataManager(Note.class);
        Note note = dataManager.findById(data.getLongExtra(NewEditNoteActivity.RESULT_NOTE_ID, -1));

        if (requestCode == ViewNoteActivity.REQUEST_UPDATE) {
            long noteId = data.getLongExtra(NewEditNoteActivity.RESULT_NOTE_ID, -1);
            for (int i = 0; i < mNotes.size(); i++) {
                if (mNotes.get(i).getId() == noteId) {
                    mNotes.remove(i);
                    break;
                }
            }
        }
        // on update/insert
        mNotes.add(0, note);
        mAdapter.notifyDataSetChanged();
    }
}
