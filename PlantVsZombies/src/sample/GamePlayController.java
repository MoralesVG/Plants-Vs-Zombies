package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

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

public abstract class GamePlayController {

    @FXML
    protected AnchorPane GamePlayRoot;
    @FXML
    protected ImageView lawnImage;
    @FXML
    protected ImageView peaShooterBuy;
    @FXML
    protected ImageView repeaterBuy;
    @FXML
    protected ImageView cherryBombBuy;
    @FXML
    protected ImageView jalapenoBuy;
    @FXML
    protected ImageView wallnutBuy;
    @FXML
    protected ImageView sunBuy;
    @FXML
    protected Label sunCountLabel;
    @FXML
    protected ImageView GameMenuLoaderButton;
    @FXML
    protected ImageView UndoButton;
    @FXML
    protected ProgressBar progressBar;
    @FXML
    protected int levelNumber;
    @FXML
    protected GridPane lawn_grid;
    protected static int sunCount;
    public static final int LANE1=50;
    public static final int LANE2=150;
    public static final int LANE3=250;
    public static final int LANE4=350;
    public static final int LANE5=450;
    public static boolean gameStatus;
    public static Timeline sunTimeline;
    public static Timeline spZ1;
    public static Timeline spZ2;
    protected static Label sunCountDisplay;
    protected static double timeElapsed;
    protected static Level l;
    public static List allZombies;
    public static List<Plant> allPlants;
    public static List allMowers;
    public static ArrayList<Integer> zombieList1;
    public static ArrayList<Integer> zombieList2;
    protected static DataTable d;
    public static int wonGame = 0;
    protected volatile int spawnedZombies = 0;
    public static double numZombiesKilled = 0;
    public static ArrayList<Timeline> animationTimelines;
    public static String theme;// = "day";
    protected Shovel shovel;

    abstract void initialize() throws Exception ;


    @FXML
    abstract void initData(int levelNumber, DataTable d);

    abstract void startAnimations(Random rand);


    public void gameProgress()
    {
        Timeline gameStatus = new Timeline(new KeyFrame(Duration.seconds(1), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    timeElapsed = ( numZombiesKilled / l.getTotalZombies());
                    progressBar.setProgress(timeElapsed);
                    if (wonGame == (-1)) {
                        //System.out.println("LostGame :(");
                        numZombiesKilled = 0;
                        endAnimations();
                        gameLost();
                    } else if (wonGame == 0 && allZombies.size() == 0 && l.getTotalZombies() == spawnedZombies) {
                        //System.out.println("GAME WON!!");
                        numZombiesKilled = 0;
                        endAnimations();
                        gameWon();
                    }
                    else if(progressBar.getProgress()>=1)
                    {
                        spZ1.stop();
                        spZ2.stop();
                            endAnimations();
                            gameWon();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }));
        gameStatus.setCycleCount(Timeline.INDEFINITE);
        gameStatus.play();
        animationTimelines.add(gameStatus);
    }


    public synchronized void updateSpawnedZombies()
    {
        this.spawnedZombies+=1;
    }

    @FXML
    void loadGameMenu(MouseEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("GameMenu.fxml"));
        Parent gameMenu = (Parent) fxmlLoader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(gameMenu));
        GameMenuController controller = fxmlLoader.<GameMenuController>getController();
        controller.initData(GamePlayRoot, levelNumber,d,sunCount,allPlants, allZombies, allMowers, timeElapsed, l.getZombieList1(), l.getZombieList2());
        stage.show();
    }
    
    @FXML
    void UndoButton(MouseEvent event) throws IOException {
    	d.restore(); 	
    	
    	Plant lastPlant = allPlants.get(allPlants.size()-1);
    	
    	
    	allMowers = d.getAllLawnMowers();
        allPlants = d.getAllPlants();
        allZombies = d.getAllZombie();
        levelNumber = d.getLevelNumber();
        sunCount = d.getSunCount();
        timeElapsed = d.getTimeElapsed();
        zombieList1 = d.getZombieList1();
        zombieList2 = d.getZombieList2();
        gameStatus = d.getStatus();
        
        
        restoreSunCount(sunCount);
        removePlant(lastPlant);
    }

    public static void removePlant(Plant p){
        p.img.setVisible(false);
        allPlants.remove(p);
    }
    public static void removeZombie(Zombie z){
        z.img.setVisible(false);
        allZombies.remove(z);
    }
    public static void removeMower(LawnMower l){
        l.img.setVisible(false);
        allMowers.remove(l);
    }

    public static void updateSunCount(int val) {
    	d.setSunCount(sunCount);
    	d.backup();
    	sunCount+=val;
        getSunCountLabel().setText(Integer.toString(sunCount));   
        
    }  
    
    public static void restoreSunCount(int val) {
        getSunCountLabel().setText(Integer.toString(val));
    }

    public static Label getSunCountLabel() {
        return(sunCountDisplay);
    }

    public void fallingSuns(Random rand) {
        Timeline sunDropper = new Timeline(new KeyFrame(Duration.seconds(10), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                int sunPosition = rand.nextInt(850);
                sunPosition += 100;
                Sun s = new Sun(sunPosition, 0, true);
                s.makeImage(GamePlayRoot);
                s.dropSun();
                
                
            }
        }));
        
        
        sunDropper.setCycleCount(Timeline.INDEFINITE);
        sunDropper.play();
        sunTimeline = sunDropper;
        animationTimelines.add(sunDropper);
    }

    public void zombieSpawner1(Random rand, double t){
        Timeline spawnZombie1 = new Timeline(new KeyFrame(Duration.seconds(t), event -> {
            int lane;
            int laneNumber = rand.nextInt(5);
            if(laneNumber==0)
                lane = LANE1;
            else if(laneNumber==1)
                lane = LANE2;
            else if(laneNumber==2)
                lane = LANE3;
            else if(laneNumber==3)
                lane = LANE4;
            else
                lane = LANE5;
            try
            {
                if(zombieList1.get(0)==0) {
                    Level.spawnNormalZombie(GamePlayRoot, lane, laneNumber);
                    zombieList1.remove(0);
                    updateSpawnedZombies();
                }
                else if(zombieList1.get(0)==1)
                {
                    Level.spawnConeZombie(GamePlayRoot, lane, laneNumber);
                    zombieList1.remove(0);
                    updateSpawnedZombies();
                }
                else if(zombieList1.get(0)==2)
                {
                    Level.spawnBucketZombie(GamePlayRoot, lane, laneNumber);
                    zombieList1.remove(0);
                    updateSpawnedZombies();
                }
            }
            catch(IndexOutOfBoundsException e)
            {
                endZombieSpawner1();
            }
        }));

        spawnZombie1.setCycleCount(Timeline.INDEFINITE);
        spawnZombie1.play();
        spZ1 = spawnZombie1;
        animationTimelines.add(spawnZombie1);
    }

    public void zombieSpawner2(Random rand, double t){
        Timeline spawnZombie2 = new Timeline(new KeyFrame(Duration.seconds(t), event -> {
            int lane;
            int laneNumber = rand.nextInt(5);
            if(laneNumber==0)
                lane = LANE1;
            else if(laneNumber==1)
                lane = LANE2;
            else if(laneNumber==2)
                lane = LANE3;
            else if(laneNumber==3)
                lane = LANE4;
            else
                lane = LANE5;
            try
            {
                if(zombieList2.get(0)==0) {
                    Level.spawnNormalZombie(GamePlayRoot, lane, laneNumber);
                    zombieList2.remove(0);
                    updateSpawnedZombies();
                }
                else if(zombieList2.get(0)==1)
                {
                    Level.spawnConeZombie(GamePlayRoot, lane, laneNumber);
                    zombieList2.remove(0);
                    updateSpawnedZombies();
                }
                else if(zombieList2.get(0)==2)
                {
                    Level.spawnBucketZombie(GamePlayRoot, lane, laneNumber);
                    zombieList2.remove(0);
                    updateSpawnedZombies();
                }
            }
            catch(IndexOutOfBoundsException e)
            {
                endZombieSpawner2();
            }
        }));

        spawnZombie2.setCycleCount(Timeline.INDEFINITE);
        spawnZombie2.play();
        spZ2 = spawnZombie2;
        animationTimelines.add(spawnZombie2);
    }

    public void endZombieSpawner1()
    {
        spZ1.stop();
    }

    public void endZombieSpawner2()
    {
        spZ2.stop();
    }

    @FXML
    void getGridPosition(MouseEvent event) throws IOException {
        Node source = (Node) event.getSource();
        Integer colIndex = lawn_grid.getColumnIndex(source);
        Integer rowIndex = lawn_grid.getRowIndex(source);
        if (!shovel.isIsDisabled()) {
            shovel.disable();
            if (colIndex != null && rowIndex != null) {
                //System.out.println("shovelling"+colIndex+" "+rowIndex);
                Media shove = new Media(getClass().getResource("/assets/sounds/plant.wav").toString());
                MediaPlayer mediaPlayer = new MediaPlayer(shove);
                mediaPlayer.setAutoPlay(true);
                mediaPlayer.play();
                synchronized (allPlants) {
                    Iterator<Plant> i = allPlants.iterator();
                    while (i.hasNext()) {
                        Plant p = i.next();
                        //System.out.println("plant"+p.col+" "+p.row);
                        if (p.col == colIndex && p.row == rowIndex) {
                        	d.backup();
                            p.img.setVisible(false);
                            p.img.setDisable(true);
                            allPlants.remove(p);
                            //System.out.println(p.getClass());
                            p.setHp(0);
                            ((Shooter)p).checkHp();
                            ((Sunflower)p).checkHp();
                            ((Wallnut)p).checkHp();
                            break;
                        }
                    }
                }
            }
        }
        if (SidebarElement.getCardSelected() != -1) {
            if (colIndex != null && rowIndex != null) {
                boolean flag = true;
                synchronized (allPlants) {
                    Iterator<Plant> i = allPlants.iterator();
                    while (i.hasNext()) {
                        Plant p = i.next();
                        if (p.col == colIndex && p.row == rowIndex) {
                            flag = false;
                        }
                    }
                }
                if (flag) {
                    if (SidebarElement.getElement(SidebarElement.getCardSelected()).getCost() <= sunCount) {
                    	//d.backup();
                        placePlant(SidebarElement.getCardSelected(), (int) (source.getLayoutX() + source.getParent().getLayoutX()), (int) (source.getLayoutY() + source.getParent().getLayoutY()), colIndex, rowIndex);
                        updateSunCount((-1) * SidebarElement.getElement(SidebarElement.getCardSelected()).getCost());
                       
                        SidebarElement.getElement(SidebarElement.getCardSelected()).setDisabledOn(GamePlayRoot);
                    }
                    //else System.out.println("Not enough sun score");
                }
                //else System.out.println("Cant place more than one plant on cell");

            }
            SidebarElement.setCardSelectedToNull();
        }

    }

    public void placePlant(int val, int x, int y,int row,int col) {
        Plant p;
        Media plant = new Media(getClass().getResource("/assets/sounds/plant.wav").toString());
        MediaPlayer mediaPlayer = new MediaPlayer(plant);
        mediaPlayer.setAutoPlay(true);
        mediaPlayer.play();
        switch (val) {
            case 1:
                p=new Sunflower(x, y,row,col);
                allPlants.add(p);
                p.makeImage(lawn_grid);
                p.attack(GamePlayRoot);
                break;
            case 2:
                p=new PeaShooter(x, y,row,col);
                allPlants.add(p);
                p.makeImage(lawn_grid);
                p.attack(GamePlayRoot);
                break;
            case 3:
                p=new Wallnut(x, y,row,col);
                allPlants.add(p);
                p.makeImage(lawn_grid);
                p.attack(GamePlayRoot);
                break;
            case 4:
                p=new CherryBomb(x, y,row,col);
                allPlants.add(p);
                p.makeImage(lawn_grid);
                p.attack(GamePlayRoot);
                break;
            case 5:
                p=new Repeater(x, y,row,col);
                allPlants.add(p);
                p.makeImage(lawn_grid);
                p.attack(GamePlayRoot);
                break;
            case 6:
                p=new Jalapeno(x, y,row,col);
                allPlants.add(p);
                p.makeImage(lawn_grid);
                p.attack(GamePlayRoot);
                break;
            default:
                //System.out.println("No case match" + val);
        }
    }

    public static void endAnimations()
    {
        for(int i = 0; i<animationTimelines.size(); i++)
        {
            animationTimelines.get(i).stop();
        }
    }
    public void gameLost() throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("EndGame.fxml"));
        //FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("MainPage.fxml"));
        AnchorPane pane=fxmlLoader.load();
        EndGameController controller = fxmlLoader.<EndGameController>getController();
        controller.initData(levelNumber, false,d);
        GamePlayRoot.getChildren().setAll(pane);

    }
    public void gameWon() throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("EndGame.fxml"));
        //FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("MainPage.fxml"));
        AnchorPane pane=fxmlLoader.load();
        EndGameController controller = fxmlLoader.<EndGameController>getController();
        controller.initData(levelNumber, true,d);
        GamePlayRoot.getChildren().setAll(pane);

    }
}