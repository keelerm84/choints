package com.cupfullofcode.choints.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.cupfullofcode.choints.R;
import com.cupfullofcode.choints.domain.Child;
import com.cupfullofcode.choints.domain.Reward;
import com.cupfullofcode.choints.infrastructure.SQLiteRewardRepository;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class RewardFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    private SQLiteRewardRepository rewardsRepository = null;
    private Child child = null;
    private ArrayAdapter<Reward> rewardsAdapter = null;

    @Override
    public void onPause() {
        rewardsRepository.close();
        super.onPause();
    }

    @Override
    public void onStart() {
        super.onStart();
        populateRewardsList();
    }

    @Override
    public void onResume() {
        rewardsRepository = new SQLiteRewardRepository(getActivity());
        try {
            rewardsRepository.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        super.onResume();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_add_reward) {
            addNewReward();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rewardsRepository = new SQLiteRewardRepository(getActivity());
        setHasOptionsMenu(true);

        try {
            rewardsRepository.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Bundle extras = getActivity().getIntent().getExtras();

        if (extras == null) return;

        this.child = (Child) extras.getSerializable("child");
    }

    public RewardFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_child_detail_reward, container, false);

        rewardsAdapter = new ArrayAdapter<Reward>(
                getActivity(),
                android.R.layout.simple_list_item_1,
                new ArrayList<Reward>()
        );

        ListView rewardsListView = (ListView) rootView.findViewById(R.id.listView_rewards);
        rewardsListView.setAdapter(rewardsAdapter);

        return rootView;
    }

    protected void populateRewardsList() {
        List<Reward> rewards = rewardsRepository.rewards(child);
        rewardsAdapter.setNotifyOnChange(true);

        rewardsAdapter.clear();
        for (Reward reward : rewards) {
            rewardsAdapter.add(reward);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_fragment_reward, menu);
    }

    protected void addNewReward() {
        LayoutInflater inflater = getLayoutInflater(null);
        final View view;
        view = inflater.inflate(R.layout.alert_add_reward, null);

        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        alert.setView(view);
        alert.setTitle(R.string.add_reward);
        alert.setMessage(R.string.add_reward_message);

        alert.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EditText descriptionEditText = (EditText) view.findViewById(R.id.reward_description);
                EditText pointsEditText = (EditText) view.findViewById(R.id.reward_points);

                String description = descriptionEditText.getText().toString();
                long points = Integer.parseInt(pointsEditText.getText().toString());

                rewardsRepository.add(description, points, child);
                populateRewardsList();
            }
        });

        alert.show();
    }
}
