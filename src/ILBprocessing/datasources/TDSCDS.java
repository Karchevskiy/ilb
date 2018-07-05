package ILBprocessing.datasources;

import ILBprocessing.beans.NodeTDSC;
import ILBprocessing.configuration.KeysDictionary;
import lib.errorHandling.ValueAlreadyExistsException;
import lib.model.Component;
import lib.model.Pair;
import lib.pattern.Datasource;
import lib.pattern.NodeForParsedCatalogue;

/**
 * Created by Алекс on 13.02.2017.
 */
public class TDSCDS implements Datasource {
    @Override
    public void propagate(Pair e, NodeForParsedCatalogue nodeRaw) throws Exception{
        if(nodeRaw instanceof NodeTDSC) {
            NodeTDSC node = (NodeTDSC)nodeRaw;
            e.addMappedEntity(NodeTDSC.uniqueCatalogueID,nodeRaw.source);
            e.addParams(NodeTDSC.uniqueCatalogueID,KeysDictionary.MODIFIERS,"  v");
            if(node.params.containsKey(KeysDictionary.HD) && !node.params.get(KeysDictionary.HD).equals("")) e.addParams(NodeTDSC.uniqueCatalogueID,KeysDictionary.HD,node.params.get(KeysDictionary.HD));

            if(node.params.containsKey(KeysDictionary.WDSSYSTEM) && !node.params.get(KeysDictionary.WDSSYSTEM).equals("")) e.addParams(NodeTDSC.uniqueCatalogueID,KeysDictionary.WDSSYSTEM, node.params.get(KeysDictionary.WDSSYSTEM));
            if(node.params.containsKey(KeysDictionary.TDSCSYSTEM) && !node.params.get(KeysDictionary.TDSCSYSTEM).equals("")) e.addParams(NodeTDSC.uniqueCatalogueID,KeysDictionary.TDSCSYSTEM, node.params.get(KeysDictionary.TDSCSYSTEM));
            if(node.params.containsKey(KeysDictionary.TDSCPAIR) && !node.params.get(KeysDictionary.TDSCPAIR).equals("")) e.addParams(NodeTDSC.uniqueCatalogueID,KeysDictionary.TDSCPAIR, node.params.get(KeysDictionary.TDSCPAIR));


            try {
                double x = Double.parseDouble(node.params.get(KeysDictionary.X));
                e.addCoordinates(NodeTDSC.uniqueCatalogueID, KeysDictionary.X, x);
            }catch (ValueAlreadyExistsException e2){
             //  e2.printStackTrace();
            }
            try{
                double y = Double.parseDouble(node.params.get(KeysDictionary.Y));
                e.addCoordinates(NodeTDSC.uniqueCatalogueID, KeysDictionary.Y, y);
            }catch (ValueAlreadyExistsException e2){
             //   e2.printStackTrace();
            }
            try{
                double rho = Double.parseDouble(node.params.get(KeysDictionary.RHO));
                e.addCoordinates(NodeTDSC.uniqueCatalogueID, KeysDictionary.RHO, rho);
            }catch (ValueAlreadyExistsException e2){
              //  e2.printStackTrace();
            }
            try{
                double theta = Double.parseDouble(node.params.get(KeysDictionary.THETA));
                e.addCoordinates(NodeTDSC.uniqueCatalogueID, KeysDictionary.THETA, theta);
            }catch (ValueAlreadyExistsException e2){
               // e2.printStackTrace();
            }
            e.el1.addMappedEntity(NodeTDSC.uniqueCatalogueID, node.el1.source);
            e.el2.addMappedEntity(NodeTDSC.uniqueCatalogueID, node.el2.source);
            extractNameForComponents(node.params.get(KeysDictionary.TDSCPAIR),e);
            try {
                double x = Double.parseDouble(node.el1.params.get(KeysDictionary.X));
                e.el1.addCoordinates(NodeTDSC.uniqueCatalogueID, KeysDictionary.X,x );
            }catch (ValueAlreadyExistsException exc){
              //  exc.printStackTrace();
            }
            try {
                double x = Double.parseDouble(node.el2.params.get(KeysDictionary.X));
                e.el2.addCoordinates(NodeTDSC.uniqueCatalogueID, KeysDictionary.X,x );
            }catch (ValueAlreadyExistsException exc){
               // exc.printStackTrace();
            }try {
                double y = Double.parseDouble(node.el1.params.get(KeysDictionary.Y));
                e.el1.addCoordinates(NodeTDSC.uniqueCatalogueID, KeysDictionary.Y,y);
            }catch (ValueAlreadyExistsException exc){
                //exc.printStackTrace();
            }
            try {
                double y = Double.parseDouble(node.el2.params.get(KeysDictionary.Y));
                e.el2.addCoordinates(NodeTDSC.uniqueCatalogueID, KeysDictionary.Y,y);
            }catch (ValueAlreadyExistsException exc){
                //exc.printStackTrace();
            }
        }else{
            throw new Exception("illegal use of TDSCDS");
        }
    }

    @Override
    public void improve(Component pair, NodeForParsedCatalogue node) throws Exception {
        throw new Exception("illegal use of TDSCDS");
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
            pair.addParams(NodeTDSC.uniqueCatalogueID, KeysDictionary.TDSCPAIR, nameOfPair);
            pair.el1.addParams(NodeTDSC.uniqueCatalogueID, KeysDictionary.TDSCCOMP, name1);
            pair.el2.addParams(NodeTDSC.uniqueCatalogueID, KeysDictionary.TDSCCOMP, name2);
        }catch (ValueAlreadyExistsException exc){
            //exc.printStackTrace();
        }
    }
}
