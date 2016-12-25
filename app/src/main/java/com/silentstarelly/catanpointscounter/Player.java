package com.silentstarelly.catanpointscounter;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by elena on 12/23/2016.
 */

public class Player {
    enum Color {
      BLUE, RED, ORANGE, WHITE, GREEN, BROWN, NONE
    };
    enum Metropolis {
        BLUE, GREEN, YELLOW, NONE
    };

    String name;
    boolean merchant;
    ArrayList<Metropolis> metropolises;
    boolean longestRoad;

    boolean largestArmy; // TODO: Need to add support / stealing for this

    int numSettlements;
    int numCities;
    int numProgressCardPoints;
    int numTimesDefender;

    Color color;

    public Player() {
        this("", Color.NONE);
    }

    public Player(String playerName, Color c) {
        name = playerName;

        merchant = false;
        metropolises = new ArrayList<>();
        longestRoad = false;

        numSettlements = 1;
        numCities = 0;
        numProgressCardPoints = 0;
        numTimesDefender = 0;

        color = c;
    }

    public Player(String playerName) {
        this(playerName, Color.NONE);
    }

    public void setColor(Color c) {
        color = c;
    }
    public Color getColor() {
        return color;
    }
    public void buildCity() {
        if (numSettlements < 1) {
            throw new CatanRuleViolation(name + " does not have any settlements to upgrade.");
        }
        // TODO: Check max number of cities
        if (numCities >= 6) {
            throw new CatanRuleViolation(name + " has already build all of their cities.");
        }
        numSettlements--;
        numCities++;
    }
    public void burnCity() {
        if (numCities < 1) {
            throw new CatanRuleViolation(name + " does not have a city to burn.");
        }
        numCities--;
        numSettlements++;
    }
    public void buildSettlement() {
        // TODO: Check how many settlements you actually have
        if (numSettlements >= 6) {
            throw new CatanRuleViolation(name + " has already built all their settlements.");
        }
        numSettlements++;
    }
    public void takeMetropolis(Metropolis metropolisWon) {
        Log.d("Catan", "  Player " + name + " is taking metropolis");
        if (numCities < 1) {
            throw new CatanRuleViolation(name + " does not have a city to put a metropolis on.");
        }
        if (metropolisWon != Metropolis.NONE) {
            metropolises.add(metropolisWon);
        }
    }
    public void loseMetropolis(Metropolis metropolisLost) {
        if (!metropolises.contains(metropolisLost)) {
            throw new CatanRuleViolation(name + " does not have that metropolis.");
        }
        metropolises.remove(metropolisLost);
    }
    public void addProgressCardPoint() {
        numProgressCardPoints++;
    }
    public void addDefenderOfCatan() {
        numTimesDefender++;
    }
    public void takeLongestRoad() {
        longestRoad = true;
    }
    public void loseLongestRoad() {
        if (!longestRoad) {
            throw new CatanRuleViolation(name + " did not have longest road to begin with.");
        }
        longestRoad = false;
    }
    public void takeMerchant() {
        merchant = true;
    }
    public void loseMerchant() {
        if (!merchant) {
            throw new CatanRuleViolation(name + " did not have the merchant to begin with.");
        }
        merchant = false;
    }

    public int getNumVictoryPoints() {
        return numSettlements + (2 * numCities) + numTimesDefender + numProgressCardPoints +
                (2 * metropolises.size()) + (merchant? 1 : 0) + (longestRoad ? 2 : 0);
    }

    public void setName(String n) {
        name = n;
    }
    public String getName() {
        return name;
    }

    // TODO: Maybe have a "get victory points reasons" or something instead of all of these?
    public boolean hasMerchant() {
        return merchant;
    }
    public ArrayList<Metropolis> getMetropolises() {
        return metropolises;
    }
    public boolean hasLongestRoad() {
        return longestRoad;
    }

    public int getNumSettlements() {
        return numSettlements;
    }
    public int getNumCities() {
        return numCities;
    }
    public int getNumProgressCardPoints() {
        return numProgressCardPoints;
    }
    public int getNumTimesDefender() {
        return numTimesDefender;
    }
}

