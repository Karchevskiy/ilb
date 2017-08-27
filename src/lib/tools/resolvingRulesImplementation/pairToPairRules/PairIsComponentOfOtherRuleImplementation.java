package lib.tools.resolvingRulesImplementation.pairToPairRules;


import ILBprocessing.configuration.KeysDictionary;
import lib.model.Pair;
import lib.storage.CachedStorage;

import java.util.ArrayList;

public class PairIsComponentOfOtherRuleImplementation extends CachedStorage {
    public static void rebuildTree() {
        int f = sysList.size();
        for (int i = 0; i < f; i++) {
            for (int k = 0; k < sysList.get(i).pairs.size(); k++) {
                Pair currentPair=sysList.get(i).pairs.get(k);
                for (int j = 0; j < sysList.get(i).pairs.size(); j++) {

                    ArrayList<Double> rhoN = (ArrayList<Double>) sysList.get(i).pairs.get(j).getCoordinatesByKey(KeysDictionary.RHO);
                    ArrayList<Double> rhoE = (ArrayList<Double>) currentPair.getCoordinatesByKey(KeysDictionary.RHO);

                   layerOf2Pairs: for(Double n: rhoN){
                        if(n!=-1 && n>0 && n<1){
                            for(Double e: rhoE){
                                if(e!=-1 && e>0 && e>n*6){
                                    if(MatchingByCoordinatesRuleImplementation.corresponds(sysList.get(i).pairs.get(j).el1,currentPair)){
                                        currentPair.equalNodeOnNextLevel=sysList.get(i).pairs.get(j).el1;
                                        break layerOf2Pairs;
                                    }else if(MatchingByCoordinatesRuleImplementation.corresponds(sysList.get(i).pairs.get(j).el2,currentPair)){
                                        currentPair.equalNodeOnNextLevel=sysList.get(i).pairs.get(j).el2;
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
