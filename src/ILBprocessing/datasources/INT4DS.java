package ILBprocessing.datasources;

import ILBprocessing.beans.NodeINT4;
import ILBprocessing.configuration.KeysDictionary;
import lib.errorHandling.ValueAlreadyExistsException;
import lib.model.Component;
import lib.model.Pair;
import lib.pattern.Datasource;
import lib.pattern.NodeForParsedCatalogue;
import lib.service.CoordinatesCalculator;

import static ILBprocessing.configuration.SharedConstants.LOGGING_LEVEL_VERBOSE_ENABLED;


public class INT4DS implements Datasource {
    @Override
    public void propagate(Pair e, NodeForParsedCatalogue nodeRaw) throws Exception{
        if(nodeRaw instanceof NodeINT4) {
            NodeINT4 node = (NodeINT4) nodeRaw;
            e.addMappedEntity(NodeINT4.uniqueCatalogueID,nodeRaw.source);
            e.addParams(NodeINT4.uniqueCatalogueID,KeysDictionary.MODIFIERS,"    i");
            if(node.coordinatesNotFoundInINT4){
                e.addParams(NodeINT4.uniqueCatalogueID,KeysDictionary.COORDFAKE, "f");
            }
            if(node.params.containsKey(KeysDictionary.ADS) && !node.params.get(KeysDictionary.ADS).equals("")) e.addParams(NodeINT4.uniqueCatalogueID,KeysDictionary.ADS,node.params.get(KeysDictionary.ADS));
            if(node.params.containsKey(KeysDictionary.FLAMSTEED) && !node.params.get(KeysDictionary.FLAMSTEED).equals("")) e.addParams(NodeINT4.uniqueCatalogueID,KeysDictionary.FLAMSTEED,node.params.get(KeysDictionary.FLAMSTEED));
            if(node.params.containsKey(KeysDictionary.BAYER) && !node.params.get(KeysDictionary.BAYER).equals("")) e.addParams(NodeINT4.uniqueCatalogueID,KeysDictionary.BAYER,node.params.get(KeysDictionary.BAYER));
            if(node.params.containsKey(KeysDictionary.DM) && !node.params.get(KeysDictionary.DM).equals("")) e.addParams(NodeINT4.uniqueCatalogueID,KeysDictionary.DM,node.params.get(KeysDictionary.DM));
            if(node.params.containsKey(KeysDictionary.OBSERVER) && !node.params.get(KeysDictionary.OBSERVER).equals("")) e.addParams(NodeINT4.uniqueCatalogueID,KeysDictionary.OBSERVER, node.params.get(KeysDictionary.OBSERVER));
            if(node.params.containsKey(KeysDictionary.WDSSYSTEM) && !node.params.get(KeysDictionary.WDSSYSTEM).equals("")) e.addParams(NodeINT4.uniqueCatalogueID,KeysDictionary.WDSSYSTEM, node.params.get(KeysDictionary.WDSSYSTEM));
            parseCoordinatesDirectlyFromWDS(e,node);
        }else{
            throw new Exception("illegal use of INT4DS");
        }
    }

    @Override
    public void improve(Component pair, NodeForParsedCatalogue node) throws Exception {
        throw new Exception("illegal use of INT4DS");
    }

    //clone from WDSDS TODO:refactor: encapsulate in special class
    public static void parseCoordinatesDirectlyFromWDS(Pair e, NodeINT4 node){

        try {
            double f2=0d;
            double f=-1d;
            e.addCoordinates(NodeINT4.uniqueCatalogueID, KeysDictionary.X, node.el1.rightCoordX);
            e.addCoordinates(NodeINT4.uniqueCatalogueID, KeysDictionary.Y, node.el1.rightCoordY);
            try{
                f = Double.parseDouble(node.params.get(KeysDictionary.RHO));
                e.addCoordinates(NodeINT4.uniqueCatalogueID, KeysDictionary.RHO, f);
            }catch(Exception exc){
                e.addCoordinates(NodeINT4.uniqueCatalogueID, KeysDictionary.RHO, 0d);
                if(LOGGING_LEVEL_VERBOSE_ENABLED)System.err.println("null coordinates INT4:" + node.source);
            }
            try{
                f2= Double.parseDouble(node.params.get(KeysDictionary.THETA));
                e.addCoordinates(NodeINT4.uniqueCatalogueID, KeysDictionary.THETA, f2);
            }catch(Exception exc){
                e.addCoordinates(NodeINT4.uniqueCatalogueID, KeysDictionary.THETA, 0d);
                if(LOGGING_LEVEL_VERBOSE_ENABLED)System.err.println("null coordinates INT4:" + node.source);
            }

            e.el1.addCoordinates(NodeINT4.uniqueCatalogueID, KeysDictionary.X, node.el1.rightCoordX);
            e.el1.addCoordinates(NodeINT4.uniqueCatalogueID, KeysDictionary.Y, node.el1.rightCoordY);
            javafx.util.Pair<Double,Double> result = CoordinatesCalculator.calculateByRhoTheta(node.el1.rightCoordX/180*Math.PI,node.el1.rightCoordY/180*Math.PI,f/360/60/60*2*Math.PI, f2/180*Math.PI);
            e.el2.addCoordinates(NodeINT4.uniqueCatalogueID, KeysDictionary.X, result.getKey()*180/Math.PI);
            e.el2.addCoordinates(NodeINT4.uniqueCatalogueID, KeysDictionary.Y, result.getValue()*180/Math.PI);



            e.el1.addMappedEntity(NodeINT4.uniqueCatalogueID,node.source);
            e.el1.addParams(NodeINT4.uniqueCatalogueID,KeysDictionary.WDSSYSTEM,node.params.get(KeysDictionary.WDSSYSTEM));
            e.el2.addMappedEntity(NodeINT4.uniqueCatalogueID,node.source);
            e.el2.addParams(NodeINT4.uniqueCatalogueID,KeysDictionary.WDSSYSTEM,node.params.get(KeysDictionary.WDSSYSTEM));

        }catch(ValueAlreadyExistsException exc){
            exc.printStackTrace();
        }
    }
}

