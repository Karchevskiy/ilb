package ru.inasan.karchevsky.catalogues.ccdm;

import ru.inasan.karchevsky.configuration.KeysDictionary;
import ru.inasan.karchevsky.lib.service.ConverterFINALIZED;
import ru.inasan.karchevsky.lib.storage.GlobalPoolOfIdentifiers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

public class CCDMHelperComponent implements Serializable {
    public String source = "";
    public String pairCCDM = "";
    public String nameOfObserver = "";
    public String wdsID = "";
    public String ccdmID = "";
    public String DM = "";
    public String HD = "";
    public String ADS = "";
    public String HIP = "";
    public char componentInfo;
    public String coord_I2_1fake = "";// in 00149-3209 00149
    public String coord_I2_2fake = "";// in 00149-3209 -3209
    public double coord_F2_1fake = 0;
    public double coord_F2_2fake = 0;
    public boolean astrometrical = false;
    boolean coordinatesNotFoundInCCDM = false;
    public double x;
    public double y;

    public CCDMHelperComponent(String s) {
        source = s.substring(0, s.length() - 1);
        ccdmID = s.substring(1, 11);
        if (s.charAt(12) == 'A') {
            componentInfo = 'A';
        } else if (s.charAt(11) == ' ') {
            pairCCDM = "A" + s.charAt(12);
            componentInfo = s.charAt(12);
        } else {
            pairCCDM = "" + s.charAt(11) + s.charAt(12);
            componentInfo = s.charAt(12);
        }
        parseCoordinates(s);
        if (s.charAt(14) == '%' || s.charAt(14) == '&') {
            astrometrical = true;
        }
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
    }

    public void parseCoordinates(String s) {//refactored. finished method
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

    public static void translateToPairs(ArrayList<CCDMHelperComponent> listComp, Collection<NodeCCDM> listPair) {
        for (int i = 0; i < listComp.size(); i++) {
            if (listComp.get(i).componentInfo != 'A') {
                NodeCCDM e = new NodeCCDM();

                try {
                    e.params.put(KeysDictionary.THETA, "" + listComp.get(i).source.substring(46, 49));
                    e.params.put(KeysDictionary.RHO, "" + listComp.get(i).source.substring(50, 55));
                } catch (Exception e1) {
                    e.params.put(KeysDictionary.THETA, "" + 0);
                    e.params.put(KeysDictionary.RHO, "" + 0.0);
                }
                if (listComp.get(i).astrometrical) {
                    e.el2.astrometrical = true;
                }
                e.params.put(KeysDictionary.CCDMSYSTEM, listComp.get(i).ccdmID);
                char target = listComp.get(i).pairCCDM.charAt(0);
                for (int j = 0; j < listComp.size(); j++) {
                    if (listComp.get(j).componentInfo == target) {
                        propagateToPairNode(e, listComp.get(j), 1);
                        e.el1 = listComp.get(j);
                        if (listComp.get(j).astrometrical) {
                            e.el1.astrometrical = true;
                        }
                        break;
                    }
                }
                propagateToPairNode(e, listComp.get(i), 2);
                e.el2 = listComp.get(i);
                e.source = listComp.get(i).source;
                listPair.add(e);
            }
        }
    }

    private static void propagateToPairNode(NodeCCDM e, CCDMHelperComponent c1, int number) {
        if (c1.nameOfObserver.length() > 1) {
            e.params.put(KeysDictionary.OBSERVER, c1.nameOfObserver);
        }
        if (c1.coordinatesNotFoundInCCDM) {
            e.coordinatesNotFoundInCCDM = c1.coordinatesNotFoundInCCDM;
        }
        e.params.put(KeysDictionary.CCDMSYSTEM, c1.ccdmID);
        e.params.put(KeysDictionary.CCDMPAIR, c1.pairCCDM);
        //TODO:propagate astrometrical flag
        if (c1.HIP.length() > 1) {
            e.params.put(KeysDictionary.HIP, c1.HIP);
        }
        if (c1.DM.length() > 1) {
            e.params.put(KeysDictionary.DM, c1.DM);
        }
        if (c1.HD.length() > 1) {
            e.params.put(KeysDictionary.HD, c1.HD);
        }
        if (c1.ADS.length() > 1) {
            e.params.put(KeysDictionary.ADS, c1.ADS);
        }
        if (c1.wdsID.length() > 1) {
            e.params.put(KeysDictionary.WDSSYSTEM, c1.wdsID);
        }
        if (!e.params.containsKey(KeysDictionary.X)) {
            if (c1.x != 0) {
                e.params.put(KeysDictionary.X, "" + c1.x);
                e.params.put(KeysDictionary.Y, "" + c1.y);
            } else {
                e.params.put(KeysDictionary.COORD_I1_1, "" + c1.coord_I2_1fake);
                e.params.put(KeysDictionary.COORD_I1_2, "" + c1.coord_I2_2fake);
                e.params.put(KeysDictionary.COORD_F1_1, "" + c1.coord_F2_1fake);
                e.params.put(KeysDictionary.COORD_F1_2, "" + c1.coord_F2_2fake);
                //050612.55+372122.2
                //00018+6437
                float h = Integer.parseInt(c1.coord_I2_1fake) / 10.0f * 100 + Integer.parseInt(c1.coord_I2_1fake) % 10 * 6;
                double x = ConverterFINALIZED.hrsToRad(h, c1.coord_F2_1fake * 100) / Math.PI * 180;
                double y = ConverterFINALIZED.grToRad(Double.parseDouble(c1.coord_I2_2fake) * 100, c1.coord_F2_2fake * 10) / Math.PI * 180;

                e.params.put(KeysDictionary.X, "" + x);
                e.params.put(KeysDictionary.Y, "" + y);
                c1.x = x;
                c1.y = y;
            }
        } else {
            try {
                double x = e.el1.x + Double.parseDouble(e.params.get(KeysDictionary.RHO)) * Math.sin(Double.parseDouble(e.params.get(KeysDictionary.THETA)) / 180 * Math.PI) / 60 / 60;
                double y = e.el1.y + Double.parseDouble(e.params.get(KeysDictionary.RHO)) * Math.cos(Double.parseDouble(e.params.get(KeysDictionary.THETA)) / 180 * Math.PI) / 60 / 60;
                c1.x = x;
                c1.y = y;
            } catch (Exception exc) {
                //TODO:logging
                //exc.printStackTrace();
                c1.x = e.el1.x;
                c1.y = e.el1.y;
            }
        }
    }
}
