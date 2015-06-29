package net.maiatoday.minotaur.utils.db;

import android.support.annotation.IntDef;

import java.util.Random;

/**
 * Fake data to fill the db
 * Created by maia on 2015/06/28.
 */
public class FakeData {

    @IntDef({TYPE_CAVE, TYPE_CAVERN, TYPE_PASSAGE})
    public @interface RoomType {
    }

    public static final int TYPE_CAVE = 0;
    public static final int TYPE_CAVERN = 1;
    public static final int TYPE_PASSAGE = 2;

    public static final String[] HERRING = {"herring", "wooden spoon", "damp rag", "dustbunny", "dog hair", "toothpick", "faded credit card slip"};
    public static final String[] TREASURE = {"gold coin", "brass ring", "RAM upgrade", "steaming cappuccino", "SD card with music", "battery pack", "gummy worm", "churros and caramel"};
    public static final String[] ADJECTIVES = {"damp", "luminescent", "vaulted", "dusty", "warm", "humid", "dry", "slippery", "crepuscular", "shadowy", "magical"};
    public static final String[] CAVE_NAME = {"cave", "cavern", "grotto", "hollow", "chamber", "den", "crypt", "cistern"};
    public static final String[] PASSAGE_NAME = {"tunnel", "chimney", "passage", "channel", "hole", "mine", "pit", "shaft", "subway", "burrow", "tube", "crawlspace"};

    private static Random dice = new Random();

    public static String getHerring() {
        return HERRING[dice.nextInt(HERRING.length)];
    }

    public static String getTreasure() {
        return TREASURE[dice.nextInt(TREASURE.length)];
    }

    public static String getCave() {
        return ADJECTIVES[dice.nextInt(ADJECTIVES.length)] + " " + CAVE_NAME[dice.nextInt(CAVE_NAME.length)];
    }

    public static String getPassage() {
        return ADJECTIVES[dice.nextInt(ADJECTIVES.length)] + " " + PASSAGE_NAME[dice.nextInt(PASSAGE_NAME.length)];
    }
}
