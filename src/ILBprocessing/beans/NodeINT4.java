package ILBprocessing.beans;

import ILBprocessing.beans.helpers.HelperComponent;
import ILBprocessing.configuration.KeysDictionary;
import lib.pattern.NodeForParsedCatalogue;
import lib.service.ConverterFINALIZED;
import lib.storage.GlobalPoolOfIdentifiers;

import java.util.ArrayList;

import static ILBprocessing.configuration.SharedConstants.LOGGING_LEVEL_VERBOSE_ENABLED;

public class NodeINT4  extends NodeForParsedCatalogue {
    public static String uniqueCatalogueID = "INT4";
    public String coordinates ="";
    public String chameleonID1 ="";

    public HelperComponent el1= new HelperComponent();
    public HelperComponent el2= new HelperComponent();

    public NodeINT4(ArrayList<String> sources) throws Exception {
        String s=sources.get(0);
        source=s;
        coordinates =s.substring(0, 18);
        if(s.length()>=113 && s.charAt(103)!=' ') {
            params.put(KeysDictionary.WDSSYSTEM, s.substring(103, 113));
        }else{
            if(s.charAt(104)!=' '){
                params.put(KeysDictionary.WDSSYSTEM, s.substring(104, 114));
            }else {
                throw new Exception(s);
            }
        }
        //Stupid id in SCO
        chameleonID1 =s.substring(46, 58);
        if(GlobalPoolOfIdentifiers.likeBayer(chameleonID1)){
            params.put(KeysDictionary.BAYER,GlobalPoolOfIdentifiers.rebuildIdToUnifiedBase(chameleonID1));
            GlobalPoolOfIdentifiers.BAYER.add(params.get(KeysDictionary.BAYER));
        }else if(GlobalPoolOfIdentifiers.likeFlamsteed(chameleonID1)){
            params.put(KeysDictionary.FLAMSTEED, GlobalPoolOfIdentifiers.rebuildIdToUnifiedBase(chameleonID1));
            GlobalPoolOfIdentifiers.FLAMSTEED.add(params.get(KeysDictionary.FLAMSTEED));
        }else if(GlobalPoolOfIdentifiers.likeDM(chameleonID1)){
            params.put(KeysDictionary.DM,GlobalPoolOfIdentifiers.rebuildIdForDM(chameleonID1));
            GlobalPoolOfIdentifiers.DM.add(params.get(KeysDictionary.DM));
        }else{
            if(s.charAt(37)!=' ') {
                if(s.charAt(37)>='0'&&s.charAt(37)<='9'){
                    params.put(KeysDictionary.OBSERVER, GlobalPoolOfIdentifiers.rebuildIdToUnifiedBase(s.substring(46, 53)));
                }else {
                    params.put(KeysDictionary.OBSERVER, GlobalPoolOfIdentifiers.rebuildIdToUnifiedBase(s.substring(46, 53)));
                    params.put(KeysDictionary.WDSPAIR, GlobalPoolOfIdentifiers.rebuildIdToUnifiedBase(s.substring(53, 58)));
                }
            }else{
                params.put(KeysDictionary.OBSERVER, GlobalPoolOfIdentifiers.rebuildIdToUnifiedBase(s.substring(46, 53)));
            }
        }

        //ADS
        String idADS = (s.substring(20, 32)).replaceAll(" ","");
        if(!idADS.equals(".") && idADS.contains("ADS")){
            idADS=GlobalPoolOfIdentifiers.rebuildIdToUnifiedBase(idADS.replaceAll("ADS",""));
            idADS= GlobalPoolOfIdentifiers.rebuildIdToUnifiedBase(idADS);
            params.put(KeysDictionary.ADS,idADS);
            GlobalPoolOfIdentifiers.ADS.add(params.get(KeysDictionary.ADS));
        }else if(GlobalPoolOfIdentifiers.likeDM(idADS)){
            params.put(KeysDictionary.DM,GlobalPoolOfIdentifiers.rebuildIdForDM(idADS));
            GlobalPoolOfIdentifiers.DM.add(params.get(KeysDictionary.DM));
        }

        for(int i=1; i<sources.size();i++) {
            String s1 = sources.get(i);
            String theta = s1.substring(14, 19);
            if (theta.replaceAll(" ", "").equals(".")) {
            }else{
                params.put(KeysDictionary.THETA, theta);
            }
            String rho = s1.substring(29, 37);
            if (rho.replaceAll(" ", "").equals(".")) {
            }else{
                params.put(KeysDictionary.RHO, rho);
            }
        }
        if(!params.containsKey(KeysDictionary.RHO)){
            params.put(KeysDictionary.RHO, "-1");
        }
        if(!params.containsKey(KeysDictionary.THETA)){
            params.put(KeysDictionary.THETA, "0");
        }
        parseCoordinatesDirectlyFromWDS(this);
    }


    //clone from WDSDS TODO:refactor: encapsulate in special class
    public static void parseCoordinatesDirectlyFromWDS(NodeINT4 node){
        try{
            node.coordinates=node.coordinates.replaceAll(" ","0");
            node.el1.coord1I=Integer.parseInt(node.coordinates.substring(0,6));
            node.el1.coord_flag=11;
            try{
                node.el1.coord1F=Integer.parseInt(node.coordinates.substring(7,9));
            }catch(NumberFormatException exc1){
                try {
                    node.el1.coord1F=Integer.parseInt(node.coordinates.substring(7,8));
                    node.el1.coord_flag=-11;
                    if(LOGGING_LEVEL_VERBOSE_ENABLED)System.out.println("ERR01 caught while parsing "+ node.coordinates);
                }catch(NumberFormatException exc2){
                    if(LOGGING_LEVEL_VERBOSE_ENABLED)System.out.println("ERR01b caught while parsing "+ node.coordinates);
                    node.el1.coord1F = 0;
                    node.el1.coord_flag=-11;
                }
            }
            try{
                node.el1.coord2I=Integer.parseInt(node.coordinates.substring(9,16));
            }catch(NumberFormatException exc) {
                if(LOGGING_LEVEL_VERBOSE_ENABLED)System.err.println("ERR02 caught while parsing "+ node.coordinates);
                node.el1.coord2I = 0;
                node.el1.coord_flag = -11;
            }
            try{
                node.el1.coord2F=Integer.parseInt(node.coordinates.substring(17,18));
            }catch(Exception exc){
                if(LOGGING_LEVEL_VERBOSE_ENABLED)System.out.println("ERR03 caught while parsing "+ node.coordinates);
                node.el1.coord2F=0;
                node.el1.coord_flag=-11;
            }


            double rho=Double.parseDouble(node.params.get(KeysDictionary.RHO));
            double theta=Double.parseDouble(node.params.get(KeysDictionary.THETA));
            if(rho!=0){
                rho=rho/360/60/60*2*Math.PI;
                node.el2.coord_flag=12;
                try{
                    node.el2.coord1I=(int)(long) ConverterFINALIZED.radToHrs(ConverterFINALIZED.hrsToRad(node.el1.coord1I,node.el1.coord1F)+ rho*Math.sin(theta/180*Math.PI));
                }catch(NumberFormatException exc){
                    node.el2.coord1I=0;
                    node.el2.coord_flag=-12;
                }
                try{
                    node.el2.coord1F=(int)(long)(100*(ConverterFINALIZED.radToHrs(ConverterFINALIZED.hrsToRad(node.el1.coord1I,node.el1.coord1F)+ rho*Math.sin(theta/180*Math.PI))-node.el2.coord1I));
                }catch(NumberFormatException exc){
                    node.el2.coord1F=0;
                    node.el2.coord_flag=-12;
                }
                try{
                    node.el2.coord2I=(int)(long) ConverterFINALIZED.radToGr(ConverterFINALIZED.grToRad(node.el1.coord2I,node.el1.coord2F)+ rho*Math.cos(theta/180*Math.PI));
                }catch(NumberFormatException exc){
                    node.el2.coord2I=0;
                    node.el2.coord_flag=-12;
                }
                try{
                    node.el2.coord2F=(int)(long)(10*(ConverterFINALIZED.radToGr(ConverterFINALIZED.grToRad(node.el1.coord2I,node.el1.coord2F)+ rho*Math.cos(theta/180*Math.PI))-node.el2.coord2I));
                }catch(NumberFormatException exc){
                    node.el2.coord2F=0;
                    node.el2.coord_flag=-12;
                }
                if(node.el2.coord2I<0){
                    node.el2.coord2F*=-1;
                }
            }
            //
            //Case for fatal error caught. Transaction should be rolled back(?) (should we implement ORM?)
            //
        }catch(Exception td){
            if(LOGGING_LEVEL_VERBOSE_ENABLED)System.err.println("ERR08 FATAL error caught"+ node.coordinates);
            node.el1.coord_flag=-1;
            td.printStackTrace();
        }

        node.el1.rightCoordX= ConverterFINALIZED.hrsToRad(node.el1.coord1I,node.el1.coord1F)/Math.PI*180;
        node.el1.rightCoordY=ConverterFINALIZED.grToRad(node.el1.coord2I,node.el1.coord2F)/Math.PI*180;
        node.el2.rightCoordX=ConverterFINALIZED.hrsToRad(node.el2.coord1I,node.el2.coord1F)/Math.PI*180;
        node.el2.rightCoordY=ConverterFINALIZED.grToRad(node.el2.coord2I,node.el2.coord2F)/Math.PI*180;


        node.params.put(KeysDictionary.X, ""+node.el1.rightCoordX);
        node.params.put(KeysDictionary.Y, ""+node.el1.rightCoordY);
    }
}
