package ru.inasan.karchevsky.lib.tools.resolvingRulesImplementation;

import ru.inasan.karchevsky.configuration.KeysDictionary;
import ru.inasan.karchevsky.lib.model.Pair;
import ru.inasan.karchevsky.lib.pattern.Datasource;
import ru.inasan.karchevsky.lib.pattern.NodeForParsedCatalogue;
import ru.inasan.karchevsky.lib.storage.CachedStorage;

import java.util.ArrayList;
import java.util.List;

import static ru.inasan.karchevsky.configuration.MatchingParameters.ANGLE_MATCHING_LIMIT;

/**
 * Created by Алекс on 08.01.2017.
 */
public class MatchingByAnglesImplementation extends CachedStorage {
    public static ArrayList<? extends NodeForParsedCatalogue> resolve(ArrayList<? extends NodeForParsedCatalogue> list,
                                                                      Datasource datasourceClass,
                                                                      CachedStorage cachedStorage) {
        int f = list.size();
        for (int i = 0; i < f; i++) {
            double dist = ANGLE_MATCHING_LIMIT;
            Pair matchedTo = null;
            for (int j = 0; j < cachedStorage.getSysList().size(); j++) {
                for (int k = 0; k < cachedStorage.getSysList().get(j).pairs.size(); k++) {
                    double distCurrent = dist(cachedStorage.getSysList().get(j).pairs.get(k), list.get(i));
                    if (distCurrent != -1 && distCurrent < dist) {
                        dist = distCurrent;
                        matchedTo = cachedStorage.getSysList().get(j).pairs.get(k);
                    }
                }
            }
            try {
                if (matchedTo != null) {
                    //System.err.println("MATCH"+ list.get(i).source);
                    datasourceClass.getClass().newInstance().propagate(matchedTo, list.get(i));
                    list.remove(i);
                    i--;
                    f--;
                }
            } catch (Exception e) {
                //e.printStackTrace();
                /**System.err.println(e.getMessage());*/
            }
        }
        return list;
    }


    public static ArrayList<? extends NodeForParsedCatalogue> resolveOnlyByRho(ArrayList<? extends NodeForParsedCatalogue> list,
                                                                               Datasource datasourceClass,
                                                                               CachedStorage cachedStorage) {
        int f = list.size();
        for (int i = 0; i < f; i++) {
            boolean alreadyMatched = false;
            boolean tooManyMatches = false;
            Pair matchedTo = null;
            for (int j = 0; j < cachedStorage.getSysList().size(); j++) {
                for (int k = 0; k < cachedStorage.getSysList().get(j).pairs.size(); k++) {
                    if (fastAngleValidatorOnlyForRho(cachedStorage.getSysList().get(j).pairs.get(k), list.get(i))) {
                        if (!alreadyMatched) {
                            alreadyMatched = true;
                            matchedTo = cachedStorage.getSysList().get(j).pairs.get(k);
                        } else {
                            tooManyMatches = true;
                        }
                    }
                }
            }
            if (!tooManyMatches && alreadyMatched) {
                try {
                    //System.err.println("MATCH 1 angle"+ list.get(i).source);
                    datasourceClass.getClass().newInstance().propagate(matchedTo, list.get(i));
                    list.remove(i);
                    i--;
                    f--;
                } catch (Exception e) {
                    //e.printStackTrace();
                    /**System.err.println(e.getMessage());*/
                }
            } else {
                if (tooManyMatches) {
                    // System.err.println("  tooManyMatches "+ list.get(i).source);
                } else {
                    //System.err.println("  notCorrespondsToCriteria "+ list.get(i).source);
                }
            }
        }
        return list;
    }

    public static boolean fastAngleValidatorOnlyForRho(Pair e, NodeForParsedCatalogue n) {
        if (MatchingByCoordinatesRuleImplementation.corresponds(e, n)) {
            try {
                Double r1 = Double.parseDouble(n.params.get(KeysDictionary.RHO));
                List<Double> r2values = e.getCoordinatesByKey(KeysDictionary.RHO);
                for (Double r2 : r2values) {
                    double func = (r1 - r2) * (r1 - r2) / Math.max(r1, r2) / Math.max(r1, r2);
                    if (func < ANGLE_MATCHING_LIMIT) {
                        //   System.err.println("resolved only by RHO: " + r1 + " :" + r2);
                        return true;
                    }
                }
            } catch (Exception e1) {
                //System.err.print("not enough data: "+n.source);
            }
        }
        return false;
    }

    public static double dist(Pair e, NodeForParsedCatalogue n) {
        double best = 10000;
        if (MatchingByCoordinatesRuleImplementation.corresponds(e, n)) {
            for (String catalog : e.getUsedCatalogues()) {
                double rho = 1000, theta = 100;
                for (javafx.util.Pair<String, Double> value : e.getCoordinatesForCurrentCatalogue(catalog)) {
                    if (value.getKey().equals(KeysDictionary.RHO)) {
                        rho = value.getValue();
                    } else if (value.getKey().equals(KeysDictionary.THETA)) {
                        theta = value.getValue();
                    }
                }
                double result = dist(rho, theta, n);
                if (result >= 0 && result < best) {
                    best = result;
                }

            }
        }
        return best;
    }
    public static double dist(double rho, double theta, NodeForParsedCatalogue n){
            try {
                double r1 = Double.parseDouble(n.params.get(KeysDictionary.RHO));
                double r2 = rho;
                double t1 = Double.parseDouble(n.params.get(KeysDictionary.THETA));
                double t2 = theta;
                double func1 = (r1 - r2) * (r1 - r2) / Math.max(r1, r2) + (t1 - t2) * (t1 - t2) / Math.max(t1, t2)/2;
                double func2 = (r1 - r2) * (r1 - r2) / Math.max(r1, r2) + (t1 - t2-180) * (t1 - t2-180) / Math.max(t1, t2)/2;
                return Math.min(func1,func2);
            }catch (Exception e1){
               // System.err.print("not enough data: "+n.source);
            }
        return -1;
    }
}
