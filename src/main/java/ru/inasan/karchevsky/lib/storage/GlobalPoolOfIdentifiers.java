package ru.inasan.karchevsky.lib.storage;

import java.util.ArrayList;
import java.util.stream.Stream;


public class GlobalPoolOfIdentifiers {
    public static ArrayList<String> HD = new ArrayList<>();
    public static ArrayList<String> DM = new ArrayList<>();
    public static ArrayList<String> HIP = new ArrayList<>();
    public static ArrayList<String> ADS = new ArrayList<>();
    public static ArrayList<String> BAYER = new ArrayList<>();
    public static ArrayList<String> FLAMSTEED = new ArrayList<>();

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

    public static boolean isFlamsteed(String flamsteedCandidate) {
        final String flamsteedCandidateUpper = flamsteedCandidate.toUpperCase();

        boolean containsNumbers =
                Stream.of(Numbers.numbers)
                        .anyMatch(
                                z -> flamsteedCandidateUpper.contains(z)
                                        && flamsteedCandidateUpper.replaceAll(" ", "").startsWith(z));

        return containsNumbers && Stream.of(CelestiaList.values()).anyMatch(z -> flamsteedCandidateUpper.contains(z.toString()));
    }

    public static boolean isBayer(String bayerCandidate) {
        final String bayerCandidateLower = bayerCandidate.toLowerCase();
        boolean containsAlphabetLetter =
                Stream.of(GreekAlphabet.values())
                        .anyMatch(z -> bayerCandidateLower.contains(z.toString()));

        return containsAlphabetLetter && Stream.of(CelestiaList.values()).anyMatch(z -> bayerCandidateLower.toUpperCase().contains(z.toString()));
    }

    public static boolean isDM(String a) {
        a = a.toLowerCase();
        if (a.contains("i")) {
            return false;
        }
        return (a.contains("+") || a.contains("-")) && a.length() > 2;
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
        if (s.length > 1 && s[0].length() > 0) {
            if (!s[0].contains("CP") && !s[0].contains("CPD") && !s[0].contains("BD")) {
                return "";
            }
        }
        if (a.length() <= 2) {
            return "";
        }

        String postfix = s[s.length - 1];
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
