package main.java.controllers;

import javafx.animation.PauseTransition;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.awt.*;
import java.net.URL;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.locks.Lock;

public class PlayerVoice {
    final public Duration wordDelimiter = Duration.millis(250);
    private static HashMap<String, Media> audio;
    private static HashMap<String, Duration> audioDuration;

    public PlayerVoice() {
        if (audio==null) audio = new HashMap<>();
        if (audioDuration==null) audioDuration = new HashMap<>();
    }

    void init(ArrayList<String> words) {
        words.forEach(o->init(o));
    }

    void initDuration(String word, Duration duration){
        audioDuration.put(word, duration);
    }

    void init(String word) {
        URL resource;
        if (!audio.containsKey(word)) {
            System.out.println("/mp3/" + word);
            resource = getClass().getResource("/mp3/" + word);
            Media media = new Media(resource.toString());
            audio.put(word, media);
            MediaPlayer mediaPlayer = new MediaPlayer(media);
            audioDuration.put(word, Duration.millis(0));
            mediaPlayer.setOnReady(() -> {
                initDuration(word, media.getDuration());
            });
        }
    }

    Duration getDuration(ArrayList<String> words) {
        init(words);
        Duration res = Duration.millis(0);
        audio.get(words.get(0)).getDuration();
        Duration durationWord;
        for (String word : words) {
            durationWord = audio.get(word).getDuration();
            res.add(durationWord);
            res.add(wordDelimiter);
        }
        return res.subtract(wordDelimiter);
    }

    Duration getDuration(String word) {
        init(word);
        return audioDuration.get(word);
    }

    void play(String word, Duration delay) {
        init(word);
        MediaPlayer mediaPlayer = new MediaPlayer(audio.get(word));
        mediaPlayer.setStartTime(delay);
        mediaPlayer.play();
    }

    void play(String word) {
        play(word,Duration.millis(0));
    }

    class MyTimerTask extends TimerTask{
        PlayerVoice pv;
        String word;

        public MyTimerTask(PlayerVoice pv, String word) {
            this.pv = pv;
            this.word = word;
        }

        @Override
        public void run() {
            pv.play(word);
        }
    }

    void play(ArrayList<String> words) {
        System.out.println("    play --> " + words);
        init(words);
        MediaPlayer mediaPlayer;
        if (words.size()==1) {
            mediaPlayer = new MediaPlayer(audio.get(words.get(0)));
            mediaPlayer.play();
            return;
        }

        Duration allDuration =  Duration.millis(100);
        Timer timer = new Timer();
        TimerTask task;
//        System.out.println(audioDuration);

        for (int i = 0; i < words.size(); i++) {
            task = new MyTimerTask(this, words.get(i));
            Duration wordDuration = getDuration(words.get(i));
            System.out.println(wordDuration);
            timer.schedule(task, (int) allDuration.toMillis());
            allDuration = allDuration.add(wordDuration).add(wordDelimiter);
        }
    }
}
