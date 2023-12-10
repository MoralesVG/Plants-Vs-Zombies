package sample;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class GamePlayControllerNight extends GamePlayController {
    public void initialize() throws Exception {
        // different
        theme = "night";// different for day
        l = null;
        Media wave = new Media(getClass().getResource("/assets/sounds/zombies_are_coming.wav").toString());
        MediaPlayer mediaPlayer = new MediaPlayer(wave);
        mediaPlayer.setAutoPlay(true);
        mediaPlayer.setStartTime(Duration.seconds(0));
        mediaPlayer.setStopTime(Duration.seconds(5));
        mediaPlayer.play();

        gameStatus = true;
        sunCountDisplay = sunCountLabel;
        allZombies = Collections.synchronizedList(new ArrayList<Zombie>());
        allPlants = Collections.synchronizedList(new ArrayList<Plant>());
        //allMowers = Collections.synchronizedList(new ArrayList<LawnMower>());
    }

    @FXML
    public void initData(int levelNumber, DataTable d) {
        // different
        // no mower at night!!!
        // no status needed for if else since if is day and the else is night
        theme = "night";// different for day

        wonGame = 0;
        Random rand = new Random();
        this.levelNumber = levelNumber;
        Level l = new Level(levelNumber);
        this.l = l;
        zombieList1 = d.getZombieList1();
        zombieList2 = d.getZombieList2();
        allPlants = d.getAllPlants();
        allZombies = d.getAllZombie();
        //allMowers=d.getAllLawnMowers();
        sunCount=d.getSunCount();
        timeElapsed = d.getTimeElapsed();
        animationTimelines = new ArrayList<Timeline>();
        LevelMenuController.status = d.getStatus();        //not needed
        startAnimations(rand);

        shovel=Shovel.getInstance();
        shovel.makeImage(GamePlayRoot);
        sunCountDisplay.setText(String.valueOf(sunCount));
        this.d=d;
        SidebarElement.getSideBarElements(levelNumber, GamePlayRoot);

        gameProgress();

        String lawn_path = getClass().getResource("/assets/lawn_night.png").toString();
        Image lawn = new Image(lawn_path, 1024, 600, false, false);
        lawnImage.setImage(lawn);
        zombieSpawner1(rand, 25);
        zombieSpawner2(rand, 40);
    }

    public void startAnimations(Random rand)
    {
        // different
        // no mower at night!!!
        synchronized (allPlants) {
            Iterator<Plant> i = allPlants.iterator();
            while (i.hasNext()) {
                Plant p = i.next();
                p.makeImage(lawn_grid);
                p.attack(GamePlayRoot);
            }
        }

        synchronized (allZombies)
        {
            Iterator<Zombie> i = allZombies.iterator();
            while(i.hasNext())
            {
                Zombie z = i.next();
                z.makeImage(GamePlayRoot);
                z.moveZombie();
            }
        }
        numZombiesKilled = l.getTotalZombies()*timeElapsed;
        progressBar.setProgress(timeElapsed);
    }

}