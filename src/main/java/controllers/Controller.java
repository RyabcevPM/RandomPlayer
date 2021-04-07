package main.java.controllers;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.Duration;

import java.net.URL;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    Random rand;
    //    ArrayList<Media> mediaNumbers;
    PlayerVoice voice;
    KeyFrame keyFrame;
    int roundCnt;
    RandomGame game;
    ArrayList<String> voiceWords;
    private Label currentGameText;
    private Timeline timeline;
    @FXML
    private Label lBigNumber;
    @FXML

    private Label lTwisterText;
    @FXML
    private Button bPlay;
    @FXML
    private Button bPause;
    @FXML
    private TextField edNumFrom;
    @FXML
    private TextField edNumTo;
    @FXML
    private TextField edRoundCount;
    @FXML
    private Slider slTimeInterval;

    @FXML
    private CheckBox bNoDuplicate;
    @FXML
    private CheckBox cbChangeColor;


    @FXML
    private CheckBox cbPlayer1;
    @FXML
    private CheckBox cbPlayer2;
    @FXML
    private CheckBox cbPlayer3;
    @FXML
    private CheckBox cbPlayer4;
    @FXML
    private CheckBox cbPlayer5;

    @FXML
    private ComboBox cboxPlayer1;
    @FXML
    private ComboBox cboxPlayer2;
    @FXML
    private ComboBox cboxPlayer3;
    @FXML
    private ComboBox cboxPlayer4;
    @FXML
    private ComboBox cboxPlayer5;

    @FXML
    private TabPane tabPane;


    public Controller() {
    }

    int getRandomVal(int min, int max, HashSet<Integer> ignoreList) {
        Integer res = rand.nextInt(max - min) + min;
        if (ignoreList == null) return res;
        while (ignoreList.contains(res))
            res = rand.nextInt(max - min) + min;
        ignoreList.add(res);
        return res;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        roundCnt = 0;
        voice = new PlayerVoice();
        rand = new Random();
        ArrayList<String> itemList = new ArrayList<>();

        Player.getDefaultPlayers().stream().forEach(o -> itemList.add(o.getName()));
        ObservableList obList = FXCollections.observableList(itemList);
        ArrayList<ComboBox> listCB = new ArrayList<>();
        HashSet<Integer> ignoreList = new HashSet<>();
        listCB.add(cboxPlayer1);
        listCB.add(cboxPlayer2);
        listCB.add(cboxPlayer3);
        listCB.add(cboxPlayer4);
        listCB.add(cboxPlayer5);

        for (ComboBox cb : listCB) {
            cb.getItems().clear();
            cb.setItems(obList);
            cb.getSelectionModel().select(getRandomVal(0, obList.size() - 1, ignoreList));
        }
    }

    Duration getInterval() {
        return Duration.millis((int) (slTimeInterval.getValue() * 1000));
    }

    private void nextRound() {
        System.out.println(String.format("  round %d start - ", roundCnt) + Instant.now());
        game.NextRound();
        voice.play(game.voiceWords);
        currentGameText.setText(String.valueOf(game.showText));
        currentGameText.setTextFill(game.showTextColor);
        if (++roundCnt == 10) {
            Runtime.getRuntime().gc();
            roundCnt = 0;
        }
        if (game.isFinal) OnPause();
        System.out.println(String.format("  round %d finish - ", roundCnt - 1) + Instant.now());
    }

    void initNewGame() {
        System.out.println("\n\ninit " + game.getClass().getName() + " " + Instant.now());
        Duration duration = getInterval().add(game.getMaxDuration());
        keyFrame = new KeyFrame(duration, e -> nextRound());
        System.out.println(String.format("\n\ninit %s %s - interval:%f sec", game.getClass().getName(), Instant.now(), duration.toSeconds()));
        timeline = new Timeline(keyFrame);
        timeline.setCycleCount(Animation.INDEFINITE); // loop forever
        timeline.play();
        lBigNumber.setVisible(true);
        Runtime.getRuntime().gc();
    }

    @FXML
    private void OnPlay() {
        String currentTab = tabPane.getSelectionModel().getSelectedItem().getText();
        if (currentTab.equals("Numbers")) {
            game = new NumberPlayer(Integer.valueOf(edNumFrom.getText()),
                    Integer.valueOf(edNumTo.getText()),
                    Integer.valueOf(edRoundCount.getText()),
                    bNoDuplicate.isSelected(),
                    cbChangeColor.isSelected());
            currentGameText = lBigNumber;
            currentGameText.setText("...");
        }
        if (currentTab.equals("Twister")) {
            ArrayList<Player> playersList = new ArrayList<>();
            ArrayList<Player> defPlayersList = Player.getDefaultPlayers();
            ArrayList<String> selectedPlayerName = new ArrayList<>();
            if (cbPlayer1.isSelected())
                selectedPlayerName.add(cboxPlayer1.getSelectionModel().getSelectedItem().toString());
            if (cbPlayer2.isSelected())
                selectedPlayerName.add(cboxPlayer2.getSelectionModel().getSelectedItem().toString());
            if (cbPlayer3.isSelected())
                selectedPlayerName.add(cboxPlayer3.getSelectionModel().getSelectedItem().toString());
            if (cbPlayer4.isSelected())
                selectedPlayerName.add(cboxPlayer4.getSelectionModel().getSelectedItem().toString());
            if (cbPlayer5.isSelected())
                selectedPlayerName.add(cboxPlayer5.getSelectionModel().getSelectedItem().toString());

            selectedPlayerName.stream().forEach(pn -> playersList.add(defPlayersList.stream().filter(o -> o.getName().equals(pn)).findFirst().get()));
            game = new Twister(playersList);
            currentGameText = lTwisterText;
            currentGameText.setText("loading...");
        }
        System.out.println(">>> start init " + Instant.now());
        voice.init(game.getAllWords());
        System.out.println(">> init finish " + Instant.now());

        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> initNewGame()));
        timeline.play();
    }

    @FXML
    private void OnPause() {
        timeline.stop();
        System.out.println("timer stop");
    }


}
