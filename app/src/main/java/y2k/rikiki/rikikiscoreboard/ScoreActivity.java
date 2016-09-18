package y2k.rikiki.rikikiscoreboard;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
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
    int maxRounds = 8;
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
    }

    public void newRound(View view) {
        Intent roundIntent = new Intent(this, RoundActivity.class);
        roundIntent.putExtra(DEALER, "DEALER");
        roundIntent.putStringArrayListExtra(PLAYERS, players);
        roundIntent.putExtra(ROUND, 3);
        startActivityForResult(roundIntent, 0);
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
        }
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
