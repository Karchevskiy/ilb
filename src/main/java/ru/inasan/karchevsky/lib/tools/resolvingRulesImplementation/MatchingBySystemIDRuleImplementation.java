package ru.inasan.karchevsky.lib.tools.resolvingRulesImplementation;

import ru.inasan.karchevsky.lib.model.Pair;
import ru.inasan.karchevsky.lib.model.StarSystem;
import ru.inasan.karchevsky.lib.pattern.Datasource;
import ru.inasan.karchevsky.lib.pattern.NodeForParsedCatalogue;
import ru.inasan.karchevsky.lib.storage.CachedStorage;

import java.util.ArrayList;

public class MatchingBySystemIDRuleImplementation extends CachedStorage {
    public static ArrayList<? extends NodeForParsedCatalogue> resolve(String key, ArrayList<? extends NodeForParsedCatalogue> list,
                                                                      Datasource datasourceClass,
                                                                      CachedStorage cachedStorage) {
        if(list.size()==0){
            return list;
        }
        for (NodeForParsedCatalogue aList : list) {
            StarSystem matchedTo = cachedStorage.s;

            try {
                Pair e = new Pair();
                datasourceClass.getClass().newInstance().propagate(e, aList);
                matchedTo.pairs.add(e);
            } catch (Exception e) {
                //e.printStackTrace();
                System.err.println(e.getMessage());
            }

        }

        return list;
    }
}
