package ILBprocessing;

import ILBprocessing.beans.*;
import ILBprocessing.beans.helpers.CCDMHelperComponent;
import ILBprocessing.beans.helpers.NodeSB9Params;
import ILBprocessing.beans.helpers.TDSCHelperComponent;
import ILBprocessing.configuration.SharedConstants;
import ILBprocessing.storage.CachedStorageILB;

import javax.management.InvalidAttributeValueException;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;


public class CatalogueParser implements SharedConstants {
    public static void parseFile(String xxx){
        try {
            String[] fileName={"WDS"+xxx+".txt"};//all zones;
            for(int h=0;h<fileName.length;h++){
                if(LOGGING_LEVEL_VERBOSE_ENABLED)System.out.println("Stage:"+h);
                File dataFile = new File(OUTPUT_FOLDER+WDS_GENERATED_STUFF+"/"+fileName[h]);
                FileReader in = new FileReader(dataFile);
                char c;
                StringBuffer ss = new StringBuffer();
                long d=dataFile.length();
                for(long i=0;i<d;i++){
                    c = (char) in.read();
                    ss.append(c);
                    if(c==10){
                        String s=ss.toString();
                        if(s.length()>10){
                            s=s.substring(0, s.length()-2);
                            NodeWDSFINALIZED star = new NodeWDSFINALIZED(s);
                            CachedStorageILB.listWDS.add(star);
                            ss = new StringBuffer();
                        }else{
                            ss = new StringBuffer();
                        }
                    }
                    if(d-i<1){
                        String s=ss.toString();
                        NodeWDSFINALIZED star = new NodeWDSFINALIZED(s);
                        CachedStorageILB.listWDS.add(star);
                        ss = new StringBuffer();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void parseSCO(){
        System.out.println("       parse SCO");
        try {
            File dataFile = new File(INPUT_FOLDER+SCO_SOURCE_FILE);
            FileReader in = new FileReader(dataFile);
            char c;
            StringBuffer ss = new StringBuffer();
            long d = dataFile.length();
            for(long i=0;i<d;i++){
                c = (char) in.read();
                ss.append(c);
                if(c==10){
                    String s=ss.toString();
                    s=s.substring(0, s.length()-2);
                    NodeORB6FINALIZED star = new NodeORB6FINALIZED(s);
                    CachedStorageILB.listSCO.add(star);
                    ss = new StringBuffer();
                }
            }
            if(LOGGING_LEVEL_VERBOSE_ENABLED)System.out.println("       Success. fileLength="+dataFile.length());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void parseCCDM(String xxx){
        try {
            String[] fileName={"CCDM"+xxx+".txt"};
            File dataFile = new File(OUTPUT_FOLDER+CCDM_GENERATED_STUFF+"/"+fileName[0]);
            FileReader in = new FileReader(dataFile);
            char c;
            long d = dataFile.length();
            StringBuffer ss = new StringBuffer();
            ArrayList<CCDMHelperComponent> listCCDMComponents = new ArrayList<CCDMHelperComponent>();
            String currentSystem="initial";
            for(long i=0;i<d;i++){
                c = (char) in.read();
                ss.append(c);
                if(c==10){
                    String s=ss.toString();
                    if(s.length()>1){
                        try {
                            CCDMHelperComponent star = new CCDMHelperComponent(s);
                            if(currentSystem.equals(star.ccdmID)) {
                                listCCDMComponents.add(star);
                            }else{
                                if(!currentSystem.equals("initial")) {
                                    CCDMHelperComponent.translateToPairs(listCCDMComponents,CachedStorageILB.listCCDMPairs);
                                }
                                listCCDMComponents = new ArrayList<CCDMHelperComponent>();
                                listCCDMComponents.add(star);
                                currentSystem=star.ccdmID;
                            }
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                    ss = new StringBuffer();
                }
            }
            CCDMHelperComponent.translateToPairs(listCCDMComponents,CachedStorageILB.listCCDMPairs);
            if(LOGGING_LEVEL_VERBOSE_ENABLED)System.out.println("       Success. fileLength="+dataFile.length());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void parseTDSC(String xxx){
        try {
            String[] fileName={"TDSC"+xxx+".txt"};//all zones;
            File dataFile = new File(OUTPUT_FOLDER+TDSC_GENERATED_STUFF+"/"+fileName[0]);
            FileReader in = new FileReader(dataFile);
            char c;
            StringBuffer ss = new StringBuffer();
            ArrayList<TDSCHelperComponent> listTDSCComponents = new ArrayList<TDSCHelperComponent>();
            String currentSystem="initial";
            long d=dataFile.length();
            for(long i=0;i<d;i++){
                c = (char) in.read();
                ss.append(c);
                if(c==10){
                    String s=ss.toString();
                    if(s.length()>1){
                        try {
                            TDSCHelperComponent star = new TDSCHelperComponent(s);
                            if(currentSystem.equals(star.tdscID)) {
                                listTDSCComponents.add(star);
                            }else{
                                if(!currentSystem.equals("initial")) {
                                    TDSCHelperComponent.translateToPairs(listTDSCComponents,CachedStorageILB.listTDSC);
                                }
                                listTDSCComponents = new ArrayList<TDSCHelperComponent>();
                                listTDSCComponents.add(star);
                                currentSystem=star.tdscID;
                            }
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                    ss = new StringBuffer();
                }
                /*if(d-i<1){
                    String s=ss.toString();
                    TDSCHelperComponent star = new TDSCHelperComponent(s);
                    CachedStorageILB.listTDSC.add(star);
                    ss = new StringBuffer();
                }*/
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void parseINT4(String xxx){
        try {
            String[] fileName={"INT"+xxx+".txt"};//all zones;
            for(int h=0;h<fileName.length;h++){
                if(LOGGING_LEVEL_VERBOSE_ENABLED)System.out.println("Stage:"+h);
                File dataFile = new File(OUTPUT_FOLDER+INT4_GENERATED_STUFF+"/"+fileName[h]);
                FileReader in = new FileReader(dataFile);
                char c;
                StringBuffer ss = new StringBuffer();
                long d=dataFile.length();
                ArrayList<String> sources = new ArrayList<>();
                for(long i=0;i<d;i++){
                    c = (char) in.read();
                    ss.append(c);
                    if(c==10){
                        String s=ss.toString();
                        if(s.length()>10){
                            s=s.substring(0, s.length()-2);
                            if(s.charAt(0)!=' '){
                                if(sources.size()>=2){
                                    try {
                                        NodeINT4 star = new NodeINT4(sources);
                                        CachedStorageILB.listINT4.add(star);
                                    }catch (InvalidAttributeValueException e){
                                        if(LOGGING_LEVEL_VERBOSE_ENABLED)System.err.println("INT4 parse error:"+ e.getMessage());
                                    }
                                    sources=new ArrayList<>();
                                }
                                sources.add(s);
                            }else {
                                sources.add(s);
                            }
                            ss = new StringBuffer();
                        }else{
                            ss = new StringBuffer();
                        }
                    }
                    if(d-i<1){
                        String s=ss.toString();
                        sources.add(s);
                        NodeINT4 star = new NodeINT4(sources);
                        CachedStorageILB.listINT4.add(star);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void parseCEV(){
        System.out.println("       parse CEV");
        try {
            File dataFile = new File(INPUT_FOLDER+CEV_SOURCE_FILE);
            FileReader in = new FileReader(dataFile);
            char c;
            StringBuffer ss = new StringBuffer();
            long d = dataFile.length();
            for(long i=0;i<d;i++){
                c = (char) in.read();
                ss.append(c);
                if(c==10){
                    String s=ss.toString();
                    s=s.substring(0, s.length()-2);
                    NodeCEV star = new NodeCEV(s);
                    CachedStorageILB.listCEV.add(star);
                    ss = new StringBuffer();
                }
            }
            if(LOGGING_LEVEL_VERBOSE_ENABLED)System.out.println("       Success. fileLength="+dataFile.length());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void parseSB9(){
        System.out.println("       parse SB9");
        try {
            File dataFile = new File(INPUT_FOLDER+SB9_SOURCE_FILE);
            FileReader in = new FileReader(dataFile);
            char c;
            StringBuffer ss = new StringBuffer();
            long d = dataFile.length();
            for(long i=0;i<d;i++){
                c = (char) in.read();
                ss.append(c);
                if(c==10){
                    String s=ss.toString();
                    s=s.substring(0, s.length()-2);
                    NodeSB9 star = new NodeSB9(s);
                    CachedStorageILB.listSB9.add(star);
                    ss = new StringBuffer();
                }
            }
            if(LOGGING_LEVEL_VERBOSE_ENABLED)System.out.println("       Success. fileLength="+dataFile.length());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void parseLMX(){
        System.out.println("       parse LMX");
        try {
            File dataFile = new File(INPUT_FOLDER+LMX_SOURCE_FILE);
            FileReader in = new FileReader(dataFile);
            char c;
            StringBuffer ss = new StringBuffer();
            long d = dataFile.length();
            for(long i=0;i<d;i++){
                c = (char) in.read();
                ss.append(c);
                if(c==10){
                    String s=ss.toString();
                    s=s.substring(0, s.length()-2);
                    NodeXR star = new NodeXR(s);
                    CachedStorageILB.listLMX.add(star);
                    ss = new StringBuffer();
                }
            }
            if(LOGGING_LEVEL_VERBOSE_ENABLED)System.out.println("       Success. fileLength="+dataFile.length());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void parseSB9params(){
        System.out.println("       parse SB9 params");
        try {
            File dataFile = new File(INPUT_FOLDER+SB9_PARAMS_FILE);
            FileReader in = new FileReader(dataFile);
            char c;
            StringBuffer ss = new StringBuffer();
            long d = dataFile.length();
            for(long i=0;i<d;i++){
                c = (char) in.read();
                ss.append(c);
                if(c==10){
                    String s=ss.toString();
                    s=s.substring(0, s.length()-1);
                    NodeSB9Params star = new NodeSB9Params(s);
                    ss = new StringBuffer();
                }
            }
            if(LOGGING_LEVEL_VERBOSE_ENABLED)System.out.println("       Success. fileLength="+dataFile.length());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}