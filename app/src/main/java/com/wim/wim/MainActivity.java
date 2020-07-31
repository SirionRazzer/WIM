package com.wim.wim;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.wim.wim.adapter.RecyclerViewHorizontalListAdapter;
import com.wim.wim.model.Card;
import com.wim.wim.util.Pair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final String ZERO = "0";
    private static final String EMPTY = "";
    private static final int MULTIPLIER = 100; //TODO set to 100
    private static final String DATA = "data.txt";

    private TextView level;
    private TextView total;
    private TextView value;

    Integer m_left_prev_value;
    Integer m_right_prev_value;

    private TextView left_prev_word;
    private TextView left_prev_value;
    private TextView right_prev_word;
    private TextView right_prev_value;

    private Button left_button;
    private Button right_button;

    private ProgressBar progress_bar;

    private RecyclerView cardRecyclerView;
    private RecyclerViewHorizontalListAdapter cardAdapter;

    private LinkedList<Pair<String, Integer>> word_counts_list;
    private Iterator<Pair<String, Integer>> iterator_list;

    private List<Card> cardList = new ArrayList<>();
    private CardManager cardManager;

    private String left_key = "";
    private String right_key = "";

    private String[] mColorArray = {"red", "pink", "purple", "deep_purple",
            "indigo", "blue", "light_blue", "cyan", "teal", "green",
            "light_green", "lime", "yellow", "amber", "orange", "deep_orange",
            "brown", "grey", "blue_grey", "black"
    };

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        //preserve string contents from views
        outState.putString("value", value.getText().toString());
        outState.putString("level", level.getText().toString());
        outState.putString("total", total.getText().toString());
        outState.putString("left_button_word", left_button.getText().toString());
        outState.putString("right_button_word", right_button.getText().toString());
        outState.putString("left_prev_word", left_prev_word.getText().toString());
        outState.putString("left_prev_value", left_prev_value.getText().toString());
        outState.putString("right_prev_word", right_prev_word.getText().toString());
        outState.putString("right_prev_value", right_prev_value.getText().toString());
        outState.putInt("progress_bar_value", progress_bar.getProgress());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        cardRecyclerView = findViewById(R.id.idRecyclerViewHorizontalList);

        //decorate cards for more clarity with divider
        //cardRecyclerView.addItemDecoration(new DividerItemDecoration(MainActivity.this, LinearLayoutManager.HORIZONTAL));

        cardAdapter = new RecyclerViewHorizontalListAdapter(cardList, getApplicationContext());
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false);
        cardRecyclerView.setLayoutManager(horizontalLayoutManager);
        cardRecyclerView.setAdapter(cardAdapter);
        //populatecardList();

        value = (TextView) findViewById(R.id.value);
        level = (TextView) findViewById(R.id.level);
        total = (TextView) findViewById(R.id.total);
        left_prev_word = (TextView) findViewById(R.id.left_prev_word);
        left_prev_value = (TextView) findViewById(R.id.left_prev_value);
        right_prev_value = (TextView) findViewById(R.id.right_prev_value);
        right_prev_word = (TextView) findViewById(R.id.right_prev_word);
        left_button = (Button) findViewById(R.id.button_left);
        right_button = (Button) findViewById(R.id.button_right);
        progress_bar = (ProgressBar) findViewById(R.id.progress_bar);

        word_counts_list = new LinkedList<>();
        cardManager = new CardManager(this);
        readFile(DATA);
        initialize(savedInstanceState);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId()) {
            case R.id.action_restart:
                turn(true);
                return true;
            case R.id.action_contact:
                Intent intent = new Intent(this, Contact.class);
                startActivity(intent);
                return true;
            default:
                // Do nothing
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Opens file with words and their values.
     * Fills map with these values.
     */
    private void readFile(String fileName) {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(getAssets().open(fileName)));
            String[] strings; // key and value
            for (String line = br.readLine(); line != null; line = br.readLine()) {
                strings = line.split("\\s|\\.|,|;");

                word_counts_list.add(new Pair<String, Integer>(strings[0], Integer.parseInt(strings[1])));

            }
        } catch (IOException e) {
            Toast.makeText(this, "Error: Can't open data assets", Toast.LENGTH_SHORT).show();
            Log.d("IOException: ", "Error: Can't open data assets");
        }
    }

    private void shuffleWordCounts (){
        Collections.shuffle(word_counts_list);
    }

    /**
     * Fills data into buttons and views from previous instance or starts new game.
     *
     * @param savedInstanceState
     */
    private void initialize(Bundle savedInstanceState) {

        if (savedInstanceState != null) {
            value.setText(savedInstanceState.getString("value"));
            total.setText(savedInstanceState.getString("total"));
            level.setText(savedInstanceState.getString("level"));
            left_button.setText(savedInstanceState.getString("left_button_word"));
            right_button.setText(savedInstanceState.getString("right_button_word"));
            progress_bar.setProgress(savedInstanceState.getInt("progress_bar_value"));

            // restore text content
            left_prev_word.setText(savedInstanceState.getString("left_prev_word"));
            left_prev_value.setText(savedInstanceState.getString("left_prev_value"));
            right_prev_word.setText(savedInstanceState.getString("right_prev_word"));
            right_prev_value.setText(savedInstanceState.getString("right_prev_value"));

        } else {
            turn(true);
        }
    }

    /**
     * Insert data on new game or on new round pass data from buttons to textviews below.
     */
    private void turn(boolean isNewGame) {
        if (isNewGame) {
            // set initial zero/empty values and
            shuffleWordCounts();
            value.setText(ZERO);
            total.setText(ZERO);
            level.setText(ZERO);
            left_prev_word.setText(EMPTY);
            left_prev_value.setText(EMPTY);
            right_prev_word.setText(EMPTY);
            right_prev_value.setText(EMPTY);
            progress_bar.setProgress(0);

            iterator_list = word_counts_list.iterator(); // initialize iterator


            if (iterator_list.hasNext()) {
                Pair p = iterator_list.next();
                left_key = (String) p.getLeft();
                m_left_prev_value = (Integer) p.getRight();

                if (!iterator_list.hasNext()) {
                    iterator_list = word_counts_list.iterator(); // reset iterator
                }

                p = iterator_list.next();
                right_key = (String)p.getLeft();
                m_right_prev_value = (Integer)p.getRight();
            }

            left_button.setText(left_key);
            right_button.setText(right_key);

            // clear cards and shuffle them in the background
            cardManager.shuffleCards();
            cardList.clear();
            cardAdapter.notifyDataSetChanged();
        } else { // this branch is used each game round except for the start/resume activity
            // update values and set buttons as in previous branch
            left_prev_word.setText(left_key);
            left_prev_value.setText(m_left_prev_value.toString());

            right_prev_word.setText(right_key);
            right_prev_value.setText(m_right_prev_value.toString());

            if (!iterator_list.hasNext()) {
                iterator_list = word_counts_list.iterator();
            }

            Pair p = iterator_list.next();
            left_key = (String) p.getLeft();
            m_left_prev_value = (Integer) p.getRight();
            left_button.setText(left_key);

            if (!iterator_list.hasNext()) {
                iterator_list = word_counts_list.iterator();
            }

            p = iterator_list.next();
            right_key = (String) p.getLeft();
            m_right_prev_value = (Integer) p.getRight();
            right_button.setText(right_key);
        }
    }

    public void leftButtonOnClick(View view) {
        if (m_left_prev_value == m_right_prev_value) {
            Toast.makeText(this, "They are the same!", Toast.LENGTH_SHORT).show(); //TODO extract string values to constant
            turn(false);
        } else if (m_left_prev_value > m_right_prev_value) {
            Integer total_value = Integer.parseInt(total.getText().toString());
            total_value += m_left_prev_value;
            total.setText(total_value.toString());
            value.setText(m_left_prev_value.toString());
            updateProgressBar(total_value);
        } else {
            Integer total_value = Integer.parseInt(total.getText().toString());
            if (total_value - m_left_prev_value >= 0) {
                total_value -= m_left_prev_value;
            } else {
                total_value = 0;
            }
            total.setText(total_value.toString());
            value.setText("-" + m_left_prev_value.toString());
            updateProgressBar(total_value);
        }
    }

    public void rightButtonOnClick(View view) {
        if (m_left_prev_value == m_right_prev_value) {
            Toast.makeText(this, "They are the same!", Toast.LENGTH_SHORT).show(); //TODO extract string values to constant
            turn(false);
        } else if (m_left_prev_value > m_right_prev_value) {
            Integer total_value = Integer.parseInt(total.getText().toString());
            if (total_value - m_right_prev_value >= 0) {
                total_value -= m_right_prev_value;
            } else {
                total_value = 0;
            }
            total.setText(total_value.toString());
            value.setText("-" + m_right_prev_value.toString());
            updateProgressBar(total_value);
        } else {
            Integer total_value = Integer.parseInt(total.getText().toString());
            total_value += m_right_prev_value;
            total.setText(total_value.toString());
            value.setText(m_right_prev_value.toString());
            updateProgressBar(total_value);
        }
    }

    private void updateProgressBar(int value) {

        // check for level up
        if (value / MULTIPLIER >= 100) {
            levelUp();
        } else {
            progress_bar.setProgress(value / MULTIPLIER);
            turn(false);
        }
    }

    private void levelUp() {
        //shuffleWordCounts();
        Toast.makeText(this, "Level Up!", Toast.LENGTH_SHORT).show();
        int currentLevel = Integer.parseInt(level.getText().toString());
        currentLevel++;
        level.setText(Integer.toString(currentLevel));

        // hide quote placeholder
        //TODO

        // show next card
        Card card = cardManager.getNextCard();
        cardList.add(card);

        cardAdapter.notifyDataSetChanged();

        // clear progress bar
        progress_bar.setProgress(0);

        // reset total score
        total.setText("0");
        turn(false);
    }
}