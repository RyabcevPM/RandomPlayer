package main.java.controllers;

import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Random;

public class NumberPlayer extends RandomGame {
    int start;
    int stop;
    int currentRoundCnt;
    int maxRoundCnt;
    boolean isAlwaysNew;
    boolean isChangeColor;
    Random rand;
    int prevNum;

    public NumberPlayer(int start, int stop, int roundCount, boolean isAlwaysNew, boolean isChangeColor) {
        this.start = start;
        this.stop = stop;
        currentRoundCnt = 0;
        this.maxRoundCnt = Integer.MAX_VALUE;
        if (roundCount != 0) this.maxRoundCnt = roundCount;
        this.isAlwaysNew = isAlwaysNew;
        this.isChangeColor = isChangeColor;
        showTextColor = Color.BLACK;
        rand = new Random();
        prevNum = -1;
        isFinal = false;
        progress = 0;

    }

    @Override
    ArrayList<String> getAllWords() {
        ArrayList<String> words = new ArrayList<String>();
        for (int i = start; i <= stop; i++) {
            words.add( "numbers/" + String.valueOf(i));
        }
        return words;
    }

    @Override
    void NextRound() {
        int num = rand.nextInt(stop - start + 1) + start;
        while (num == prevNum && isAlwaysNew) {
            num = rand.nextInt(stop - start + 1) + start;
        }
        prevNum = num;
        voiceWords = new ArrayList<>();
        voiceWords.add("numbers/" + String.valueOf(num) + ".mp3");
        showText = String.valueOf(num);
        if (isChangeColor) showTextColor = Color.rgb(rand.nextInt(255), rand.nextInt(255), rand.nextInt(255));
        if (++currentRoundCnt >= maxRoundCnt) isFinal = true;
        progress = currentRoundCnt / maxRoundCnt;
    }



    @Override
    Duration getMaxDuration() {
        Duration maxRoundDuration;
        maxRoundDuration = Duration.millis(0);
        String word;
        PlayerVoice voice = new PlayerVoice();
        for (int i = start; i <= stop; i++) {
            word = "numbers/" + String.valueOf(i);
            maxRoundDuration = Duration.millis(Math.max((int) maxRoundDuration.toMillis(), (int) voice.getDuration(word).toMillis()));
        }
        System.out.println("getMaxDuration" + maxRoundDuration);
        return maxRoundDuration;
    }
}
