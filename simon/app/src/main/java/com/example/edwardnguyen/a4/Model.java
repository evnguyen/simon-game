package com.example.edwardnguyen.a4;

import android.os.Handler;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by edwardnguyen on 2017-12-02.
 */

public class Model extends Observable {
    // Create static instance of this mModel
    private static final Model ourInstance = new Model();

    static Model getInstance() {
        return ourInstance;
    }

    private int number_buttons = 4;

    //Easy = 0, Normal = 1, Hard = 2
    private int difficulty = 1;
    private int difficulty_speed = 1000;

    Simon simon = new Simon(number_buttons, true);

    int active_button = -1;
    boolean computer_playing = false;
    boolean game_started = false;
    int human_clicks = 0;


    public void setNumButtons(int n) {
        number_buttons = n;
        setChangedAndNotify();
    }

    public int getNumButtons() {
        return number_buttons;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public String getDifficultyAsString() {
        String s = "Difficulty: ";
        switch (difficulty) {
            case 0:
                s += "Easy";
                break;
            case 1:
                s += "Normal";
                break;
            case 2:
                s += "Hard";
                break;
        }

        return s;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
        setChangedAndNotify();
    }

    public int getDifficulty_speed() {
        return difficulty_speed;
    }

    public void setDifficulty_speed(int difficult) {
        switch (difficulty) {
            case 0:
                difficulty_speed = 2000;
                break;
            case 1:
                difficulty_speed = 1000;
                break;
            case 2:
                difficulty_speed = 500;
                break;
        }
    }

    public int getScore() {
        return simon.getScore();
    }

    public void delayNotify(int t, final int i, final int button_id) {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                active_button = button_id;

                //Check if this is computer's last play
                if(i == simon.sequence.size()-1) {

                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            computer_playing = false;
                            setChangedAndNotify();
                        }
                    }, 500);
                }

                setChangedAndNotify();
                active_button = -1;

            }
        }, t + (t*i));

    }

    public void startGame() {
        game_started = true;
        computer_playing = true;
        //Update on start game messages

        simon.newRound();
        setChangedAndNotify();

        int i = 0;
        while (simon.getState() == Simon.State.COMPUTER){
            active_button = simon.nextButton();
            delayNotify(getDifficulty_speed(), i, active_button);
            i++;

        }
    }

    public void humanClickHandler(int id) {
        human_clicks++;
        simon.verifyButton(id);
        //Is this the last button to be clicked?
        if(simon.getState() != Simon.State.HUMAN){
            game_started = false;
            human_clicks = 0;
        }
        setChangedAndNotify();
    }

    public boolean isComputer_playing() {
        return computer_playing;
    }

    public boolean isGame_started() {
        return game_started;
    }

    public void resetSimon() {
        simon = new Simon(number_buttons, true);
        game_started = false;
    }

    public void settings_prompt_button_handler() {
        resetSimon();
        setChangedAndNotify();
    }

    /**
     * Helper method to make it easier to initialize all observers
     */
    void setChangedAndNotify() {
        setChanged();
        notifyObservers();
    }

    /**
     * Adds an observer to the set of observers for this object, provided
     * that it is not the same as some observer already in the set.
     * The order in which notifications will be delivered to multiple
     * observers is not specified. See the class comment.
     *
     * @param o an observer to be added.
     * @throws NullPointerException if the parameter o is null.
     */
    @Override
    public synchronized void addObserver(Observer o) {
        super.addObserver(o);
    }


    /**
     *If this object has changed, as indicated by the
     * <code>hasChanged</code> method, then notify all of its observers
     * and then call the <code>clearChanged</code> method to
     * indicate that this object has no longer changed.
     * <p>
     * Each observer has its <code>update</code> method called with two
     * arguments: this observable object and <code>null</code>. In other
     * words, this method is equivalent to:
            * <blockquote><tt>
     * notifyObservers(null)</tt></blockquote>
            *
            * @see Observable#clearChanged()
     * @see Observable#hasChanged()
     * @see Observer#update(Observable, Object)
     */
    @Override
    public void notifyObservers() {
        super.notifyObservers();
    }

}
