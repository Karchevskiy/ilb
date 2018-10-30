package ru.inasan.karchevsky.processor;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.TreeMultimap;
import org.springframework.data.util.Pair;
import ru.inasan.karchevsky.InterpreterProxy;
import ru.inasan.karchevsky.catalogues.ccdm.ParserCCDM;
import ru.inasan.karchevsky.catalogues.int4.ParserINT4;
import ru.inasan.karchevsky.catalogues.tdsc.ParserTDSC;
import ru.inasan.karchevsky.catalogues.wds.ParserWDS;
import ru.inasan.karchevsky.lib.pattern.NodeForParsedCatalogue;

import java.util.*;

public class SystemGroupingRules {

    private InterpreterProxy storage;

    private static final double RADII = 0.1;

    private TreeMultimap<Double, Pair<Double, NodeForParsedCatalogue>> mapXCoord;

    public HashMap<String, List<NodeForParsedCatalogue>> systemGrouping(InterpreterProxy storage) {
        this.storage = storage;
        System.out.println();
        System.out.println("PHASE 2: PROCESSING STARTED");
        HashMap<String, List<NodeForParsedCatalogue>> systems = Maps.newHashMap();

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 1; j++) {

                System.out.println("    zone " + i + "h" + j + "(" + (i * 6 + j) + " from 144 (each zone - 10min))");
                clearCache();
                String zoneIndex = "" + i + j;
                ParserWDS.parseWDS(zoneIndex, storage.getListWDS());
                ParserCCDM.parseCCDM(zoneIndex, storage.getListCCDMPairs());
                ParserTDSC.parseTDSC(zoneIndex, storage.getListTDSC());
                ParserINT4.parseINT4(zoneIndex, storage.getListINT4());

                mapXCoord = TreeMultimap.create(Double::compareTo, Comparator.comparingInt(Object::hashCode));
                enrichCoordTree(storage.getListWDS());
                enrichCoordTree(storage.getListCCDMPairs());
                enrichCoordTree(storage.getListTDSC());
                enrichCoordTree(storage.getListINT4());


                Double initial = mapXCoord.keySet().first();
                Double to = mapXCoord.keySet().floor(initial + RADII * 50);

                Set<Double> keysPage = mapXCoord.keySet();
                for (Double key : keysPage) {
                    mapXCoord.get(key).stream().forEach(currentWindowValue -> {
                        final NodeForParsedCatalogue currentWindowNode = currentWindowValue.getSecond();
                        if (currentWindowValue.getSecond().getSystemGenId() == null) {
                            systems.values()
                                    .stream().filter(z -> systemCloseEnough(currentWindowValue.getSecond(), z)).flatMap(z -> z.stream())
                                    .forEach(x -> {
                                        if (closeEnough(currentWindowNode, x)) {
                                            currentWindowNode.setSystemGenId(x.getSystemGenId());
                                        }
                                    });
                            if (currentWindowValue.getSecond().getSystemGenId() == null) {
                                String id = UUID.randomUUID().toString();
                                currentWindowNode.setSystemGenId(id);
                                systems.put(id, Lists.newArrayList(currentWindowNode));
                            } else {
                                systems.get(currentWindowValue.getSecond().getSystemGenId()).add(currentWindowNode);
                            }
                        }
                    });
                }

                System.out.println("systems.size():" + systems.size());
            }
            mapXCoord =
                    TreeMultimap.create(Double::compareTo, Comparator.comparingInt(Object::hashCode));
        }
        System.out.println("PHASE 3: SUCCESS");
        return systems;
    }


    private void enrichCoordTree(ArrayList<? extends NodeForParsedCatalogue> nodes) {
        nodes.forEach(z -> {
            mapXCoord.put(z.getXel1(), Pair.of(z.getYel1(), z));
            //      mapXCoord.put(z.getXel2(), Pair.of(z.getYel2(), z));
        });
    }


    private void clearCache() {
        storage.getListWDS().clear();
        storage.getListCCDMPairs().clear();
        storage.getListTDSC().clear();
        storage.getListINT4().clear();

    }

    public static boolean closeEnough(NodeForParsedCatalogue n1, NodeForParsedCatalogue n2) {
        return closeEnough(n1.getXel1(), n2.getXel1(), n1.getYel1(), n2.getYel2());
    }

    public static boolean systemCloseEnough(NodeForParsedCatalogue n1, List<NodeForParsedCatalogue> n2) {
        return closeEnough(n1.getXel1(), n2.get(0).getXel1(), n1.getYel1(), n2.get(0).getYel2(), RADII * 15);
    }

    public static boolean closeEnough(double x1, double x2, double y1, double y2) {
        return (x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2) < RADII;
    }

    public static boolean closeEnough(double x1, double x2, double y1, double y2, Double RADII) {
        return (x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2) < RADII;
    }

}
