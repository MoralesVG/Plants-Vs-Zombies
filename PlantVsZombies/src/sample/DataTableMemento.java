package sample;

public class DataTableMemento {
	private DataTable gameSession = null;         
        
    public DataTableMemento(DataTable gameSession) {
        this.gameSession = gameSession;
        
    }

    public DataTable restore(){
        return this.gameSession;
    }
}