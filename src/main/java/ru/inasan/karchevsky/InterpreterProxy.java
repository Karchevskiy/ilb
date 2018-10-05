package ru.inasan.karchevsky;

import ru.inasan.karchevsky.catalogues.bincep.BINCEPDS;
import ru.inasan.karchevsky.catalogues.bincep.NodeBinCep;
import ru.inasan.karchevsky.catalogues.ccdm.CCDMDS;
import ru.inasan.karchevsky.catalogues.ccdm.NodeCCDM;
import ru.inasan.karchevsky.catalogues.cev.CEVDS;
import ru.inasan.karchevsky.catalogues.cev.NodeCEV;
import ru.inasan.karchevsky.catalogues.int4.INT4DS;
import ru.inasan.karchevsky.catalogues.int4.NodeINT4;
import ru.inasan.karchevsky.catalogues.orb6.NodeORB6;
import ru.inasan.karchevsky.catalogues.orb6.ORB6DS;
import ru.inasan.karchevsky.catalogues.sb9.NodeSB9;
import ru.inasan.karchevsky.catalogues.sb9.SB9DS;
import ru.inasan.karchevsky.catalogues.tdsc.DatasourceTDSC;
import ru.inasan.karchevsky.catalogues.tdsc.NodeTDSC;
import ru.inasan.karchevsky.catalogues.wds.DatasourceWDS;
import ru.inasan.karchevsky.catalogues.wds.NodeWDS;
import ru.inasan.karchevsky.catalogues.xr.NodeXR;
import ru.inasan.karchevsky.catalogues.xr.DatasourceXR;
import ru.inasan.karchevsky.configuration.KeysDictionary;
import ru.inasan.karchevsky.storage.CachedStorageILB;
import ru.inasan.karchevsky.lib.model.Pair;
import ru.inasan.karchevsky.lib.model.StarSystem;
import ru.inasan.karchevsky.lib.storage.GlobalPoolOfIdentifiers;
import ru.inasan.karchevsky.lib.tools.resolvingRulesImplementation.*;
import ru.inasan.karchevsky.lib.tools.resolvingRulesImplementation.pairToCompRules.MatchingByCoordRuleForComponentsImplementation;

import java.util.ArrayList;

import static ru.inasan.karchevsky.lib.storage.GlobalPoolOfIdentifiers.resolvedCEV;
import static ru.inasan.karchevsky.lib.storage.GlobalPoolOfIdentifiers.resolvedSB9;
import static ru.inasan.karchevsky.lib.storage.GlobalPoolOfIdentifiers.resolvedLMX;

@SuppressWarnings("unchecked")
public class InterpreterProxy extends CachedStorageILB {

    public void interprWDS() {
        //int xLog=0;
        //long timePrev=System.nanoTime();
        for (NodeWDS listWD : listWDS) {
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
                    new DatasourceWDS().propagate(e, listWD);
                    s.pairs.add(e);
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                }
            } else {
                try {
                    Pair e = new Pair();
                    new DatasourceWDS().propagate(e, listWD);
                    sysList.get(sysNumber).pairs.add(e);
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                }
            }
        }
    }

    public void interprSCO() {
        int current = listSCO.size();
        String[] criteria = {KeysDictionary.OBSERVER, KeysDictionary.WDSSYSTEM, KeysDictionary.WDSPAIR};
        listSCO = (ArrayList<NodeORB6>) MatchingByIDRuleImplementation.multipleIdCriteria(criteria, listSCO, new ORB6DS(), this);
        String[] criteria2 = {KeysDictionary.OBSERVER};
        listSCO = (ArrayList<NodeORB6>) MatchingByIDRuleImplementation.multipleIdCriteria(criteria2, listSCO, new ORB6DS(), this);
        listSCO = (ArrayList<NodeORB6>) MatchingBySystemIDRuleImplementation.resolve(KeysDictionary.WDSSYSTEM, listSCO, new ORB6DS(), this);
        GlobalPoolOfIdentifiers.resolvedSCO += current - listSCO.size();
        listSCO = (ArrayList<NodeORB6>) MatchingByTimeZoneRuleImplementation.resolve(KeysDictionary.WDSSYSTEM, listSCO, new ORB6DS(), this);
    }

    public void interprCCDM() {
        GlobalPoolOfIdentifiers.totalCCDM += listCCDMPairs.size();
        listCCDMPairs = (ArrayList<NodeCCDM>) MatchingByAnglesImplementation.resolve(listCCDMPairs, new CCDMDS(), this);
        listCCDMPairs = (ArrayList<NodeCCDM>) MatchingByAnglesImplementation.resolveOnlyByRho(listCCDMPairs, new CCDMDS(), this);
        listCCDMPairs = (ArrayList<NodeCCDM>) MatchingByCoordinatesRuleImplementation.resolve(listCCDMPairs, new CCDMDS(), this);
        GlobalPoolOfIdentifiers.unresolvedCCDM += listCCDMPairs.size();
        listCCDMPairs = (ArrayList<NodeCCDM>) MatchingBySystemIDRuleImplementation.resolve(KeysDictionary.CCDMSYSTEM, listCCDMPairs, new CCDMDS(), this);
        listCCDMPairs = (ArrayList<NodeCCDM>) MatchingBySystemIDRuleImplementation.resolve(KeysDictionary.CCDMSYSTEM, listCCDMPairs, new CCDMDS(), this);
        listCCDMPairs = (ArrayList<NodeCCDM>) MatchingByTimeZoneRuleImplementation.resolve(KeysDictionary.CCDMSYSTEM, listCCDMPairs, new CCDMDS(), this);
    }

    public void interprTDSC() {
        GlobalPoolOfIdentifiers.totalTDSC += listTDSC.size();
        listTDSC = (ArrayList<NodeTDSC>) MatchingByAnglesImplementation.resolve(listTDSC, new DatasourceTDSC(), this);
        listTDSC = (ArrayList<NodeTDSC>) MatchingByAnglesImplementation.resolveOnlyByRho(listTDSC, new DatasourceTDSC(), this);
        listTDSC = (ArrayList<NodeTDSC>) MatchingByCoordinatesRuleImplementation.resolve(listTDSC, new DatasourceTDSC(), this);
        GlobalPoolOfIdentifiers.unresolvedTDSC += listTDSC.size();
    }

    public void interprINT4() {
        GlobalPoolOfIdentifiers.totalINT4 += listINT4.size();
        listINT4 = (ArrayList<NodeINT4>) MatchingByAnglesImplementation.resolve(listINT4, new INT4DS(), this);
        listINT4 = (ArrayList<NodeINT4>) MatchingByAnglesImplementation.resolveOnlyByRho(listINT4, new INT4DS(), this);
        GlobalPoolOfIdentifiers.unresolvedINT4 += listINT4.size();
        listINT4 = (ArrayList<NodeINT4>) MatchingBySystemIDRuleImplementation.resolve(KeysDictionary.WDSSYSTEM, listINT4, new INT4DS(), this);
        listINT4 = (ArrayList<NodeINT4>) MatchingByTimeZoneRuleImplementation.resolve(KeysDictionary.WDSSYSTEM, listINT4, new INT4DS(), this);
    }

    public void interprCEV() {
        int current = listCEV.size();

        System.err.println("current: " + current);
        listCEV = (ArrayList<NodeCEV>) MatchingByAnglesImplementation.resolveOnlyByRho(listCEV, new CEVDS(), this);
        System.err.println("afterFirst: " + listCEV.size());
        listCEV = (ArrayList<NodeCEV>) MatchingByCoordRuleForComponentsImplementation.resolve(listCEV, new CEVDS(), this);
        if (listCEV.size() != current) {
            resolvedCEV += current - listCEV.size();
        }
        System.err.println("afterSecond: " + listCEV.size());
        listCEV = (ArrayList<NodeCEV>) MatchingByTimeZoneRuleImplementation.resolve(KeysDictionary.COORD_I1_1, listCEV, new CEVDS(), this);
        System.err.println("total: " + listCEV.size());
    }

    public void interprBinCep() {
        int current = listBinCep.size();

        System.err.println("current listBinCep: " + current);
        listBinCep = (ArrayList<NodeBinCep>) MatchingByAnglesImplementation.resolveOnlyByRho(listBinCep, new BINCEPDS(), this);
        System.err.println("afterFirst: " + listBinCep.size());
        listBinCep = (ArrayList<NodeBinCep>) MatchingByCoordRuleForComponentsImplementation.resolve(listBinCep, new BINCEPDS(), this);
        if (listBinCep.size() != current) {
            resolvedCEV += current - listBinCep.size();
        }
        System.err.println("afterSecond: " + listBinCep.size());
        listBinCep = (ArrayList<NodeBinCep>) MatchingByTimeZoneRuleImplementation.resolve(KeysDictionary.COORD_I1_1, listBinCep, new BINCEPDS(), this);
        System.err.println("total listBinCep: " + listBinCep.size());
    }

    public void interprSB9() {
        int current = listSB9.size();

        System.err.println("current: " + current);
        listSB9 = (ArrayList<NodeSB9>) MatchingByAnglesImplementation.resolve(listSB9, new SB9DS(), this);
        System.err.println("afterFirst: " + listSB9.size());
        listSB9 = (ArrayList<NodeSB9>) MatchingByCoordRuleForComponentsImplementation.resolve(listSB9, new SB9DS(), this);
        System.err.println("afterSecond: " + listSB9.size());
        listSB9 = (ArrayList<NodeSB9>) MatchingBySystemIDRuleImplementation.resolve(KeysDictionary.WDSSYSTEM, listSB9, new SB9DS(), this);
        System.err.println("afterThird: " + listSB9.size());
        if (listSB9.size() != current) {
            resolvedSB9 += current - listSB9.size();
        }
        listSB9 = (ArrayList<NodeSB9>) MatchingByTimeZoneRuleImplementation.resolve(KeysDictionary.COORD_I1_1, listSB9, new SB9DS(), this);
        System.err.println("total: " + listSB9.size());
    }

    public void interprLMX() {
        int current = listLMX.size();
        listLMX = (ArrayList<NodeXR>) MatchingByCoordRuleForComponentsImplementation.resolve(listLMX, new DatasourceXR(), this);
        if (listLMX.size() != current) {
            resolvedLMX += current - listLMX.size();
            for (int i = 0; i < 10; i++) {
                System.err.println("TOUCH!" + resolvedLMX);
            }
        }
        listLMX = (ArrayList<NodeXR>) MatchingByTimeZoneRuleImplementation.resolve(KeysDictionary.COORD_I1_1, listLMX, new DatasourceXR(), this);
    }
}
