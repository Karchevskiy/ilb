package ru.inasan.karchevsky.catalogues.sb9;

import ru.inasan.karchevsky.configuration.KeysDictionary;
import ru.inasan.karchevsky.lib.model.Component;
import ru.inasan.karchevsky.lib.model.Pair;
import ru.inasan.karchevsky.lib.pattern.Datasource;
import ru.inasan.karchevsky.lib.pattern.NodeForParsedCatalogue;


public class SB9DS implements Datasource {
    @Override
    public void propagate(Pair e, NodeForParsedCatalogue nodeRaw) throws Exception{
        if(nodeRaw instanceof NodeSB9) {
            NodeSB9 node = (NodeSB9) nodeRaw;
            e.el1.addMappedEntity(NodeSB9.uniqueCatalogueID,nodeRaw.source);
            e.el2.addMappedEntity(NodeSB9.uniqueCatalogueID,nodeRaw.source);

            e.el1.addCoordinates(NodeSB9.uniqueCatalogueID, KeysDictionary.X, Double.parseDouble(node.params.get(KeysDictionary.X)));
            e.el1.addCoordinates(NodeSB9.uniqueCatalogueID, KeysDictionary.Y, Double.parseDouble(node.params.get(KeysDictionary.Y)));

            e.el2.addCoordinates(NodeSB9.uniqueCatalogueID, KeysDictionary.X, Double.parseDouble(node.params.get(KeysDictionary.X)));
            e.el2.addCoordinates(NodeSB9.uniqueCatalogueID, KeysDictionary.Y, Double.parseDouble(node.params.get(KeysDictionary.Y)));


            e.addCoordinates(NodeSB9.uniqueCatalogueID, KeysDictionary.X, Double.parseDouble(node.params.get(KeysDictionary.X)));
            e.addCoordinates(NodeSB9.uniqueCatalogueID, KeysDictionary.Y, Double.parseDouble(node.params.get(KeysDictionary.Y)));


            e.addMappedEntity(NodeSB9.uniqueCatalogueID,node.source);
            e.addParams(NodeSB9.uniqueCatalogueID,KeysDictionary.MODIFIERS,"      s");
            e.addParams(NodeSB9.uniqueCatalogueID, KeysDictionary.SB9, node.params.get(KeysDictionary.SB9));
            e.addParams(NodeSB9.uniqueCatalogueID, KeysDictionary.CEV, node.params.get(KeysDictionary.CEV));
            e.addParams(NodeSB9.uniqueCatalogueID, KeysDictionary.FLAMSTEED, node.params.get(KeysDictionary.FLAMSTEED));
            e.addParams(NodeSB9.uniqueCatalogueID, KeysDictionary.BAYER, node.params.get(KeysDictionary.BAYER));
            e.addParams(NodeSB9.uniqueCatalogueID, KeysDictionary.DM, node.params.get(KeysDictionary.DM));
            e.addParams(NodeSB9.uniqueCatalogueID, KeysDictionary.HD, node.params.get(KeysDictionary.HD));

            e.addCoordinates(NodeSB9.uniqueCatalogueID, KeysDictionary.RHO, 0.00000001d);
            e.addCoordinates(NodeSB9.uniqueCatalogueID, KeysDictionary.THETA, 0.00000001d);

        }else{
            throw new Exception("illegal use of NodeSB9");
        }
    }

    @Override
    public void improve(Component component, NodeForParsedCatalogue node) throws Exception {
        Pair e = new Pair();
        e.equalNodeOnNextLevel = component;
        component.equalNodeOnPrevLevel=e;
        e.el1.addCoordinates(NodeSB9.uniqueCatalogueID, KeysDictionary.X, Double.parseDouble(node.params.get(KeysDictionary.X)));
        e.el1.addCoordinates(NodeSB9.uniqueCatalogueID, KeysDictionary.Y, Double.parseDouble(node.params.get(KeysDictionary.Y)));
        e.el2.addCoordinates(NodeSB9.uniqueCatalogueID, KeysDictionary.X, Double.parseDouble(node.params.get(KeysDictionary.X)));
        e.el2.addCoordinates(NodeSB9.uniqueCatalogueID, KeysDictionary.Y, Double.parseDouble(node.params.get(KeysDictionary.Y)));
        e.addCoordinates(NodeSB9.uniqueCatalogueID, KeysDictionary.X, Double.parseDouble(node.params.get(KeysDictionary.X)));
        e.addCoordinates(NodeSB9.uniqueCatalogueID, KeysDictionary.Y, Double.parseDouble(node.params.get(KeysDictionary.Y)));
        e.addCoordinates(NodeSB9.uniqueCatalogueID, KeysDictionary.RHO, 0.00000001d);
        e.addCoordinates(NodeSB9.uniqueCatalogueID, KeysDictionary.THETA, 0.00000001d);
        e.addMappedEntity(NodeSB9.uniqueCatalogueID,node.source);
        e.addParams(NodeSB9.uniqueCatalogueID,KeysDictionary.MODIFIERS,"      s");
        e.addParams(NodeSB9.uniqueCatalogueID, KeysDictionary.SB9, node.params.get(KeysDictionary.SB9));

        e.el1.addMappedEntity(NodeSB9.uniqueCatalogueID,node.source);
        e.el2.addMappedEntity(NodeSB9.uniqueCatalogueID,node.source);
    }
}
