package ca.uwaterloo.mapapp.ui;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.baoyz.widget.PullRefreshLayout;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import ca.uwaterloo.mapapp.R;
import ca.uwaterloo.mapapp.data.DatabaseHelper;
import ca.uwaterloo.mapapp.logic.net.ServerRestApi;
import ca.uwaterloo.mapapp.shared.ICallback;
import ca.uwaterloo.mapapp.shared.data.DataManager;
import ca.uwaterloo.mapapp.shared.objects.event.Event;
import ca.uwaterloo.mapapp.ui.adapters.EventAdapter;

public class AllEventsFragment extends Fragment {
    @InjectView(R.id.event_list)
    protected ListView eventList;
    @InjectView(R.id.pullRefresh)
    protected PullRefreshLayout refreshLayout;

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
        final DataManager dataManager = databaseHelper.getDataManager(Event.class);

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

        refreshLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ICallback eventsCallback = new ICallback() {
                    @Override
                    public void call(Object param) {
                        DatabaseHelper databaseHelper = DatabaseHelper.getDatabaseHelper();
                        DataManager<Event, Integer> eventDataManager = databaseHelper.getDataManager(Event.class);
                        List<Event> events = (List<Event>) param;
                        eventDataManager.insertOrUpdateAll(events);
                        mEvents = eventDataManager.getAll();
                        mAdapter.notifyDataSetChanged();
                        refreshLayout.setRefreshing(false);
                    }
                };
                ServerRestApi.requestEvents(eventsCallback);
            }
        });
        Log.i("AllEventsFragment", "onCreateView finished" );
        return view;
    }

}
