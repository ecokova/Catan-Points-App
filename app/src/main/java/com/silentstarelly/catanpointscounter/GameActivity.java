package com.silentstarelly.catanpointscounter;

import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.ArrayList;
import java.util.HashMap;

public class GameActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {
    private GridView mGridView;
    private PlayerGridAdapter mPlayerGridAdapter;
    private Integer mSelectedActionId;

    private ArrayList<Player> mPlayers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Bundle extras = getIntent().getExtras();
        mPlayers = (ArrayList<Player>) extras.get("players");

        mGridView = (GridView) findViewById(R.id.players);

        mPlayerGridAdapter = new PlayerGridAdapter(this);
        mGridView.setAdapter(mPlayerGridAdapter);

        mGridView.setOnItemClickListener(this);
        mGridView.setOnItemLongClickListener(this);

        initializeActionButtons();
    }


    private void initializeActionButtons() {
        int[] ids = { R.id.city_action, R.id.settlement_action, R.id.progress_card_action,
                R.id.defender_of_catan_action, R.id.merchant_action, R.id.longest_road_action,
                R.id.blue_metropolis_action, R.id.green_metropolis_action,
                R.id.yellow_metropolis_action, R.id.barbarians_action };
        for (int id :ids) {
            ImageView actionButton = (ImageView) findViewById(id);
            actionButton.setOnClickListener(this);
        }
    }

    private void initializeDummyPlayers() {
        mPlayers = new ArrayList<>();

        Player elena = new Player("Elena", Player.Color.WHITE);
        elena.buildCity();
        /*elena.buildCity();
        elena.takeMetropolis(Player.Metropolis.BLUE);
        elena.takeMerchant();*/
        mPlayers.add(elena);


        Player catherine = new Player("Catherine", Player.Color.RED);
        catherine.buildCity();
        /*catherine.takeMetropolis(Player.Metropolis.YELLOW);
        catherine.takeMetropolis(Player.Metropolis.GREEN);
        catherine.takeMetropolis(Player.Metropolis.BLUE);
        catherine.buildCity();
        catherine.takeLongestRoad();
        catherine.takeMerchant();
        catherine.addProgressCardPoint();
        catherine.addProgressCardPoint();
        catherine.addProgressCardPoint();*/
        mPlayers.add(catherine);

        Player jeff = new Player("Jeff", Player.Color.ORANGE);
        jeff.buildCity();
        mPlayers.add(jeff);

        Player monica = new Player("Monica", Player.Color.BLUE);
        monica.buildCity();
        /*monica.takeLongestRoad();
        monica.takeMerchant();
        monica.buildSettlement();
        monica.buildSettlement();
        monica.buildSettlement();
        monica.buildSettlement();
        monica.buildCity();
        monica.buildCity();
        monica.buildCity();
        monica.addDefenderOfCatan();
        monica.addDefenderOfCatan();
        monica.addDefenderOfCatan();
        monica.addDefenderOfCatan();*/
        mPlayers.add(monica);

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        View actionView = findViewById(id);
        if (mSelectedActionId != null && mSelectedActionId == id) {
            mSelectedActionId = null;
            actionView.setAlpha(1f);
        } else {
            mSelectedActionId = id;
            actionView.setAlpha(0.5f);
        }
    }


    private void transferMetropolis(Player.Metropolis metropolis, int playerId) {
        mPlayers.get(playerId).takeMetropolis(metropolis);
        for (int i = 0; i < mPlayers.size(); i++) {
            Player p = mPlayers.get(i);
            ArrayList<Player.Metropolis> metropolises = p.getMetropolises();
            for (Player.Metropolis m : metropolises) {
                if (m == metropolis && i != playerId) {
                    p.loseMetropolis(m);
                    break;
                }
            }
        }
    }
    private void transferMerchant(int playerId) {
        for (Player p : mPlayers) {
            if (p.hasMerchant()) {
                p.loseMerchant();
            }
        }
        mPlayers.get(playerId).takeMerchant();

    }
    private void transferLongestRoad(int playerId) {
        for (Player p : mPlayers) {
            if (p.hasLongestRoad()) {
                p.loseLongestRoad();
            }
        }
        mPlayers.get(playerId).takeLongestRoad();

    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Player player = mPlayers.get(position);
        if (mSelectedActionId != null) {
            try {
                switch (mSelectedActionId) {
                    case R.id.blue_metropolis_action:
                        transferMetropolis(Player.Metropolis.BLUE, position);
                        break;
                    case R.id.green_metropolis_action:
                        transferMetropolis(Player.Metropolis.GREEN, position);
                        break;
                    case R.id.yellow_metropolis_action:
                        transferMetropolis(Player.Metropolis.YELLOW, position);
                        break;
                    case R.id.merchant_action:
                        transferMerchant(position);
                        break;
                    case R.id.longest_road_action:
                        transferLongestRoad(position);
                        break;
                    case R.id.barbarians_action:
                        player.burnCity();
                        break;
                    case R.id.progress_card_action:
                        player.addProgressCardPoint();
                        break;
                    case R.id.defender_of_catan_action:
                        player.addDefenderOfCatan();
                        break;
                    case R.id.city_action:
                        player.buildCity();
                        break;
                    case R.id.settlement_action:
                        player.buildSettlement();
                        break;
                }
            } catch (CatanRuleViolation e) {
                Toast.makeText(GameActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    findViewById(mSelectedActionId).setAlpha(1f);
                }
            });
        }

        mPlayerGridAdapter.notifyDataSetChanged();

    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        Player player = mPlayers.get(position);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        StringBuilder points = new StringBuilder();
        points.append(Integer.toString(player.getNumSettlements()) +
                (player.getNumSettlements() == 1 ? " settlement\n" : " settlements\n"));
        points.append(Integer.toString(player.getNumCities()) +
                (player.getNumCities() == 1 ? " city\n" : " cities\n"));
        points.append(Integer.toString(player.getMetropolises().size()) +
                (player.getMetropolises().size() == 1 ? " metropolis\n" : " metropolises\n"));
        points.append(Integer.toString(player.getNumTimesDefender()) + " Defender of Catan " +
                (player.getNumTimesDefender() == 1 ? "point\n" : "points\n"));

        points.append(Integer.toString(player.getNumProgressCardPoints()) + " progress card victory " +
                (player.getNumProgressCardPoints() == 1 ? "point\n" : "points\n"));
        if (player.hasLongestRoad()) {
            points.append("Longest Road\n");
        }
        if (player.hasMerchant()) {
            points.append("Merchant\n");
        }
        // TODO: Largest army

        builder.setMessage(points);

        builder.setTitle(player.getName() + "'s Points");
        builder.setPositiveButton("OK", null);

        builder.show();
        return true;
    }

    public class PlayerGridAdapter extends BaseAdapter {
        private Context mContext;

        public PlayerGridAdapter(Context c) {
            mContext = c;

        }

        @Override
        public void registerDataSetObserver(DataSetObserver observer) {
            super.registerDataSetObserver(observer);
        }

        @Override
        public void unregisterDataSetObserver(DataSetObserver observer) {
            super.unregisterDataSetObserver(observer);
        }

        @Override
        public int getCount() {
            if (mPlayers != null) {
                return mPlayers.size();
            } else {
                return 0;
            }
        }

        @Override
        public Object getItem(int position) {
            if (mPlayers != null) {
                return mPlayers.get(position);
            } else {
                return null;
            }
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View playerView;

            playerView = inflater.inflate(R.layout.player_layout, null);
            playerView = fillPlayerView(playerView, mPlayers.get(position));

            return playerView;
        }

        private View fillPlayerView(View playerView, Player player) {
            TextView name = (TextView) playerView.findViewById(R.id.name);
            name.setText(player.getName());
            //TODO: Move colors into values file
            switch (player.getColor()) {
                case BLUE:
                    name.setTextColor(Color.parseColor("#2196F3"));
                    break;
                case RED:
                    name.setTextColor(Color.parseColor("#F44336"));
                    break;
                case WHITE:
                    name.setTextColor(Color.WHITE);
                    name.setShadowLayer(2,1,1,Color.BLACK);
                    break;
                case BROWN:
                    name.setTextColor(Color.parseColor("#795548"));
                    break;
                case GREEN:
                    name.setTextColor(Color.parseColor("#4CAF50"));
                    break;
                case ORANGE:
                    name.setTextColor(Color.parseColor("#ff9000"));
                    break;
            }
            TextView score = (TextView) playerView.findViewById(R.id.score);
            score.setText("" + player.getNumVictoryPoints());

            ImageView longestRoadIcon = (ImageView) playerView.findViewById(R.id.longest_road_icon);
            if (player.hasLongestRoad()) {
                longestRoadIcon.setVisibility(View.VISIBLE);
            } else {
                longestRoadIcon.setVisibility(View.GONE);
            }

            ImageView merchantIcon = (ImageView) playerView.findViewById(R.id.merchant_icon);
            if (player.hasMerchant()) {
                merchantIcon.setVisibility(View.VISIBLE);
            } else {
                merchantIcon.setVisibility(View.GONE);
            }

            ArrayList<Player.Metropolis> metropolises = player.getMetropolises();
            for (int i = 0; i < 3; i++) {
                ImageView metropolis;
                if (i == 0) {
                    metropolis = (ImageView) playerView.findViewById(R.id.metropolis_icon1);
                } else if (i == 1) {
                    metropolis = (ImageView) playerView.findViewById(R.id.metropolis_icon2);
                } else {
                    metropolis = (ImageView) playerView.findViewById(R.id.metropolis_icon3);
                }

                if (i >= metropolises.size()) {
                    metropolis.setVisibility(View.GONE);
                }
                else{
                    metropolis.setVisibility(View.VISIBLE);
                    switch (metropolises.get(i)) {
                        case BLUE:
                            metropolis.setImageResource(R.mipmap.ic_blue_metropolis);
                            break;
                        case GREEN:
                            metropolis.setImageResource(R.mipmap.ic_green_metropolis);
                            break;
                        case YELLOW:
                            metropolis.setImageResource(R.mipmap.ic_yellow_metropolis);
                            break;
                    }
                }
            }
            return playerView;
        }
    }
}
