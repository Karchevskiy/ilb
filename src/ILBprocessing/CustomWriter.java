package ILBprocessing;

import ILBprocessing.configuration.KeysDictionary;
import ILBprocessing.configuration.SharedConstants;
import ILBprocessing.storage.CachedStorageILB;
import lib.model.Component;
import lib.model.Pair;
import lib.model.StarSystem;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class CustomWriter extends CachedStorageILB implements SharedConstants,KeysDictionary {
    public static void writeAllCachedData(String xxx){
        if(LOGGING_LEVEL_VERBOSE_ENABLED)System.out.println("writing current cache in temp files");
        try{
            String fileName=FILE_NAME_DEFAULT+xxx+FILE_RESULT_FORMAT;
            Writer outer = new FileWriter(new File(OUTPUT_FOLDER+fileName));
            for(int k = 0; k< sysList.size(); k++){
                writeSystem(sysList.get(k),outer);
                for(int j = 0; j< sysList.get(k).pairs.size(); j++){
                    writePairLayer(sysList.get(k).pairs.get(j),outer);
                }
            }
            outer.flush();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public static void writeSystem(StarSystem system, Writer outer){
        StringBuffer line = new StringBuffer();
        //line.append('\n');
        line.append(system.ILBId+":"+"s");
        for(javafx.util.Pair<String,Integer> entry: systemFormat){
            StringBuffer param = new StringBuffer();

                Set<String> valuesTotal = new HashSet<>();
                for(Pair pair:system.pairs){
                    ArrayList<String> values = pair.getParamsForByKey(entry.getKey());
                    valuesTotal.addAll(values);
                }
                for(String value:valuesTotal){
                    if(value!=null && !value.equals("")) {
                        param.append(value);
                        break;
                    }
                }
                while(param.length()<entry.getValue()){
                    param.append(' ');
                }

            line.append(param);
            line.append("|");
        }
        line.append('\n');
        try {
            outer.write(line.toString());
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    public static void writePairLayer(Pair pair, Writer outer){
        StringBuffer line = new StringBuffer();
        for(String entity: pair.getUsedCatalogues()){
            List<javafx.util.Pair<String, Double>> coords = pair.getCoordinatesForCurrentCatalogue(entity);
            List<javafx.util.Pair<String, String>> params = pair.getParamsForCurrentCatalogue(entity);
            for(javafx.util.Pair<String,Integer> entry: pairFormat){
                StringBuffer param = new StringBuffer();
                for(javafx.util.Pair<String, Double> c:coords) {
                    if (c.getKey().equals(entry.getKey())) {
                        if(entry.getKey().equals(KeysDictionary.RHO) || entry.getKey().equals(KeysDictionary.THETA)) {
                            param.append(String.format("%.1f", c.getValue()).replaceAll(",","."));
                        }else{
                            param.append(String.format("%.5f", c.getValue()).replaceAll(",","."));
                        }
                    }
                }
                for(javafx.util.Pair<String, String> c:params) {
                    if (c.getKey().equals(entry.getKey())) {
                        if(!(c.getValue()==null) && !c.getValue().equals("")) {
                            param.append(c.getValue());
                        }
                    }
                }
                if(entry.getKey().equals("primaryName")){
                    param.append("p"+pair.primaryName);
                }
                if(entry.getKey().equals(KeysDictionary.ILBid)){
                    param.append(pair.ILBId);
                }
                while(param.length()<entry.getValue()){
                    param.append(' ');
                }
                line.append(param);
                line.append('|');
            }
            if(LOGGING_LEVEL_SOURCE_ENABLED){
                line.append(entity+":"+pair.getMappedEntityByCatalogue(entity));
            }
            try {
                line.append('\n');
                String result = line.toString();
                result = result.replaceFirst("\\|",":");
                outer.write(result);
                line = new StringBuffer();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        if(pair.el1 instanceof Pair){
            writePairLayer((Pair)pair.el1,outer);
        }else{
            writeComponentLayer((Component)pair.el1,outer);
        }
        if(pair.el2 instanceof Pair){
            writePairLayer((Pair)pair.el2,outer);
        }else{
            writeComponentLayer((Component)pair.el2,outer);
        }
    }
    public static void writeComponentLayer(Component comp, Writer outer){
        StringBuffer line = new StringBuffer();
        for(String entity: comp.getUsedCatalogues()){
            List<javafx.util.Pair<String, Double>> coords = comp.getCoordinatesForCurrentCatalogue(entity);
            List<javafx.util.Pair<String, String>> params = comp.getParamsForCurrentCatalogue(entity);
            for(javafx.util.Pair<String,Integer> entry: pairFormat){
                StringBuffer param = new StringBuffer();
                for(javafx.util.Pair<String, Double> c:coords) {
                    if (c.getKey().equals(entry.getKey())) {
                        if(entry.getKey().equals(KeysDictionary.RHO) || entry.getKey().equals(KeysDictionary.THETA)) {
                            param.append(String.format("%.1f", c.getValue()).replaceAll(",","."));
                        }else{
                            param.append(String.format("%.5f", c.getValue()).replaceAll(",","."));
                        }
                    }
                }
                for(javafx.util.Pair<String, String> c:params) {
                    if (c.getKey().equals(entry.getKey())) {
                        if(!(c.getValue()==null) && !c.getValue().equals("")) {
                            param.append(c.getValue());
                        }
                    }
                }
                if(entry.getKey().equals("primaryName")){
                    param.append("c"+comp.primaryName);
                }
                if(entry.getKey().equals(KeysDictionary.ILBid)){
                    param.append(comp.ILBId);
                }
                while(param.length()<entry.getValue()){
                    param.append(' ');
                }
                line.append(param);
                line.append('|');
            }
            if(LOGGING_LEVEL_SOURCE_ENABLED){
                line.append(entity+":"+comp.getMappedEntityByCatalogue(entity));
            }
            try {

                line.append('\n');
                String result = line.toString();
                result = result.replaceFirst("\\|",":");
                outer.write(result);
                line = new StringBuffer();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        if(comp.equalNodeOnPrevLevel!=null){
            writePairLayer(comp.equalNodeOnPrevLevel,outer);
        }
    }
}
