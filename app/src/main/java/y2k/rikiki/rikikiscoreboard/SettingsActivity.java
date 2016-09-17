package y2k.rikiki.rikikiscoreboard;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class SettingsActivity extends AppCompatActivity {

    public static final String PLAYERS = "y2k.rikiki.rikikiscoreboard.PLAYERS";

    ArrayList<String> players;
    ArrayAdapter<String> playerAdapter;
    int maxRounds = 8;
    SharedPreferences settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        settings = getPreferences(MODE_PRIVATE);
        Set<String> savedPlayers = settings.getStringSet(PLAYERS, new HashSet<String>());

        players = new ArrayList<String>(savedPlayers);
        ListView playerListView = (ListView) findViewById(R.id.playerListView);
        playerAdapter = new ArrayAdapter<String>(this, R.layout.player_list_item, R.id.listName, players);
        playerListView.setAdapter(playerAdapter);
    }

    public void addPlayer(View view) {
        EditText newPlayerName = (EditText) findViewById(R.id.newPlayerName);
        String name = newPlayerName.getText().toString();
        if (! "".equals(name) && ! players.contains(name)) {
            players.add(name);
            newPlayerName.setText("");
            playerAdapter.notifyDataSetChanged();
        }
    }

    public void removePlayer(View view) {
        LinearLayout playerListLayout = (LinearLayout) view.getParent();
        TextView playerName = (TextView) playerListLayout.findViewById(R.id.listName);
        players.remove(playerName.getText());
        playerAdapter.notifyDataSetChanged();
    }

    public void startGame(View view) {
        SharedPreferences.Editor settingsEditor = settings.edit();
        settingsEditor.putStringSet(PLAYERS, new HashSet<String>(players));
        settingsEditor.commit();
        Intent scoreIntent = new Intent(this, ScoreActivity.class);
        scoreIntent.putStringArrayListExtra(PLAYERS, players);
        startActivity(scoreIntent);
    }
}
