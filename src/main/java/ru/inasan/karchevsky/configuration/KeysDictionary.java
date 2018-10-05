package ru.inasan.karchevsky.configuration;

/**
 * Created by Алекс on 07.01.2017.
 */

import javafx.util.Pair;

import java.util.ArrayList;

/**
 * Base interface. Contains all supported parameters, which we can use in interpreter.
 */
public interface KeysDictionary {
    String ILBid = "ILBSysId";
    String MODIFIERS = "MODIFIERS";

    String RHO = "RHO";
    String THETA = "THETA";
    String X = "X";
    String Y = "Y";
    String UBV = "UBV";
    String COORDFAKE = "F";//00000-1930

    String HD = "HD";
    String HIP = "HIP";
    String DM = "DM"; 	//BD+85   63
    String SB9 = "SB9id";
    String XR = "XRid";

    String CEV = "CEV";
    String ADS = "ADS";
    String BAYER = "BAYER";
    String FLAMSTEED = "FLAMSTEED";
    String OBSERVER = "OBSERVER";

    String WDSSYSTEM = "WDSSYSTEM";//00000-1930
    String WDSPAIR = "WDSPAIR";
    String WDSCOMP = "WDSPAIR";

    String CCDMSYSTEM = "CCDMSYSTEM";
    String CCDMPAIR = "CCDMPAIR";
    String CCDMCOMP = "CCDMPAIR";

    String TDSCSYSTEM = "TDSCSYSTEM";
    String TDSCPAIR = "TDSCPAIR";
    String TDSCCOMP = "TDSCPAIR";


    String COORD_I1_1 = "CI11";
    String COORD_I1_2 = "CI12";
    String COORD_F1_1 = "CF11";
    String COORD_F1_2 = "CF12";
    String COORD_I2_1 = "CI21";
    String COORD_I2_2 = "CI22";
    String COORD_F2_1 = "CF21";
    String COORD_F2_2 = "CF22";

    ArrayList<Pair<String,Integer>> pairFormat = FormatPair.getValue();
    ArrayList<Pair<String,Integer>> systemFormat = FormatSystem.getValue();

    enum FormatPair {;
        static ArrayList<Pair<String,Integer>> getValue() {
            ArrayList<Pair<String,Integer>> arr = new ArrayList<>();

            arr.add(new Pair<>(ILBid,19));
            arr.add(new Pair<>("primaryName",10));
            arr.add(new Pair<>(MODIFIERS,10));
            arr.add(new Pair<>(COORDFAKE,1));
            arr.add(new Pair<>(WDSSYSTEM,10));
            arr.add(new Pair<>(OBSERVER,9));
            arr.add(new Pair<>(WDSPAIR,5));
            arr.add(new Pair<>(CCDMSYSTEM,10));
            arr.add(new Pair<>(CCDMPAIR,4));
            arr.add(new Pair<>(TDSCSYSTEM,5));
            arr.add(new Pair<>(TDSCPAIR,4));

            arr.add(new Pair<>(X,10));
            arr.add(new Pair<>(Y,10));
            arr.add(new Pair<>(RHO,5));
            arr.add(new Pair<>(THETA,5));

            arr.add(new Pair<>(BAYER,10));
            arr.add(new Pair<>(FLAMSTEED,7));
            arr.add(new Pair<>(HD,7));
            arr.add(new Pair<>(HIP,7));
            arr.add(new Pair<>(DM,13));
            arr.add(new Pair<>(SB9,4));
            arr.add(new Pair<>(CEV,12));
            arr.add(new Pair<>(ADS,7));

            return arr;
        }
    }

    enum FormatSystem {;
        static ArrayList<Pair<String,Integer>> getValue() {
            ArrayList<Pair<String,Integer>> arr = new ArrayList<Pair<String,Integer>>();

            arr.add(new Pair<>("nullparam",9));
            arr.add(new Pair<>("nullparam",10));
            arr.add(new Pair<>("nullparam",1));
            arr.add(new Pair<>(WDSSYSTEM,10));
            arr.add(new Pair<>(OBSERVER,9));
            arr.add(new Pair<>("nullparam",5));
            arr.add(new Pair<>(CCDMSYSTEM,10));
            arr.add(new Pair<>("nullparam",4));
            arr.add(new Pair<>(TDSCSYSTEM,5));
            arr.add(new Pair<>("nullparam",4));

            arr.add(new Pair<>(X,10));
            arr.add(new Pair<>(Y,10));
            arr.add(new Pair<>(RHO,5));
            arr.add(new Pair<>(THETA,5));

            arr.add(new Pair<>(BAYER,10));
            arr.add(new Pair<>(FLAMSTEED,9));

            arr.add(new Pair<>(HD,7));
            arr.add(new Pair<>(HIP,7));
            arr.add(new Pair<>(DM,13));
            arr.add(new Pair<>(SB9,4));
            arr.add(new Pair<>(CEV,12));
            arr.add(new Pair<>(ADS,7));

            return arr;
        }
    }
}
