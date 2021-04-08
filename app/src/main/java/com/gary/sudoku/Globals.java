package com.gary.sudoku;

import android.app.Application;

public class Globals extends Application {

    private double[] difficulty = {0.25, 0.50, 0.75, 0.90};

    private double gameDifficulty = difficulty[1];

    public void setGameDifficulty( int difficultyLevel ) {

        switch( difficultyLevel ) {
            case 1: this.gameDifficulty = this.difficulty[0]; break;
            case 2: this.gameDifficulty = this.difficulty[1]; break;
            case 3: this.gameDifficulty = this.difficulty[2]; break;
            case 4: this.gameDifficulty = this.difficulty[3]; break;
        }
    }

    public double getGameDifficulty() {
        return this.gameDifficulty;
    }

    public String getStringGameDifficulty() {

        if ( this.gameDifficulty == this.difficulty[3] ) {
            return "Easy";

        } else if ( this.gameDifficulty == this.difficulty[2] ) {
            return "Medium";

        } else if ( this.gameDifficulty == this.difficulty[1] ) {
            return "Hard";

        } else {
            return "Expert";
        }

    }
}
