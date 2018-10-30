package ru.inasan.karchevsky.lib.pattern;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;

/**
 * Created by Алекс on 07.01.2017.
 */

@Setter
@Getter
public abstract class NodeForParsedCatalogue {
    public String source;
    public String uniqueCatalogueID;
    public HashMap<String,String> params = new HashMap<>();


    private String systemGenId;

    public abstract Double getXel1();
    public abstract Double getYel1();

    public abstract Double getXel2();
    public abstract Double getYel2();
}
