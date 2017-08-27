package ILBprocessing.datasources;

import ILBprocessing.beans.NodeWDSFINALIZED;
import ILBprocessing.configuration.KeysDictionary;
import lib.errorHandling.ValueAlreadyExistsException;
import lib.model.Component;
import lib.model.Pair;
import lib.pattern.Datasourse;
import lib.pattern.NodeForParsedCatalogue;
import lib.service.ConverterFINALIZED;
import lib.service.CoordinatesCalculator;

import static ILBprocessing.configuration.SharedConstants.LOGGING_LEVEL_VERBOSE_ENABLED;

/**
 * Created by Алекс on 25.11.2016.
 */
public class WDSDS implements Datasourse {
    @Override
    public void propagate(Pair e, NodeForParsedCatalogue nodeRaw) throws Exception{
        if(nodeRaw instanceof NodeWDSFINALIZED) {
            NodeWDSFINALIZED node = (NodeWDSFINALIZED)nodeRaw;
            e.addMappedEntity(NodeWDSFINALIZED.uniqueCatalogueID,nodeRaw.source);

            if(!node.idDM.equals("")) {
                e.addParams(NodeWDSFINALIZED.uniqueCatalogueID, KeysDictionary.DM, node.idDM);
            }
            e.addParams(NodeWDSFINALIZED.uniqueCatalogueID,KeysDictionary.OBSERVER,node.params.get(KeysDictionary.OBSERVER));
            e.addParams(NodeWDSFINALIZED.uniqueCatalogueID,KeysDictionary.WDSSYSTEM,node.params.get(KeysDictionary.WDSSYSTEM));
            e.addParams(NodeWDSFINALIZED.uniqueCatalogueID,KeysDictionary.WDSPAIR,node.params.get(KeysDictionary.WDSPAIR));

            e.addCoordinates(NodeWDSFINALIZED.uniqueCatalogueID,KeysDictionary.RHO,node.rho);
            e.addCoordinates(NodeWDSFINALIZED.uniqueCatalogueID,KeysDictionary.THETA,node.theta);

            e.modifier[0]= node.modifier2[0];
            if(node.modifier2[1]!=0){
                e.modifier[0]= node.modifier2[1];
            }
            parseCoordinatesDirectlyFromWDS(e,node);

        }else{
            throw new Exception("illegal use of WDSDS");
        }
    }

    @Override
    public void improve(Component pair, NodeForParsedCatalogue node) throws Exception {
        throw new Exception("illegal use of WDSDS");
    }

    public static void parseCoordinatesDirectlyFromWDS(Pair e, NodeWDSFINALIZED node){
       //global error tracker. Should be never thrown;
        try{
            //here can't be exception XXXXXX.YY-YYYYYY.Y
            node.el1.coord1I=Integer.parseInt(node.coordinatesFromWDSasString.substring(0,6));
            node.el1.coord_flag=11;

            // YYYYYY.XX-YYYYYY.Y sometimes missing
            try{
                node.el1.coord1F=Integer.parseInt(node.coordinatesFromWDSasString.substring(7,9));
            }catch(NumberFormatException exc1){
                try {
                    node.el1.coord1F=Integer.parseInt(node.coordinatesFromWDSasString.substring(7,8));
                    node.el1.coord_flag=-11;
                    if(LOGGING_LEVEL_VERBOSE_ENABLED)System.out.println("ERR01 caught while parsing "+ node.coordinatesFromWDSasString);
                }catch(NumberFormatException exc2){
                    if(LOGGING_LEVEL_VERBOSE_ENABLED)System.out.println("ERR01b caught while parsing "+ node.coordinatesFromWDSasString);
                    node.el1.coord1F = 0;
                    node.el1.coord_flag=-11;
                }
            }

            // YYYYYY.YY-XXXXXX.Y should be always clear
            try{
                node.el1.coord2I=Integer.parseInt(node.coordinatesFromWDSasString.substring(9,16));
            }catch(NumberFormatException exc) {
                if(LOGGING_LEVEL_VERBOSE_ENABLED)System.err.println("ERR02 caught while parsing "+ node.coordinatesFromWDSasString);
                node.el1.coord2I = 0;
                node.el1.coord_flag = -11;
            }

            // YYYYYY.YY-YYYYYY.X sometimes missing
            try{
                node.el1.coord2F=Integer.parseInt(node.coordinatesFromWDSasString.substring(17,18));
            }catch(Exception exc){
                if(LOGGING_LEVEL_VERBOSE_ENABLED)System.out.println("ERR03 caught while parsing "+ node.coordinatesFromWDSasString);
                node.el1.coord2F=0;
                node.el1.coord_flag=-11;
            }

            if(node.rho!=0){
                node.rho=node.rho/360/60/60*2*Math.PI;
            }

            //
            //Case for fatal error caught. Transaction should be rolled back(?) (should we implement ORM?)
            //
        }catch(Exception td){
            if(LOGGING_LEVEL_VERBOSE_ENABLED)System.err.println("ERR08 FATAL error caught"+ node.coordinatesFromWDSasString);
            node.el1.coord_flag=-1;
            td.printStackTrace();
        }

        node.el1.rightCoordX=ConverterFINALIZED.hrsToRad(node.el1.coord1I,node.el1.coord1F)/Math.PI*180;
        node.el1.rightCoordY=ConverterFINALIZED.grToRad(node.el1.coord2I,node.el1.coord2F)/Math.PI*180;


        try {
            e.addCoordinates(NodeWDSFINALIZED.uniqueCatalogueID, KeysDictionary.X, node.el1.rightCoordX);
            e.addCoordinates(NodeWDSFINALIZED.uniqueCatalogueID, KeysDictionary.Y, node.el1.rightCoordY);

            e.el1.addCoordinates(NodeWDSFINALIZED.uniqueCatalogueID, KeysDictionary.X, node.el1.rightCoordX);
            e.el1.addCoordinates(NodeWDSFINALIZED.uniqueCatalogueID, KeysDictionary.Y, node.el1.rightCoordY);
            javafx.util.Pair<Double,Double> result = CoordinatesCalculator.calculateByRhoTheta(node.el1.rightCoordX/180*Math.PI,node.el1.rightCoordY/180*Math.PI,node.rho, node.theta/180*Math.PI);
            e.el2.addCoordinates(NodeWDSFINALIZED.uniqueCatalogueID, KeysDictionary.X, result.getKey()*180/Math.PI);
            e.el2.addCoordinates(NodeWDSFINALIZED.uniqueCatalogueID, KeysDictionary.Y, result.getValue()*180/Math.PI);


            e.el1.addMappedEntity(NodeWDSFINALIZED.uniqueCatalogueID,node.source);
            e.el1.addParams(NodeWDSFINALIZED.uniqueCatalogueID,KeysDictionary.WDSSYSTEM,node.params.get(KeysDictionary.WDSSYSTEM));
            e.el2.addMappedEntity(NodeWDSFINALIZED.uniqueCatalogueID,node.source);
            e.el2.addParams(NodeWDSFINALIZED.uniqueCatalogueID,KeysDictionary.WDSSYSTEM,node.params.get(KeysDictionary.WDSSYSTEM));
            extractNameForComponents(node.params.get(KeysDictionary.WDSPAIR),e);

        }catch(ValueAlreadyExistsException exc){
            exc.printStackTrace();
        }
    }
    public static void extractNameForComponents(String nameOfPair,Pair pair){
       String name1="",name2="";
        if(nameOfPair.length()==2){
            name1=""+nameOfPair.charAt(0);
            name2=""+nameOfPair.charAt(1);
       }else if(nameOfPair.contains(",")){
            String[] names = nameOfPair.split(",");
            name1=names[0];
            name2=names[1];
        }
        try {
            pair.el1.addParams(NodeWDSFINALIZED.uniqueCatalogueID, KeysDictionary.WDSCOMP, name1);
            pair.el2.addParams(NodeWDSFINALIZED.uniqueCatalogueID, KeysDictionary.WDSCOMP, name2);
        }catch (ValueAlreadyExistsException exc){
            exc.printStackTrace();
        }
    }
}