package main.java.controllers;

import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.util.ArrayList;

abstract class RandomGame {
    protected ArrayList<String> voiceWords;
    protected String showText;
    protected Color showTextColor  = Color.BLACK;
    protected boolean isFinal;
    double progress;

    abstract ArrayList<String> getAllWords();
    abstract void NextRound();
    abstract Duration getMaxDuration();
}
