package ru.inasan.karchevsky.catalogues.bincep;

import ru.inasan.karchevsky.configuration.KeysDictionary;
import ru.inasan.karchevsky.lib.model.Component;
import ru.inasan.karchevsky.lib.model.Pair;
import ru.inasan.karchevsky.lib.pattern.Datasource;
import ru.inasan.karchevsky.lib.pattern.NodeForParsedCatalogue;


public class BINCEPDS implements Datasource {
    @Override
    public void propagate(Pair e, NodeForParsedCatalogue nodeRaw) throws Exception {
        if (nodeRaw instanceof NodeBinCep) {
            e.addMappedEntity(NodeBinCep.uniqueCatalogueID, nodeRaw.source);

            e.addParams(NodeBinCep.uniqueCatalogueID, KeysDictionary.MODIFIERS, "     p");

            e.el1.addMappedEntity(NodeBinCep.uniqueCatalogueID, nodeRaw.source);
            e.el2.addMappedEntity(NodeBinCep.uniqueCatalogueID, nodeRaw.source);

            NodeBinCep node = (NodeBinCep) nodeRaw;
            e.el1.addCoordinates(NodeBinCep.uniqueCatalogueID, KeysDictionary.X, Double.parseDouble(node.params.get(KeysDictionary.X)));
            e.el1.addCoordinates(NodeBinCep.uniqueCatalogueID, KeysDictionary.Y, Double.parseDouble(node.params.get(KeysDictionary.Y)));

            e.el2.addCoordinates(NodeBinCep.uniqueCatalogueID, KeysDictionary.X, Double.parseDouble(node.params.get(KeysDictionary.X)));
            e.el2.addCoordinates(NodeBinCep.uniqueCatalogueID, KeysDictionary.Y, Double.parseDouble(node.params.get(KeysDictionary.Y)));


            e.addCoordinates(NodeBinCep.uniqueCatalogueID, KeysDictionary.X, Double.parseDouble(node.params.get(KeysDictionary.X)));
            e.addCoordinates(NodeBinCep.uniqueCatalogueID, KeysDictionary.Y, Double.parseDouble(node.params.get(KeysDictionary.Y)));


            e.addCoordinates(NodeBinCep.uniqueCatalogueID, KeysDictionary.RHO, 0.0000000001d);
            e.addCoordinates(NodeBinCep.uniqueCatalogueID, KeysDictionary.THETA, 0.0000000001d);

            e.addParams(NodeBinCep.uniqueCatalogueID, KeysDictionary.CEV, node.params.get(KeysDictionary.CEV));
        } else {
            throw new Exception("illegal use of BINCEPDS");
        }
    }

    @Override
    public void improve(Component component, NodeForParsedCatalogue node) throws Exception {
        Pair e = new Pair();
        String modifiers = "      p";
        e.equalNodeOnNextLevel = component;
        component.equalNodeOnPrevLevel = e;
        e.el1.addCoordinates(NodeBinCep.uniqueCatalogueID, KeysDictionary.X, Double.parseDouble(node.params.get(KeysDictionary.X)));
        e.el1.addCoordinates(NodeBinCep.uniqueCatalogueID, KeysDictionary.Y, Double.parseDouble(node.params.get(KeysDictionary.Y)));
        e.el2.addCoordinates(NodeBinCep.uniqueCatalogueID, KeysDictionary.X, Double.parseDouble(node.params.get(KeysDictionary.X)));
        e.el2.addCoordinates(NodeBinCep.uniqueCatalogueID, KeysDictionary.Y, Double.parseDouble(node.params.get(KeysDictionary.Y)));
        e.addCoordinates(NodeBinCep.uniqueCatalogueID, KeysDictionary.X, Double.parseDouble(node.params.get(KeysDictionary.X)));
        e.addCoordinates(NodeBinCep.uniqueCatalogueID, KeysDictionary.Y, Double.parseDouble(node.params.get(KeysDictionary.Y)));
        e.addCoordinates(NodeBinCep.uniqueCatalogueID, KeysDictionary.RHO, 0.0000000001d);
        e.addCoordinates(NodeBinCep.uniqueCatalogueID, KeysDictionary.THETA, 0.0000000001d);

        e.addMappedEntity(NodeBinCep.uniqueCatalogueID, node.source);
        e.addParams(NodeBinCep.uniqueCatalogueID, KeysDictionary.MODIFIERS, modifiers);
        e.addParams(NodeBinCep.uniqueCatalogueID, KeysDictionary.CEV, node.params.get(KeysDictionary.CEV));
        e.el1.addMappedEntity(NodeBinCep.uniqueCatalogueID, node.source);
        e.el2.addMappedEntity(NodeBinCep.uniqueCatalogueID, node.source);
    }
}
