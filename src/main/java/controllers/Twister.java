package main.java.controllers;

import javafx.scene.paint.Color;
import javafx.util.Duration;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

enum MoveColor {
    RED("красный", "colors/красный.m4a"),
    GOLD("желтый", "colors/желтый.m4a"),
    GREEN("зеленый", "colors/зеленый.m4a"),
    BLUE("синий", "colors/синий.m4a"),
    NONE("белый", "colors/белый.mp3");
    private final String audioFilePath;
    private final String text;

    MoveColor(String text, String audioFilePath) {
        this.audioFilePath = audioFilePath;
        this.text = text;
    }

    public String getAudioFilePath() {
        return audioFilePath;
    }

    public String getTextWord() {
        return text;
    }

}


enum MoveType {
    LEFT_FOOT("левая нога", "others/левая.mp3", "others/нога.m4a"),
    RIGHT_FOOT("левая нога", "others/правая.mp3", "others/нога.m4a"),
    LEFT_HAND("левая нога", "others/левая.mp3", "others/рука.m4a"),
    RIGHT_HAND("левая нога", "others/правая.mp3", "others/рука.m4a");

    private final ArrayList<String> audioFiles;
    private final String text;

    public ArrayList<String> getAudioFiles() {
        return audioFiles;
    }

    public String getText() {
        return text;
    }

    MoveType(String text, String audioFile1, String audioFile2) {
        ArrayList<String> list = new ArrayList<>();
        list.add(audioFile1);
        list.add(audioFile2);
        this.audioFiles = list;
        this.text = text;
    }
}

public class Twister extends RandomGame {
    ArrayList<HashMap<MoveType, MoveColor>> playersStatus;
    private Random random;
    private ArrayList<Player> players;
    private int currentPlayer;


    private HashMap<MoveColor, Integer> colorCnt;

    public Twister(ArrayList<Player> players) {
        this.players = players;
        currentPlayer = 0;
        random = new Random();
        colorCnt = new HashMap<>();
        playersStatus = new ArrayList<>();
        for (int i = 0; i < players.size(); i++) {
            HashMap<MoveType, MoveColor> hm = new HashMap<>();
            Arrays.stream(MoveType.values()).forEach(o -> hm.put(o, MoveColor.NONE));
            playersStatus.add(hm);
        }
        Arrays.stream(MoveColor.values()).forEach(o -> colorCnt.put(o, 0));
    }

    @Override
    ArrayList<String> getAllWords() {
        ArrayList<String> words = new ArrayList<>();
        players.stream().forEach(o -> words.add(o.getAudioFilePath()));
        Arrays.stream(MoveColor.values()).forEach(o -> words.add(o.getAudioFilePath()));
        Arrays.stream(MoveType.values()).forEach(o -> words.addAll(o.getAudioFiles()));
        return words;
    }

    @Override
    void NextRound() {
        HashMap<MoveType, MoveColor> playerStatus = playersStatus.get(currentPlayer);

        MoveColor moveColor;
        MoveType moveType;
        {
            moveColor = MoveColor.values()[random.nextInt(MoveColor.values().length - 1)];
            moveType = MoveType.values()[random.nextInt(MoveType.values().length - 1)];
        }
        while (colorCnt.get(moveColor) == MoveColor.values().length - 1 && !playerStatus.get(moveType).equals(moveColor));

        playerStatus.put(moveType, moveColor);

        voiceWords = new ArrayList<>();
        voiceWords.add(players.get(currentPlayer).getAudioFilePath());
//        voiceWords.add("nickname/игрок");
        voiceWords.addAll(moveType.getAudioFiles());
        voiceWords.add("others/на.m4a");
        voiceWords.add(moveColor.getAudioFilePath());

        showText = moveType.getText() + " на " + moveColor.getTextWord();
        showTextColor = Color.valueOf(moveColor.toString());

        if (++currentPlayer == players.size()) currentPlayer = 0;
    }

    @Override
    Duration getMaxDuration() {
        return Duration.seconds(5);
    }
}
