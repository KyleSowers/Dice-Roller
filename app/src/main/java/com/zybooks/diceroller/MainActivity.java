package com.zybooks.diceroller;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity implements RollLengthDialogFragment.OnRollLengthSelectedListener {

    public static final int MAX_DICE = 3;
    public int mVisibleDice;
    private Dice[] mDice;
    private ImageView[] mDiceImageViews;
    private Menu mMenu;
    private CountDownTimer mTimer;
    private long mTimerLength = 2000;
    private int mCurrentDie;
    private  int mInitX;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create an array of dice
        mDice = new Dice[MAX_DICE];
        for (int i = 0; i < MAX_DICE; i++) {
            mDice[i] = new Dice(i + 1);
        }

        // Create an array of ImageVIews
        mDiceImageViews = new ImageView[MAX_DICE];
        mDiceImageViews[0] = findViewById(R.id.dice1);
        mDiceImageViews[1] = findViewById(R.id.dice2);
        mDiceImageViews[2] = findViewById(R.id.dice3);

        // All dice are initially visible
        mVisibleDice = MAX_DICE;

        showDice();

//        registerForContextMenu(mDiceImageViews[0]);
        // Register context menus for all dice and tag each die
        for (int i = 0; i < mDiceImageViews.length; i++) {
            registerForContextMenu(mDiceImageViews[i]);
            mDiceImageViews[i].setTag(i);
        }

        // Moving finger left or right changes dice number
        mDiceImageViews[0].setOnTouchListener((v, event) -> {
            int action = event.getAction();
            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    mInitX = (int) event.getX();
                    return true;
                case MotionEvent.ACTION_MOVE:
                    int x = (int) event.getX();

                    // See if movement is at least 20 pixels
                    if (Math.abs(x - mInitX) >= 20) {
                        if (x > mInitX) {
                            mDice[0].addOne();
                        } else {
                            mDice[0].subtractOne();
                        }
                        showDice();
                        mInitX = x;
                    }
                    return true;
            }
            return false;
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.appbar_menu, menu);
        mMenu = menu;
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

        super.onCreateContextMenu(menu,v, menuInfo);
        mCurrentDie = (int) v.getTag();                 // Which die is selected?
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);
    }

    private void showDice() {

        // Display only the number of dice visible
        for (int i = 0; i < mVisibleDice; i++) {
            Drawable diceDrawable = ContextCompat.getDrawable(this, mDice[i].getImageId());
            mDiceImageViews[i].setImageDrawable(diceDrawable);
            mDiceImageViews[i].setContentDescription(Integer.toString(mDice[i].getNumber()));
        }
    }

    @Override
    public void onRollLengthClick(int which) {
        // Convert to milliseconds
        mTimerLength = 1000L * (which + 1);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        // Determine which menu option was chosen
        if (item.getItemId() == R.id.action_one) {
            changeDiceVisibility(1);
            showDice();
            return true;
        } else if (item.getItemId() == R.id.action_two) {
            changeDiceVisibility(2);
            showDice();
            return true;
        } else if (item.getItemId() == R.id.action_three) {
            changeDiceVisibility(3);
            showDice();
            return true;
        } else if (item.getItemId() == R.id.action_stop) {
            mTimer.cancel();
            item.setVisible(false);
            return true;
        } else if (item.getItemId() == R.id.action_roll) {
            rollDice();
            return true;
        } else if (item.getItemId() == R.id.action_roll_length) {
            RollLengthDialogFragment dialog = new RollLengthDialogFragment();
            dialog.show(getSupportFragmentManager(), "rollLengthDialog");
            return true;
        }

        return  super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.add_one) {
            mDice[mCurrentDie].addOne();
            showDice();
            return true;
        } else if (item.getItemId() == R.id.subtract_one) {
            mDice[mCurrentDie].subtractOne();
            showDice();
            return true;
        } else if (item.getItemId() == R.id.roll) {
            rollDice();
            return true;
        }

        return super.onContextItemSelected(item);
    }

    private void changeDiceVisibility(int numVisible) {
        mVisibleDice = numVisible;

        // Make dice visible
        for (int i = 0; i < numVisible; i++) {
            mDiceImageViews[i].setVisibility(View.VISIBLE);
        }

        // Hide remaining dice
        for (int i = numVisible; i < MAX_DICE; i++) {
            mDiceImageViews[i].setVisibility(View.GONE);
        }
    }

    private void rollDice() {
        mMenu.findItem(R.id.action_stop).setVisible(true);

        if (mTimer != null) {
            mTimer.cancel();
        }

        mTimer = new CountDownTimer(mTimerLength, 100) {
            public  void onTick(long millisUnitFinished) {
                for (int i = 0; i < mVisibleDice; i++) {
                    mDice[i].roll();
                }
                showDice();
            }
            public  void onFinish() {
                mMenu.findItem(R.id.action_stop).setVisible(false);
            }
        }.start();
    }

}