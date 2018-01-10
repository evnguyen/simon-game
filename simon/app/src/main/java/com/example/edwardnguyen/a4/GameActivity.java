package com.example.edwardnguyen.a4;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;


public class GameActivity extends AppCompatActivity implements Observer {
    GridLayout grid;
    Model model;
    Button begin;
    TextView difficulty_message, score_message, result_message;
    ArrayList<AnimationDrawable> animations = new ArrayList<>();
    ArrayList<TextView> simon_buttons = new ArrayList<>();
    AlertDialog.Builder alertDialogBuilder;
    AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set Content View
        setContentView(R.layout.activity_game);

        model = Model.getInstance();
        model.addObserver(this);

        grid = (GridLayout) findViewById(R.id.gridlayout);

        //Display the back button on the menu bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        makeButtons();

        //Set up text for difficulty message
        difficulty_message = (TextView) findViewById(R.id.diffculty_message);
        String diffuclty = "Difficulty: " + model.getDifficulty();
        difficulty_message.setText(diffuclty);


        //Set up text for score message
        score_message = (TextView) findViewById(R.id.score_message);
        String score = "Score: " + model.getScore();
        score_message.setText(score);

        //Set up begin button
        begin = (Button) findViewById(R.id.begin);
        String label = "Begin Round " + (model.getScore() + 1);
        begin.setText(label);
        begin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                model.startGame();
            }
        });

        //Set up Result message
        result_message = (TextView) findViewById(R.id.result_message);

        //Set up alert dialog for when player loses
        alertDialogBuilder = new AlertDialog.Builder(GameActivity.this, R.style.DialogTheme);
        alertDialogBuilder.setTitle("You lose!");
        alertDialogBuilder.setMessage("Perhaps this is too hard? \nChoose either to go back to main menu, continue or change settings");
        alertDialogBuilder.setCancelable(false);

        //Set buttons for alert
        alertDialogBuilder.setNeutralButton("Main Menu", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog,int id) {
                Intent intent = new Intent(GameActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        alertDialogBuilder.setNegativeButton("Continue", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }

        });
        alertDialogBuilder.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int id) {
                model.settings_prompt_button_handler();
                Intent intent = new Intent(GameActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });

        alertDialog = alertDialogBuilder.create();

        model.setChangedAndNotify();

    }

    protected void makeButtons(){
        grid.setColumnCount(2);
        grid.setRowCount(3);
        animations.clear();
        simon_buttons.clear();

        //Begin adding the simon buttons
        int num_buttons = model.getNumButtons();
        GridLayout.LayoutParams param;
        for (int i = 0, c = 0, r = 0; i < num_buttons; i++, c++){
            if (c == 2) {
                c = 0;
                r++;
            }

            final TextView button = new TextView(this);
            button.setBackgroundResource(R.drawable.button_animation);
            button.setTextColor(Color.WHITE);
            String s = "" + (i+1);
            button.setText(s);
            button.setId(i);
            button.setGravity(Gravity.CENTER);
            final AnimationDrawable button_animation = (AnimationDrawable) button.getBackground();
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(model.simon.getStateAsString().equals("HUMAN") && !model.isComputer_playing()){
                        button_animation.stop();
                        button.setBackgroundResource(R.drawable.button_animation);
                        button_animation.start();
                        model.humanClickHandler(button.getId());
                    }
                    //Toast.makeText(getBaseContext(), button.getText().toString(), Toast.LENGTH_SHORT).show();
                }
            });

            param = new GridLayout.LayoutParams();
            param.height = 300;
            param.width = 300;
            param.rightMargin = 125;
            param.leftMargin = 125;
            param.topMargin = 30;
            param.bottomMargin = 30;
            param.setGravity(Gravity.CENTER);
            param.columnSpec = GridLayout.spec(c);
            param.rowSpec = GridLayout.spec(r);

            animations.add(button_animation);
            simon_buttons.add(button);

            grid.addView(button, param);

        }//Finished adding simon buttons
    }


    @Override
    protected void onResume() {
        super.onResume();
        alertDialog.cancel();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Remove observer when activity is destroyed.
        //mModel.deleteObserver(this);
    }

    /**
     * Initialize the contents of the Activity's standard options menu.  You
     * should place your menu items in to <var>menu</var>.
     * <p>
     * <p>This is only called once, the first time the options menu is
     * displayed.  To update the menu every time it is displayed, see
     * {@link #onPrepareOptionsMenu}.
     * <p>
     * <p>The default implementation populates the menu with standard system
     * menu items.  These are placed in the {@link Menu#CATEGORY_SYSTEM} group so that
     * they will be correctly ordered with application-defined menu items.
     * Deriving classes should always call through to the base implementation.
     * <p>
     * <p>You can safely hold on to <var>menu</var> (and any items created
     * from it), making modifications to it as desired, until the next
     * time onCreateOptionsMenu() is called.
     * <p>
     * <p>When you add items to the menu, you can implement the Activity's
     * {@link #onOptionsItemSelected} method to handle them there.
     *
     * @param menu The options menu in which you place your items.
     * @return You must return true for the menu to be displayed;
     * if you return false it will not be shown.
     * @see #onPrepareOptionsMenu
     * @see #onOptionsItemSelected
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        //MenuInflater inflater = getMenuInflater();
        //inflater.inflate(R.menu.view1, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * This hook is called whenever an item in your options menu is selected.
     * The default implementation simply returns false to have the normal
     * processing happen (calling the item's Runnable or sending a message to
     * its Handler as appropriate).  You can use this method for any items
     * for which you would like to do processing without those other
     * facilities.
     * <p>
     * <p>Derived classes should call through to the base class for it to
     * perform the default menu handling.</p>
     *
     * @param item The menu item that was selected.
     * @return boolean Return false to allow normal menu processing to
     * proceed, true to consume it here.
     * @see #onCreateOptionsMenu
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle interaction based on item selection
        switch (item.getItemId())
        {
            case android.R.id.home:
                Intent intent = new Intent(GameActivity.this, MainActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * This method is called whenever the observed object is changed. An
     * application calls an <tt>Observable</tt> object's
     * <code>notifyObservers</code> method to have all the object's
     * observers notified of the change.
     *
     * @param o   the observable object.
     * @param arg an argument passed to the <code>notifyObservers</code>
     */
    @Override
    public void update(Observable o, Object arg) {
        if(model.active_button != -1) {
            if(animations.size() <= model.active_button || animations.size() != model.getNumButtons()){
                makeButtons();
            }
            animations.get(model.active_button).stop();
            simon_buttons.get(model.active_button).setBackgroundResource(R.drawable.button_animation);
            animations.get(model.active_button).start();
        }

        String s;
        difficulty_message.setText(model.getDifficultyAsString());

        s = "Score: " + model.getScore();
        score_message.setText(s);

        s = "Begin Round " + (model.getScore() + 1);
        begin.setText(s);

        if (model.simon.getStateAsString().equals("START")){
            result_message.setText("Are You Ready?");
        }
        else if(model.simon.getStateAsString().equals("WIN") && !model.isGame_started()) {
            s = "Winner!";
            result_message.setText(s);
        }
        else if(model.simon.getStateAsString().equals("LOSE") && !model.isGame_started()){
            s = "You lose!";
            result_message.setText(s);

            alertDialog.show();

        }
        else if(model.isComputer_playing()){
            s = "Computer's turn!";
            result_message.setText(s);
        }
        else if(!model.isComputer_playing() && model.isGame_started()){
            s = "Human's turn!";
            result_message.setText(s);
        }

    }
}
