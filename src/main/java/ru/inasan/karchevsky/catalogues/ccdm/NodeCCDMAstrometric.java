package ru.inasan.karchevsky.catalogues.ccdm;

import ru.inasan.karchevsky.configuration.KeysDictionary;
import ru.inasan.karchevsky.lib.pattern.NodeForParsedCatalogue;
import ru.inasan.karchevsky.lib.service.ConverterFINALIZED;
import ru.inasan.karchevsky.lib.storage.GlobalPoolOfIdentifiers;
import ru.inasan.karchevsky.storage.HelperComponent;

@SuppressWarnings("Duplicates")
public class NodeCCDMAstrometric extends NodeForParsedCatalogue {

    public static String uniqueCatalogueID = "CCDMAstrometric";

    public HelperComponent el1 = new HelperComponent();
    public HelperComponent el2 = new HelperComponent();

    public String source = "";
    public String nameOfObserver = "";
    public String wdsID = "";
    public String ccdmID = "";
    public String DM = "";
    public String HD = "";
    public String ADS = "";
    public String HIP = "";
    public double x;
    public double y;

    private String pairCCDM;
    private String coord_I2_1fake = "";// in 00149-3209 00149
    private String coord_I2_2fake = "";// in 00149-3209 -3209
    private double coord_F2_1fake = 0;
    private double coord_F2_2fake = 0;

    @Override
    public Double getXel1() {
        return Double.valueOf(params.get(KeysDictionary.X));
    }
    @Override
    public Double getYel1() {
        return Double.valueOf(params.get(KeysDictionary.Y));
    }

    @Override
    public Double getXel2() {
        return Double.valueOf(params.get(KeysDictionary.X));
    }

    @Override
    public Double getYel2() {
        return Double.valueOf(params.get(KeysDictionary.Y)) + 0.0000000000001d;
    }


    public NodeCCDMAstrometric(String s) {
        source = s.substring(0, s.length() - 1);
        ccdmID = s.substring(1, 11);
        pairCCDM = s.charAt(12) + "A" + s.charAt(12) + "B";

        parseCoordinates(s);
        HIP = GlobalPoolOfIdentifiers.rebuildIdToUnifiedBase(s.substring(126, 132));
        if (HIP.length() > 1) GlobalPoolOfIdentifiers.HIP.add(HIP);

        nameOfObserver = GlobalPoolOfIdentifiers.rebuildIdToUnifiedBase(s.substring(15, 22));

        DM = GlobalPoolOfIdentifiers.rebuildIdForDM(s.substring(77, 87));
        if (DM.length() > 1) GlobalPoolOfIdentifiers.DM.add(DM);

        HD = GlobalPoolOfIdentifiers.rebuildIdToUnifiedBase(s.substring(98, 104));
        if (HD.length() > 1) GlobalPoolOfIdentifiers.HD.add(HD);

        if (s.charAt(106) == 'A') {
            ADS = GlobalPoolOfIdentifiers.rebuildIdToUnifiedBase(s.substring(107, 112));
            if (ADS.length() > 1) GlobalPoolOfIdentifiers.ADS.add(ADS);
        }
        wdsID = s.substring(114, 124).replaceAll("N", "+").replaceAll("S", "-");

        translateToPair();
    }


    private void parseCoordinates(String s) {
        try {
            coord_I2_1fake = s.substring(1, 6);
            if (s.charAt(6) != ' ') {
                coord_I2_2fake = s.substring(6, 11);
            } else {
                coord_I2_2fake = s.substring(7, 11);
            }

            if (s.charAt(27) != ' ') {
                if (s.charAt(23) != ' ') {
                    coord_F2_1fake = Double.parseDouble(s.substring(23, 30));
                } else {
                    coord_F2_1fake = Double.parseDouble(s.substring(24, 30));
                }

                if (s.charAt(30) != ' ') {
                    coord_F2_2fake = Double.parseDouble(s.substring(30, 37));
                } else {
                    coord_F2_2fake = Double.parseDouble(s.substring(31, 37));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("causedBy:" + s);
        }
    }


    private void translateToPair() {


        try {
            this.params.put(KeysDictionary.THETA, "" + source.substring(46, 49));
            this.params.put(KeysDictionary.RHO, "" + source.substring(50, 55));
        } catch (Exception e1) {
            this.params.put(KeysDictionary.THETA, "" + 0);
            this.params.put(KeysDictionary.RHO, "" + 0.0);
        }
        this.params.put(KeysDictionary.CCDMSYSTEM, ccdmID);

        propagateToPairNode();
    }

    private void propagateToPairNode() {
        this.params.put(KeysDictionary.OBSERVER, this.nameOfObserver);

        this.params.put(KeysDictionary.CCDMSYSTEM, this.ccdmID);
        this.params.put(KeysDictionary.CCDMPAIR, this.pairCCDM);
        if (this.HIP.length() > 1) {
            this.params.put(KeysDictionary.HIP, this.HIP);
        }
        if (this.DM.length() > 1) {
            this.params.put(KeysDictionary.DM, this.DM);
        }
        if (this.HD.length() > 1) {
            this.params.put(KeysDictionary.HD, this.HD);
        }
        if (this.ADS.length() > 1) {
            this.params.put(KeysDictionary.ADS, this.ADS);
        }
        if (this.wdsID.length() > 1) {
            this.params.put(KeysDictionary.WDSSYSTEM, this.wdsID);
        }

        this.params.put(KeysDictionary.COORD_I1_1, "" + this.coord_I2_1fake);
        this.params.put(KeysDictionary.COORD_I1_2, "" + this.coord_I2_2fake);
        this.params.put(KeysDictionary.COORD_F1_1, "" + this.coord_F2_1fake);
        this.params.put(KeysDictionary.COORD_F1_2, "" + this.coord_F2_2fake);


        float h = Integer.parseInt(this.coord_I2_1fake) / 10.0f * 100 + Integer.parseInt(this.coord_I2_1fake) % 10 * 6;
        double x = ConverterFINALIZED.hrsToRad(h, this.coord_F2_1fake * 100) / Math.PI * 180;
        double y = ConverterFINALIZED.grToRad(Double.parseDouble(this.coord_I2_2fake) * 100, this.coord_F2_2fake * 10) / Math.PI * 180;

        this.params.put(KeysDictionary.X, "" + x);
        this.params.put(KeysDictionary.Y, "" + y);

        this.x = x;
        this.y = y;

    }

    @Override
    public String getUniqueCatalogueID() {
        return uniqueCatalogueID;
    }

    public HelperComponent getEl1() {
        return el1;
    }

    public HelperComponent getEl2() {
        return el2;
    }

    public String getSource() {
        return source;
    }

    public String getNameOfObserver() {
        return nameOfObserver;
    }

    public String getWdsID() {
        return wdsID;
    }

    public String getCcdmID() {
        return ccdmID;
    }

    public String getDM() {
        return DM;
    }

    public String getHD() {
        return HD;
    }

    public String getADS() {
        return ADS;
    }

    public String getHIP() {
        return HIP;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public String getPairCCDM() {
        return pairCCDM;
    }

    public String getCoord_I2_1fake() {
        return coord_I2_1fake;
    }

    public String getCoord_I2_2fake() {
        return coord_I2_2fake;
    }

    public double getCoord_F2_1fake() {
        return coord_F2_1fake;
    }

    public double getCoord_F2_2fake() {
        return coord_F2_2fake;
    }
}
