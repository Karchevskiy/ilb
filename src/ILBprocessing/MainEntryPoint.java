package ILBprocessing;

import ILBprocessing.beans.NodeCCDM;
import ILBprocessing.beans.NodeINT4;
import ILBprocessing.beans.NodeTDSC;
import ILBprocessing.beans.NodeWDSFINALIZED;
import ILBprocessing.configuration.KeysDictionary;
import ILBprocessing.configuration.SharedConstants;
import ILBprocessing.storage.CachedStorageILB;
import lib.model.Pair;
import lib.model.StarSystem;
import lib.pattern.SplitRule;
import lib.service.BigFilesSplitterByHours;
import lib.storage.GlobalPoolOfIdentifiers;
import lib.tools.namingRulesImplementation.SysTreeNamesGenerator;
import lib.tools.resolvingRulesImplementation.pairToPairRules.PairIsComponentOfOtherRuleImplementation;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;

import static ILBprocessing.storage.CachedStorageILB.listINT4;

public class MainEntryPoint implements SharedConstants{
    public static CachedStorageILB storage = new CachedStorageILB();
    public static ArrayList<String> errors= new ArrayList<String>();

    public static void main(String[] args) {
        preProcessing();
        processing();
        postProcessing();
    }
    public static void preProcessing(){
        System.out.println("PHASE 1: SMALL CACHE GENERATION STARTED");
        if(LOGGING_LEVEL_VERBOSE_ENABLED) {
            System.setOut(new PrintStream(new OutputStream() {

                @Override
                public void write(int arg0) throws IOException {

                }
            }));
        }
        //split large files on small (current algorithm complexity O(n^3))
        split();
        //adding in cache small catalogues
        CatalogueParser.parseSCO();
        CatalogueParser.parseCEV();
        CatalogueParser.parseSB9();
        CatalogueParser.parseSB9params();

        System.out.println("PHASE 1: SUCCESS");
    }
    public static void processing(){
        System.out.println("");
        System.out.println("PHASE 2: PROCESSING STARTED");
        for (int i = 0; i < 24; i++) {
            for (int j = 0; j < 6; j++) {
                System.out.println("    zone " + i + "h" + j + "(" + (i * 6 + j) + " from 144 (each zone - 10min))");
                clearCache();
                solve("" + i + j);
                analyse();
            }
        }
        System.out.println("PHASE 2: SUCCESS");
    }
    public static void postProcessing(){
        System.out.println("");
        System.out.println("PHASE 3: VALIDATION STARTED");
        // BigFilesSplitterByHours.concatenator();
        System.err.println("fail on matching SCO nodes: "+storage.listSCO.size());
        for(int i=0;i<storage.listSCO.size();i++){
            System.err.println(""+storage.listSCO.get(i).source);
        }
        System.err.println("fail on matching CEV nodes: "+storage.listCEV.size());
        for(int i=0;i<storage.listCEV.size();i++){
            System.err.println(""+storage.listCEV.get(i).source);
        }
        System.err.println("fail on matching SB9 nodes: "+storage.listSB9.size());
        for(int i=0;i<storage.listSB9.size();i++){
            System.err.println(""+storage.listSB9.get(i).source);
        }
        System.err.println("fail on matching CCDM nodes: "+ GlobalPoolOfIdentifiers.unresolvedCCDM+"/"+GlobalPoolOfIdentifiers.totalCCDM+ " "+1.0*GlobalPoolOfIdentifiers.unresolvedCCDM/GlobalPoolOfIdentifiers.totalCCDM*100+"%");
        System.err.println("fail on matching TDSC nodes: "+ GlobalPoolOfIdentifiers.unresolvedTDSC+"/"+GlobalPoolOfIdentifiers.totalTDSC+ " "+1.0*GlobalPoolOfIdentifiers.unresolvedTDSC/GlobalPoolOfIdentifiers.totalTDSC*100+"%");
        System.err.println("fail on matching INT4 nodes: "+ GlobalPoolOfIdentifiers.unresolvedINT4+"/"+GlobalPoolOfIdentifiers.totalINT4+ " "+1.0*GlobalPoolOfIdentifiers.unresolvedINT4/GlobalPoolOfIdentifiers.totalINT4*100+"%");

        System.err.println("success on matching SB9 nodes: "+ GlobalPoolOfIdentifiers.resolvedSB9);
        System.err.println("success on matching CEV nodes: "+ GlobalPoolOfIdentifiers.resolvedCEV);
        System.err.println("success on matching SCO nodes: "+ GlobalPoolOfIdentifiers.resolvedSCO);

        GlobalPoolOfIdentifiers.validateDuplicates();
        GlobalPoolOfIdentifiers.printCount();
        BigFilesSplitterByHours.concatenator();
    }

    public static void split(){
        System.out.println("  Splitting large files on 10min-zones");
        if(WDS_SPLIT_BEFORE_PROCESSING) {
            System.out.println("  WDS split-flag:TRUE");
            SplitRule splitRule = (string, hour, decade) -> {
                if(Integer.parseInt("" + string.charAt(0) + string.charAt(1) + string.charAt(2)) == hour * 10 + decade){
                    return true;
                }
                return false;
            };
            BigFilesSplitterByHours.splitLargeFile(WDS_SOURCE_FILES,WDS_GENERATED_STUFF,splitRule);
            System.out.println("  WDS splitting success");
        }else{
            System.out.println("  WDS split-flag:FALSE");
        }
        if(CCDM_SPLIT_BEFORE_PROCESSING) {
            System.out.println("  CCDM split-flag:TRUE");
            SplitRule splitRule = (string, hour, decade) -> {
                if(Integer.parseInt("" + string.charAt(1) + string.charAt(2) + string.charAt(3)) == hour * 10 + decade){
                    return true;
                }
                return false;
            };
            BigFilesSplitterByHours.splitLargeFile(CCDM_SOURCE_FILES,CCDM_GENERATED_STUFF,splitRule);
            System.out.println("  CCDM splitting SUCCESS");
        }else{
            System.out.println("  CCDM split-flag:FALSE");
        }
        if(TDSC_SPLIT_BEFORE_PROCESSING) {
            System.out.println("  TDSC split-flag:TRUE");
            SplitRule splitRule = (string, hour, decade) -> {
                try {
                    String[] cache = string.split("\\|");
                    if ((int)(Double.parseDouble(cache[4])/15)==hour && decade==(int)(Double.parseDouble(cache[4])%15*6/15)) {
                        //System.err.println(""+cache[4]+" "+(int)(Double.parseDouble(cache[4])%15*6/15));
                        return true;
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }
                return false;
            };
            BigFilesSplitterByHours.splitLargeFile(TDSC_SOURCE_FILES,TDSC_GENERATED_STUFF,splitRule);
            System.out.println("  TDSC splitting SUCCESS");
        }else{
            System.out.println("  TDSC split-flag:FALSE");
        }
        if(INT4_SPLIT_BEFORE_PROCESSING) {
            System.out.println("  INT4 split-flag:TRUE");
            SplitRule splitRule = (string, hour, decade) -> {
                try {
                    if (string.length()>2 && string.charAt(0)!=' ' && Integer.parseInt("" + string.charAt(0) + string.charAt(1) + string.charAt(2)) == hour * 10 + decade) {
                        return true;
                    }
                }catch(Exception e){
                    System.err.print("ERR p1c101: unresolved line in INT4: "+string);
                }
                return false;
            };
            BigFilesSplitterByHours.splitLargeFile(INT4_SOURCE_FILES,INT4_GENERATED_STUFF,splitRule);
            System.out.println("  INT4 splitting SUCCESS");
        }else{
            System.out.println("  INT4 split-flag:FALSE");
        }
    }
    public static void clearCache() {
        storage.listWDS = new ArrayList<NodeWDSFINALIZED>();
        storage.sysList = new ArrayList<StarSystem>();
        storage.listCCDMPairs = new ArrayList<NodeCCDM>();
        storage.listTDSC = new ArrayList<NodeTDSC>();
        listINT4 = new ArrayList<NodeINT4>();
    }

    public static void solve(String i) {
        CatalogueParser.parseFile(i);
        CatalogueParser.parseCCDM(i);
        CatalogueParser.parseTDSC(i);
        CatalogueParser.parseINT4(i);

        InterpreterProxy.interprWDS();
        InterpreterProxy.interprCCDM();
        InterpreterProxy.interprTDSC();
        InterpreterProxy.interprINT4();

        InterpreterProxy.interprSB9();
        InterpreterProxy.interprSCO();
        InterpreterProxy.interprCEV();
        SysTreeNamesGenerator.generateNames();
        PairIsComponentOfOtherRuleImplementation.rebuildTree();

        SysTreeNamesGenerator.generateILBSystemNames();
        CustomWriter.writeAllCachedData(i);
        for(String s:errors){
            System.err.println(s);
        }
        System.err.println(errors.size());
    }
    public static void analyse(){
        for(StarSystem system: storage.sysList){
            l1: for(Pair pair: system.pairs){
                ArrayList<String> nameWDS = pair.el1.getParamsForByKey(KeysDictionary.WDSCOMP);
                ArrayList<String> nameCCDM = pair.el1.getParamsForByKey(KeysDictionary.CCDMCOMP);
                for(String name1: nameWDS){
                    for(String name2: nameCCDM){
                        if(!name1.equals(name2) && !name1.equals("") && !name2.equals("")){
                            errors.add(system.ILBId+" : "+pair.el1.getParamsForByKey(KeysDictionary.WDSPAIR).get(0)+name1+" vs " + pair.el1.getParamsForByKey(KeysDictionary.CCDMPAIR).get(0)+name2);
                            System.err.println(errors.size()+" "+system.params.get(KeysDictionary.WDSSYSTEM)+" : "+pair.getParamsForByKey(KeysDictionary.WDSPAIR).get(0)+name1+" vs " + pair.getParamsForByKey(KeysDictionary.CCDMPAIR).get(0)+name2);
                            break l1;
                        }
                    }
                }
            }
            l2: for(Pair pair: system.pairs){
                ArrayList<String> nameWDS = pair.el2.getParamsForByKey(KeysDictionary.WDSCOMP);
                ArrayList<String> nameCCDM = pair.el2.getParamsForByKey(KeysDictionary.CCDMCOMP);
                for(String name1: nameWDS){
                    for(String name2: nameCCDM){
                        if(!name1.equals(name2) && !name1.equals("") && !name2.equals("")){
                            errors.add(system.ILBId+" : "+pair.el2.getParamsForByKey(KeysDictionary.WDSPAIR).get(0)+name1+" vs " + pair.el2.getParamsForByKey(KeysDictionary.CCDMPAIR).get(0)+name2);
                            System.err.println(errors.size()+" "+system.params.get(KeysDictionary.WDSSYSTEM)+" : "+pair.getParamsForByKey(KeysDictionary.WDSPAIR).get(0)+name1+" vs " + pair.getParamsForByKey(KeysDictionary.CCDMPAIR).get(0)+name2);
                            break l2;
                        }
                    }
                }
            }
        }
    }
}