package lib.tools.resolvingRulesImplementation.pairToCompRules;

import ILBprocessing.configuration.KeysDictionary;
import ILBprocessing.configuration.MatchingParameters;
import lib.model.Component;
import lib.model.NodeILB;
import lib.pattern.Datasourse;
import lib.pattern.NodeForParsedCatalogue;
import lib.storage.CachedStorage;

import java.util.ArrayList;

import static ILBprocessing.configuration.MatchingParameters.COORDINATES_MATCHING_LIMIT_SAME;


public class MatchingByCoordRuleForComponentsImplementation  extends CachedStorage {
    public static ArrayList<? extends NodeForParsedCatalogue> resolve(ArrayList<? extends NodeForParsedCatalogue> list, Datasourse datasourceClass) {
        int f = list.size();
        for (int i = 0; i < f; i++) {
            Component matchedTo = null;
            double dist=COORDINATES_MATCHING_LIMIT_SAME;

            for (int j = 0; j < sysList.size(); j++) {
                for (int k = 0; k < sysList.get(j).pairs.size(); k++) {
                    double distCurrent=corresponds(sysList.get(j).pairs.get(k).el1,list.get(i));
                    if (distCurrent!=-1 && distCurrent<dist) {
                        dist=distCurrent;
                        matchedTo = sysList.get(j).pairs.get(k).el1;
                    }
                    distCurrent=corresponds(sysList.get(j).pairs.get(k).el2,list.get(i));
                    if (distCurrent!=-1 && distCurrent<dist) {
                        dist=distCurrent;
                        matchedTo = sysList.get(j).pairs.get(k).el1;
                    }
                }
            }
            if (matchedTo!=null) {
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
        double r0=MatchingParameters.COORDINATES_MATCHING_LIMIT_SAME;
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