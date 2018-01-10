package com.example.edwardnguyen.a4;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Observable;
import java.util.Observer;
import java.util.Set;

public class SettingsActivity extends AppCompatActivity implements Observer {
    Spinner spinner_buttons;
    Spinner spinner_difficulty;
    Model model;
    Button apply;
    int number_buttons;
    int difficulty_setting;
    AlertDialog.Builder alertDialogBuilder;
    AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        model = Model.getInstance();
        model.addObserver(this);

        spinner_buttons = (Spinner) findViewById(R.id.spinner_buttons);
        String[] entries = new String[]{
                "1 button",
                "2 buttons",
                "3 buttons",
                "4 buttons",
                "5 buttons",
                "6 buttons"
        };
        ArrayAdapter<String> adapter= new ArrayAdapter<String>(this, R.layout.spinner_dialog_layout, entries);
        adapter.setDropDownViewResource(R.layout.spinner_dialog_layout);
        spinner_buttons.setAdapter(adapter);

        spinner_buttons.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String s = "" + parent.getItemAtPosition(position);
                number_buttons = Integer.parseInt("" + s.charAt(0));

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spinner_difficulty = (Spinner) findViewById(R.id.spinner_difficulty);
        String[] entries2 = new String[]{
                "Easy",
                "Normal",
                "Hard",
        };
        ArrayAdapter<String> adapter2= new ArrayAdapter<String>(this, R.layout.spinner_dialog_layout, entries2);
        adapter2.setDropDownViewResource(R.layout.spinner_dialog_layout);
        spinner_difficulty.setAdapter(adapter2);

        spinner_difficulty.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String s = "" + parent.getItemAtPosition(position);
                int diffuclty = 1;
                switch (s) {
                    case "Easy":
                        diffuclty = 0;
                        break;
                    case "Normal":
                        diffuclty = 1;
                        break;
                    case "Hard":
                        diffuclty = 2;
                        break;
                }

                difficulty_setting = diffuclty;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        //Set up alert dialog for when player loses
        alertDialogBuilder = new AlertDialog.Builder(this, R.style.DialogTheme);
        alertDialogBuilder.setTitle("Confirm");
        alertDialogBuilder.setMessage("Warning: Changing settings will reset your game.");
        alertDialogBuilder.setCancelable(false);

        //Set buttons for alert
        alertDialogBuilder.setNegativeButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                model.setNumButtons(number_buttons);
                model.setDifficulty(difficulty_setting);
                model.setDifficulty_speed(difficulty_setting);
                model.resetSimon();
                Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
                startActivity(intent);
            }

        });
        alertDialogBuilder.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int id) {
                dialog.cancel();

            }
        });

        alertDialog = alertDialogBuilder.create();


        apply = (Button) findViewById(R.id.apply_settings);
        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(difficulty_setting != model.getDifficulty() || number_buttons != model.getNumButtons()){
                    alertDialog.show();
                }
                else{
                    finish();
                }
            }
        });

        model.setChangedAndNotify();

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
                Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void update(Observable o, Object arg) {

        //Set number of buttons to whatever the model is at
        spinner_buttons.setSelection(model.getNumButtons()-1);

        //Set the difficulty to whatever the model is at
        spinner_difficulty.setSelection(model.getDifficulty());
    }
}
