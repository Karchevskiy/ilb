package ru.inasan.karchevsky.storage;

import ru.inasan.karchevsky.catalogues.bincep.NodeBinCep;
import ru.inasan.karchevsky.catalogues.ccdm.NodeCCDM;
import ru.inasan.karchevsky.catalogues.ccdm.NodeCCDMAstrometric;
import ru.inasan.karchevsky.catalogues.cev.NodeCEV;
import ru.inasan.karchevsky.catalogues.int4.NodeINT4;
import ru.inasan.karchevsky.catalogues.orb6.NodeORB6;
import ru.inasan.karchevsky.catalogues.sb9.NodeSB9;
import ru.inasan.karchevsky.catalogues.sb9.NodeSB9Params;
import ru.inasan.karchevsky.catalogues.tdsc.NodeTDSC;
import ru.inasan.karchevsky.catalogues.wds.NodeWDS;
import ru.inasan.karchevsky.catalogues.xr.NodeXR;
import ru.inasan.karchevsky.lib.storage.CachedStorage;

import java.util.ArrayList;

public class CachedStorageILB extends CachedStorage {
    protected ArrayList<NodeWDS> listWDS = new ArrayList<>();
    protected ArrayList<NodeORB6> listSCO = new ArrayList<>();
    protected ArrayList<NodeCCDM> listCCDMPairs = new ArrayList<>();
    protected ArrayList<NodeCCDMAstrometric> listCCDMAstrometricPairs = new ArrayList<>();
    protected ArrayList<NodeINT4> listINT4 = new ArrayList<>();
    protected ArrayList<NodeTDSC> listTDSC = new ArrayList<>();
    protected ArrayList<NodeSB9> listSB9 = new ArrayList<>();
    protected ArrayList<NodeXR> listLMX = new ArrayList<>();
    protected ArrayList<NodeCEV> listCEV = new ArrayList<>();
    protected ArrayList<NodeBinCep> listBinCep = new ArrayList<>();

    public ArrayList<NodeWDS> getListWDS() {
        return listWDS;
    }

    public void setListWDS(ArrayList<NodeWDS> listWDS) {
        this.listWDS = listWDS;
    }

    public ArrayList<NodeORB6> getListSCO() {
        return listSCO;
    }

    public void setListSCO(ArrayList<NodeORB6> listSCO) {
        this.listSCO = listSCO;
    }

    public ArrayList<NodeCCDM> getListCCDMPairs() {
        return listCCDMPairs;
    }

    public void setListCCDMPairs(ArrayList<NodeCCDM> listCCDMPairs) {
        this.listCCDMPairs = listCCDMPairs;
    }

    public ArrayList<NodeCCDMAstrometric> getListCCDMAstrometricPairs() {
        return listCCDMAstrometricPairs;
    }

    public void setListCCDMAstrometricPairs(ArrayList<NodeCCDMAstrometric> listCCDMAstrometricPairs) {
        this.listCCDMAstrometricPairs = listCCDMAstrometricPairs;
    }

    public ArrayList<NodeINT4> getListINT4() {
        return listINT4;
    }

    public void setListINT4(ArrayList<NodeINT4> listINT4) {
        this.listINT4 = listINT4;
    }

    public ArrayList<NodeTDSC> getListTDSC() {
        return listTDSC;
    }

    public void setListTDSC(ArrayList<NodeTDSC> listTDSC) {
        this.listTDSC = listTDSC;
    }

    public ArrayList<NodeSB9> getListSB9() {
        return listSB9;
    }

    public void setListSB9(ArrayList<NodeSB9> listSB9) {
        this.listSB9 = listSB9;
    }

    public ArrayList<NodeXR> getListLMX() {
        return listLMX;
    }

    public void setListLMX(ArrayList<NodeXR> listLMX) {
        this.listLMX = listLMX;
    }

    public ArrayList<NodeCEV> getListCEV() {
        return listCEV;
    }

    public void setListCEV(ArrayList<NodeCEV> listCEV) {
        this.listCEV = listCEV;
    }

    public ArrayList<NodeBinCep> getListBinCep() {
        return listBinCep;
    }

    public void setListBinCep(ArrayList<NodeBinCep> listBinCep) {
        this.listBinCep = listBinCep;
    }
}
