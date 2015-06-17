package ca.uwaterloo.mapapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
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
import ca.uwaterloo.mapapp.data.DataManager;
import ca.uwaterloo.mapapp.data.DatabaseHelper;
import ca.uwaterloo.mapapp.data.objects.Note;

public class AllNotesFragment extends Fragment {

    @InjectView(R.id.note_list)
    protected ListView noteList;
    @InjectView(R.id.fab_new_note)
    protected FloatingActionButton fab;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_all_notes, container, false);
        // This has to come after setContentView
        ButterKnife.inject(this, view);

        DatabaseHelper databaseHelper = DatabaseHelper.getDatabaseHelper(getActivity());
        DataManager<Note, Long> dataManager = databaseHelper.getDataManager(Note.class);

        List<Note> notes = dataManager.getAll();

        noteList.setAdapter(new NoteAdapter(getActivity(), notes));
        noteList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), NewEditNoteActivity.class);
                intent.putExtra(NewEditNoteActivity.ARG_NOTE_ID, id);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_up, R.anim.nothing);
            }
        });
        fab.attachToListView(noteList);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), NewEditNoteActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_up, R.anim.nothing);
            }
        });

        return view;
    }
}
