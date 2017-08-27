package ILBprocessing.beans.helpers;


import ILBprocessing.beans.NodeSB9;
import ILBprocessing.storage.CachedStorageILB;

public class NodeSB9Params {
    public NodeSB9Params(String s){
        //System.err.println(s);
        String[] params = s.split("\\|");
        int nodeKey = Integer.parseInt(params[0]);
        String paramKey =params[1];
        if(params.length>2) {
            String value = params[2];
            for (NodeSB9 node : CachedStorageILB.listSB9) {
                if (node.key == nodeKey) {
                    node.update(paramKey, value);
                }
            }
        }
    }
}
