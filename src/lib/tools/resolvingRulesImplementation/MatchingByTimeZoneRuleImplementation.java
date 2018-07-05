package lib.tools.resolvingRulesImplementation;

import ILBprocessing.beans.NodeCCDM;
import ILBprocessing.configuration.KeysDictionary;
import ILBprocessing.datasources.CCDMDS;
import ILBprocessing.storage.CachedStorageILB;
import lib.model.Pair;
import lib.model.StarSystem;
import lib.pattern.Datasource;
import lib.pattern.NodeForParsedCatalogue;

import java.util.ArrayList;

public class MatchingByTimeZoneRuleImplementation  extends CachedStorageILB {
    public static ArrayList<? extends NodeForParsedCatalogue> resolve(String key, ArrayList<? extends NodeForParsedCatalogue> list, Datasource datasourceClass){
        for(int i=0;i<list.size();i++) {
            try {
                NodeForParsedCatalogue node = list.get(i);
                int zone = Integer.parseInt(node.params.get(key).substring(0, 5));
                int zoneMin = Integer.parseInt(listWDS.get(0).params.get(KeysDictionary.WDSSYSTEM).substring(0, 5));
                int zoneMax = Integer.parseInt(listWDS.get(listWDS.size() - 1).params.get(KeysDictionary.WDSSYSTEM).substring(0, 5));
                if (zone >= zoneMin && zone <= zoneMax) {
                    StarSystem system = new StarSystem();
                    if (node.params.containsKey(KeysDictionary.WDSSYSTEM)) {
                        system.params.put(KeysDictionary.WDSSYSTEM, node.params.get(KeysDictionary.WDSSYSTEM));
                    }
                    if (node.params.containsKey(KeysDictionary.CCDMSYSTEM)) {
                        system.params.put(KeysDictionary.CCDMSYSTEM, node.params.get(KeysDictionary.CCDMSYSTEM));
                    }
                    try {
                        Pair e = new Pair();
                        datasourceClass.getClass().newInstance().propagate(e, node);
                        system.pairs.add(e);
                        sysList.add(system);
                        list.remove(node);
                        i--;
                        if (node instanceof NodeCCDM) {
                            listCCDMPairs = (ArrayList<NodeCCDM>) MatchingBySystemIDRuleImplementation.resolve(KeysDictionary.CCDMSYSTEM, listCCDMPairs, new CCDMDS());
                            i = 0;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }catch (Exception ee){

                ee.printStackTrace();
            }
        }
        return list;
    }
}
