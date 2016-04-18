package com.ar.rossier.usc_ar_app.dBase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by itsfunshine on 3/28/2016.
 */
public class DatabaseContent {

    /**
     * An array of sample (dummy) items.
     */
    public final List<DBItem> ITEMS = new ArrayList<DBItem>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public final Map<String, DBItem> ITEM_MAP = new HashMap<String, DBItem>();

    private final int COUNT = 25;

    public DatabaseContent() {
        // Add instruction item.
        addItem( new DBItem("Add New Database", "null", "null", true, ""));
    }

    private void addItem(DBItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    private void createDBItem(String id, String secret, String key, boolean blankItem) {
        addItem(new DBItem(id, secret, key, blankItem, ""));
        //return new DBItem(id, secret, key);
    }

    private String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about Item: ").append(position);
        for (int i = 0; i < position; i++) {
            builder.append("\nMore details information here.");
        }
        return builder.toString();
    }

    /**
     * A dummy item representing a piece of content.
     */
    public static class DBItem {
        public final String id;
        public final String secret;
        public final String key;
        public final boolean blankItem;
        public final String tag;

        public DBItem(String id, String secret, String key, boolean blank, String tag) {
            this.id = id;
            this.secret = secret;
            this.key = key;
            this.blankItem = blank;
            this.tag = tag;
        }

        @Override
        public String toString() {
            return id;
        }
    }
}

