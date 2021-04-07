package main.java.controllers;

import java.util.ArrayList;
import java.util.HashMap;

class Player {
    private String name;
    private String audioFilePath;

    public String getName() {
        return name;
    }

    public String getAudioFilePath() {
        return audioFilePath;
    }

    public Player(String name, String audioFilePath) {
        this.name = name;

        this.audioFilePath = audioFilePath;
    }
    static ArrayList<Player> getDefaultPlayers(){
        ArrayList<Player> players = new ArrayList<>();
        players.add(new Player("гибкий", "nickname/гибкий.mp3"));
        players.add(new Player("ежик", "nickname/ежик.mp3"));
        players.add(new Player("первый", "numbers/первый.mp3"));
        players.add(new Player("второй", "numbers/второй.mp3"));
        players.add(new Player("третий", "numbers/третий.mp3"));
        players.add(new Player("четвертый", "numbers/четвертый.mp3"));
        players.add(new Player("пятый", "numbers/пятый.mp3"));
        return players;
    }
}
