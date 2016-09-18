package y2k.rikiki.rikikiscoreboard;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class ScoreActivity extends AppCompatActivity {

    private static final long BACK_THRESHOLD = 1000l;

    public static final String DEALER = "y2k.rikiki.rikikiscoreboard.DEALER";
    public static final String PLAYERS = "y2k.rikiki.rikikiscoreboard.PLAYERS";
    public static final String ROUND = "y2k.rikiki.rikikiscoreboard.ROUND";

    ArrayList<String> players;
    ArrayList<Bundle> scores;
    ArrayAdapter<Bundle> scoreAdapter;
    int maxRounds = 10;
    int nextRound = 1;
    boolean incrementRound = true;
    long backLastPressed = 0l;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.score);

        players = new ArrayList<String>(getIntent().getStringArrayListExtra(SettingsActivity.PLAYERS));
        scores = new ArrayList<>();

        ListView scoreListView = (ListView) findViewById(R.id.scoreListView);
        scoreAdapter = new ScoreAdapter(this, R.layout.score_list_single_item, players, scores);
        scoreListView.setAdapter(scoreAdapter);
        scoreListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FloatingActionButton newRoundButton = (FloatingActionButton) findViewById(R.id.newRoundButton);
                switch (newRoundButton.getVisibility()) {
                    case View.VISIBLE:
                        newRoundButton.setVisibility(View.INVISIBLE);
                        break;
                    case View.INVISIBLE:
                        newRoundButton.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            Bundle score = data.getBundleExtra(RoundActivity.SCORE);

            if (scores.size() > 0) {
                Bundle previousScore = scores.get(scores.size() - 1);

                for (String player : score.keySet()) {
                    RoundResult roundResult = score.getParcelable(player);
                    RoundResult previousRoundResult = previousScore.getParcelable(player);
                    roundResult.setPoints(previousRoundResult.getPoints() + roundResult.getPoints());
                }
            }

            scores.add(score);
            scoreAdapter.notifyDataSetChanged();

            if (incrementRound) {
                if (nextRound < maxRounds) {
                    nextRound++;
                } else {
                    incrementRound = false;
                    nextRound--;
                }
            } else {
                if (nextRound > 1) {
                    nextRound--;
                } else {
                    FloatingActionButton newRoundButton = (FloatingActionButton) findViewById(R.id.newRoundButton);
                    newRoundButton.setVisibility(View.GONE);
                }
            }
        }
    }

    public void newRound(View view) {
        Intent roundIntent = new Intent(this, RoundActivity.class);
        roundIntent.putExtra(DEALER, players.get((nextRound - 1) % players.size()));
        roundIntent.putStringArrayListExtra(PLAYERS, players);
        roundIntent.putExtra(ROUND, nextRound);
        startActivityForResult(roundIntent, 0);
    }

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - backLastPressed > BACK_THRESHOLD) {
            backLastPressed = System.currentTimeMillis();
            Toast toast = Toast.makeText(getApplicationContext(), "Tap back again to exit.", Toast.LENGTH_SHORT);
            toast.show();
        } else {
            super.onBackPressed();
        }
    }
}
