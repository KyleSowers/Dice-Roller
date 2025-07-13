package com.zybooks.diceroller;

import java.util.Random;

public class Dice {

    public static int LARGEST_NUM = 6;
    public static int SMALLEST_NUM = 1;

    private int mNumber = SMALLEST_NUM;
    private int mImageId;
    private Random mRandomGenerator;


    public Dice(int number) {                   // Dice constructor
        setNumber(number);                      // sets the number provided (if valid)
        mRandomGenerator = new Random();        // initializes the random number generator
    }

    public int getNumber() {                    // Getter
        return mNumber;                         // returns the current number showing on the dice
    }

    public void setNumber(int number) {                             // setter
        if (number >= SMALLEST_NUM && number <= LARGEST_NUM) {      // checks if number is between 1 and 6
            mNumber = number;                           // if valid, sets mNumber
            switch (number) {                           // maps that number to a drawable image for display in UI
                case 1:
                    mImageId = R.drawable.dice_1;
                    break;
                case 2:
                    mImageId = R.drawable.dice_2;
                    break;
                case 3:
                    mImageId = R.drawable.dice_3;
                    break;
                case 4:
                    mImageId = R.drawable.dice_4;
                    break;
                case 5:
                    mImageId = R.drawable.dice_5;
                    break;
                case 6:
                    mImageId = R.drawable.dice_6;
                    break;
            }
        }
    }

    public int getImageId() {                  // returns image resource ID for the current die
        return mImageId;
    }

    public void addOne() {                    // increments the number by one
        setNumber(mNumber + 1);               // uses setNumber() ensuring number is valid and updates image
    }

    public void subtractOne() {               // decrease the number by one
        setNumber(mNumber - 1);               // uses setNumber() ensuring number is valid and updates image
    }

    public void roll() {                      // generate random number 1-6(inclusive)
        setNumber(mRandomGenerator.nextInt(LARGEST_NUM) +1); // uses setNumber() ensuring number is valid and updates image
    }
}
