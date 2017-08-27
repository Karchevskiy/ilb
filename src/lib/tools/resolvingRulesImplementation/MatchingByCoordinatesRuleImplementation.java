package lib.tools.resolvingRulesImplementation;

import ILBprocessing.configuration.KeysDictionary;
import lib.model.Pair;
import lib.pattern.Datasourse;
import ILBprocessing.configuration.MatchingParameters;
import lib.pattern.NodeForParsedCatalogue;
import lib.storage.CachedStorage;

import java.util.ArrayList;

public class MatchingByCoordinatesRuleImplementation extends CachedStorage {
    public static ArrayList<? extends NodeForParsedCatalogue> resolve(ArrayList<? extends NodeForParsedCatalogue> list, Datasourse datasourceClass) {
        int f = list.size();
        for (int i = 0; i < f; i++) {
            boolean alreadyMatched = false;
            boolean tooManyMatches = false;
            Pair matchedTo = null;
            for (int j = 0; j < sysList.size(); j++) {
                for (int k = 0; k < sysList.get(j).pairs.size(); k++) {
                        if (corresponds(sysList.get(j).pairs.get(k),list.get(i))) {
                                if (!alreadyMatched) {
                                    alreadyMatched = true;
                                    matchedTo = sysList.get(j).pairs.get(k);
                                } else {
                                    tooManyMatches = true;
                                }
                    }
                }
            }
            if (!tooManyMatches && alreadyMatched) {
                try {
                   // System.err.println("MATCH by coordinates!"+ list.get(i).source);
                    datasourceClass.getClass().newInstance().propagate(matchedTo, list.get(i));
                    list.remove(i);
                    i--;
                    f--;
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                }
            }else{
                if(tooManyMatches) {
                   // System.err.println("UNRESOLVED! tooManyMatches:"+ list.get(i).source);
                }else{
                    //System.err.println("UNRESOLVED! not found matches:"+ list.get(i).source);
                }
            }
        }
        return list;
    }
    public static boolean corresponds(Pair e, NodeForParsedCatalogue n) {
        for (String catalogue : e.getUsedCatalogues()) {
            double x1 = -100, y1 = -100;
            for (javafx.util.Pair<String, Double> entry : e.getCoordinatesForCurrentCatalogue(catalogue)) {
                if (entry.getKey().equals(KeysDictionary.X)) {
                    x1 = entry.getValue();
                } else if (entry.getKey().equals(KeysDictionary.Y)) {
                    y1 = entry.getValue();
                }
            }
            double x2 = Double.parseDouble(n.params.get(KeysDictionary.X));
            double y2 = Double.parseDouble(n.params.get(KeysDictionary.Y));

            double r = Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));

            if (r < MatchingParameters.COORDINATES_MATCHING_LIMIT_SAME) {
                //System.err.println("matched to: " + n.params.get(KeysDictionary.X) + " | " + n.params.get(KeysDictionary.Y));
                return true;
            } else {
                return false;
            }
        }
        return false;
    }
}
