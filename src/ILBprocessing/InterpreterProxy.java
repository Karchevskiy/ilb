package ILBprocessing;

import ILBprocessing.beans.*;
import ILBprocessing.configuration.KeysDictionary;
import ILBprocessing.datasources.*;
import ILBprocessing.storage.CachedStorageILB;
import lib.model.Pair;
import lib.model.StarSystem;
import lib.storage.GlobalPoolOfIdentifiers;
import lib.tools.resolvingRulesImplementation.*;
import lib.tools.resolvingRulesImplementation.pairToCompRules.MatchingByCoordRuleForComponentsImplementation;

import java.util.ArrayList;

import static lib.storage.GlobalPoolOfIdentifiers.resolvedCEV;
import static lib.storage.GlobalPoolOfIdentifiers.resolvedSB9;
import static lib.storage.GlobalPoolOfIdentifiers.resolvedLMX;

@SuppressWarnings("unchecked")
public class InterpreterProxy extends CachedStorageILB {

    public static void interprWDS() {
        //int xLog=0;
        //long timePrev=System.nanoTime();
        for (NodeWDSFINALIZED listWD : listWDS) {
            //create coordinatesNotFoundInWDS sysList
            StarSystem s = new StarSystem();
            s.data = listWD.coordinatesFromWDSasString;
            s.params.put(KeysDictionary.WDSSYSTEM, listWD.params.get(KeysDictionary.WDSSYSTEM));
            s.coordinatesNotFoundInWDS = listWD.coordinatesNotFoundInWDS;

            //check if such sysList not exists
            boolean uniqueSystem = true;
            int sysNumber = 0;
            int h = sysList.size();
            for (int j = 0; j < h; j++) {
                if (s.params.get(KeysDictionary.WDSSYSTEM).equals(sysList.get(j).params.get(KeysDictionary.WDSSYSTEM))) {
                    uniqueSystem = false;
                    sysNumber = j;
                    break;
                }
            }
            if (uniqueSystem) {
                sysList.add(s);
                try {
                    Pair e = new Pair();
                    new WDSDS().propagate(e, listWD);
                    s.pairs.add(e);
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                }
            } else {
                try {
                    Pair e = new Pair();
                    new WDSDS().propagate(e, listWD);
                    sysList.get(sysNumber).pairs.add(e);
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                }
            }
        }
    }

    public static void interprSCO() {
        int current = listSCO.size();
        String[] criteria = {KeysDictionary.OBSERVER, KeysDictionary.WDSSYSTEM, KeysDictionary.WDSPAIR};
        listSCO = (ArrayList<NodeORB6FINALIZED>) MatchingByIDRuleImplementation.multipleIdCriteria(criteria, listSCO, new ORB6DS());
        String[] criteria2 = {KeysDictionary.OBSERVER};
        listSCO = (ArrayList<NodeORB6FINALIZED>) MatchingByIDRuleImplementation.multipleIdCriteria(criteria2, listSCO, new ORB6DS());
        listSCO = (ArrayList<NodeORB6FINALIZED>) MatchingBySystemIDRuleImplementation.resolve(KeysDictionary.WDSSYSTEM, listSCO, new ORB6DS());
        GlobalPoolOfIdentifiers.resolvedSCO += current - listSCO.size();
        listSCO = (ArrayList<NodeORB6FINALIZED>) MatchingByTimeZoneRuleImplementation.resolve(KeysDictionary.WDSSYSTEM, listSCO, new ORB6DS());
    }

    public static void interprCCDM() {
        GlobalPoolOfIdentifiers.totalCCDM += listCCDMPairs.size();
        listCCDMPairs = (ArrayList<NodeCCDM>) MatchingByAnglesImplementation.resolve(listCCDMPairs, new CCDMDS());
        listCCDMPairs = (ArrayList<NodeCCDM>) MatchingByAnglesImplementation.resolveOnlyByRho(listCCDMPairs, new CCDMDS());
        listCCDMPairs = (ArrayList<NodeCCDM>) MatchingByCoordinatesRuleImplementation.resolve(listCCDMPairs, new CCDMDS());
        GlobalPoolOfIdentifiers.unresolvedCCDM += listCCDMPairs.size();
        listCCDMPairs = (ArrayList<NodeCCDM>) MatchingBySystemIDRuleImplementation.resolve(KeysDictionary.CCDMSYSTEM, listCCDMPairs, new CCDMDS());
        listCCDMPairs = (ArrayList<NodeCCDM>) MatchingBySystemIDRuleImplementation.resolve(KeysDictionary.CCDMSYSTEM, listCCDMPairs, new CCDMDS());
        listCCDMPairs = (ArrayList<NodeCCDM>) MatchingByTimeZoneRuleImplementation.resolve(KeysDictionary.CCDMSYSTEM, listCCDMPairs, new CCDMDS());
    }

    public static void interprTDSC() {
        GlobalPoolOfIdentifiers.totalTDSC += listTDSC.size();
        listTDSC = (ArrayList<NodeTDSC>) MatchingByAnglesImplementation.resolve(listTDSC, new TDSCDS());
        listTDSC = (ArrayList<NodeTDSC>) MatchingByAnglesImplementation.resolveOnlyByRho(listTDSC, new TDSCDS());
        listTDSC = (ArrayList<NodeTDSC>) MatchingByCoordinatesRuleImplementation.resolve(listTDSC, new TDSCDS());
        GlobalPoolOfIdentifiers.unresolvedTDSC += listTDSC.size();
    }

    public static void interprINT4() {
        GlobalPoolOfIdentifiers.totalINT4 += listINT4.size();
        listINT4 = (ArrayList<NodeINT4>) MatchingByAnglesImplementation.resolve(listINT4, new INT4DS());
        listINT4 = (ArrayList<NodeINT4>) MatchingByAnglesImplementation.resolveOnlyByRho(listINT4, new INT4DS());
        GlobalPoolOfIdentifiers.unresolvedINT4 += listINT4.size();
        listINT4 = (ArrayList<NodeINT4>) MatchingBySystemIDRuleImplementation.resolve(KeysDictionary.WDSSYSTEM, listINT4, new INT4DS());
        listINT4 = (ArrayList<NodeINT4>) MatchingByTimeZoneRuleImplementation.resolve(KeysDictionary.WDSSYSTEM, listINT4, new INT4DS());
    }

    public static void interprCEV() {
        int current = listCEV.size();

        System.err.println("current: " + current);
        listCEV = (ArrayList<NodeCEV>) MatchingByAnglesImplementation.resolveOnlyByRho(listCEV, new CEVDS());
        System.err.println("afterFirst: " + listCEV.size());
        listCEV = (ArrayList<NodeCEV>) MatchingByCoordRuleForComponentsImplementation.resolve(listCEV, new CEVDS());
        if (listCEV.size() != current) {
            resolvedCEV += current - listCEV.size();
        }
        System.err.println("afterSecond: " + listCEV.size());
        listCEV = (ArrayList<NodeCEV>) MatchingByTimeZoneRuleImplementation.resolve(KeysDictionary.COORD_I1_1, listCEV, new CEVDS());
        System.err.println("total: " + listCEV.size());
    }

    public static void interprSB9() {
        int current = listSB9.size();

        System.err.println("current: " + current);
        listSB9 = (ArrayList<NodeSB9>) MatchingByAnglesImplementation.resolve(listSB9, new SB9DS());
        System.err.println("afterFirst: " + listSB9.size());
        listSB9 = (ArrayList<NodeSB9>) MatchingByCoordRuleForComponentsImplementation.resolve(listSB9, new SB9DS());
        System.err.println("afterSecond: " + listSB9.size());
        listSB9 = (ArrayList<NodeSB9>) MatchingBySystemIDRuleImplementation.resolve(KeysDictionary.WDSSYSTEM, listSB9, new SB9DS());
        System.err.println("afterThird: " + listSB9.size());
        if (listSB9.size() != current) {
            resolvedSB9 += current - listSB9.size();
        }
        listSB9 = (ArrayList<NodeSB9>) MatchingByTimeZoneRuleImplementation.resolve(KeysDictionary.COORD_I1_1, listSB9, new SB9DS());
        System.err.println("total: " + listSB9.size());
    }

    public static void interprLMX() {
        int current = listLMX.size();
        listLMX = (ArrayList<NodeXR>) MatchingByCoordRuleForComponentsImplementation.resolve(listLMX, new XRDS());
        if (listLMX.size() != current) {
            resolvedLMX += current - listLMX.size();
            for (int i = 0; i < 100; i++) {
                System.err.println("TOUCH!" + resolvedLMX);
            }
        }
        listLMX = (ArrayList<NodeXR>) MatchingByTimeZoneRuleImplementation.resolve(KeysDictionary.COORD_I1_1, listLMX, new XRDS());
    }
}
