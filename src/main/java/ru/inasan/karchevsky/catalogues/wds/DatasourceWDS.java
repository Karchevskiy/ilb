package ru.inasan.karchevsky.catalogues.wds;

import ru.inasan.karchevsky.configuration.KeysDictionary;
import ru.inasan.karchevsky.lib.errorHandling.ValueAlreadyExistsException;
import ru.inasan.karchevsky.lib.model.Component;
import ru.inasan.karchevsky.lib.model.Pair;
import ru.inasan.karchevsky.lib.pattern.Datasource;
import ru.inasan.karchevsky.lib.pattern.NodeForParsedCatalogue;
import ru.inasan.karchevsky.lib.service.ConverterFINALIZED;
import ru.inasan.karchevsky.lib.service.CoordinatesCalculator;
import ru.inasan.karchevsky.configuration.SharedConstants;

/**
 * Created by Алекс on 25.11.2016.
 */
public class DatasourceWDS implements Datasource {
    @Override
    public void propagate(Pair e, NodeForParsedCatalogue nodeRaw) throws Exception{
        if(nodeRaw instanceof NodeWDS) {
            NodeWDS node = (NodeWDS)nodeRaw;
            e.addMappedEntity(NodeWDS.uniqueCatalogueID,nodeRaw.source);
            if(!node.idDM.equals("")) {
                e.addParams(NodeWDS.uniqueCatalogueID, KeysDictionary.DM, node.idDM);
            }
            e.addParams(NodeWDS.uniqueCatalogueID,KeysDictionary.OBSERVER,node.params.get(KeysDictionary.OBSERVER));
            e.addParams(NodeWDS.uniqueCatalogueID,KeysDictionary.WDSSYSTEM,node.params.get(KeysDictionary.WDSSYSTEM));
            e.addParams(NodeWDS.uniqueCatalogueID,KeysDictionary.WDSPAIR,node.params.get(KeysDictionary.WDSPAIR));

            e.addCoordinates(NodeWDS.uniqueCatalogueID,KeysDictionary.RHO,node.rho);
            e.addCoordinates(NodeWDS.uniqueCatalogueID,KeysDictionary.THETA,node.theta);

            if(node.coordinatesNotFoundInWDS){
                    e.addParams(NodeWDS.uniqueCatalogueID,KeysDictionary.COORDFAKE, "f");
            }
            if(node.modifier2[1]==' ' || node.modifier2[1]==0){
                e.addParams(NodeWDS.uniqueCatalogueID,KeysDictionary.MODIFIERS,node.modifier2[0]+"        ");
            }else{
                e.addParams(NodeWDS.uniqueCatalogueID,KeysDictionary.MODIFIERS,"" + node.modifier2[0] + node.modifier2[1]+"       ");
            }
            parseCoordinatesDirectlyFromWDS(e,node);

        }else{
            throw new Exception("illegal use of DatasourceWDS");
        }
    }

    @Override
    public void improve(Component pair, NodeForParsedCatalogue node) throws Exception {
        throw new Exception("illegal use of DatasourceWDS");
    }

    public static void parseCoordinatesDirectlyFromWDS(Pair e, NodeWDS node){
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
                    if(SharedConstants.LOGGING_LEVEL_VERBOSE_ENABLED)System.out.println("ERR01 caught while parsing "+ node.coordinatesFromWDSasString);
                }catch(NumberFormatException exc2){
                    if(SharedConstants.LOGGING_LEVEL_VERBOSE_ENABLED)System.out.println("ERR01b caught while parsing "+ node.coordinatesFromWDSasString);
                    node.el1.coord1F = 0;
                    node.el1.coord_flag=-11;
                }
            }

            // YYYYYY.YY-XXXXXX.Y should be always clear
            try{
                node.el1.coord2I=Integer.parseInt(node.coordinatesFromWDSasString.substring(9,16));
            }catch(NumberFormatException exc) {
                if(SharedConstants.LOGGING_LEVEL_VERBOSE_ENABLED)System.err.println("ERR02 caught while parsing "+ node.coordinatesFromWDSasString);
                node.el1.coord2I = 0;
                node.el1.coord_flag = -11;
            }

            // YYYYYY.YY-YYYYYY.X sometimes missing
            try{
                node.el1.coord2F=Integer.parseInt(node.coordinatesFromWDSasString.substring(17,18));
            }catch(Exception exc){
                if(SharedConstants.LOGGING_LEVEL_VERBOSE_ENABLED)System.out.println("ERR03 caught while parsing "+ node.coordinatesFromWDSasString);
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
            if(SharedConstants.LOGGING_LEVEL_VERBOSE_ENABLED)System.err.println("ERR08 FATAL error caught"+ node.coordinatesFromWDSasString);
            node.el1.coord_flag=-1;
            td.printStackTrace();
        }

        node.el1.rightCoordX=ConverterFINALIZED.hrsToRad(node.el1.coord1I,node.el1.coord1F)/Math.PI*180;
        node.el1.rightCoordY=ConverterFINALIZED.grToRad(node.el1.coord2I,node.el1.coord2F)/Math.PI*180;


        try {
            e.addCoordinates(NodeWDS.uniqueCatalogueID, KeysDictionary.X, node.el1.rightCoordX);
            e.addCoordinates(NodeWDS.uniqueCatalogueID, KeysDictionary.Y, node.el1.rightCoordY);

            e.el1.addCoordinates(NodeWDS.uniqueCatalogueID, KeysDictionary.X, node.el1.rightCoordX);
            e.el1.addCoordinates(NodeWDS.uniqueCatalogueID, KeysDictionary.Y, node.el1.rightCoordY);
            javafx.util.Pair<Double,Double> result = CoordinatesCalculator.calculateByRhoTheta(node.el1.rightCoordX/180*Math.PI,node.el1.rightCoordY/180*Math.PI,node.rho, node.theta/180*Math.PI);
            e.el2.addCoordinates(NodeWDS.uniqueCatalogueID, KeysDictionary.X, result.getKey()*180/Math.PI);
            e.el2.addCoordinates(NodeWDS.uniqueCatalogueID, KeysDictionary.Y, result.getValue()*180/Math.PI);


            e.el1.addMappedEntity(NodeWDS.uniqueCatalogueID,node.source);
            e.el1.addParams(NodeWDS.uniqueCatalogueID,KeysDictionary.WDSSYSTEM,node.params.get(KeysDictionary.WDSSYSTEM));
            e.el2.addMappedEntity(NodeWDS.uniqueCatalogueID,node.source);
            e.el2.addParams(NodeWDS.uniqueCatalogueID,KeysDictionary.WDSSYSTEM,node.params.get(KeysDictionary.WDSSYSTEM));
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
            pair.el1.addParams(NodeWDS.uniqueCatalogueID, KeysDictionary.WDSCOMP, name1);
            pair.el2.addParams(NodeWDS.uniqueCatalogueID, KeysDictionary.WDSCOMP, name2);
        }catch (ValueAlreadyExistsException exc){
            exc.printStackTrace();
        }
    }
}