package ru.inasan.karchevsky.lib.tools.namingRulesImplementation;


import ru.inasan.karchevsky.configuration.KeysDictionary;
import ru.inasan.karchevsky.lib.model.Component;
import ru.inasan.karchevsky.lib.model.Pair;
import ru.inasan.karchevsky.lib.model.StarSystem;
import ru.inasan.karchevsky.lib.storage.CachedStorage;
import ru.inasan.karchevsky.lib.tools.resolvingRulesImplementation.InWDSWeTrustRule;
import ru.inasan.karchevsky.lib.tools.resolvingRulesImplementation.pairToPairRules.MatchingByCoordinatesRuleImplementation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static ru.inasan.karchevsky.configuration.SharedConstants.LOGGING_LEVEL_VERBOSE_ENABLED;

public class SysTreeNamesGenerator {
    public static void generateNames(CachedStorage storage) {
        for (StarSystem system : storage.getSysList()) {
            generateNamesForSystem(system);
            System.out.println("ILBID:" + system.ILBId);
        }
    }

    public static void generateILBSystemNames(CachedStorage storage) {
        for (StarSystem system : storage.getSysList()) {
            generateILBIdsForSystem(system);
        }
    }

    private static void rebuldTree(StarSystem system) {
        for (Pair pairEx : system.pairs) {
            for (Pair pairIn : system.pairs) {
                if (pairIn.getCoordinatesByKey(KeysDictionary.RHO).size() > 0 && pairEx.getCoordinatesByKey(KeysDictionary.RHO).size() > 0 && pairIn.getCoordinatesByKey(KeysDictionary.RHO).get(0) * 5 < pairEx.getCoordinatesByKey(KeysDictionary.RHO).get(0)) {
                    if (pairIn != pairEx && MatchingByCoordinatesRuleImplementation.correspondsPairToNode(pairIn, pairEx.el1)) {
                        if (pairEx.el1.equalNodeOnPrevLevel == null) {
                            pairEx.el1.equalNodeOnPrevLevel = pairIn;
                            system.pairs.remove(pairIn);
                            rebuldTree(system);
                        } else {
                            if (LOGGING_LEVEL_VERBOSE_ENABLED)
                                System.err.println("ALARM! TWO equal pairs has different nodes!1" + pairEx.getUsedCatalogues().get(0) + pairEx.getMappedEntityByCatalogue(pairEx.getUsedCatalogues().get(0)));
                            if (LOGGING_LEVEL_VERBOSE_ENABLED)
                                System.err.println("ALARM! TWO equal pairs has different nodes!2" + pairIn.getUsedCatalogues().get(0) + pairIn.getMappedEntityByCatalogue(pairIn.getUsedCatalogues().get(0)));
                        }
                        return;
                    }
                    if (pairIn != pairEx && MatchingByCoordinatesRuleImplementation.correspondsPairToNode(pairIn, pairEx.el2)) {
                        if (pairEx.el2.equalNodeOnPrevLevel == null) {
                            pairEx.el2.equalNodeOnPrevLevel = pairIn;
                            system.pairs.remove(pairIn);
                            rebuldTree(system);
                        } else {
                            if (LOGGING_LEVEL_VERBOSE_ENABLED)
                                System.err.println("ALARM! TWO equal pairs has different nodes!1" + pairEx.getUsedCatalogues().get(0) + pairEx.getMappedEntityByCatalogue(pairEx.getUsedCatalogues().get(0)));
                            if (LOGGING_LEVEL_VERBOSE_ENABLED)
                                System.err.println("ALARM! TWO equal pairs has different nodes!2" + pairIn.getUsedCatalogues().get(0) + pairIn.getMappedEntityByCatalogue(pairIn.getUsedCatalogues().get(0)));
                        }
                        return;
                    }
                }
            }
        }
    }

    private static void generateNamesForSystem(StarSystem system) {
        int i = 0;

        for (int pi = 0; pi < system.pairs.size(); pi++) {
            Pair pair1 = system.pairs.get(pi);
            if (pair1.el1.primaryName.equals("")) {
                pair1.el1.primaryName = "" + ++i;
            }
            if (pair1.el2.primaryName.equals("")) {
                pair1.el2.primaryName = "" + ++i;
            }
            for (int pj = pi + 1; pj < system.pairs.size(); pj++) {
                Pair pair2 = system.pairs.get(pj);
                if (InWDSWeTrustRule.corresponds(pair1.el1, pair2.el1)) {
                    pair2.el1.primaryName = pair1.el1.primaryName;
                } else if (InWDSWeTrustRule.corresponds(pair1.el2, pair2.el1)) {
                    pair2.el1.primaryName = pair1.el2.primaryName;
                }
                if (InWDSWeTrustRule.corresponds(pair1.el1, pair2.el2)) {
                    pair2.el2.primaryName = pair1.el1.primaryName;
                } else if (InWDSWeTrustRule.corresponds(pair1.el2, pair2.el2)) {
                    pair2.el2.primaryName = pair1.el2.primaryName;
                }
            }
        }

        for (int pi = 0; pi < system.pairs.size(); pi++) {
            Pair pair1 = system.pairs.get(pi);
            if (pair1.el1.primaryName.equals("")) {
                pair1.el1.primaryName = "" + ++i;
            }
            if (pair1.el2.primaryName.equals("")) {
                pair1.el2.primaryName = "" + ++i;
            }
            for (int pj = pi + 1; pj < system.pairs.size(); pj++) {
                Pair pair2 = system.pairs.get(pj);
                if (MatchingByCoordinatesRuleImplementation.corresponds(pair1.el1, pair2.el1)) {
                    pair2.el1.primaryName = pair1.el1.primaryName;
                }else if (MatchingByCoordinatesRuleImplementation.corresponds(pair1.el2, pair2.el1)) {
                    pair2.el1.primaryName = pair1.el2.primaryName;
                }
                if (MatchingByCoordinatesRuleImplementation.corresponds(pair1.el1, pair2.el2)) {
                    pair2.el2.primaryName = pair1.el1.primaryName;
                }else if (MatchingByCoordinatesRuleImplementation.corresponds(pair1.el2, pair2.el2)) {
                    pair2.el2.primaryName = pair1.el2.primaryName;
                }
            }
        }

        rebuldTree(system);

        for (Pair pair : system.pairs) {
            if (pair.el2.primaryName.equals(pair.el1.primaryName)) {
                pair.el2.primaryName = "" + ++i;
            }
        }

        Map<Integer,Integer> ids = new HashMap<>();
        List<Integer> collect = system.pairs.stream()
                .flatMapToInt(z -> IntStream.of(Integer.parseInt(z.el1.primaryName),
                Integer.parseInt(z.el2.primaryName)))
                .distinct()
                .boxed()
                .sorted()
                .collect(Collectors.toList());

        final Integer[] a = {1};
        collect.forEach(z-> ids.put(z,a[0]++));

        system.pairs.forEach(pair ->{
            pair.el1.primaryName = "" + ids.get(Integer.parseInt(pair.el1.primaryName));
            pair.el2.primaryName = "" + ids.get(Integer.parseInt(pair.el2.primaryName));
        });


        for (Pair pair : system.pairs) {
            pair.primaryName = pair.el1.primaryName + "-" + pair.el2.primaryName;
        }
        for (Pair pair : system.pairs) {
            if (pair.el1.equalNodeOnPrevLevel != null) {
                nameInComponents(pair.el1);
            }
            if (pair.el2.equalNodeOnPrevLevel != null) {
                nameInComponents(pair.el2);
            }
        }
    }

    public static void nameInComponents(Component z) {
        Pair pair = z.equalNodeOnPrevLevel;
        pair.el1.primaryName = z.primaryName + 'A';
        pair.el2.primaryName = z.primaryName + 'B';
        pair.primaryName = pair.el1.primaryName + "-" + pair.el2.primaryName;
        if (pair.el1.equalNodeOnPrevLevel != null) {
            nameInComponents(pair.el1);
        }
        if (pair.el2.equalNodeOnPrevLevel != null) {
            nameInComponents(pair.el2);
        }
    }

    private static void generateILBIdsForSystem(StarSystem system) {
        Double x = system.pairs.get(0).el1.getCoordinatesByKey(KeysDictionary.X).get(0);
        Double y = system.pairs.get(0).el1.getCoordinatesByKey(KeysDictionary.Y).get(0);

        StringBuilder value = new StringBuilder();
        value.append("J");
        int h = (int) (x / 360 * 24);
        if (h < 10) {
            value.append("0");
        }
        value.append(h);
        int m = (int) (x / 360 * 24 * 60 - h * 60);
        if (m < 10) {
            value.append("0");
        }
        value.append(m);
        int s = (int) (x / 360 * 24 * 60 * 60 - h * 60 * 60 - m * 60);
        if (s < 10) {
            value.append("0");
        }
        value.append(s);
        value.append(".");
        int ms = (int) (x / 360 * 24 * 60 * 60 * 100 - h * 60 * 60 * 100 - m * 60 * 100 - s * 100);
        if (ms < 10) {
            value.append("0");
        }
        value.append(ms);

        if (y > 0) {
            value.append("+");
        } else {
            y = -y;
            value.append("-");
        }
        int gr = (int) (y / 1);
        if (gr < 10) {
            value.append("0");
        }
        value.append(gr);
        int min = (int) (y * 60 - gr * 60);
        if (min < 10) {
            value.append("0");
        }
        value.append(min);
        int sec = (int) (y * 60 * 60 - gr * 60 * 60 - min * 60);
        if (sec < 10) {
            value.append("0");
        }
        value.append(sec);
        value.append(".");
        int msec = (int) (y * 60 * 60 * 10 - gr * 60 * 60 * 10 - min * 60 * 10 - sec * 10);
        value.append(msec);

        String id = value.toString();

        for (Pair pair1 : system.pairs) {
            appendValueToPair(pair1, id);
        }
        system.ILBId = id;
    }

    public static void appendValueToPair(Pair pair1, String id) {
        try {
            pair1.ILBId = id;
            pair1.el1.ILBId = id;
            pair1.el2.ILBId = id;
            if (pair1.el1.equalNodeOnPrevLevel != null) {
                appendValueToPair(pair1.el1.equalNodeOnPrevLevel, id);
            }
            if (pair1.el2.equalNodeOnPrevLevel != null) {
                appendValueToPair(pair1.el2.equalNodeOnPrevLevel, id);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
