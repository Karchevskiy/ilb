package ru.inasan.karchevsky.lib.pattern;


import java.util.HashMap;

/**
 * Created by Алекс on 07.01.2017.
 */

public abstract class NodeForParsedCatalogue {
    public String source;
    public String uniqueCatalogueID;
    public HashMap<String,String> params = new HashMap<>();

    public abstract Double getXel1();
    public abstract Double getYel1();

    public abstract Double getXel2();
    public abstract Double getYel2();

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getUniqueCatalogueID() {
        return uniqueCatalogueID;
    }

    public void setUniqueCatalogueID(String uniqueCatalogueID) {
        this.uniqueCatalogueID = uniqueCatalogueID;
    }

    public HashMap<String, String> getParams() {
        return params;
    }

    public void setParams(HashMap<String, String> params) {
        this.params = params;
    }
}
