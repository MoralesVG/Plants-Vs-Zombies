package sample;

import java.util.ArrayList;
import java.util.List;

public class DataTableCaretaker {
	private final transient List<DataTableMemento> mementoList = new ArrayList<>();


    public void addMemento(DataTableMemento DTM) {
        mementoList.add(DTM);
    }

    public DataTableMemento getMemento() {
        if (mementoList.size() > 0) {
            DataTableMemento temp = mementoList.get(mementoList.size()-1);
            mementoList.remove(mementoList.size()-1);
            return temp;
        }
        return null;
    }
}