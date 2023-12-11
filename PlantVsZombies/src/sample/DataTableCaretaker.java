//
//package sample;
//
//import java.io.Serializable;
//import java.util.ArrayList;
//import java.util.List;
//
//public class DataTableCaretaker implements Serializable {
//	private static final long serialVersionUID=42L;
//	private final transient List<DataTableMemento> mementoList = new ArrayList<>();
//
//
//    public void addMemento(DataTableMemento memento) {
//        mementoList.add(memento);
//    }
//
//    public DataTableMemento getMemento(int index) {
//    	System.out.println(toString());
//        if (index >= 0 && index < mementoList.size()) {
//            return mementoList.get(index);
//        }
//        return null;
//    }
//    
//    public String toString() {
//    	
//    	return mementoList.toString();
//    }
//
//}