package com.cupfullofcode.choints.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.cupfullofcode.choints.R;
import com.cupfullofcode.choints.domain.Child;
import com.cupfullofcode.choints.infrastructure.SQLiteChildRepository;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class ChildChooserFragment extends Fragment {
    private ArrayAdapter<Child> childrenAdapter = null;
    private SQLiteChildRepository childRepository = null;

    @Override
    public void onStart() {
        super.onStart();
        populateChildListView();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        childRepository = new SQLiteChildRepository(getActivity());
        try {
            childRepository.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_add_child) {
            addNewChild();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.menu_fragment_child_chooser, menu);
    }

    @Override
    public void onResume() {
        try {
            childRepository.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        super.onResume();
    }

    @Override
    public void onPause() {
        childRepository.close();
        super.onPause();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_child_chooser, container, false);

        childrenAdapter = new ArrayAdapter<Child>(
                getActivity(),
                android.R.layout.simple_list_item_1,
                new ArrayList<Child>()
        );

        ListView childrenListView = (ListView) rootView.findViewById(R.id.listview_children);
        childrenListView.setAdapter(childrenAdapter);
        childrenListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), ChildDetailActivity.class);
                intent.putExtra("child", childrenAdapter.getItem(position));
                startActivity(intent);
            }
        });

        return rootView;
    }

    protected void addNewChild() {
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());

        alert.setTitle(R.string.add_child);
        alert.setMessage(R.string.add_child_message);

        final EditText name = new EditText(getActivity());
        alert.setView(name);

        alert.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                childRepository.add(name.getText().toString());
                populateChildListView();
            }
        });

        alert.show();
    }

    protected void populateChildListView() {
        List<Child> children = childRepository.children();

        childrenAdapter.clear();
        for (Child child : children) {
            childrenAdapter.add(child);
        }
    }
}
