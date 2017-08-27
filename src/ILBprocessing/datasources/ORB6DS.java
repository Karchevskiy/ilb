package ILBprocessing.datasources;

import ILBprocessing.beans.NodeORB6FINALIZED;
import ILBprocessing.configuration.KeysDictionary;
import lib.errorHandling.ValueAlreadyExistsException;
import lib.model.Component;
import lib.model.Pair;
import lib.pattern.Datasourse;
import lib.pattern.NodeForParsedCatalogue;
import lib.service.ConverterFINALIZED;

/**
 * Created by Алекс on 25.11.2016.
 */
public class ORB6DS implements Datasourse {
    @Override
    public void propagate(Pair e, NodeForParsedCatalogue nodeRaw) throws Exception{
        e.modifier[1]='o';
        if(nodeRaw instanceof NodeORB6FINALIZED) {
            NodeORB6FINALIZED node = (NodeORB6FINALIZED) nodeRaw;
            e.addMappedEntity(NodeORB6FINALIZED.uniqueCatalogueID,nodeRaw.source);
            if(node.params.containsKey(KeysDictionary.HIP) && !node.params.get(KeysDictionary.HIP).equals(""))  e.addParams(NodeORB6FINALIZED.uniqueCatalogueID,KeysDictionary.HIP,node.params.get(KeysDictionary.HIP));
            if(node.params.containsKey(KeysDictionary.ADS) && !node.params.get(KeysDictionary.ADS).equals("")) e.addParams(NodeORB6FINALIZED.uniqueCatalogueID,KeysDictionary.ADS,node.params.get(KeysDictionary.ADS));
            if(node.params.containsKey(KeysDictionary.FLAMSTEED) && !node.params.get(KeysDictionary.FLAMSTEED).equals("")) e.addParams(NodeORB6FINALIZED.uniqueCatalogueID,KeysDictionary.FLAMSTEED,node.params.get(KeysDictionary.FLAMSTEED));
            if(node.params.containsKey(KeysDictionary.BAYER) && !node.params.get(KeysDictionary.BAYER).equals("")) e.addParams(NodeORB6FINALIZED.uniqueCatalogueID,KeysDictionary.BAYER,node.params.get(KeysDictionary.BAYER));
            if(node.params.containsKey(KeysDictionary.DM) && !node.params.get(KeysDictionary.DM).equals("")) e.addParams(NodeORB6FINALIZED.uniqueCatalogueID,KeysDictionary.DM,node.params.get(KeysDictionary.DM));
            if(node.params.containsKey(KeysDictionary.HD) && !node.params.get(KeysDictionary.HD).equals("")) e.addParams(NodeORB6FINALIZED.uniqueCatalogueID,KeysDictionary.HD,node.params.get(KeysDictionary.HD));
            if(node.params.containsKey(KeysDictionary.OBSERVER) && !node.params.get(KeysDictionary.OBSERVER).equals("")) e.addParams(NodeORB6FINALIZED.uniqueCatalogueID,KeysDictionary.OBSERVER, node.params.get(KeysDictionary.OBSERVER));
            if(node.params.containsKey(KeysDictionary.WDSSYSTEM) && !node.params.get(KeysDictionary.WDSSYSTEM).equals("")) e.addParams(NodeORB6FINALIZED.uniqueCatalogueID,KeysDictionary.WDSSYSTEM, node.params.get(KeysDictionary.WDSSYSTEM));
            parseCoordinatesDirectlyFromWDS(e,node);
        }else{
            throw new Exception("illegal use of ORB6DS");
        }
    }

    @Override
    public void improve(Component pair, NodeForParsedCatalogue node) throws Exception {
        throw new Exception("illegal use of ORB6DS");
    }

    //clone from WDSDS TODO:refactor: encapsulate in special class
    public static void parseCoordinatesDirectlyFromWDS(Pair e, NodeORB6FINALIZED node){
        //global error tracker. Should be never thrown;
        try{
            //here can't be exception XXXXXX.YY-YYYYYY.Y
            node.el1.coord1I=Integer.parseInt(node.coordinates.substring(0,6));
            node.el1.coord_flag=11;

            // YYYYYY.XX-YYYYYY.Y sometimes missing
            try{
                node.el1.coord1F=Integer.parseInt(node.coordinates.substring(7,9));
            }catch(NumberFormatException exc1){
                try {
                    node.el1.coord1F=Integer.parseInt(node.coordinates.substring(7,8));
                    node.el1.coord_flag=-11;
                    System.out.println("ERR01 caught while parsing "+ node.coordinates);
                }catch(NumberFormatException exc2){
                    System.out.println("ERR01b caught while parsing "+ node.coordinates);
                    node.el1.coord1F = 0;
                    node.el1.coord_flag=-11;
                }
            }

            // YYYYYY.YY-XXXXXX.Y should be always clear
            try{
                node.el1.coord2I=Integer.parseInt(node.coordinates.substring(9,16));
            }catch(NumberFormatException exc) {
                System.err.println("ERR02 caught while parsing "+ node.coordinates);
                node.el1.coord2I = 0;
                node.el1.coord_flag = -11;
            }

            // YYYYYY.YY-YYYYYY.X sometimes missing
            try{
                node.el1.coord2F=Integer.parseInt(node.coordinates.substring(17,18));
            }catch(Exception exc){
                System.out.println("ERR03 caught while parsing "+ node.coordinates);
                node.el1.coord2F=0;
                node.el1.coord_flag=-11;
            }

            if(false){//()node.rho!=0){//TODO:complete this
                /*node.rho=node.rho/24/60/60*2*Math.PI;
                node.el2.coord_flag=12;
                try{
                    node.el2.coord1I=(int)(long) ConverterFINALIZED.radToHrs(ConverterFINALIZED.hrsToRad(node.el1.coord1I,node.el1.coord1F)+ node.rho*Math.sin(node.theta/180*Math.PI)/Math.cos(ConverterFINALIZED.grToRad(node.el1.coord2I,node.el1.coord2F)));
                }catch(NumberFormatException exc){
                    System.err.println("ERR04 caught while processing with e.el2.coord1I "+ node.coordinates);
                    node.el2.coord1I=0;
                    node.el2.coord_flag=-12;
                }
                try{
                    node.el2.coord1F=(int)(long)(100*(ConverterFINALIZED.radToHrs(ConverterFINALIZED.hrsToRad(node.el1.coord1I,node.el1.coord1F)+ node.rho*Math.sin(node.theta/180*Math.PI)/Math.cos(ConverterFINALIZED.grToRad(node.el1.coord2I,node.el1.coord2F)))-node.el2.coord1I));
                }catch(NumberFormatException exc){
                    System.err.println("ERR05 caught while processing with e.el2.coord1F "+ node.coordinates);
                    node.el2.coord1F=0;
                    node.el2.coord_flag=-12;
                }
                try{
                    node.el2.coord2I=(int)(long) ConverterFINALIZED.radToGr(ConverterFINALIZED.grToRad(node.el1.coord2I,node.el1.coord2F)+ node.rho*Math.cos(node.theta/180*Math.PI));
                }catch(NumberFormatException exc){
                    System.err.println("ERR06 caught while processing with e.el2.coord2I "+ node.coordinates);
                    node.el2.coord2I=0;
                    node.el2.coord_flag=-12;
                }
                try{
                    node.el2.coord2F=(int)(long)(10*(ConverterFINALIZED.radToGr(ConverterFINALIZED.grToRad(node.el1.coord2I,node.el1.coord2F)+ node.rho*Math.cos(node.theta/180*Math.PI))-node.el2.coord2I));
                }catch(NumberFormatException exc){
                    System.err.println("ERR07 caught while processing with e.el2.coord2F "+ node.coordinates);
                    node.el2.coord2F=0;
                    node.el2.coord_flag=-12;
                }
                if(node.el2.coord2I<0){
                    node.el2.coord2F*=-1;
                }*/
            }else{
                node.el2.coord1I= node.el1.coord1I;
                node.el2.coord1F= node.el1.coord1F;
                node.el2.coord2I= node.el1.coord2I;
                node.el2.coord2F= node.el1.coord2F;
            }

            //
            //Case for fatal error caught. Transaction should be rolled back(?) (should we implement ORM?)
            //
        }catch(Exception td){
            System.err.println("ERR08 FATAL error caught"+ node.coordinates);
            node.el1.coord_flag=-1;
            td.printStackTrace();
        }

        node.el1.rightCoordX=ConverterFINALIZED.hrsToRad(node.el1.coord1I,node.el1.coord1F)/Math.PI*180;
        node.el1.rightCoordY=ConverterFINALIZED.grToRad(node.el1.coord2I,node.el1.coord2F)/Math.PI*180;
        node.el2.rightCoordX=ConverterFINALIZED.hrsToRad(node.el2.coord1I,node.el2.coord1F)/Math.PI*180;
        node.el2.rightCoordY=ConverterFINALIZED.grToRad(node.el2.coord2I,node.el2.coord2F)/Math.PI*180;


        try {
            e.addCoordinates(NodeORB6FINALIZED.uniqueCatalogueID, KeysDictionary.X, node.el1.rightCoordX);
            e.addCoordinates(NodeORB6FINALIZED.uniqueCatalogueID, KeysDictionary.Y, node.el1.rightCoordY);
            e.addCoordinates(NodeORB6FINALIZED.uniqueCatalogueID, KeysDictionary.RHO, 0d);
            e.addCoordinates(NodeORB6FINALIZED.uniqueCatalogueID, KeysDictionary.THETA, 0d);


            e.el1.addCoordinates(NodeORB6FINALIZED.uniqueCatalogueID, KeysDictionary.X, node.el1.rightCoordX);
            e.el1.addCoordinates(NodeORB6FINALIZED.uniqueCatalogueID, KeysDictionary.Y, node.el1.rightCoordY);
            e.el2.addCoordinates(NodeORB6FINALIZED.uniqueCatalogueID, KeysDictionary.X, node.el2.rightCoordX);
            e.el2.addCoordinates(NodeORB6FINALIZED.uniqueCatalogueID, KeysDictionary.Y, node.el2.rightCoordY);


            e.el1.addMappedEntity(NodeORB6FINALIZED.uniqueCatalogueID,node.source);
            e.el1.addParams(NodeORB6FINALIZED.uniqueCatalogueID,KeysDictionary.WDSSYSTEM,node.params.get(KeysDictionary.WDSSYSTEM));
            e.el2.addMappedEntity(NodeORB6FINALIZED.uniqueCatalogueID,node.source);
            e.el2.addParams(NodeORB6FINALIZED.uniqueCatalogueID,KeysDictionary.WDSSYSTEM,node.params.get(KeysDictionary.WDSSYSTEM));
            extractNameForComponents(node.params.get(KeysDictionary.WDSPAIR),e);

        }catch(ValueAlreadyExistsException exc){
            exc.printStackTrace();
        }
    }
    public static void extractNameForComponents(String nameOfPair,Pair pair){
        String name1="",name2="";
        if(nameOfPair==null){

        }else if(nameOfPair.length()==2){
            name1=""+nameOfPair.charAt(0);
            name2=""+nameOfPair.charAt(1);
        }else if(nameOfPair.contains(",")){
            String[] names = nameOfPair.split(",");
            name1=names[0];
            name2=names[1];
        }
        try {
            pair.addParams(NodeORB6FINALIZED.uniqueCatalogueID, KeysDictionary.WDSPAIR, nameOfPair);
            pair.el1.addParams(NodeORB6FINALIZED.uniqueCatalogueID, KeysDictionary.WDSCOMP, name1);
            pair.el2.addParams(NodeORB6FINALIZED.uniqueCatalogueID, KeysDictionary.WDSCOMP, name2);
        }catch (ValueAlreadyExistsException exc){
            exc.printStackTrace();
        }
    }
}
