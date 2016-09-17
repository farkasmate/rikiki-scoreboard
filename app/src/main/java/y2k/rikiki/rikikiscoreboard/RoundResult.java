package y2k.rikiki.rikikiscoreboard;

import android.os.Parcel;
import android.os.Parcelable;

public class RoundResult implements Parcelable {
    private int guess;
    private int won;
    private int points;

    public RoundResult() {
    }

    public RoundResult(Parcel in) {
        int[] orderedValues = new int[3];
        in.readIntArray(orderedValues);
        guess = orderedValues[0];
        won = orderedValues[1];
        points = orderedValues[2];
    }

    public int getGuess() {
        return guess;
    }

    public int getWon() {
        return won;
    }

    public int getPoints() {
        return points;
    }

    public void setGuess(int guess) {
        this.guess = guess;
    }

    public void setWon(int won) {
        this.won = won;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public static final Parcelable.Creator<RoundResult> CREATOR = new Parcelable.Creator<RoundResult>() {
        public RoundResult createFromParcel(Parcel in) {
            return new RoundResult(in);
        }

        public RoundResult[] newArray(int size) {
            return new RoundResult[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        int[] orderedValues = new int[3];
        orderedValues[0] = guess;
        orderedValues[1] = won;
        orderedValues[2] = points;
        dest.writeIntArray(orderedValues);
    }
}