package ru.inasan.karchevsky.catalogues.cev;

import ru.inasan.karchevsky.configuration.KeysDictionary;
import ru.inasan.karchevsky.lib.model.Component;
import ru.inasan.karchevsky.lib.model.Pair;
import ru.inasan.karchevsky.lib.pattern.Datasource;
import ru.inasan.karchevsky.lib.pattern.NodeForParsedCatalogue;


public class CEVDS implements Datasource {
    @Override
    public void propagate(Pair e, NodeForParsedCatalogue nodeRaw) throws Exception{
        if(nodeRaw instanceof NodeCEV) {
            e.addMappedEntity(NodeCEV.uniqueCatalogueID,nodeRaw.source);

            e.addParams(NodeCEV.uniqueCatalogueID, KeysDictionary.MODIFIERS,"     e");

            e.el1.addMappedEntity(NodeCEV.uniqueCatalogueID,nodeRaw.source);
            e.el2.addMappedEntity(NodeCEV.uniqueCatalogueID,nodeRaw.source);

            NodeCEV node = (NodeCEV) nodeRaw;
            e.el1.addCoordinates(NodeCEV.uniqueCatalogueID, KeysDictionary.X, Double.parseDouble(node.params.get(KeysDictionary.X)));
            e.el1.addCoordinates(NodeCEV.uniqueCatalogueID, KeysDictionary.Y, Double.parseDouble(node.params.get(KeysDictionary.Y)));

            e.el2.addCoordinates(NodeCEV.uniqueCatalogueID, KeysDictionary.X, Double.parseDouble(node.params.get(KeysDictionary.X)));
            e.el2.addCoordinates(NodeCEV.uniqueCatalogueID, KeysDictionary.Y, Double.parseDouble(node.params.get(KeysDictionary.Y)));


            e.addCoordinates(NodeCEV.uniqueCatalogueID, KeysDictionary.X, Double.parseDouble(node.params.get(KeysDictionary.X)));
            e.addCoordinates(NodeCEV.uniqueCatalogueID, KeysDictionary.Y, Double.parseDouble(node.params.get(KeysDictionary.Y)));


            e.addCoordinates(NodeCEV.uniqueCatalogueID, KeysDictionary.RHO, 0.00000001d);
            e.addCoordinates(NodeCEV.uniqueCatalogueID, KeysDictionary.THETA, 0.00000001d);

            e.addParams(NodeCEV.uniqueCatalogueID,KeysDictionary.CEV,node.params.get(KeysDictionary.CEV));
        }else{
            throw new Exception("illegal use of CEVDS");
        }
    }

    @Override
    public void improve(Component component, NodeForParsedCatalogue node) throws Exception {
        Pair e = new Pair();
        String modifiers="      e";
        e.equalNodeOnNextLevel = component;
        component.equalNodeOnPrevLevel=e;
        e.el1.addCoordinates(NodeCEV.uniqueCatalogueID, KeysDictionary.X, Double.parseDouble(node.params.get(KeysDictionary.X)));
        e.el1.addCoordinates(NodeCEV.uniqueCatalogueID, KeysDictionary.Y, Double.parseDouble(node.params.get(KeysDictionary.Y)));
        e.el2.addCoordinates(NodeCEV.uniqueCatalogueID, KeysDictionary.X, Double.parseDouble(node.params.get(KeysDictionary.X)));
        e.el2.addCoordinates(NodeCEV.uniqueCatalogueID, KeysDictionary.Y, Double.parseDouble(node.params.get(KeysDictionary.Y)));
        e.addCoordinates(NodeCEV.uniqueCatalogueID, KeysDictionary.X, Double.parseDouble(node.params.get(KeysDictionary.X)));
        e.addCoordinates(NodeCEV.uniqueCatalogueID, KeysDictionary.Y, Double.parseDouble(node.params.get(KeysDictionary.Y)));
        e.addCoordinates(NodeCEV.uniqueCatalogueID, KeysDictionary.RHO, 0.00000001d);
        e.addCoordinates(NodeCEV.uniqueCatalogueID, KeysDictionary.THETA, 0.00000001d);

        e.addMappedEntity(NodeCEV.uniqueCatalogueID,node.source);
        e.addParams(NodeCEV.uniqueCatalogueID,KeysDictionary.MODIFIERS,modifiers);
        e.addParams(NodeCEV.uniqueCatalogueID,KeysDictionary.CEV,node.params.get(KeysDictionary.CEV));
        e.el1.addMappedEntity(NodeCEV.uniqueCatalogueID,node.source);
        e.el2.addMappedEntity(NodeCEV.uniqueCatalogueID,node.source);
    }
}
