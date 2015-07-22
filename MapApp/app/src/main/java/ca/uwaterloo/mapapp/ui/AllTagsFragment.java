package ca.uwaterloo.mapapp.ui;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.melnykov.fab.FloatingActionButton;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import ca.uwaterloo.mapapp.R;
import ca.uwaterloo.mapapp.data.DatabaseHelper;
import ca.uwaterloo.mapapp.objects.Tag;
import ca.uwaterloo.mapapp.shared.data.DataManager;
import ca.uwaterloo.mapapp.ui.adapters.TagAdapter;

public class AllTagsFragment extends Fragment {

    @InjectView(R.id.tag_list)
    protected ListView tagList;
    @InjectView(R.id.fab_new_tag)
    protected FloatingActionButton fab;

    private List<Tag> mTags;
    private TagAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_all_tags, container, false);
        ButterKnife.inject(this, view);

        DatabaseHelper databaseHelper = DatabaseHelper.getDatabaseHelper();
        DataManager<Tag, Long> dataManager = databaseHelper.getDataManager(Tag.class);

        mTags = dataManager.getAll(Tag.COLUMN_TITLE, true);
        mAdapter = new TagAdapter(getActivity(), mTags);

        tagList.setAdapter(mAdapter);
        // set on item click listener
        tagList.setEmptyView(view.findViewById(R.id.empty_list_state));

        tagList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), FilteredNoteListActivity.class);
                intent.putExtra(FilteredNoteListActivity.ARG_FILTER_TYPE, FilteredNoteListActivity.FILTER_TAGS);
                intent.putExtra(FilteredNoteListActivity.ARG_FILTER_VALUE_ID, id);
                startActivity(intent);
                //getActivity().overridePendingTransition(R.anim.slide_up, R.anim.nothing);
            }
        });

        fab.attachToListView(tagList);
        // on click listener
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MaterialDialog.Builder(getActivity())
                        .title("New Tag")
                        .inputType(InputType.TYPE_CLASS_TEXT)
                        .positiveText("Add")
                        .input("Tag name", "", new MaterialDialog.InputCallback() {
                            @Override
                            public void onInput(MaterialDialog dialog, CharSequence input) {
                                addTag(input.toString());
                            }
                        }).show();
            }
        });

        return view;
    }

    private void addTag(String tagTitle) {
        Tag newTag = new Tag();
        newTag.setTitle(tagTitle);

        DatabaseHelper databaseHelper = DatabaseHelper.getDatabaseHelper();
        DataManager<Tag, Long> dataManager = databaseHelper.getDataManager(Tag.class);

        dataManager.insert(newTag);

        int insert = 0;
        for (insert = 0; insert < mTags.size(); insert++) {
            // newTag is before mTags(insert)
            if (tagTitle.compareTo(mTags.get(insert).getTitle()) < 0)
                break;
        }
        mTags.add(insert, newTag);
        mAdapter.notifyDataSetChanged();
    }
}
