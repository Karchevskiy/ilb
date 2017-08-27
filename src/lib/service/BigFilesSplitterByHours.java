package lib.service;

import ILBprocessing.configuration.SharedConstants;
import lib.pattern.SplitRule;

import java.io.*;

/**
 * Created by Алекс on 22.03.2016.
 */
public final class BigFilesSplitterByHours implements SharedConstants {
    //TODO: low priority - refactor this method. Instead of 144 read use writePairLayer streams
    public static void splitLargeFile(String[] fileName, String tempFileName, SplitRule splitRule){
        try {
            File file= new File(OUTPUT_FOLDER+tempFileName+"/");
            file.mkdirs();
            for(int n=0;n<24;n++){
                for(int m=0;m<6;m++){
                    String fileLogName=OUTPUT_FOLDER+tempFileName+"/"+tempFileName+n+m+FILE_RESULT_FORMAT;
                    System.out.println("writing "+ fileLogName);
                    file= new File(fileLogName);
                    Writer outer = new FileWriter(file);
                    for(int h=0;h<fileName.length;h++){
                        File dataFile = new File(INPUT_FOLDER+fileName[h]);
                        FileReader in = new FileReader(dataFile);
                        char c;
                        StringBuffer ss = new StringBuffer();
                        long d=dataFile.length();



                        boolean sameFlag=false;
                        boolean prevCorresponds=false;
                        for(long i=0;i<d;i++){
                            c = (char) in.read();
                            ss.append(c);
                            try {
                                if (c == 10) {
                                    String s = ss.toString();
                                    sameFlag=sameFlagCalculator(s);
                                    if (splitRule.StringCorrespondsToFile(s,n,m) || sameFlag && prevCorresponds) {
                                        outer.write(s);
                                        outer.flush();
                                        prevCorresponds=true;
                                    }else{
                                        prevCorresponds=false;
                                    }
                                    ss = new StringBuffer();
                                }
                            }catch (Exception e){
                                ss = new StringBuffer();
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static boolean sameFlagCalculator(String s){
        boolean sameFlag=false;
        if(s.replaceAll(" ","").length()<=2){
            sameFlag=false;
        }else if(s.charAt(0)==' ' && s.charAt(1)==' '){
            sameFlag=true;
        }else{
            sameFlag=false;
        }
        return sameFlag;
    }
    public static void concatenator(){
        int componentCounter=0;
        int systemCounter=0;
        int pairCounter=0;
        try{
            String fileName2=ILB_RESULT_FILE;
            Writer outer2 = new FileWriter(new File(OUTPUT_FOLDER+fileName2));
            String fileName;
            for(int i=0;i<24;i++){
                for(int j=0;j<6;j++){
                    fileName=FILE_NAME_DEFAULT+i+j+FILE_RESULT_FORMAT;
                    File dataFile = new File(OUTPUT_FOLDER+fileName);
                    FileReader in = new FileReader(dataFile);
                    char c;
                    StringBuffer ss = new StringBuffer();
                    long d = dataFile.length();
                    for(long ii=0;ii<d;ii++){
                        c=(char)in.read();
                        ss.append(c);
                        if(c==10){
                            outer2.append(ss);
                            outer2.flush();
                            if(ss.toString().contains(":s")){
                                systemCounter++;
                            }else if(ss.toString().contains(":p")){
                                pairCounter++;
                            }else if(ss.toString().contains(":c")){
                                componentCounter++;
                            }else{

                            }
                            ss = new StringBuffer();
                        }
                    }
                    System.out.println("File"+fileName+"successful concatenated");
                    System.out.println("Systems :"+systemCounter);
                    System.out.println("Pairs :"+pairCounter);
                    System.out.println("Components :"+componentCounter);
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
