package com.silentstarelly.catanpointscounter;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class GameActivity extends AppCompatActivity {
    private PlayerAdapter mPlayerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        GridView gridview = (GridView) findViewById(R.id.players);

        mPlayerAdapter = new PlayerAdapter(this);
        gridview.setAdapter(mPlayerAdapter);

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Toast.makeText(GameActivity.this, "" + position,
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
    public class PlayerAdapter extends BaseAdapter {
        private ArrayList<Player> mPlayers;
        private Context mContext;

        public PlayerAdapter(Context c) {
            mPlayers = new ArrayList<>();
            mContext = c;

            Player elena = new Player("Elena");
            elena.buildCity();
            elena.takeMetropolis(Player.Metropolis.BLUE);
            mPlayers.add(elena);
            mPlayers.add(new Player("Catherine"));
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
                playerView = new View(mContext);
                playerView = inflater.inflate(R.layout.player_layout, null);

                TextView name = (TextView) playerView.findViewById(R.id.name);
                name.setText(player.getName());
                TextView score = (TextView) playerView.findViewById(R.id.score);
                score.setText("" + player.getNumVictoryPoints());

            } else {
                playerView = (View) convertView;
            }

            return playerView;
        }
    }
}
