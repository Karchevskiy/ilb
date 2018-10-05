package ru.inasan.karchevsky.lib.pattern;

import ru.inasan.karchevsky.lib.model.Component;
import ru.inasan.karchevsky.lib.model.Pair;

/**
 * Created by Алекс on 07.01.2017.
 */

/**
 * Base interface. Store all data from @node to @pair.
 */
public interface Datasource {

    /**
     * All values of equals attributes should be verified here.
     * */
    void propagate(Pair pair, NodeForParsedCatalogue node) throws Exception;
    void improve(Component pair, NodeForParsedCatalogue node) throws Exception;

}
