package com.cupfullofcode.choints.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.cupfullofcode.choints.R;
import com.cupfullofcode.choints.domain.Child;
import com.cupfullofcode.choints.domain.History;
import com.cupfullofcode.choints.infrastructure.SQLiteHistoryRepository;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class HistoryFragment extends Fragment {
    private SQLiteHistoryRepository historiesRepository = null;
    private Child child = null;
    private ArrayAdapter<History> historiesAdapter = null;

    @Override
    public void onPause() {
        historiesRepository.close();
        super.onPause();
    }

    @Override
    public void onStart() {
        super.onStart();
        populatehistoriesList();
    }

    @Override
    public void onResume() {
        historiesRepository = new SQLiteHistoryRepository(getActivity());
        try {
            historiesRepository.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        super.onResume();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        historiesRepository = new SQLiteHistoryRepository(getActivity());

        try {
            historiesRepository.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Bundle extras = getActivity().getIntent().getExtras();

        if (extras == null) return;

        this.child = (Child) extras.getSerializable("child");
    }

    public HistoryFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_child_detail_history, container, false);

        historiesAdapter = new ArrayAdapter<History>(
                getActivity(),
                android.R.layout.simple_list_item_1,
                new ArrayList<History>()
        );

        ListView historiesListView = (ListView) rootView.findViewById(R.id.listView_histories);
        historiesListView.setAdapter(historiesAdapter);

        return rootView;
    }

    protected void populatehistoriesList() {
        List<History> histories = historiesRepository.histories(child);

        historiesAdapter.clear();
        for (History history : histories) {
            historiesAdapter.add(history);
        }
    }
}
