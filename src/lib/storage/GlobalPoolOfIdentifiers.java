package lib.storage;

import java.util.ArrayList;


public class GlobalPoolOfIdentifiers {
    public static ArrayList<String> HD = new ArrayList<String>();
    public static ArrayList<String> DM = new ArrayList<String>();
    public static ArrayList<String> HIP = new ArrayList<String>();
    public static ArrayList<String> ADS = new ArrayList<String>();
    public static ArrayList<String> BAYER = new ArrayList<String>();
    public static ArrayList<String> FLAMSTEED = new ArrayList<String>();

    public static int unresolvedCCDM = 0;
    public static int totalCCDM = 0;

    public static int unresolvedTDSC = 0;
    public static int totalTDSC = 0;

    public static int unresolvedINT4 = 0;
    public static int totalINT4 = 0;


    public static int resolvedSCO = 0;
    public static int resolvedCEV = 0;
    public static int resolvedSB9 = 0;
    public static int resolvedLMX = 0;

    public static boolean likeFlamsteed(String temp) {
        temp = temp.toUpperCase();
        if (temp.contains("AND") ||
                temp.contains("GEM".toUpperCase()) ||
                temp.contains("UMA".toUpperCase()) ||
                temp.contains("CMA".toUpperCase()) ||
                temp.contains("LIB".toUpperCase()) ||
                temp.contains("AQR".toUpperCase()) ||
                temp.contains("AUR".toUpperCase()) ||
                temp.contains("LUP".toUpperCase()) ||
                temp.contains("BOO".toUpperCase()) ||
                temp.contains("COM".toUpperCase()) ||
                temp.contains("CRV".toUpperCase()) ||
                temp.contains("HER".toUpperCase()) ||
                temp.contains("HYA".toUpperCase()) ||
                temp.contains("COL".toUpperCase()) ||
                temp.contains("CVN".toUpperCase()) ||
                temp.contains("VIR".toUpperCase()) ||
                temp.contains("DEL".toUpperCase()) ||
                temp.contains("DRA".toUpperCase()) ||
                temp.contains("MON".toUpperCase()) ||
                temp.contains("ARA".toUpperCase()) ||
                temp.contains("PIC".toUpperCase()) ||
                temp.contains("CAM".toUpperCase()) ||
                temp.contains("GRU".toUpperCase()) ||
                temp.contains("LEP".toUpperCase()) ||
                temp.contains("OPH".toUpperCase()) ||
                temp.contains("SER".toUpperCase()) ||
                temp.contains("DOR".toUpperCase()) ||
                temp.contains("IND".toUpperCase()) ||
                temp.contains("CAS".toUpperCase()) ||
                temp.contains("CAR".toUpperCase()) ||
                temp.contains("CET".toUpperCase()) ||
                temp.contains("CAP".toUpperCase()) ||
                temp.contains("PYX".toUpperCase()) ||
                temp.contains("PUP".toUpperCase()) ||
                temp.contains("CYG".toUpperCase()) ||
                temp.contains("LEC".toUpperCase()) ||
                temp.contains("VOL".toUpperCase()) ||
                temp.contains("LYR".toUpperCase()) ||
                temp.contains("VUL".toUpperCase()) ||
                temp.contains("UMI".toUpperCase()) ||
                temp.contains("EQU".toUpperCase()) ||
                temp.contains("LMI".toUpperCase()) ||
                temp.contains("CMI".toUpperCase()) ||
                temp.contains("MIC".toUpperCase()) ||
                temp.contains("MUS".toUpperCase()) ||
                temp.contains("ANT".toUpperCase()) ||
                temp.contains("NOR".toUpperCase()) ||
                temp.contains("ARI".toUpperCase()) ||
                temp.contains("OCT".toUpperCase()) ||
                temp.contains("AQL".toUpperCase()) ||
                temp.contains("ORI".toUpperCase()) ||
                temp.contains("PAV".toUpperCase()) ||
                temp.contains("VEL".toUpperCase()) ||
                temp.contains("PEG".toUpperCase()) ||
                temp.contains("PER".toUpperCase()) ||
                temp.contains("FOR".toUpperCase()) ||
                temp.contains("APS".toUpperCase()) ||
                temp.contains("CNC".toUpperCase()) ||
                temp.contains("CAE".toUpperCase()) ||
                temp.contains("PSC".toUpperCase()) ||
                temp.contains("LYN".toUpperCase()) ||
                temp.contains("CRB".toUpperCase()) ||
                temp.contains("SEX".toUpperCase()) ||
                temp.contains("RET".toUpperCase()) ||
                temp.contains("SCO".toUpperCase()) ||
                temp.contains("SCL".toUpperCase()) ||
                temp.contains("MEN".toUpperCase()) ||
                temp.contains("SGE".toUpperCase()) ||
                temp.contains("SGR".toUpperCase()) ||
                temp.contains("TEL".toUpperCase()) ||
                temp.contains("TAU".toUpperCase()) ||
                temp.contains("TRI".toUpperCase()) ||
                temp.contains("TUC".toUpperCase()) ||
                temp.contains("PHE".toUpperCase()) ||
                temp.contains("CHA".toUpperCase()) ||
                temp.contains("CEN".toUpperCase()) ||
                temp.contains("CEP".toUpperCase()) ||
                temp.contains("CIR".toUpperCase()) ||
                temp.contains("HOR".toUpperCase()) ||
                temp.contains("CRT".toUpperCase()) ||
                temp.contains("SCT".toUpperCase()) ||
                temp.contains("ERI".toUpperCase()) ||
                temp.contains("HYI".toUpperCase()) ||
                temp.contains("CRA".toUpperCase()) ||
                temp.contains("PSA".toUpperCase()) ||
                temp.contains("CRU".toUpperCase()) ||
                temp.contains("TRA".toUpperCase()) ||
                temp.contains("LAC".toUpperCase())) {
            return true;
        }
        return false;
    }

    public static boolean likeBayer(String a) {
        a = a.toLowerCase();
        if (a.contains("alp") ||
                a.contains("bet") ||
                a.contains("gam") ||
                a.contains("del") ||
                a.contains("eps") ||
                a.contains("zet") ||
                a.contains("eta") ||
                a.contains("the") ||
                a.contains("iot") ||
                a.contains("kap") ||
                a.contains("lam") ||
                a.contains("mu") ||
                a.contains("nu") ||
                a.contains("xi") ||
                a.contains("omi") ||
                a.contains("pi") ||
                a.contains("rho") ||
                a.contains("sig") ||
                a.contains("tau") ||
                a.contains("ups") ||
                a.contains("phi") ||
                a.contains("chi") ||
                a.contains("psi") ||
                a.contains("ome")) {
            if (rebuildIdToUnifiedBase(a).length() > 9) {
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    public static boolean likeDM(String a) {
        a = a.toLowerCase();
        if (a.contains("i")) {
            return false;
        }
        if ((a.contains("+") || a.contains("-")) && a.length() > 2) {
            return true;
        }
        return false;
    }

    public static String rebuildIdToUnifiedBase(String a) {
        a = a.toUpperCase().replaceAll("  ", " ").replaceAll(" ", " ").replaceAll(" ", "").replaceAll("[^a-zA-Z0-9,+-]", "");
        return a;
    }

    public static String rebuildIdForDM(String a) {
        //remove all words before '+' or '-'. cut part with CP, CD etc
        a = rebuildIdToUnifiedBase(a);
        String[] s = a.split("\\+|\\-");
        String prefix = "";
        if (a.contains("CP")) {
            prefix = "CP";
        }
        if (a.contains("CPD")) {
            prefix = "CPD";
        }
        if (a.contains("BD")) {
            prefix = "BD";
        }
        String postfix = s[s.length - 1];
        if (s.length > 1 && s[0].length() > 0) {
            if (!s[0].contains("CP") && !s[0].contains("CPD") && !s[0].contains("BD")) {
                return "";
            }
        }
        if (a.length() <= 2) {
            return "";
        }
        try {
            postfix = postfix.substring(0, 2) + " " + postfix.substring(2, postfix.length());
        } catch (Exception e) {
            System.err.println("failed to buildID " + a);
        }
        if (a.contains("-")) {
            return prefix + "-" + postfix;
        } else {
            return prefix + "+" + postfix;
        }
    }

    public static void validateDuplicates() {
        System.err.println("HIP with duplicates size:" + HIP.size());
        removeDuplicates(HIP);
        System.err.println("HD with duplicates size:" + HD.size());
        removeDuplicates(HD);
        System.err.println("DM with duplicates size:" + DM.size());
        removeDuplicates(DM);
        System.err.println("ADS with duplicatess size:" + ADS.size());
        removeDuplicates(ADS);
        System.err.println("BAYER with duplicates size:" + BAYER.size());
        removeDuplicates(BAYER);
        System.err.println("FLAMSTEED with duplicates size:" + FLAMSTEED.size());
        removeDuplicates(FLAMSTEED);
    }

    ;

    public static void removeDuplicates(ArrayList<String> list) {
        for (int i = 0; i < list.size(); i++) {
            for (int j = i + 1; j < list.size(); j++) {
                if (list.get(j).equals(list.get(i))) {
                    list.remove(j);
                    j--;
                }
            }
        }
    }

    public static void printCount() {
        System.err.println("HIP size:" + HIP.size());
        System.err.println("HD size:" + HD.size());
        System.err.println("DM size:" + DM.size());
        System.err.println("ADS size:" + ADS.size());
        System.err.println("BAYER size:" + BAYER.size());
        System.err.println("FLAMSTEED size:" + FLAMSTEED.size());
    }

    ;
}
