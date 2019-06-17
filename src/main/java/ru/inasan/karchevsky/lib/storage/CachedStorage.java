package ru.inasan.karchevsky.lib.storage;

import ru.inasan.karchevsky.lib.model.StarSystem;
import java.util.ArrayList;

public abstract class CachedStorage {
    public ArrayList<StarSystem> sysList = new ArrayList<>();

    public ArrayList<StarSystem> getSysList() {
        return sysList;
    }

    public void setSysList(ArrayList<StarSystem> sysList) {
        this.sysList = sysList;
    }
}
