package lib.tools.resolvingRulesImplementation;

import lib.model.Pair;
import lib.model.StarSystem;
import lib.pattern.Datasource;
import lib.pattern.NodeForParsedCatalogue;
import lib.storage.CachedStorage;

import java.util.ArrayList;

public class MatchingBySystemIDRuleImplementation extends CachedStorage {
    public static ArrayList<? extends NodeForParsedCatalogue> resolve(String key, ArrayList<? extends NodeForParsedCatalogue> list, Datasource datasourceClass){
        int f=list.size();
        for(int i=0;i<f;i++){
            boolean alreadyMatched=false;
            boolean tooManyMatches=false;
            StarSystem matchedTo=null;
            for(int j = 0; j< sysList.size(); j++){
                    if(list.get(i).params.containsKey(key)){
                        if(sysList.get(j).params.containsKey(key)) {
                            if (list.get(i).params.get(key).equals(sysList.get(j).params.get(key))) {
                                if (!alreadyMatched) {
                                    alreadyMatched = true;
                                    matchedTo = sysList.get(j);
                                }
                            }
                        }
                    for(Pair pair: sysList.get(j).pairs){
                        for(String value: pair.getParamsForByKey(key)){
                            if (list.get(i).params.get(key).equals(value)) {
                                if (!alreadyMatched) {
                                    alreadyMatched = true;
                                    matchedTo = sysList.get(j);
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
