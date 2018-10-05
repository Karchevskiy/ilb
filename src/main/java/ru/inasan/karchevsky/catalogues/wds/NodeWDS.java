package ru.inasan.karchevsky.catalogues.wds;

import ru.inasan.karchevsky.storage.HelperComponent;
import ru.inasan.karchevsky.configuration.KeysDictionary;
import ru.inasan.karchevsky.lib.pattern.NodeForParsedCatalogue;
import ru.inasan.karchevsky.lib.storage.GlobalPoolOfIdentifiers;

import static ru.inasan.karchevsky.configuration.SharedConstants.LOGGING_LEVEL_VERBOSE_ENABLED;

//add f identifier
public class NodeWDS extends NodeForParsedCatalogue {
    public static String uniqueCatalogueID = "WDS";

    public String pairNameXXXXXfromWDS;
    public boolean coordinatesNotFoundInWDS;
    public String coordinatesFromWDSasString; //  055957.08+530946.2
    public String idDM;    //+40 5210 notNull
    public double theta;
    public double rho;
    public char[] modifier2 = new char[2];// V
    public HelperComponent el1 = new HelperComponent();
    public HelperComponent el2 = new HelperComponent();

    public NodeWDS(String s) {
        source = s;
        coordinatesNotFoundInWDS = false;
        calculateIdDM(s);
        calculateModifier(s);
        calculateIdentifier(s);
        calculateNameOfObserver(s);
        calculatePair(s);
        calculateData(s);
    }

    public void calculateIdDM(String s) {
        idDM = s.substring(98, 106);
        if (idDM.replaceAll(" {2}", "").length() > 2) {
            idDM = GlobalPoolOfIdentifiers.rebuildIdForDM(idDM);
            if (idDM.length() > 3) {
                int angle = Integer.parseInt(idDM.substring(0, 3));
                if (angle >= -22) {
                    idDM = "BD" + idDM;
                } else if (angle >= -51) {
                    idDM = "CPD" + idDM;
                } else {
                    idDM = "CP" + idDM;
                }
            }
            GlobalPoolOfIdentifiers.DM.add(idDM);
        } else {
            idDM = "";
        }
    }

    public void calculateModifier(String s) {
        String modifierYX = s.substring(107, 112);
        modifier2[0] = 'v';
        if (modifierYX.toUpperCase().contains("C")) {
            modifier2[0] = 'c';
        }
        if (modifierYX.toUpperCase().contains("L")) {
            modifier2[0] = 'l';
        }
        if (modifierYX.toUpperCase().contains("S")) {
            modifier2[0] = 'l';
        }
        if (modifierYX.toUpperCase().contains("U")) {
            modifier2[0] = 'l';
        }
        if (modifierYX.toUpperCase().contains("Y")) {
            modifier2[0] = 'l';
        }
        if (modifierYX.toUpperCase().contains("O")) {
            modifier2[1] = 'o';
        }
    }

    public void calculateIdentifier(String s) {
        params.put(KeysDictionary.WDSSYSTEM, s.substring(0, 10));
    }

    public void calculateData(String s) {
        coordinatesFromWDSasString = s.substring(111, s.length());
        while (coordinatesFromWDSasString.charAt(0) == ' ') {
            coordinatesFromWDSasString = coordinatesFromWDSasString.substring(1, coordinatesFromWDSasString.length());
        }
        if (coordinatesFromWDSasString.charAt(0) == '.') {
            coordinatesFromWDSasString = s.substring(0, 5) + "0.00" + s.substring(5, 10) + "00.0";
            coordinatesNotFoundInWDS = true;
        }
        try {
            rho = Double.parseDouble(s.substring(52, 57));
            if (rho == -1) {
                if (LOGGING_LEVEL_VERBOSE_ENABLED) {
                    System.err.println("found pair with rho=-1: " + source);
                }
            }
        } catch (Exception e) {
            if (LOGGING_LEVEL_VERBOSE_ENABLED){
                System.err.println("ERR10 Exception caught" + s.substring(52, 57));
            }
            rho = -1;
        }
        try {
            theta = Double.parseDouble(s.substring(42, 45));
        } catch (Exception e) {
            if (LOGGING_LEVEL_VERBOSE_ENABLED){
                System.err.println("ERR09 Exception caught" + s.substring(42, 45));
            }
            theta = -1;
            rho = -1;
        }
    }

    ;

    public void calculateNameOfObserver(String s) {
        params.put(KeysDictionary.OBSERVER, GlobalPoolOfIdentifiers.rebuildIdToUnifiedBase(s.substring(10, 17)));
    }

    public void calculatePair(String s) {
        pairNameXXXXXfromWDS = s.substring(17, 22);
        try {
            pairNameXXXXXfromWDS = pairNameXXXXXfromWDS.replaceAll(" ", "");
            if (pairNameXXXXXfromWDS.length() < 2) {
                pairNameXXXXXfromWDS = "AB";
            }
        } catch (Exception e) {
            if (LOGGING_LEVEL_VERBOSE_ENABLED)
                System.err.println("ERR11 FATAL Exception caught while parsing nameInILB for" + source);
            e.printStackTrace();
        }
        params.put(KeysDictionary.WDSPAIR, GlobalPoolOfIdentifiers.rebuildIdToUnifiedBase(pairNameXXXXXfromWDS));
    }


    @Override
    public Double getXel1() {
        return el1.rightCoordX;
    }

    @Override
    public Double getYel1() {
        return el1.rightCoordY;
    }

    @Override
    public Double getXel2() {
        return el2.rightCoordX;
    }

    @Override
    public Double getYel2() {
        return el2.rightCoordY;
    }
}