package ru.inasan.karchevsky.catalogues.orb6;

import ru.inasan.karchevsky.storage.HelperComponent;
import ru.inasan.karchevsky.configuration.KeysDictionary;
import ru.inasan.karchevsky.lib.pattern.NodeForParsedCatalogue;
import ru.inasan.karchevsky.lib.storage.GlobalPoolOfIdentifiers;

import java.io.Serializable;

public class NodeORB6 extends NodeForParsedCatalogue implements Serializable {
    public static String uniqueCatalogueID = "ORB6";

    public String chameleonID = "";//DD or flamsteed or DM or Bayer
    public String coordinates = "";
    public boolean pairWdsDefault = false;

    public HelperComponent el1 = new HelperComponent();
    public HelperComponent el2 = new HelperComponent();

    public NodeORB6(String s) {
        source = s;
        coordinates = s.substring(0, 18);
        params.put(KeysDictionary.WDSSYSTEM, s.substring(19, 29));

        //Stupid id in SCO
        chameleonID = s.substring(30, 45);
        if (GlobalPoolOfIdentifiers.isBayer(chameleonID)) {
            params.put(KeysDictionary.BAYER, GlobalPoolOfIdentifiers.rebuildIdToUnifiedBase(chameleonID));
            GlobalPoolOfIdentifiers.BAYER.add(params.get(KeysDictionary.BAYER));
        } else if (GlobalPoolOfIdentifiers.isFlamsteed(chameleonID)) {
            params.put(KeysDictionary.FLAMSTEED, GlobalPoolOfIdentifiers.rebuildIdToUnifiedBase(chameleonID));
            GlobalPoolOfIdentifiers.FLAMSTEED.add(params.get(KeysDictionary.FLAMSTEED));
        } else if (GlobalPoolOfIdentifiers.isDM(chameleonID)) {
            params.put(KeysDictionary.DM, GlobalPoolOfIdentifiers.rebuildIdForDM(chameleonID));
            GlobalPoolOfIdentifiers.DM.add(params.get(KeysDictionary.DM));
        } else {
            if (s.charAt(37) != ' ') {
                if (s.charAt(37) >= '0' && s.charAt(37) <= '9') {
                    params.put(KeysDictionary.OBSERVER, GlobalPoolOfIdentifiers.rebuildIdToUnifiedBase(s.substring(30, 42)));
                } else {
                    params.put(KeysDictionary.OBSERVER, GlobalPoolOfIdentifiers.rebuildIdToUnifiedBase(s.substring(30, 37)));
                    params.put(KeysDictionary.WDSPAIR, GlobalPoolOfIdentifiers.rebuildIdToUnifiedBase(s.substring(37, 42)));
                }
            } else {
                params.put(KeysDictionary.OBSERVER, GlobalPoolOfIdentifiers.rebuildIdToUnifiedBase(s.substring(30, 37)));
                pairWdsDefault = true;
            }
        }

        //ADS
        String idADS = (s.substring(45, 50)).replaceAll(" ", "");
        if (!idADS.equals(".")) {
            idADS = GlobalPoolOfIdentifiers.rebuildIdToUnifiedBase(idADS);
            params.put(KeysDictionary.ADS, idADS);
            GlobalPoolOfIdentifiers.ADS.add(params.get(KeysDictionary.ADS));
        }

        //HD
        String idHD = (s.substring(51, 58)).replaceAll(" ", "");
        if (!idHD.equals(".")) {
            idHD = GlobalPoolOfIdentifiers.rebuildIdToUnifiedBase(idHD);
            params.put(KeysDictionary.HD, idHD);
            GlobalPoolOfIdentifiers.HD.add(params.get(KeysDictionary.HD));
        }

        //HIP
        String idHIP = (s.substring(58, 65)).replaceAll(" ", "");
        if (!idHIP.equals(".")) {
            idHIP = GlobalPoolOfIdentifiers.rebuildIdToUnifiedBase(idHIP);
            params.put(KeysDictionary.HIP, idHIP);
            GlobalPoolOfIdentifiers.HIP.add(params.get(KeysDictionary.HIP));
        }
    }

    @Override
    public Double getXel1() {
        return null;
    }

    @Override
    public Double getYel1() {
        return null;
    }

    @Override
    public Double getXel2() {
        return null;
    }

    @Override
    public Double getYel2() {
        return null;
    }
}
