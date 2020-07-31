package com.wim.wim;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;
import android.widget.Toast;

import com.wim.wim.model.Card;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Creates cards from quotes and other assets
 */
public class CardManager {

    Integer cardsCount = 0;
    LinkedList<Card> cards;
    int cardPtr; // points to next unused card in cards list
    private AssetManager assetManager;
    private Context context;
    private String fileName = "quotes.txt";

    public CardManager(Context myContext) {
        this.context = myContext;
        cards = new LinkedList<Card>();
        cardsCount = cards.size();
        assetManager = myContext.getAssets();
        readFile(fileName);
        Collections.shuffle(cards);
    }

    // iterable collection with quotes and other assets
    private void readFile(String fileName) {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(assetManager.open(fileName)));
            String[] strings;
            for (String line = br.readLine(); line != null; line = br.readLine()) {
                strings = line.split(";");
                insertCard(strings[0], strings[1]);
            }
        } catch (IOException e) {
            Toast.makeText(context, "Error: Can't open data assets", Toast.LENGTH_SHORT).show();
            Log.d("IOException: ", "Error: Can't open data assets");
        }
    }

    private void insertCard(String text, String header) {
        Card card = new Card(header, text);
        cardsCount++;
        cards.add(card);
    }

    public Card getNextCard() {
        Log.d("CARDCOUNT", cardsCount.toString());
        if (cardPtr >= cardsCount) {
            cardPtr = 0;
            return cards.get(cardPtr);
        } else {
            return cards.get(cardPtr++);
        }
    }

    public void shuffleCards() {
        Collections.shuffle(cards);
    }
}
