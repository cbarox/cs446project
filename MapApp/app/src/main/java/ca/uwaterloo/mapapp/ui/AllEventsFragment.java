package ca.uwaterloo.mapapp.ui;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import ca.uwaterloo.mapapp.R;
import ca.uwaterloo.mapapp.data.DatabaseHelper;
import ca.uwaterloo.mapapp.shared.data.DataManager;
import ca.uwaterloo.mapapp.shared.objects.event.Event;
import ca.uwaterloo.mapapp.ui.adapters.EventAdapter;

public class AllEventsFragment extends Fragment {
    @InjectView(R.id.event_list)
    protected ListView eventList;

    private List<Event> mEvents;
    private EventAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_all_events, container, false);

        // This has to come after setContentView
        ButterKnife.inject(this, view);
        DatabaseHelper databaseHelper = DatabaseHelper.getDatabaseHelper();
        DataManager dataManager = databaseHelper.getDataManager(Event.class);

        mEvents = dataManager.getAll();
        mAdapter = new EventAdapter(getActivity(), mEvents);
        eventList.setAdapter(mAdapter);

        eventList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), ViewEventActivity.class);
                intent.putExtra(ViewEventActivity.ARG_EVENT_ID, id);
                getActivity().startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_up, R.anim.nothing);
            }
        });

        return view;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
