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
                                                                      CachedStorage cachedStorage){
        int f=list.size();
        for(int i=0;i<f;i++){
            boolean alreadyMatched=false;
            boolean tooManyMatches=false;
            StarSystem matchedTo=null;
            for(int j = 0; j< cachedStorage.getSysList().size(); j++){
                    if(list.get(i).params.containsKey(key)){
                        if(cachedStorage.getSysList().get(j).params.containsKey(key)) {
                            if (list.get(i).params.get(key).equals(cachedStorage.getSysList().get(j).params.get(key))) {
                                if (!alreadyMatched) {
                                    alreadyMatched = true;
                                    matchedTo = cachedStorage.getSysList().get(j);
                                }
                            }
                        }
                    for(Pair pair: cachedStorage.getSysList().get(j).pairs){
                        for(String value: pair.getParamsForByKey(key)){
                            if (list.get(i).params.get(key).equals(value)) {
                                if (!alreadyMatched) {
                                    alreadyMatched = true;
                                    matchedTo = cachedStorage.getSysList().get(j);
                                }
                            }
                        }
                    }
                }
            }
            if(!tooManyMatches && alreadyMatched){
                try{
                    Pair e = new Pair();
                    datasourceClass.getClass().newInstance().propagate(e,list.get(i));
                    matchedTo.pairs.add(e);
                    list.remove(i);
                    i--;
                    f--;
                }catch (Exception e){
                    /**System.err.println(e.getMessage());*/
                }
            }
        }

        return list;
    }
}
