package ILBprocessing.storage;

import ILBprocessing.beans.*;
import lib.storage.CachedStorage;

import java.util.ArrayList;


public class CachedStorageILB extends CachedStorage {
    public static ArrayList<NodeWDSFINALIZED> listWDS = new ArrayList<>();
    public static ArrayList<NodeORB6FINALIZED> listSCO = new ArrayList<>();
    public static ArrayList<NodeCCDM> listCCDMPairs = new ArrayList<>();
    public static ArrayList<NodeINT4> listINT4 = new ArrayList<>();
    public static ArrayList<NodeTDSC> listTDSC = new ArrayList<>();
    public static ArrayList<NodeSB9> listSB9 = new ArrayList<>();
    public static ArrayList<NodeCEV> listCEV = new ArrayList<>();
}
