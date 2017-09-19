package ILBprocessing.datasources;

import ILBprocessing.beans.NodeCCDM;
import ILBprocessing.configuration.KeysDictionary;
import lib.errorHandling.ValueAlreadyExistsException;
import lib.model.Component;
import lib.model.Pair;
import lib.pattern.Datasourse;
import lib.pattern.NodeForParsedCatalogue;

public class CCDMDS implements Datasourse {
    @Override
    public void propagate(Pair e, NodeForParsedCatalogue nodeRaw) throws Exception{
        if(nodeRaw instanceof NodeCCDM) {
            NodeCCDM node = (NodeCCDM)nodeRaw;
            e.addMappedEntity(NodeCCDM.uniqueCatalogueID,nodeRaw.source);

            e.addParams(NodeCCDM.uniqueCatalogueID, KeysDictionary.OBSERVER,node.params.get(KeysDictionary.OBSERVER));
            e.addParams(NodeCCDM.uniqueCatalogueID, KeysDictionary.CCDMSYSTEM,node.params.get(KeysDictionary.CCDMSYSTEM));
            e.addParams(NodeCCDM.uniqueCatalogueID, KeysDictionary.CCDMPAIR,node.params.get(KeysDictionary.CCDMPAIR));
            e.addParams(NodeCCDM.uniqueCatalogueID, KeysDictionary.HD,node.params.get(KeysDictionary.HD));

            e.addParams(NodeCCDM.uniqueCatalogueID, KeysDictionary.HIP,node.params.get(KeysDictionary.HIP));
            String dm = node.params.get(KeysDictionary.DM);
            if(dm!=null && dm.length()>1){
                char meanChar = dm.charAt(dm.length()-1);
                dm = dm.substring(0,dm.length()-1);
                if(meanChar=='0'){
                    dm="BD"+dm;
                }else if(meanChar=='2'){
                    dm="CPD"+dm;
                }else if(meanChar=='4'){
                    dm="CP"+dm;
                }
            }
            e.addParams(NodeCCDM.uniqueCatalogueID, KeysDictionary.DM,dm);
            e.addParams(NodeCCDM.uniqueCatalogueID, KeysDictionary.ADS,node.params.get(KeysDictionary.ADS));
            e.addParams(NodeCCDM.uniqueCatalogueID, KeysDictionary.HD,node.params.get(KeysDictionary.HD));

                 try{
                        double d =Double.parseDouble(node.params.get(KeysDictionary.RHO));
                        e.addCoordinates(NodeCCDM.uniqueCatalogueID, KeysDictionary.RHO, d);
                    }catch (Exception e4){
                        //e4.printStackTrace();
                    }
            try{
                double d =Double.parseDouble(node.params.get(KeysDictionary.THETA));
                e.addCoordinates(NodeCCDM.uniqueCatalogueID, KeysDictionary.THETA, d);
            }catch (Exception e4){
                //e4.printStackTrace();
            }
            try{
                double d =Double.parseDouble(node.params.get(KeysDictionary.X));
                e.addCoordinates(NodeCCDM.uniqueCatalogueID, KeysDictionary.X, d);
            }catch (Exception e4){
                //e4.printStackTrace();
            }
            try{
                double d =Double.parseDouble(node.params.get(KeysDictionary.Y));
                e.addCoordinates(NodeCCDM.uniqueCatalogueID, KeysDictionary.Y, d);
            }catch (Exception e4){
                //e4.printStackTrace();
            }

            try {

                e.el1.addCoordinates(NodeCCDM.uniqueCatalogueID, KeysDictionary.X, node.el1.x);
                e.el1.addCoordinates(NodeCCDM.uniqueCatalogueID, KeysDictionary.Y, node.el1.y);
                e.el2.addCoordinates(NodeCCDM.uniqueCatalogueID, KeysDictionary.X, node.el2.x);
                e.el2.addCoordinates(NodeCCDM.uniqueCatalogueID, KeysDictionary.Y, node.el2.y);


                e.el1.addMappedEntity(NodeCCDM.uniqueCatalogueID,node.el1.source);
                e.el1.addParams(NodeCCDM.uniqueCatalogueID,KeysDictionary.CCDMSYSTEM,node.params.get(KeysDictionary.CCDMSYSTEM));
                e.el2.addMappedEntity(NodeCCDM.uniqueCatalogueID,node.el2.source);
                e.el2.addParams(NodeCCDM.uniqueCatalogueID,KeysDictionary.CCDMSYSTEM,node.params.get(KeysDictionary.CCDMSYSTEM));

                extractNameForComponents(node.params.get(KeysDictionary.CCDMPAIR),e);

            }catch(ValueAlreadyExistsException exc){
                exc.printStackTrace();
            }
        }else{
            throw new Exception("illegal use of CCDMDS");
        }
    }

    @Override
    public void improve(Component pair, NodeForParsedCatalogue node) throws Exception {
        throw new Exception("illegal use of CCDMDS");
    }

    public static void extractNameForComponents(String nameOfPair,Pair pair){
        String name1="",name2="";
        if(nameOfPair==null){
            name1="$A";
            name2="$B";
            nameOfPair="$AB";
        }else if(nameOfPair.length()==2){
            name1=""+nameOfPair.charAt(0);
            name2=""+nameOfPair.charAt(1);
        }else if(nameOfPair.contains(",")){
            String[] names = nameOfPair.split(",");
            name1=names[0];
            name2=names[1];
        }
        try {
            pair.addParams(NodeCCDM.uniqueCatalogueID, KeysDictionary.CCDMPAIR, nameOfPair);
            pair.el1.addParams(NodeCCDM.uniqueCatalogueID, KeysDictionary.CCDMCOMP, name1);
            pair.el2.addParams(NodeCCDM.uniqueCatalogueID, KeysDictionary.CCDMCOMP, name2);
        }catch (ValueAlreadyExistsException exc){
            exc.printStackTrace();
        }
    }
}