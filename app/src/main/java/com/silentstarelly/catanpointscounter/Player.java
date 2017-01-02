package com.silentstarelly.catanpointscounter;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by elena on 12/23/2016.
 */

public class Player implements Serializable {
    enum Color {
      BLUE, RED, ORANGE, WHITE, GREEN, BROWN, NONE
    };
    enum Metropolis {
        BLUE, GREEN, YELLOW, NONE
    };

    String name;
    Color color;
    boolean merchant;
    ArrayList<Metropolis> metropolises;
    boolean longestRoad;

    boolean largestArmy; // TODO: Need to add support / stealing for this

    int numSettlements;
    int numCities;
    int numCardPoints;
    int numTimesDefender;

    public Player() {
        this("", Color.NONE);
    }

    public Player(String playerName, Color c) {
        name = playerName;

        merchant = false;
        metropolises = new ArrayList<>();
        longestRoad = false;
        largestArmy = false;

        numSettlements = 0;
        numCities = 0;
        numCardPoints = 0;
        numTimesDefender = 0;

        color = c;
    }

    public Player(String playerName) {
        this(playerName, Color.NONE);
    }

    public Player(Player template) {
        this(template.getName(), template.getColor());
        this.merchant = template.hasMerchant();
        this.metropolises.addAll(template.getMetropolises());
        this.longestRoad = template.hasLongestRoad();
        this.largestArmy = template.hasLargestArmy();

        this.numSettlements = template.getNumSettlements();
        this.numCities = template.getNumCities();
        this.numCardPoints = template.getNumCardPoints();
        this.numTimesDefender = template.getNumTimesDefender();

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
        if (numCities >= 4) {
            throw new CatanRuleViolation(name + " has already build all of their cities.");
        }
        numSettlements--;
        numCities++;
    }
    public void burnCity() {
        if (numCities < 1) {
            throw new CatanRuleViolation(name + " does not have a city to burn.");
        }
        if (metropolises.size() == numCities) {
            throw new CatanRuleViolation(name + " cannot burn, as all cities have a metropolis.");
        }
        numCities--;
        numSettlements++;
    }
    public void buildSettlement() {
        if (numSettlements >= 5) {
            throw new CatanRuleViolation(name + " has already built all their settlements.");
        }
        numSettlements++;
    }
    public void takeMetropolis(Metropolis metropolisWon) {
        if (numCities <= metropolises.size()) {
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
    public void addCardPoint() {
        numCardPoints++;
    }
    public void addDefenderOfCatan() {
        numTimesDefender++;
    }
    public void takeLargestArmy() {
        largestArmy = true;
    }
    public void loseLargestArmy() {
        if (!largestArmy) {
            throw new CatanRuleViolation(name + " did not have largest army to begin with.");
        }
        largestArmy = false;
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
        return numSettlements + (2 * numCities) + numTimesDefender + numCardPoints +
                (2 * metropolises.size()) + (merchant? 1 : 0) + (longestRoad ? 2 : 0) +
                (largestArmy ? 2 : 0);
    }

    public void setName(String n) {
        name = n;
    }
    public String getName() {
        return name;
    }

    public StringBuilder getPointsBreakdownString(Integer versionID) {
        //TODO: Need to clean up overall, and add support for other expansions. Also need to consider
        // how to handle "custom expansion" case, where a user may have combined a few different
        // expansion packs. Maybe store version id in the player?
        if (versionID == null) {
            versionID = R.id.base_game;
        }


        StringBuilder points = new StringBuilder();
        points.append(Integer.toString(numSettlements) +
                (numSettlements == 1 ? " settlement\n" : " settlements\n"));
        points.append(Integer.toString(numCities) +
                (numCities == 1 ? " city\n" : " cities\n"));

        if (versionID == R.id.cities_and_knights) {
            points.append(Integer.toString(metropolises.size()) +
                    (metropolises.size() == 1 ? " metropolis\n" : " metropolises\n"));
            points.append(Integer.toString(numTimesDefender) + " Defender of Catan " +
                    (numTimesDefender == 1 ? "point\n" : "points\n"));
        }
        points.append(Integer.toString(numCardPoints) + " card victory " +
                (numCardPoints == 1 ? "point\n" : "points\n"));

        if (versionID != R.id.seafarers) {
            if (longestRoad) {
                points.append("Longest Road\n");
            }
        }
        if (versionID == R.id.base_game) {
            if (largestArmy) {
                points.append("Largest Army\n");
            }
        }
        if (versionID == R.id.cities_and_knights) {
            if (merchant) {
                points.append("Merchant\n");
            }
        }
        return points;
    }

    public boolean hasMerchant() {
        return merchant;
    }
    public ArrayList<Metropolis> getMetropolises() {
        return metropolises;
    }
    public boolean hasLongestRoad() {
        return longestRoad;
    }
    public boolean hasLargestArmy() {
        return largestArmy;
    }

    public int getNumSettlements() {
        return numSettlements;
    }
    public int getNumCities() {
        return numCities;
    }
    public int getNumCardPoints() {
        return numCardPoints;
    }
    public int getNumTimesDefender() {
        return numTimesDefender;
    }
}

