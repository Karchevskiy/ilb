package ILBprocessing.datasources;

import ILBprocessing.beans.NodeCEV;
import ILBprocessing.configuration.KeysDictionary;
import lib.model.Component;
import lib.model.Pair;
import lib.pattern.Datasourse;
import lib.pattern.NodeForParsedCatalogue;


public class CEVDS implements Datasourse {
    @Override
    public void propagate(Pair e, NodeForParsedCatalogue nodeRaw) throws Exception{
        if(nodeRaw instanceof NodeCEV) {
            e.addMappedEntity(NodeCEV.uniqueCatalogueID,nodeRaw.source);
            e.el1.addMappedEntity(NodeCEV.uniqueCatalogueID,nodeRaw.source);
            e.el2.addMappedEntity(NodeCEV.uniqueCatalogueID,nodeRaw.source);
            NodeCEV node = (NodeCEV) nodeRaw;
            e.el1.addCoordinates(NodeCEV.uniqueCatalogueID, KeysDictionary.X, Double.parseDouble(node.params.get(KeysDictionary.X)));
            e.el1.addCoordinates(NodeCEV.uniqueCatalogueID, KeysDictionary.Y, Double.parseDouble(node.params.get(KeysDictionary.Y)));

            e.el2.addCoordinates(NodeCEV.uniqueCatalogueID, KeysDictionary.X, Double.parseDouble(node.params.get(KeysDictionary.X)));
            e.el2.addCoordinates(NodeCEV.uniqueCatalogueID, KeysDictionary.Y, Double.parseDouble(node.params.get(KeysDictionary.Y)));


            e.addCoordinates(NodeCEV.uniqueCatalogueID, KeysDictionary.X, Double.parseDouble(node.params.get(KeysDictionary.X)));
            e.addCoordinates(NodeCEV.uniqueCatalogueID, KeysDictionary.Y, Double.parseDouble(node.params.get(KeysDictionary.Y)));


            e.addCoordinates(NodeCEV.uniqueCatalogueID, KeysDictionary.RHO, 0d);
            e.addCoordinates(NodeCEV.uniqueCatalogueID, KeysDictionary.THETA, 0d);

            e.addParams(NodeCEV.uniqueCatalogueID,KeysDictionary.CEV,node.params.get(KeysDictionary.CEV));
        }else{
            throw new Exception("illegal use of CEVDS");
        }
    }

    @Override
    public void improve(Component component, NodeForParsedCatalogue node) throws Exception {
        Pair e = new Pair();
        e.equalNodeOnNextLevel = component;
        component.equalNodeOnPrevLevel=e;
        e.el1.addCoordinates(NodeCEV.uniqueCatalogueID, KeysDictionary.X, Double.parseDouble(node.params.get(KeysDictionary.X)));
        e.el1.addCoordinates(NodeCEV.uniqueCatalogueID, KeysDictionary.Y, Double.parseDouble(node.params.get(KeysDictionary.Y)));
        e.el2.addCoordinates(NodeCEV.uniqueCatalogueID, KeysDictionary.X, Double.parseDouble(node.params.get(KeysDictionary.X)));
        e.el2.addCoordinates(NodeCEV.uniqueCatalogueID, KeysDictionary.Y, Double.parseDouble(node.params.get(KeysDictionary.Y)));
        e.addCoordinates(NodeCEV.uniqueCatalogueID, KeysDictionary.X, Double.parseDouble(node.params.get(KeysDictionary.X)));
        e.addCoordinates(NodeCEV.uniqueCatalogueID, KeysDictionary.Y, Double.parseDouble(node.params.get(KeysDictionary.Y)));
        e.addCoordinates(NodeCEV.uniqueCatalogueID, KeysDictionary.RHO, 0d);
        e.addCoordinates(NodeCEV.uniqueCatalogueID, KeysDictionary.THETA, 0d);

        e.addMappedEntity(NodeCEV.uniqueCatalogueID,node.source);
        e.addParams(NodeCEV.uniqueCatalogueID,KeysDictionary.CEV,node.params.get(KeysDictionary.CEV));
        e.el1.addMappedEntity(NodeCEV.uniqueCatalogueID,node.source);
        e.el2.addMappedEntity(NodeCEV.uniqueCatalogueID,node.source);
    }
}
