package com.silentstarelly.catanpointscounter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class VersionPickActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_version_pick);

        findViewById(R.id.base_game).setOnClickListener(this);
        findViewById(R.id.seafarers).setOnClickListener(this);
        findViewById(R.id.cities_and_knights).setOnClickListener(this);
        findViewById(R.id.traders_and_barbarians).setOnClickListener(this);
        findViewById(R.id.explorers_and_pirates).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, GameCreateActivity.class);
        Player startingPlayerTemplate = new Player();
        startingPlayerTemplate.buildSettlement();
        startingPlayerTemplate.buildSettlement();

        switch (v.getId()) {
            case R.id.seafarers:
                break;
            case R.id.cities_and_knights:
                startingPlayerTemplate.buildCity();
                break;
            case R.id.traders_and_barbarians:
                break;
            case R.id.explorers_and_pirates:
                break;
            default: // Base game
                startingPlayerTemplate.buildSettlement();
                break;
        }

        intent.putExtra("startingPlayerTemplate", startingPlayerTemplate);
        intent.putExtra("version", v.getId());
        startActivity(intent);
    }
}
