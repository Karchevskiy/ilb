package ru.inasan.karchevsky.lib.tools.resolvingRulesImplementation.pairToCompRules;

import ru.inasan.karchevsky.configuration.KeysDictionary;
import ru.inasan.karchevsky.configuration.MatchingParameters;
import ru.inasan.karchevsky.lib.model.Component;
import ru.inasan.karchevsky.lib.model.NodeILB;
import ru.inasan.karchevsky.lib.model.Pair;
import ru.inasan.karchevsky.lib.pattern.Datasource;
import ru.inasan.karchevsky.lib.pattern.NodeForParsedCatalogue;
import ru.inasan.karchevsky.lib.storage.CachedStorage;

import java.util.ArrayList;

import static ru.inasan.karchevsky.configuration.MatchingParameters.COORDINATES_MATCHING_LIMIT_SAME_EQ;


public class MatchingByCoordRuleForComponentsImplementation  extends CachedStorage {
    public static ArrayList<? extends NodeForParsedCatalogue> resolve(ArrayList<? extends NodeForParsedCatalogue> list,
                                                                      Datasource datasourceClass,
                                                                      CachedStorage cachedStorage) {
        int f = list.size();
        for (int i = 0; i < f; i++) {
            Component matchedTo = null;
            Pair matchedToPair = null;
            double dist = COORDINATES_MATCHING_LIMIT_SAME_EQ, distInitial= COORDINATES_MATCHING_LIMIT_SAME_EQ;

            for (int j = 0; j < cachedStorage.getSysList().size(); j++) {
                for (int k = 0; k < cachedStorage.getSysList().get(j).pairs.size(); k++) {
                    double distCurrent=corresponds(cachedStorage.getSysList().get(j).pairs.get(k).el1,list.get(i));
                    int countMatched = 0;
                    if (distCurrent!=-1 && distCurrent<distInitial) {
                        dist=distCurrent;
                        matchedTo = cachedStorage.getSysList().get(j).pairs.get(k).el1;
                        countMatched++;
                    }
                    distCurrent=corresponds(cachedStorage.getSysList().get(j).pairs.get(k).el2,list.get(i));
                    if (distCurrent!=-1 && distCurrent<distInitial) {
                        if(distCurrent<dist) {
                            matchedTo = cachedStorage.getSysList().get(j).pairs.get(k).el2;
                        }
                        countMatched++;
                    }
                    if(countMatched>1){
                        matchedToPair = cachedStorage.getSysList().get(j).pairs.get(k);
                    }
                }
            }
            if (matchedToPair!=null) {
                try {
                    datasourceClass.getClass().newInstance().propagate(matchedToPair, list.get(i));
                    list.remove(i);
                    i--;
                    f--;
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                }
            } else if (matchedTo!=null) {
                try {
                    datasourceClass.getClass().newInstance().improve(matchedTo, list.get(i));
                    list.remove(i);
                    i--;
                    f--;
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                }
            }
        }
        return list;
    }
    public static double corresponds(NodeILB e, NodeForParsedCatalogue n) {

        double x2 = Double.parseDouble(n.params.get(KeysDictionary.X));
        double y2 = Double.parseDouble(n.params.get(KeysDictionary.Y));
        double r0=MatchingParameters.COORDINATES_MATCHING_LIMIT_SAME_EQ;
        double result = 100;
        for (String catalogue : e.getUsedCatalogues()) {
            double x1 = -100, y1 = -100;
            for (javafx.util.Pair<String, Double> entry : e.getCoordinatesForCurrentCatalogue(catalogue)) {
                if (entry.getKey().equals(KeysDictionary.X)) {
                    x1 = entry.getValue();
                } else if (entry.getKey().equals(KeysDictionary.Y)) {
                    y1 = entry.getValue();
                }
            }

            double r = Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));

            if (r < r0 && r<result) {
                result=r;
            }
        }
        if(result!=100) {
            return result;
        }else{
            return -1;
        }
    }
}