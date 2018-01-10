package com.example.edwardnguyen.a4;

import java.util.ArrayList;
import java.util.Observable;

/**
 * Created by edwardnguyen on 2017-12-01.
 */

public class Simon extends Observable{
    // possible game states:
    // PAUSE - nothing happening
    // COMPUTER - computer is play a sequence of buttons
    // HUMAN - human is guessing the sequence of buttons
    // LOSE and WIN - game is over in one of thise states
    public enum State { START, COMPUTER, HUMAN, LOSE, WIN };

    public void init(int _buttons, boolean _debug){

        // true will output additional information
        // (you will want to turn this off before)
        debug = _debug;

        length = 1;
        buttons = _buttons;
        state = State.START;
        score = 0;

        if (debug) {
            System.out.println("[DEBUG] starting " + buttons + " button game");
        }
    }

    public Simon(int _buttons) {
        init(_buttons, false);
    }

    public Simon(int _buttons, boolean _debug) {
        init(_buttons, _debug);
    }

    // the game state and score
    State state;

    int score;

    // length of sequence
    int length;
    // number of possible buttons
    int buttons;

    // the sequence of buttons and current button
    ArrayList<Integer> sequence = new ArrayList<>();
    int index;

    boolean debug;

    public int getNumButtons() { return buttons; }

    public int getScore() { return score; }

    public State getState() { return state; }

    public String getStateAsString() {

        switch (getState()) {
            case START:
                return "START";

            case COMPUTER:
                return "COMPUTER";


            case HUMAN:
                return "HUMAN";

            case LOSE:
                return "LOSE";


            case WIN:
                return "WIN";

            default:
                return "Unkown State";
        }
    }

    public void newRound() {

        if (debug) {
            System.out.println("[DEBUG] newRound, Simon::state " + getStateAsString());
        }

        // reset if they lost last time
        if (state == State.LOSE) {
            if (debug) {
                System.out.println("[DEBUG] reset length and score after loss");
            }
            length = 1;
            score = 0;
        }

        sequence.clear();

        if (debug) {
            System.out.println("[DEBUG] new sequence: ");
        }

        int min = 0;
        int max = buttons-1;

        for (int i = 0; i < length; i++) {
            int b = min + (int)(Math.random() * ((max - min) + 1));
            sequence.add(b);
            if (debug) {
                System.out.print( b + " ");
            }
        }
        if (debug) {
            System.out.println();
        }

        index = 0;
        state = State.COMPUTER;

    }

    // call this to get next button to show when computer is playing
    public int nextButton() {

        if (state != State.COMPUTER) {
            System.out.println("[WARNING] nextButton called in " + getStateAsString());
            return -1;
        }

        // this is the next button to show in the sequence
        int button = sequence.get(index);

        if (debug) {
            System.out.println("[DEBUG] nextButton:  index " + index + " button " + button);
        }

        // advance to next button
        index++;

        // if all the buttons were shown, give
        // the human a chance to guess the sequence
        if (index >= sequence.size()) {
            index = 0;
            state = State.HUMAN;
        }
        return button;
    }

    public boolean verifyButton(int button) {

        if (state != State.HUMAN) {
            System.out.println("[WARNING] verifyButton called in " + getStateAsString());
            return false;
        }

        // did they press the right button?
        boolean correct = (button == sequence.get(index));

        if (debug) {
            System.out.print("[DEBUG] verifyButton: index " + index + ", pushed " + button + ", sequence " + sequence.get(index));
        }

        // advance to next button
        index++;

        // pushed the wrong buttons
        if (!correct) {
            state = State.LOSE;
            if (debug) {
                System.out.println(", wrong. ");
                System.out.println("[DEBUG] state is now " + getStateAsString());
            }

            // they got it right
        } else {
            if (debug) {
                System.out.println(", correct.");
            }

            // if last button, then the win the round
            if (index == sequence.size()) {
                state = State.WIN;
                // update the score and increase the difficulty
                score++;
                length++;

                if (debug) {
                    System.out.println("[DEBUG] state is now " + getStateAsString());
                    System.out.println( "[DEBUG] new score " + score + ", length increased to " + length);
                }
            }
        }
        return correct;
    }


}
