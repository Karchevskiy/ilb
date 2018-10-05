package ru.inasan.karchevsky.lib.tools.resolvingRulesImplementation;

import ru.inasan.karchevsky.InterpreterProxy;
import ru.inasan.karchevsky.catalogues.ccdm.NodeCCDM;
import ru.inasan.karchevsky.configuration.KeysDictionary;
import ru.inasan.karchevsky.catalogues.ccdm.CCDMDS;
import ru.inasan.karchevsky.lib.storage.CachedStorage;
import ru.inasan.karchevsky.storage.CachedStorageILB;
import ru.inasan.karchevsky.lib.model.Pair;
import ru.inasan.karchevsky.lib.model.StarSystem;
import ru.inasan.karchevsky.lib.pattern.Datasource;
import ru.inasan.karchevsky.lib.pattern.NodeForParsedCatalogue;

import java.util.ArrayList;

public class MatchingByTimeZoneRuleImplementation  extends CachedStorageILB {
    public static ArrayList<? extends NodeForParsedCatalogue> resolve(String key, ArrayList<? extends NodeForParsedCatalogue> list,
                                                                      Datasource datasourceClass,
                                                                      InterpreterProxy cachedStorage){
        for(int i=0;i<list.size();i++) {
            try {
                NodeForParsedCatalogue node = list.get(i);
                int zone = Integer.parseInt(node.params.get(key).substring(0, 5));
                int zoneMin = Integer.parseInt(cachedStorage.getListWDS().get(0).params.get(KeysDictionary.WDSSYSTEM).substring(0, 5));
                int zoneMax = Integer.parseInt(cachedStorage.getListWDS().get(cachedStorage.getListWDS().size() - 1).params.get(KeysDictionary.WDSSYSTEM).substring(0, 5));
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
                        cachedStorage.getSysList().add(system);
                        list.remove(node);
                        i--;
                        if (node instanceof NodeCCDM) {
                            cachedStorage.setListCCDMPairs((ArrayList<NodeCCDM>) MatchingBySystemIDRuleImplementation.resolve(KeysDictionary.CCDMSYSTEM, cachedStorage.getListCCDMPairs(), new CCDMDS(), cachedStorage));
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
