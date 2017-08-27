package lib.pattern;

import lib.model.Component;
import lib.model.Pair;

/**
 * Created by Алекс on 07.01.2017.
 */

/**
 * Base interface. Store all data from @node to @pair.
 */
public interface Datasourse{

    /**
     * All values of equals attributes should be verified here.
     * */
    void propagate(Pair pair, NodeForParsedCatalogue node) throws Exception;
    void improve(Component pair, NodeForParsedCatalogue node) throws Exception;

}
