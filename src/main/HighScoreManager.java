package main;

import java.util.ArrayList;

public class HighScoreManager {

    public ArrayList<Score> scoreList = new ArrayList<>();

    public void addScore(String name, int point) {
        Score newScore = new Score(name, point);
        scoreList.add(newScore);

        sort();
    }

    // Decreasing order
    public void sort() {
        int n = scoreList.size();

        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {

                Score s1 = scoreList.get(j);
                Score s2 = scoreList.get(j + 1);

                if (s1.point < s2.point) {
                    scoreList.set(j, s2);
                    scoreList.set(j + 1, s1);
                }
            }
        }

    }
}