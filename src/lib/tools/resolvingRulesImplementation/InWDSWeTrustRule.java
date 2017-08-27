package lib.tools.resolvingRulesImplementation;

import ILBprocessing.configuration.KeysDictionary;
import lib.model.NodeILB;
import lib.storage.CachedStorage;

/**
 * Created by Алекс on 03.05.2017.
 */
public class InWDSWeTrustRule extends CachedStorage {
    public static boolean corresponds(NodeILB e, NodeILB n){
        boolean seemsLikeTrue=false;
        String WDS1="", WDS2="";
        for (String catalogueN : n.getUsedCatalogues()) {
            for (javafx.util.Pair<String, String> entry : n.getParamsForCurrentCatalogue(catalogueN)) {
                if (entry.getKey().equals(KeysDictionary.WDSCOMP)) {
                    WDS1 = entry.getValue();
                }
                for (String catalogueE : e.getUsedCatalogues()) {
                    for (javafx.util.Pair<String, String> entry2 : e.getParamsForCurrentCatalogue(catalogueE)) {
                        if (entry2.getKey().equals(KeysDictionary.WDSCOMP)) {
                            WDS2 = entry2.getValue();
                        }
                    }
                    if (WDS1!=null && !WDS1.equals("") && WDS2!=null && WDS1.equals(WDS2)) {
                        seemsLikeTrue=true;
                        return true;
                    }else{
                        seemsLikeTrue=false;
                    }
                }
            }
        }
        return false;
    }
}
