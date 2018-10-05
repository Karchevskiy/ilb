package ru.inasan.karchevsky.catalogues.sb9;


import ru.inasan.karchevsky.storage.CachedStorageILB;

public class NodeSB9Params {
    public NodeSB9Params(String s, CachedStorageILB storage) {
        //System.err.println(s);
        String[] params = s.split("\\|");
        int nodeKey = Integer.parseInt(params[0]);
        String paramKey = params[1];
        if (params.length > 2) {
            String value = params[2];
            for (NodeSB9 node : storage.getListSB9()) {
                if (node.key == nodeKey) {
                    node.update(paramKey, value);
                }
            }
        }
    }
}
