package com.silentstarelly.catanpointscounter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class GameActivity extends AppCompatActivity {
    private PlayerGridAdapter mPlayerGridAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        GridView gridview = (GridView) findViewById(R.id.players);

        mPlayerGridAdapter = new PlayerGridAdapter(this);
        gridview.setAdapter(mPlayerGridAdapter);

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Toast.makeText(GameActivity.this, "" + position,
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
    public class PlayerGridAdapter extends BaseAdapter {
        private ArrayList<Player> mPlayers;
        private Context mContext;

        public PlayerGridAdapter(Context c) {
            mPlayers = new ArrayList<>();
            mContext = c;

            Player elena = new Player("Elena", Player.Color.WHITE);
            elena.buildCity();
            elena.takeMetropolis(Player.Metropolis.BLUE);
            elena.takeMerchant();
            mPlayers.add(elena);


            Player catherine = new Player("Catherine", Player.Color.RED);
            catherine.takeMetropolis(Player.Metropolis.YELLOW);
            catherine.takeMetropolis(Player.Metropolis.GREEN);
            catherine.takeMetropolis(Player.Metropolis.BLUE);
            catherine.buildCity();
            catherine.takeLongestRoad();
            catherine.takeMerchant();
            catherine.addProgressCardPoint();
            catherine.addProgressCardPoint();
            catherine.addProgressCardPoint();
            mPlayers.add(catherine);

            Player jeff = new Player("Jeff", Player.Color.ORANGE);
            mPlayers.add(jeff);

            Player monica = new Player("Monica", Player.Color.BLUE);
            monica.takeLongestRoad();
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
            monica.addDefenderOfCatan();
            //mPlayers.add(monica);
        }

        @Override
        public int getCount() {
            return mPlayers.size();
        }

        @Override
        public Object getItem(int position) {
            return mPlayers.get(position);
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

            Player player = mPlayers.get(position);
            if (convertView == null) {
                playerView = inflater.inflate(R.layout.player_layout, null);

                TextView name = (TextView) playerView.findViewById(R.id.name);
                name.setText(player.getName());
                switch (player.getColor()) {
                    case BLUE:
                        name.setTextColor(Color.BLUE);
                        break;
                    case RED:
                        name.setTextColor(Color.RED);
                        break;
                    case WHITE:
                        name.setTextColor(Color.WHITE);
                        name.setShadowLayer(2,1,1,Color.BLACK);
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

            } else {
                playerView = (View) convertView;
            }

            return playerView;
        }
    }
}
