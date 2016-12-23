package com.silentstarelly.catanpointscounter;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private ListView mPreviousGamesList;
    private ArrayAdapter<String> mGamesAdapter;
    private ArrayList<String> mPreviousGames;
    private int mI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mI = 0;

        mPreviousGames = new ArrayList<String>();

        mPreviousGamesList = (ListView) findViewById(R.id.previous_games);
        mPreviousGames.add("First game!");
        mGamesAdapter = new ArrayAdapter<String>(this, R.layout.games_list_item, mPreviousGames);
        mPreviousGamesList.setAdapter(mGamesAdapter);
        FloatingActionButton addNewGameButton = (FloatingActionButton) findViewById(R.id.add_game_button);
        addNewGameButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.add_game_button:
                if (mPreviousGamesList != null) {
                    mI++;
                    mPreviousGames.add("Game " + mI);
                    mGamesAdapter.notifyDataSetChanged();

                    // TODO: Should go to "create new game" activity instead
                    Intent intent = new Intent(this, GameActivity.class);
                    startActivity(intent);

                }
                break;
        }
    }


}

