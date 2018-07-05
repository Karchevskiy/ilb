package lib.tools.resolvingRulesImplementation.pairToPairRules;


import ILBprocessing.beans.NodeCEV;
import ILBprocessing.beans.NodeSB9;
import ILBprocessing.configuration.KeysDictionary;
import ILBprocessing.configuration.MatchingParameters;
import ILBprocessing.datasources.SB9DS;
import lib.model.Component;
import lib.model.NodeILB;
import lib.model.Pair;

public class MatchingByCoordinatesRuleImplementation {
    public static boolean corresponds(NodeILB e, NodeILB n) {//finalized
        boolean seemsLikeTrue=false;
        for (String catalogueN : n.getUsedCatalogues()) {
            double xN = -100, yN = -100;
            for (javafx.util.Pair<String, Double> entry : n.getCoordinatesForCurrentCatalogue(catalogueN)) {
                if (entry.getKey().equals(KeysDictionary.X)) {
                    xN = entry.getValue();
                } else if (entry.getKey().equals(KeysDictionary.Y)) {
                    yN = entry.getValue();
                }
                if (entry.getKey().equals(KeysDictionary.RHO)) {
                    if(entry.getValue()==-1 || entry.getValue()==0) {
                        continue;
                    }
                }
            }
            for (String catalogueE : e.getUsedCatalogues()) {
                double xE = -100, yE = -100;
                for (javafx.util.Pair<String, Double> entry : e.getCoordinatesForCurrentCatalogue(catalogueE)) {
                    if (entry.getKey().equals(KeysDictionary.X)) {
                        xE = entry.getValue();
                    } else if (entry.getKey().equals(KeysDictionary.Y)) {
                        yE = entry.getValue();
                    }
                    if (entry.getKey().equals(KeysDictionary.RHO)) {
                        if(entry.getValue()==-1 || entry.getValue()==0) {
                            continue;
                        }
                    }
                }
                double r = (xN - xE) * (xN - xE) + (yN - yE) * (yN - yE);
                if (r < MatchingParameters.COORDINATES_MATCHING_LIMIT_EQUALS) {
                    seemsLikeTrue=true;
                }else{
                    seemsLikeTrue=false;
                }
            }
        }
        return seemsLikeTrue;
    }
    public static boolean correspondsPairToNode(Pair e, Component n) {//finalized
        if(n.getUsedCatalogues().contains(NodeSB9.uniqueCatalogueID) ||
                n.getUsedCatalogues().contains(NodeCEV.uniqueCatalogueID))
            return false;
        boolean seemsLikeTrue=true;
        for (String catalogueN : n.getUsedCatalogues()) {
            double xN = -100, yN = -100;
            for (javafx.util.Pair<String, Double> entry : n.getCoordinatesForCurrentCatalogue(catalogueN)) {
                if (entry.getKey().equals(KeysDictionary.X)) {
                    xN = entry.getValue();
                } else if (entry.getKey().equals(KeysDictionary.Y)) {
                    yN = entry.getValue();
                }
            }
            for (String catalogueE : e.el1.getUsedCatalogues()) {
                double xE1 = -100, yE1 = -100;
                double xE2 = -100, yE2 = -100;
                for (javafx.util.Pair<String, Double> entry : e.el1.getCoordinatesForCurrentCatalogue(catalogueE)) {
                    if (entry.getKey().equals(KeysDictionary.X)) {
                        xE1 = entry.getValue();
                    } else if (entry.getKey().equals(KeysDictionary.Y)) {
                        yE1 = entry.getValue();
                    }
                }
                for (javafx.util.Pair<String, Double> entry : e.el2.getCoordinatesForCurrentCatalogue(catalogueE)) {
                    if (entry.getKey().equals(KeysDictionary.X)) {
                        xE2 = entry.getValue();
                    } else if (entry.getKey().equals(KeysDictionary.Y)) {
                        yE2 = entry.getValue();
                    }
                }
                double r = (xN - xE1) * (xN - xE1) + (yN - yE1) * (yN - yE1)+(xN - xE2) * (xN - xE2) + (yN - yE2) * (yN - yE2);
                if (r < 2*MatchingParameters.COORDINATES_MATCHING_LIMIT_EQUALS) {
                    seemsLikeTrue=true;
                    return seemsLikeTrue;
                }else{
                    seemsLikeTrue=false;
                }
            }
        }
        return seemsLikeTrue;
    }
}
