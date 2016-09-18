package y2k.rikiki.rikikiscoreboard;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class ScoreAdapter extends ArrayAdapter<Bundle> {
    int layout;
    List<String> keyOrder;
    List<Bundle> rounds;

    public ScoreAdapter(Context context, int resource, List<String> keyOrder, List<Bundle> objects) {
        super(context, resource, objects);
        this.layout = resource;
        this.keyOrder = keyOrder;
        this.rounds = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (rounds.isEmpty()) {
            return new Space(getContext());
        }

        LinearLayout multiScoreView;
        if (convertView == null) {
            multiScoreView = new LinearLayout(getContext());
            multiScoreView.setOrientation(LinearLayout.HORIZONTAL);

            for (String player : keyOrder) {
                View singleScoreView = LayoutInflater.from(getContext()).inflate(layout, null);
                singleScoreView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
                multiScoreView.addView(singleScoreView);
            }

            convertView = multiScoreView;
        }

        multiScoreView = (LinearLayout) convertView;
        Bundle round = rounds.get(position);
        for (int i = 0; i < multiScoreView.getChildCount(); i++) {
            View singleScoreView = multiScoreView.getChildAt(i);
            String player = keyOrder.get(i);
            RoundResult roundResult = round.getParcelable(player);

            TextView guessText = (TextView) singleScoreView.findViewById(R.id.guessText);
            guessText.setText(String.valueOf(roundResult.getGuess()));
            TextView wonText = (TextView) singleScoreView.findViewById(R.id.wonText);
            wonText.setText(String.valueOf(roundResult.getWon()));
            TextView pointsText = (TextView) singleScoreView.findViewById(R.id.pointsText);
            pointsText.setText(String.valueOf(roundResult.getPoints()));
        }
        convertView = multiScoreView;

        return convertView;
    }
}
