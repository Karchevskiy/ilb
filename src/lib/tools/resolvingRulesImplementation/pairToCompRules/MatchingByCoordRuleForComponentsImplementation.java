package lib.tools.resolvingRulesImplementation.pairToCompRules;

import ILBprocessing.configuration.KeysDictionary;
import ILBprocessing.configuration.MatchingParameters;
import lib.model.Component;
import lib.model.NodeILB;
import lib.model.Pair;
import lib.pattern.Datasource;
import lib.pattern.NodeForParsedCatalogue;
import lib.storage.CachedStorage;

import java.util.ArrayList;

import static ILBprocessing.configuration.MatchingParameters.COORDINATES_MATCHING_LIMIT_SAME;
import static ILBprocessing.configuration.MatchingParameters.COORDINATES_MATCHING_LIMIT_SAME_EQ;


public class MatchingByCoordRuleForComponentsImplementation  extends CachedStorage {
    public static ArrayList<? extends NodeForParsedCatalogue> resolve(ArrayList<? extends NodeForParsedCatalogue> list, Datasource datasourceClass) {
        int f = list.size();
        for (int i = 0; i < f; i++) {
            Component matchedTo = null;
            Pair matchedToPair = null;
            double dist = COORDINATES_MATCHING_LIMIT_SAME_EQ, distInitial= COORDINATES_MATCHING_LIMIT_SAME_EQ;

            for (int j = 0; j < sysList.size(); j++) {
                for (int k = 0; k < sysList.get(j).pairs.size(); k++) {
                    double distCurrent=corresponds(sysList.get(j).pairs.get(k).el1,list.get(i));
                    int countMatched = 0;
                    if (distCurrent!=-1 && distCurrent<distInitial) {
                        dist=distCurrent;
                        matchedTo = sysList.get(j).pairs.get(k).el1;
                        countMatched++;
                    }
                    distCurrent=corresponds(sysList.get(j).pairs.get(k).el2,list.get(i));
                    if (distCurrent!=-1 && distCurrent<distInitial) {
                        if(distCurrent<dist) {
                            matchedTo = sysList.get(j).pairs.get(k).el2;
                        }
                        countMatched++;
                    }
                    if(countMatched>1){
                        matchedToPair = sysList.get(j).pairs.get(k);
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