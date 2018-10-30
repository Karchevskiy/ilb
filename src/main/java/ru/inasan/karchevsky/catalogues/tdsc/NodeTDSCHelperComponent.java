package ru.inasan.karchevsky.catalogues.tdsc;

import ru.inasan.karchevsky.configuration.KeysDictionary;
import ru.inasan.karchevsky.lib.storage.GlobalPoolOfIdentifiers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

/**
 * Created by Алекс on 09.03.2017.
 */
public class NodeTDSCHelperComponent implements Serializable {

    public HashMap<String, String> params = new HashMap<>();
    public String source;
    public String componentNumber;
    public String componentLetter;
    public String tdscID = "";
    public String[] data;
    String target = "";

    public NodeTDSCHelperComponent(String s) {
        source = s.substring(0, s.length() - 1);
        data = s.split("\\|");
        tdscID = data[0];
        componentLetter = data[1].replaceAll(" ", "");
        componentNumber = GlobalPoolOfIdentifiers.rebuildIdToUnifiedBase(data[0]);
        target = data[26].replaceAll(" ", "");

        params.put(KeysDictionary.WDSSYSTEM, GlobalPoolOfIdentifiers.rebuildIdToUnifiedBase(data[22]));
        params.put(KeysDictionary.HD, GlobalPoolOfIdentifiers.rebuildIdToUnifiedBase(data[25]));

        params.put(KeysDictionary.RHO, (data[28]));
        params.put(KeysDictionary.THETA, (data[27]));

        params.put(KeysDictionary.X, data[4]);
        params.put(KeysDictionary.Y, data[5]);
    }

    public static void translateToPairs(ArrayList<NodeTDSCHelperComponent> listComp, Collection<NodeTDSC> listPair) {
        for (int i = 0; i < listComp.size(); i++) {
            if (!listComp.get(i).target.equals("")) {
                for (int j = 0; j < listComp.size(); j++) {
                    if (listComp.get(j).componentLetter.equals(listComp.get(i).target)) {
                        NodeTDSC e = new NodeTDSC();
                        e.params.put(KeysDictionary.TDSCSYSTEM, listComp.get(i).tdscID);
                        e.params.put(KeysDictionary.TDSCPAIR, (listComp.get(i).target + listComp.get(i).componentLetter).replaceAll(" ", ""));
                        e.el1 = listComp.get(j);
                        e.el2 = listComp.get(i);
                        propagateToPairNode(e, listComp.get(j));
                        propagateToPairNode(e, listComp.get(i));
                        e.source = listComp.get(i).source;
                        e.params.put(KeysDictionary.THETA, listComp.get(i).params.get(KeysDictionary.THETA));
                        e.params.put(KeysDictionary.RHO, listComp.get(i).params.get(KeysDictionary.RHO));
                        listPair.add(e);
                        break;
                    }
                }
            }
        }
    }

    public static void propagateToPairNode(NodeTDSC e, NodeTDSCHelperComponent c1) {
        if (!e.params.containsKey(KeysDictionary.X)) {
            e.params.put(KeysDictionary.X, c1.params.get(KeysDictionary.X));
            e.params.put(KeysDictionary.Y, c1.params.get(KeysDictionary.Y));
        }
        e.params.put(KeysDictionary.HD, c1.params.get(KeysDictionary.HD));
        e.params.put(KeysDictionary.WDSSYSTEM, c1.params.get(KeysDictionary.WDSSYSTEM));
    }
}
