package ru.inasan.karchevsky.catalogues.xr;

import ru.inasan.karchevsky.configuration.KeysDictionary;
import ru.inasan.karchevsky.lib.model.Component;
import ru.inasan.karchevsky.lib.model.Pair;
import ru.inasan.karchevsky.lib.pattern.Datasource;
import ru.inasan.karchevsky.lib.pattern.NodeForParsedCatalogue;


public class DatasourceXR implements Datasource {
    @Override
    public void propagate(Pair e, NodeForParsedCatalogue nodeRaw) throws Exception{
        if(nodeRaw instanceof NodeXR) {
            NodeXR node = (NodeXR) nodeRaw;
            e.el1.addMappedEntity(NodeXR.uniqueCatalogueID,nodeRaw.source);
            e.el2.addMappedEntity(NodeXR.uniqueCatalogueID,nodeRaw.source);
            e.addMappedEntity(NodeXR.uniqueCatalogueID,nodeRaw.source);


            e.addParams(NodeXR.uniqueCatalogueID,KeysDictionary.MODIFIERS,"        x");

            e.el1.addCoordinates(NodeXR.uniqueCatalogueID, KeysDictionary.X, Double.parseDouble(node.params.get(KeysDictionary.X)));
            e.el1.addCoordinates(NodeXR.uniqueCatalogueID, KeysDictionary.Y, Double.parseDouble(node.params.get(KeysDictionary.Y)));

            e.el2.addCoordinates(NodeXR.uniqueCatalogueID, KeysDictionary.X, Double.parseDouble(node.params.get(KeysDictionary.X)));
            e.el2.addCoordinates(NodeXR.uniqueCatalogueID, KeysDictionary.Y, Double.parseDouble(node.params.get(KeysDictionary.Y)));

            e.addCoordinates(NodeXR.uniqueCatalogueID, KeysDictionary.X, Double.parseDouble(node.params.get(KeysDictionary.X)));
            e.addCoordinates(NodeXR.uniqueCatalogueID, KeysDictionary.Y, Double.parseDouble(node.params.get(KeysDictionary.Y)));

            e.addCoordinates(NodeXR.uniqueCatalogueID, KeysDictionary.RHO, 0d);
            e.addCoordinates(NodeXR.uniqueCatalogueID, KeysDictionary.THETA, 0d);

            e.addParams(NodeXR.uniqueCatalogueID, KeysDictionary.SB9, node.params.get(KeysDictionary.SB9));
            e.addParams(NodeXR.uniqueCatalogueID, KeysDictionary.HIP, node.params.get(KeysDictionary.HIP));
            e.addParams(NodeXR.uniqueCatalogueID, KeysDictionary.HD, node.params.get(KeysDictionary.HD));
            e.addParams(NodeXR.uniqueCatalogueID, KeysDictionary.DM, node.params.get(KeysDictionary.DM));

        }else{
            throw new Exception("illegal use of NodeXR");
        }
    }

    @Override
    public void improve(Component component, NodeForParsedCatalogue node) throws Exception {
        Pair e = new Pair();
        e.addParams(NodeXR.uniqueCatalogueID,KeysDictionary.MODIFIERS,"        x");
        e.equalNodeOnNextLevel = component;
        component.equalNodeOnPrevLevel=e;
        e.el1.addCoordinates(NodeXR.uniqueCatalogueID, KeysDictionary.X, Double.parseDouble(node.params.get(KeysDictionary.X)));
        e.el1.addCoordinates(NodeXR.uniqueCatalogueID, KeysDictionary.Y, Double.parseDouble(node.params.get(KeysDictionary.Y)));
        e.el2.addCoordinates(NodeXR.uniqueCatalogueID, KeysDictionary.X, Double.parseDouble(node.params.get(KeysDictionary.X)));
        e.el2.addCoordinates(NodeXR.uniqueCatalogueID, KeysDictionary.Y, Double.parseDouble(node.params.get(KeysDictionary.Y)));
        e.addCoordinates(NodeXR.uniqueCatalogueID, KeysDictionary.X, Double.parseDouble(node.params.get(KeysDictionary.X)));
        e.addCoordinates(NodeXR.uniqueCatalogueID, KeysDictionary.Y, Double.parseDouble(node.params.get(KeysDictionary.Y)));
        e.addCoordinates(NodeXR.uniqueCatalogueID, KeysDictionary.RHO, 0d);
        e.addCoordinates(NodeXR.uniqueCatalogueID, KeysDictionary.THETA, 0d);
        e.addMappedEntity(NodeXR.uniqueCatalogueID,node.source);
        e.addParams(NodeXR.uniqueCatalogueID, KeysDictionary.XR, node.params.get(KeysDictionary.XR));

        e.el1.addMappedEntity(NodeXR.uniqueCatalogueID,node.source);
        e.el2.addMappedEntity(NodeXR.uniqueCatalogueID,node.source);
    }
}
