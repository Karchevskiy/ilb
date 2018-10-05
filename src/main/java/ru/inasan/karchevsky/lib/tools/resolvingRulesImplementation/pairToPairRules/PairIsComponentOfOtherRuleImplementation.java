package ru.inasan.karchevsky.lib.tools.resolvingRulesImplementation.pairToPairRules;


import ru.inasan.karchevsky.configuration.KeysDictionary;
import ru.inasan.karchevsky.lib.model.Pair;
import ru.inasan.karchevsky.lib.storage.CachedStorage;

import java.util.ArrayList;

public class PairIsComponentOfOtherRuleImplementation extends CachedStorage {
    public static void rebuildTree(CachedStorage cachedStorage) {
        int f = cachedStorage.getSysList().size();
        for (int i = 0; i < f; i++) {
            for (int k = 0; k < cachedStorage.getSysList().get(i).pairs.size(); k++) {
                Pair currentPair=cachedStorage.getSysList().get(i).pairs.get(k);
                for (int j = 0; j < cachedStorage.getSysList().get(i).pairs.size(); j++) {

                    ArrayList<Double> rhoN = (ArrayList<Double>) cachedStorage.getSysList().get(i).pairs.get(j).getCoordinatesByKey(KeysDictionary.RHO);
                    ArrayList<Double> rhoE = (ArrayList<Double>) currentPair.getCoordinatesByKey(KeysDictionary.RHO);

                   layerOf2Pairs: for(Double n: rhoN){
                        if(n!=-1 && n>0 && n<1){
                            for(Double e: rhoE){
                                if(e!=-1 && e>0 && e>n*6){
                                    if(MatchingByCoordinatesRuleImplementation.corresponds(cachedStorage.getSysList().get(i).pairs.get(j).el1,currentPair)){
                                        currentPair.equalNodeOnNextLevel=cachedStorage.getSysList().get(i).pairs.get(j).el1;
                                        break layerOf2Pairs;
                                    }else if(MatchingByCoordinatesRuleImplementation.corresponds(cachedStorage.getSysList().get(i).pairs.get(j).el2,currentPair)){
                                        currentPair.equalNodeOnNextLevel=cachedStorage.getSysList().get(i).pairs.get(j).el2;
                                        break layerOf2Pairs;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
