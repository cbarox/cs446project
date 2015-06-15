package ca.uwaterloo.mapapp.ui;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

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

    SimpleCursorAdapter mAdapter;

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

        noteList.setAdapter(new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_list_item_1, notes));

        fab.attachToListView(noteList);

        return view;
    }

}
