package lib.tools.resolvingRulesImplementation.pairToPairRules;

import ILBprocessing.configuration.KeysDictionary;
import ILBprocessing.configuration.MatchingParameters;
import lib.model.NodeILB;
import lib.storage.CachedStorage;

/**
 * Created by Алекс on 08.01.2017.
 */
public class MatchingByAnglesImplementation extends CachedStorage {
    public static boolean corresponds(NodeILB e, NodeILB n)  {
        boolean seemsLikeTrue=true;

        for (String catalogueN : n.getUsedCatalogues()) {
            double Xrh = -100, Xtheta = -100;
            for (javafx.util.Pair<String, Double> entry : n.getCoordinatesForCurrentCatalogue(catalogueN)) {
                if (entry.getKey().equals(KeysDictionary.RHO)) {
                    Xrh = entry.getValue();
                } else if (entry.getKey().equals(KeysDictionary.THETA)) {
                    Xtheta = entry.getValue();
                }
                for (String catalogueE : e.getUsedCatalogues()) {
                    double Yrh = -100, Ytheta = -100;
                    for (javafx.util.Pair<String, Double> entry2 : e.getCoordinatesForCurrentCatalogue(catalogueE)) {
                        if (entry2.getKey().equals(KeysDictionary.RHO)) {
                            Yrh = entry2.getValue();
                        } else if (entry2.getKey().equals(KeysDictionary.THETA)) {
                            Ytheta = entry2.getValue();
                        }
                    }
                    if(Xrh==-100 || Yrh==-100){
                        break;
                    }
                    double r1=Xrh,r2=Yrh;
                    double t1=Xtheta,t2=Ytheta;

                    double func1 = (r1 - r2) * (r1 - r2) / Math.max(r1, r2)/ Math.max(r1, r2) + (t1 - t2) * (t1 - t2) / Math.max(t1, t2)/ Math.max(t1, t2);
                    double func2 = (r1 - r2) * (r1 - r2) / Math.max(r1, r2)/ Math.max(r1, r2) + (t1 - t2-180) * (t1 - t2-180) / Math.max(t1, t2)/ Math.max(t1, t2);
                    double r= Math.min(func1,func2)/2;

                    if (r < MatchingParameters.ANGLE_MATCHING_LIMIT) {
                        seemsLikeTrue=true;
                        return seemsLikeTrue;
                    }else{
                        seemsLikeTrue=false;
                    }
                }
            }
        }
     return seemsLikeTrue;
    }
}
