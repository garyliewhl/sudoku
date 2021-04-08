package com.gary.sudoku;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    TextView appName;
    EditText[][] sudokuNumbers = new EditText[9][9];
    int[][] solvedSudokuPuzzle;
    int[][] unsolvedSudokuPuzzle;
    Button submitPuzzleButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.init();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate( R.menu.options, menu );

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        Intent intent = new Intent( MainActivity.this, MainActivity.class );

        switch( item.getItemId() ) {

            case R.id.help:
                intent = new Intent( MainActivity.this, HelpActivity.class );
                break;
            case R.id.settings:
                intent = new Intent( MainActivity.this, SettingsActivity.class );
                break;
            case R.id.new_game:
                intent = new Intent( MainActivity.this, MainActivity.class );
                // Similar to finish() but it destroys all previous activities
                intent.addFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP );
                break;
            default:
                Log.i("MainActivity", "Default case accessed in onOptionsItemSelected()");
        }

        startActivity( intent );

        return super.onOptionsItemSelected(item);
    }

    private void init() {
        EditText[][] sudokuNumbers = {
                {findViewById( R.id.editText00 ), findViewById( R.id.editText01 ),
                        findViewById( R.id.editText02 ), findViewById( R.id.editText03 ),
                        findViewById( R.id.editText04 ), findViewById( R.id.editText05 ),
                        findViewById( R.id.editText06 ), findViewById( R.id.editText07 ),
                        findViewById( R.id.editText08 )},

                {findViewById( R.id.editText10 ), findViewById( R.id.editText11 ),
                        findViewById( R.id.editText12 ), findViewById( R.id.editText13 ),
                        findViewById( R.id.editText14 ), findViewById( R.id.editText15 ),
                        findViewById( R.id.editText16 ), findViewById( R.id.editText17 ),
                        findViewById( R.id.editText18 )},

                {findViewById( R.id.editText20 ), findViewById( R.id.editText21 ),
                        findViewById( R.id.editText22 ), findViewById( R.id.editText23 ),
                        findViewById( R.id.editText24 ), findViewById( R.id.editText25 ),
                        findViewById( R.id.editText26 ), findViewById( R.id.editText27 ),
                        findViewById( R.id.editText28 )},

                {findViewById( R.id.editText30 ), findViewById( R.id.editText31 ),
                        findViewById( R.id.editText32 ), findViewById( R.id.editText33 ),
                        findViewById( R.id.editText34 ), findViewById( R.id.editText35 ),
                        findViewById( R.id.editText36 ), findViewById( R.id.editText37 ),
                        findViewById( R.id.editText38 )},

                {findViewById( R.id.editText40 ), findViewById( R.id.editText41 ),
                        findViewById( R.id.editText42 ), findViewById( R.id.editText43 ),
                        findViewById( R.id.editText44 ), findViewById( R.id.editText45 ),
                        findViewById( R.id.editText46 ), findViewById( R.id.editText47 ),
                        findViewById( R.id.editText48 )},

                {findViewById( R.id.editText50 ), findViewById( R.id.editText51 ),
                        findViewById( R.id.editText52 ), findViewById( R.id.editText53 ),
                        findViewById( R.id.editText54 ), findViewById( R.id.editText55 ),
                        findViewById( R.id.editText56 ), findViewById( R.id.editText57 ),
                        findViewById( R.id.editText58 )},

                {findViewById( R.id.editText60 ), findViewById( R.id.editText61 ),
                        findViewById( R.id.editText62 ), findViewById( R.id.editText63 ),
                        findViewById( R.id.editText64 ), findViewById( R.id.editText65 ),
                        findViewById( R.id.editText66 ), findViewById( R.id.editText67 ),
                        findViewById( R.id.editText68 )},

                {findViewById( R.id.editText70 ), findViewById( R.id.editText71 ),
                        findViewById( R.id.editText72 ), findViewById( R.id.editText73 ),
                        findViewById( R.id.editText74 ), findViewById( R.id.editText75 ),
                        findViewById( R.id.editText76 ), findViewById( R.id.editText77 ),
                        findViewById( R.id.editText78 )},

                {findViewById( R.id.editText80 ), findViewById( R.id.editText81 ),
                        findViewById( R.id.editText82 ), findViewById( R.id.editText83 ),
                        findViewById( R.id.editText84 ), findViewById( R.id.editText85 ),
                        findViewById( R.id.editText86 ), findViewById( R.id.editText87 ),
                        findViewById( R.id.editText88 )}
        };

        this.sudokuNumbers      = sudokuNumbers;
        this.submitPuzzleButton = findViewById( R.id.submitPuzzleButton );
        this.appName            = findViewById( R.id.app_name );
        this.addOnClickEditTextListeners( this.sudokuNumbers );
        this.solvedSudokuPuzzle   = this.generateSudokuPuzzle();

        Globals globals = (Globals) getApplicationContext();

        this.unsolvedSudokuPuzzle = this.generateUnsolvedPuzzle( this.solvedSudokuPuzzle, globals.getGameDifficulty() );
        this.populatePuzzle();
        this.addOnClickButtonListener();
    }

    private void addOnClickEditTextListeners( EditText[][] sudokuNumbers ) {
        for ( EditText[] nums : sudokuNumbers ) {

            for ( EditText num : nums ) {

                num.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        closeKeyboard();
                    }
                } );

            }
        }
    }

    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private int[][] generateSudokuPuzzle() {

        int[][] solved = new int[9][9];
        for ( int i = 0; i < 9; i++ ) {
            solved[0][i] = i;
        }

        this.shuffleArray( solved[0] );

        int k;

        for ( int i = 1; i < 9; i++ ) {
            for ( int j = 0; j < 9; j++ ) {

                if ( i % 3 == 0 ) {
                    k = j + 1;
                } else {
                    k = j + 3;
                }

                k = (k == 9)  ? 0 : k;
                k = (k == 10) ? 1 : k;
                k = (k == 11) ? 2 : k;

                solved[i][j] = solved[i - 1][k];
            }
        }

        return solved;
    }

    private void shuffleArray( int[] array ) {

        int index, temp;
        Random random = new Random();

        for (int i = array.length - 1; i > 0; i--) {

            index        = random.nextInt(i + 1);
            temp         = array[index];
            array[index] = array[i];
            array[i]     = temp;
        }
    }

    private int[][] generateUnsolvedPuzzle( int[][] solved, double difficulty ) {
        int[][] unsolved = new int[9][9];

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {

                if ( Math.random() > difficulty) {
                    unsolved[i][j] = 0;

                } else {
                    unsolved[i][j] = solved[i][j];
                }

            }
        }

        return unsolved;
    }

    private void populatePuzzle() {
        for ( int i = 0; i < 9; i++ ) {
            for ( int j = 0; j < 9; j++ ) {

                if ( this.unsolvedSudokuPuzzle[i][j] == 0 ) {
                    this.sudokuNumbers[i][j].setText( "" );

                } else {
                    this.sudokuNumbers[i][j].setText(
                            getString( R.string.puzzle_numbers, this.unsolvedSudokuPuzzle[i][j] + "" )
                    );
                }

            }
        }
    }

    private void addOnClickButtonListener() {
        this.submitPuzzleButton.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if ( checkIfSolved( sudokuNumbers, solvedSudokuPuzzle ) ) {

                    appName.setText(
                            getString( R.string.solved )
                    );

                } else {
                    appName.setText(
                            getString( R.string.keep_trying )
                    );
                }
            }

        } );
    }

    private boolean checkIfSolved( EditText[][] sudokuNumbers, int[][] solvedSudokuNumbers ) {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {

                if ( !sudokuNumbers[i][j].getText().toString().equals( solvedSudokuNumbers[i][j] + "" ) ) {
                    return false;
                }
            }
        }

        return true;
    }
}
