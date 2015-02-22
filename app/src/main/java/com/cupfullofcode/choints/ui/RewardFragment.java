package com.cupfullofcode.choints.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import com.cupfullofcode.choints.domain.HistoryRepository;
import com.cupfullofcode.choints.domain.Reward;
import com.cupfullofcode.choints.domain.RewardRepository;
import com.cupfullofcode.choints.infrastructure.SQLiteHistoryRepository;
import com.cupfullofcode.choints.infrastructure.SQLiteRewardRepository;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RewardFragment extends Fragment {
    private RewardRepository rewardsRepository = null;
    private HistoryRepository historiesRepository = null;
    private Child child = null;
    private ArrayAdapter<Reward> rewardsAdapter = null;

    @Override
    public void onStart() {
        super.onStart();
        populateRewardsList();
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
        setHasOptionsMenu(true);

        Bundle extras = getActivity().getIntent().getExtras();

        if (extras == null) return;

        this.child = (Child) extras.getSerializable("child");
    }

    public RewardFragment(RewardRepository rewardRepository, HistoryRepository historyRepository) {
        rewardsRepository = rewardRepository;
        historiesRepository = historyRepository;
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

        final ListView rewardsListView = (ListView) rootView.findViewById(R.id.listView_rewards);
        rewardsListView.setAdapter(rewardsAdapter);
        rewardsListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        rewardsListView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            private long pendingPoints = 0;

            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                Reward reward = rewardsAdapter.getItem(position);
                long multiplier = checked ? 1 : -1;

                pendingPoints += multiplier * reward.points();
                mode.setTitle("Pending: " + pendingPoints);
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                pendingPoints = 0;
                MenuInflater inflater = mode.getMenuInflater();
                inflater.inflate(R.menu.cab_confirm_rewards, menu);
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
                    SparseBooleanArray checkedPositions = rewardsListView.getCheckedItemPositions();
                    for (int i = 0; i < rewardsAdapter.getCount(); i++) {
                        if (checkedPositions.get(i) == true) {
                            Reward reward = rewardsAdapter.getItem(i);
                            historiesRepository.add(reward.description(), -1 * reward.points(), child.id());
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

    protected void populateRewardsList() {
        List<Reward> rewards = rewardsRepository.rewards(child);

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
