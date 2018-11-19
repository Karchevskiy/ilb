package ru.inasan.karchevsky.lib.storage;

import lombok.Getter;
import lombok.Setter;
import ru.inasan.karchevsky.lib.model.StarSystem;
import java.util.ArrayList;

@Getter
public abstract class CachedStorage {

    @Setter
    @Getter
    public StarSystem s = new StarSystem();

    public ArrayList<StarSystem> sysList = new ArrayList<>();
}
