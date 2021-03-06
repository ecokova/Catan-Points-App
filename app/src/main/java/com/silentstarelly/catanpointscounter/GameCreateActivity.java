package com.silentstarelly.catanpointscounter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class GameCreateActivity extends AppCompatActivity implements View.OnClickListener {
    private GameCreateActivity.NewPlayerListAdapter mPlayerListAdapter;
    private ArrayList<Player> mPlayers;
    private ListView mPlayersList;
    private Player mPlayerTemplate;
    private int mVersionID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_create);

        Bundle extras = getIntent().getExtras();
        mPlayerTemplate = (Player) extras.get("startingPlayerTemplate");
        mVersionID = (int) extras.get("version");

        mPlayers = new ArrayList<>();

        mPlayersList = (ListView) findViewById(R.id.players_list);
        mPlayerListAdapter = new GameCreateActivity.NewPlayerListAdapter(this);
        mPlayersList.setAdapter(mPlayerListAdapter);

        Button addPlayerButton = (Button) findViewById(R.id.add_player_button);
        addPlayerButton.setOnClickListener(this);

        Button beginGameButton = (Button) findViewById(R.id.begin_game_button);
        beginGameButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_player_button:
                if (mPlayerTemplate != null) {
                    mPlayers.add(new Player(mPlayerTemplate));
                } else {
                    mPlayers.add(new Player());
                }
                mPlayerListAdapter.notifyDataSetChanged();

                mPlayersList.clearFocus();
                // TODO: Find a way to refocus on the new exittext
                break;
            case R.id.begin_game_button:
                Intent intent = new Intent(this, GameActivity.class);
                intent.putExtra("players", mPlayers);
                intent.putExtra("version", mVersionID);
                startActivity(intent);
        }
    }

    public class NewPlayerListAdapter extends BaseAdapter {
        private Context mContext;

        public NewPlayerListAdapter(Context c) {
            mContext = c;
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

            playerView = inflater.inflate(R.layout.new_player_layout, null);
            playerView = fillPlayerView(playerView, position);

            return playerView;
        }

        private String colorEnumToSpinnerString(Player.Color color) {
            switch (color) {
                case BLUE:
                    return "Blue";
                case GREEN:
                    return "Green";
                case BROWN:
                    return "Brown";
                case WHITE:
                    return "White";
                case RED:
                    return "Red";
                case ORANGE:
                    return "Orange";
                default:
                    return "None";
            }
        }
        private Player.Color spinnerStringToColorEnum(String color) {
            switch (color) {
                case "Blue":
                    return Player.Color.BLUE;
                case "Green":
                    return Player.Color.GREEN;
                case "Brown":
                    return Player.Color.BROWN;
                case "White":
                    return Player.Color.WHITE;
                case "Red":
                    return Player.Color.RED;
                case "Orange":
                    return Player.Color.ORANGE;
                default:
                    return Player.Color.NONE;
            }
        }
        private View fillPlayerView(View playerView, final int playerId) {
            Player player = mPlayers.get(playerId);
            EditText nameEditText = (EditText) playerView.findViewById(R.id.name);
            nameEditText.setText(player.getName());
            nameEditText.addTextChangedListener(
                    new TextWatcher() {
                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                        }
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            String name = s.toString();
                            mPlayers.get(playerId).setName(name);
                        }
                    }
            );

            final Spinner colorPicker = (Spinner) playerView.findViewById(R.id.color);
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(mContext,
                    R.array.colors_array, android.R.layout.simple_spinner_dropdown_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            colorPicker.setAdapter(adapter);
            colorPicker.setSelection(adapter.getPosition(colorEnumToSpinnerString(player.getColor())));
            colorPicker.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                    Player.Color selectedColor = spinnerStringToColorEnum(colorPicker.getSelectedItem().toString());
                    mPlayers.get(playerId).setColor(selectedColor);
                }


                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            //TODO: Make delete clickable
            return playerView;
        }
    }
}
