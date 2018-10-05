package ru.inasan.karchevsky.storage;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.inasan.karchevsky.catalogues.bincep.NodeBinCep;
import ru.inasan.karchevsky.catalogues.ccdm.NodeCCDM;
import ru.inasan.karchevsky.catalogues.cev.NodeCEV;
import ru.inasan.karchevsky.catalogues.int4.NodeINT4;
import ru.inasan.karchevsky.catalogues.orb6.NodeORB6;
import ru.inasan.karchevsky.catalogues.sb9.NodeSB9;
import ru.inasan.karchevsky.catalogues.tdsc.NodeTDSC;
import ru.inasan.karchevsky.catalogues.wds.NodeWDS;
import ru.inasan.karchevsky.catalogues.xr.NodeXR;
import ru.inasan.karchevsky.lib.storage.CachedStorage;

import java.util.ArrayList;

@Getter
@Setter
@NoArgsConstructor
public class CachedStorageILB extends CachedStorage {
    protected ArrayList<NodeWDS> listWDS = new ArrayList<>();
    protected ArrayList<NodeORB6> listSCO = new ArrayList<>();
    protected ArrayList<NodeCCDM> listCCDMPairs = new ArrayList<>();
    protected ArrayList<NodeINT4> listINT4 = new ArrayList<>();
    protected ArrayList<NodeTDSC> listTDSC = new ArrayList<>();
    protected ArrayList<NodeSB9> listSB9 = new ArrayList<>();
    protected ArrayList<NodeXR> listLMX = new ArrayList<>();
    protected ArrayList<NodeCEV> listCEV = new ArrayList<>();
    protected ArrayList<NodeBinCep> listBinCep = new ArrayList<>();
}
