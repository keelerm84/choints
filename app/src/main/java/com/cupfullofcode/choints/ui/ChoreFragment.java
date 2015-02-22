package com.cupfullofcode.choints.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.cupfullofcode.choints.R;
import com.cupfullofcode.choints.domain.Child;
import com.cupfullofcode.choints.domain.Chore;
import com.cupfullofcode.choints.infrastructure.SQLiteChoreRepository;
import com.cupfullofcode.choints.infrastructure.SQLiteHistoryRepository;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ChoreFragment extends Fragment {
    private SQLiteChoreRepository choresRepository = null;
    private SQLiteHistoryRepository historiesRepository = null;
    private Child child = null;
    private ArrayAdapter<Chore> choresAdapter = null;

    @Override
    public void onPause() {
        choresRepository.close();
        historiesRepository.close();
        super.onPause();
    }

    @Override
    public void onStart() {
        super.onStart();
        populateChoresList();
    }

    @Override
    public void onResume() {
        choresRepository = new SQLiteChoreRepository(getActivity());
        historiesRepository = new SQLiteHistoryRepository(getActivity());

        try {
            choresRepository.open();
            historiesRepository.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        super.onResume();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_add_chore) {
            addNewChore();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        choresRepository = new SQLiteChoreRepository(getActivity());
        historiesRepository = new SQLiteHistoryRepository(getActivity());
        setHasOptionsMenu(true);

        try {
            choresRepository.open();
            historiesRepository.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Bundle extras = getActivity().getIntent().getExtras();

        if (extras == null) return;

        this.child = (Child) extras.getSerializable("child");
    }

    public ChoreFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_child_detail_chore, container, false);

        choresAdapter = new ArrayAdapter<Chore>(
                getActivity(),
                android.R.layout.simple_list_item_1,
                new ArrayList<Chore>()
        );

        final ListView choresListView = (ListView) rootView.findViewById(R.id.listView_chores);
        choresListView.setAdapter(choresAdapter);
        choresListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        choresListView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            private long pendingPoints = 0;

            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                Chore chore = choresAdapter.getItem(position);
                long multiplier = checked ? 1 : -1;

                pendingPoints += multiplier * chore.points();
                mode.setTitle("Pending: " + pendingPoints);
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                pendingPoints = 0;
                MenuInflater inflater = mode.getMenuInflater();
                inflater.inflate(R.menu.cab_confirm_chores, menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                if (item.getItemId() == R.id.action_cancel) {
                    mode.finish();
                    return true;
                } else if (item.getItemId() == R.id.action_save) {
                    SparseBooleanArray checkedPositions = choresListView.getCheckedItemPositions();
                    for (int i = 0; i < choresAdapter.getCount(); i++) {
                        if (checkedPositions.get(i) == true) {
                            Chore chore = choresAdapter.getItem(i);
                            historiesRepository.add(chore.description(), chore.points(), child.id());
                        }
                    }

                    mode.finish();
                    return true;
                }

                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
            }
        });

        return rootView;
    }

    protected void populateChoresList() {
        List<Chore> chores = choresRepository.chores(child);

        choresAdapter.clear();
        for (Chore chore : chores) {
            choresAdapter.add(chore);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_fragment_chore, menu);
    }

    protected void addNewChore() {
        LayoutInflater inflater = getLayoutInflater(null);
        final View view;
        view = inflater.inflate(R.layout.alert_add_chore, null);

        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        alert.setView(view);
        alert.setTitle(R.string.add_chore);
        alert.setMessage(R.string.add_chore_message);

        alert.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EditText descriptionEditText = (EditText) view.findViewById(R.id.chore_description);
                EditText pointsEditText = (EditText) view.findViewById(R.id.chore_points);

                String description = descriptionEditText.getText().toString();
                long points = Integer.parseInt(pointsEditText.getText().toString());

                choresRepository.add(description, points, child);
                populateChoresList();
            }
        });

        alert.show();
    }
}
