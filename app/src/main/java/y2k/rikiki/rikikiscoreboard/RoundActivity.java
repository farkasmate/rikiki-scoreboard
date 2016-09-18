package y2k.rikiki.rikikiscoreboard;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class RoundActivity extends AppCompatActivity {

    private static final String LOG_TAG = "RoundActivity";
    public static final String SCORE = "y2k.rikiki.rikikiscoreboard.SCORE";

    private String dealer;
    private ArrayList<String> players;
    private int round;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.round);

        Intent intent = getIntent();
        dealer = intent.getStringExtra(ScoreActivity.DEALER);
        players = intent.getStringArrayListExtra(ScoreActivity.PLAYERS);
        round = intent.getIntExtra(ScoreActivity.ROUND, 0);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(dealer + " deals " + round);

        LinearLayout playersLayout = (LinearLayout) findViewById(R.id.playersLayout);
        for (String player : players) {
            TextView playerText = new TextView(this);
            playerText.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
            playerText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            playerText.setText(player);
            playersLayout.addView(playerText);
        }

        LinearLayout guessLinearLayout = (LinearLayout) findViewById(R.id.guessLinearLayout);
        for (String player : players) {
            View guessPickerView = LayoutInflater.from(this).inflate(R.layout.player_picker, null);
            guessPickerView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1));

            NumberPicker guessNumberPicker = (NumberPicker) guessPickerView.findViewById(R.id.numberPicker);
            guessNumberPicker.setMinValue(0);
            guessNumberPicker.setMaxValue(round);

            guessLinearLayout.addView(guessPickerView);
        }
    }

    public void startRound(View view) {
        final Bundle score = new Bundle();

        LinearLayout guessLinearLayout = (LinearLayout) findViewById(R.id.guessLinearLayout);
        for (int i = 0; i < guessLinearLayout.getChildCount(); i++) {
            NumberPicker guessNumberPicker = (NumberPicker) guessLinearLayout.getChildAt(i).findViewById(R.id.numberPicker);
            guessNumberPicker.setEnabled(false);
            RoundResult roundResult = new RoundResult();
            roundResult.setGuess(guessNumberPicker.getValue());
            score.putParcelable(players.get(i), roundResult);
        }

        LinearLayout wonLinearLayout = (LinearLayout) findViewById(R.id.wonLinearLayout);
        for (String player : players) {
            View wonPickerView = LayoutInflater.from(this).inflate(R.layout.player_picker, null);
            wonPickerView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1));

            NumberPicker wonNumberPicker = (NumberPicker) wonPickerView.findViewById(R.id.numberPicker);
            wonNumberPicker.setMinValue(0);
            wonNumberPicker.setMaxValue(round);

            wonLinearLayout.addView(wonPickerView);
        }

        FloatingActionButton rikikiButton = (FloatingActionButton) findViewById(R.id.rikikiButton);
        rikikiButton.setImageResource(R.drawable.ic_ok);
        rikikiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout wonLinearLayout = (LinearLayout) findViewById(R.id.wonLinearLayout);
                for (int i = 0; i < wonLinearLayout.getChildCount(); i++) {
                    String player = players.get(i);
                    NumberPicker wonNumberPicker = (NumberPicker) wonLinearLayout.getChildAt(i).findViewById(R.id.numberPicker);
                    RoundResult roundResult = score.getParcelable(player);
                    roundResult.setWon(wonNumberPicker.getValue());

                    if (roundResult.getGuess() == roundResult.getWon()) {
                        roundResult.setPoints(10 + roundResult.getWon() * 2);
                    } else {
                        roundResult.setPoints((1 + Math.abs(roundResult.getGuess() - roundResult.getWon())) * -2);
                    }

                    score.putParcelable(player, roundResult);

                    Log.d(LOG_TAG, player + ": guessed " + roundResult.getGuess() + ", won " + roundResult.getWon() + ", scored: " + roundResult.getPoints());
                }

                int sumWon = 0;
                for (String player : score.keySet()) {
                    RoundResult roundResult = score.getParcelable(player);
                    sumWon += roundResult.getWon();
                }

                if (sumWon == round) {
                    Intent scoreIntent = new Intent();
                    scoreIntent.putExtra(SCORE, score);
                    setResult(RESULT_OK, scoreIntent);
                    finish();
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), "Sum of won rounds needs to match dealt cards.", Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        });
    }
}
