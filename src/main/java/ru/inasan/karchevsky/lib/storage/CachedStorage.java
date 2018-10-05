package ru.inasan.karchevsky.lib.storage;

import lombok.Getter;
import ru.inasan.karchevsky.lib.model.StarSystem;
import java.util.ArrayList;

@Getter
public abstract class CachedStorage {
    public ArrayList<StarSystem> sysList = new ArrayList<>();
}
