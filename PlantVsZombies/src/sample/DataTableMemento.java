//
//package sample;
//
//import java.util.List;
//import java.util.ArrayList;
//import java.io.Serializable;
//
//public class DataTableMemento implements Serializable {
//	private DataTable gameSession = null;
//	private int gameId ;
//    private int sunCount;
//    private List<Plant> allPlants = null;
//    private List<Zombie> allZombie = null;
//    private ArrayList<Integer> zombieList1= null;
//    private ArrayList<Integer> zombieList2= null;
//    private double timeElapsed;
//    private boolean status;
//    private List<LawnMower> allLawnMowers= null;
//    private int levelNumber;
//    private String savingTimeStamp;
//         
//        
//    public DataTableMemento(DataTable gameSession) {
//        this.gameSession = gameSession;
//        this.gameId = gameSession.getGameId();
//        this.sunCount = gameSession.getSunCount();
//        this.allPlants = new ArrayList<>(gameSession.getAllPlants());
//        this.allZombie = new ArrayList<>(gameSession.getAllZombie());
//        this.zombieList1 = new ArrayList<>(gameSession.getZombieList1());
//        this.zombieList2 = new ArrayList<>(gameSession.getZombieList2());
//        this.timeElapsed = gameSession.getTimeElapsed();
//        this.status = gameSession.getStatus();
//        this.allLawnMowers = new ArrayList<>(gameSession.getAllLawnMowers());
//        this.levelNumber = gameSession.getLevelNumber();
//        this.savingTimeStamp = gameSession.getSavingTimeStamp();
//        
//        System.out.println("A memento was created");
//         
//    }
//    
//
//    //getters and setters
//    
//    public int getGameId() {
//        return this.gameId;
//    }
//    
//    public int getSunCount() {
//        return this.sunCount;
//    }
//    
//    public List<Plant> getAllPlants() {
//        return this.allPlants;
//    }
//    
//    public List<Zombie> getAllZombie() {
//        return this.allZombie;
//    }
//    
//    public ArrayList<Integer> getZombieList1()
//    {
//        return this.zombieList1;
//    }
//    
//    public ArrayList<Integer> getZombieList2()
//    {
//        return this.zombieList2;
//    }
//
//    public double getTimeElapsed() {
//        //System.out.println(this.timeElapsed);
//        return this.timeElapsed;
//    }
//    
//    public boolean getStatus() {
//    	return this.status;
//    }
//    
//    public List<LawnMower> getAllLawnMowers() {
//        return this.allLawnMowers;
//    }
//
//    public int getLevelNumber() {
//        return this.levelNumber;
//    }
//
//    public String getSavingTimeStamp() {
//        return this.savingTimeStamp;
//    }
//    
//}