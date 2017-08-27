package lib.pattern;

import java.util.HashMap;

/**
 * Created by Алекс on 07.01.2017.
 */


public abstract class NodeForParsedCatalogue {
    public String source;
    public String uniqueCatalogueID;
    public HashMap<String,String> params = new HashMap<String,String>();
}
