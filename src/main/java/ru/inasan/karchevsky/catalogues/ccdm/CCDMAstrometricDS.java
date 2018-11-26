package ru.inasan.karchevsky.catalogues.ccdm;

import ru.inasan.karchevsky.configuration.KeysDictionary;
import ru.inasan.karchevsky.lib.errorHandling.ValueAlreadyExistsException;
import ru.inasan.karchevsky.lib.model.Component;
import ru.inasan.karchevsky.lib.model.Pair;
import ru.inasan.karchevsky.lib.pattern.Datasource;
import ru.inasan.karchevsky.lib.pattern.NodeForParsedCatalogue;

public class CCDMAstrometricDS implements Datasource {
    @Override
    public void propagate(Pair e, NodeForParsedCatalogue nodeRaw) throws Exception {
        if (nodeRaw instanceof NodeCCDMAstrometric) {
            NodeCCDMAstrometric node = (NodeCCDMAstrometric) nodeRaw;
            e.addMappedEntity(NodeCCDM.uniqueCatalogueID, nodeRaw.source);
            e.addParams(NodeCCDM.uniqueCatalogueID, KeysDictionary.MODIFIERS, "       a ");
            e.addParams(NodeCCDM.uniqueCatalogueID, KeysDictionary.OBSERVER, node.params.get(KeysDictionary.OBSERVER));
            e.addParams(NodeCCDM.uniqueCatalogueID, KeysDictionary.CCDMSYSTEM, node.params.get(KeysDictionary.CCDMSYSTEM));
            e.addParams(NodeCCDM.uniqueCatalogueID, KeysDictionary.WDSSYSTEM, node.params.get(KeysDictionary.CCDMSYSTEM));
            e.addParams(NodeCCDM.uniqueCatalogueID, KeysDictionary.CCDMPAIR, node.params.get(KeysDictionary.CCDMPAIR));
            e.addParams(NodeCCDM.uniqueCatalogueID, KeysDictionary.HD, node.params.get(KeysDictionary.HD));

            e.addParams(NodeCCDM.uniqueCatalogueID, KeysDictionary.HIP, node.params.get(KeysDictionary.HIP));
            String dm = node.params.get(KeysDictionary.DM);
            if (dm != null && dm.length() > 1) {
                char meanChar = dm.charAt(dm.length() - 1);
                dm = dm.substring(0, dm.length() - 1);
                if (meanChar == '0') {
                    dm = "BD" + dm;
                } else if (meanChar == '2') {
                    dm = "CPD" + dm;
                } else if (meanChar == '4') {
                    dm = "CP" + dm;
                }
            }
            e.addParams(NodeCCDM.uniqueCatalogueID, KeysDictionary.DM, dm);
            e.addParams(NodeCCDM.uniqueCatalogueID, KeysDictionary.ADS, node.params.get(KeysDictionary.ADS));
            e.addParams(NodeCCDM.uniqueCatalogueID, KeysDictionary.HD, node.params.get(KeysDictionary.HD));

            try {
                double d = Double.parseDouble(node.params.get(KeysDictionary.RHO));
                e.addCoordinates(NodeCCDM.uniqueCatalogueID, KeysDictionary.RHO, d);
            } catch (Exception e4) {
                //e4.printStackTrace();
            }
            try {
                double d = Double.parseDouble(node.params.get(KeysDictionary.THETA));
                e.addCoordinates(NodeCCDM.uniqueCatalogueID, KeysDictionary.THETA, d);
            } catch (Exception e4) {
                //e4.printStackTrace();
            }
            try {
                double d = Double.parseDouble(node.params.get(KeysDictionary.X));
                e.addCoordinates(NodeCCDM.uniqueCatalogueID, KeysDictionary.X, d);
            } catch (Exception e4) {
                //e4.printStackTrace();
            }
            try {
                double d = Double.parseDouble(node.params.get(KeysDictionary.Y));
                e.addCoordinates(NodeCCDM.uniqueCatalogueID, KeysDictionary.Y, d);
            } catch (Exception e4) {
                //e4.printStackTrace();
            }

            try {
                e.el1.addCoordinates(NodeCCDM.uniqueCatalogueID, KeysDictionary.X, node.getXel1());
                e.el1.addCoordinates(NodeCCDM.uniqueCatalogueID, KeysDictionary.Y, node.getYel1());
                e.el2.addCoordinates(NodeCCDM.uniqueCatalogueID, KeysDictionary.X, node.getXel2());
                e.el2.addCoordinates(NodeCCDM.uniqueCatalogueID, KeysDictionary.Y, node.getYel2());


                e.el1.addMappedEntity(NodeCCDM.uniqueCatalogueID, node.source);
                e.el1.addParams(NodeCCDM.uniqueCatalogueID, KeysDictionary.CCDMSYSTEM, node.params.get(KeysDictionary.CCDMSYSTEM));
                e.el2.addMappedEntity(NodeCCDM.uniqueCatalogueID, node.source);
                e.el2.addParams(NodeCCDM.uniqueCatalogueID, KeysDictionary.CCDMSYSTEM, node.params.get(KeysDictionary.CCDMSYSTEM));

                extractNameForComponents(node.params.get(KeysDictionary.CCDMPAIR), e);

            } catch (ValueAlreadyExistsException exc) {
                exc.printStackTrace();
            }
        } else {
            throw new Exception("illegal use of CCDMAstrometricDS");
        }
    }

    @Override
    public void improve(Component component, NodeForParsedCatalogue node) throws Exception {
        Pair e = new Pair();
        e.equalNodeOnNextLevel = component;
        component.equalNodeOnPrevLevel = e;
        e.el1.addCoordinates(NodeCCDMAstrometric.uniqueCatalogueID, KeysDictionary.X, Double.parseDouble(node.params.get(KeysDictionary.X)));
        e.el1.addCoordinates(NodeCCDMAstrometric.uniqueCatalogueID, KeysDictionary.Y, Double.parseDouble(node.params.get(KeysDictionary.Y)));
        e.el2.addCoordinates(NodeCCDMAstrometric.uniqueCatalogueID, KeysDictionary.X, Double.parseDouble(node.params.get(KeysDictionary.X)));
        e.el2.addCoordinates(NodeCCDMAstrometric.uniqueCatalogueID, KeysDictionary.Y, Double.parseDouble(node.params.get(KeysDictionary.Y)));
        e.addCoordinates(NodeCCDMAstrometric.uniqueCatalogueID, KeysDictionary.X, Double.parseDouble(node.params.get(KeysDictionary.X)));
        e.addCoordinates(NodeCCDMAstrometric.uniqueCatalogueID, KeysDictionary.Y, Double.parseDouble(node.params.get(KeysDictionary.Y)));
        e.addCoordinates(NodeCCDMAstrometric.uniqueCatalogueID, KeysDictionary.RHO, 0.00000001d);
        e.addCoordinates(NodeCCDMAstrometric.uniqueCatalogueID, KeysDictionary.THETA, 0.00000001d);
        e.addMappedEntity(NodeCCDMAstrometric.uniqueCatalogueID, node.source);
        e.addParams(NodeCCDMAstrometric.uniqueCatalogueID, KeysDictionary.MODIFIERS, "       a ");
        e.addParams(NodeCCDMAstrometric.uniqueCatalogueID, KeysDictionary.CCDMSYSTEM, node.params.get(KeysDictionary.CCDMSYSTEM));

        e.el1.addMappedEntity(NodeCCDMAstrometric.uniqueCatalogueID, node.source);
        e.el2.addMappedEntity(NodeCCDMAstrometric.uniqueCatalogueID, node.source);
    }

    public static void extractNameForComponents(String nameOfPair, Pair pair) {
        String name1 = "", name2 = "";
        if (nameOfPair == null) {
            name1 = "$A";
            name2 = "$B";
            nameOfPair = "$AB";
        } else if (nameOfPair.length() == 2) {
            name1 = "" + nameOfPair.charAt(0);
            name2 = "" + nameOfPair.charAt(1);
        } else if (nameOfPair.contains(",")) {
            String[] names = nameOfPair.split(",");
            name1 = names[0];
            name2 = names[1];
        }
        try {
            pair.addParams(NodeCCDM.uniqueCatalogueID, KeysDictionary.CCDMPAIR, nameOfPair);
            pair.el1.addParams(NodeCCDM.uniqueCatalogueID, KeysDictionary.CCDMCOMP, name1);
            pair.el2.addParams(NodeCCDM.uniqueCatalogueID, KeysDictionary.CCDMCOMP, name2);
        } catch (ValueAlreadyExistsException exc) {
            exc.printStackTrace();
        }
    }
}