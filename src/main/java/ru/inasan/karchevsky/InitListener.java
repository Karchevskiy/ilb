package ru.inasan.karchevsky;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import ru.inasan.karchevsky.catalogues.bincep.ParserBinCep;
import ru.inasan.karchevsky.catalogues.ccdm.NodeCCDM;
import ru.inasan.karchevsky.catalogues.cev.NodeCEV;
import ru.inasan.karchevsky.catalogues.cev.ParserCEV;
import ru.inasan.karchevsky.catalogues.int4.NodeINT4;
import ru.inasan.karchevsky.catalogues.orb6.NodeORB6;
import ru.inasan.karchevsky.catalogues.orb6.ParserORB6;
import ru.inasan.karchevsky.catalogues.sb9.NodeSB9;
import ru.inasan.karchevsky.catalogues.sb9.ParserSB9;
import ru.inasan.karchevsky.catalogues.tdsc.NodeTDSC;
import ru.inasan.karchevsky.catalogues.wds.NodeWDS;
import ru.inasan.karchevsky.catalogues.xr.ParserXR;
import ru.inasan.karchevsky.configuration.KeysDictionary;
import ru.inasan.karchevsky.configuration.SharedConstants;
import ru.inasan.karchevsky.lib.model.NodeILB;
import ru.inasan.karchevsky.lib.model.StarSystem;
import ru.inasan.karchevsky.lib.pattern.NodeForParsedCatalogue;
import ru.inasan.karchevsky.lib.pattern.SplitRule;
import ru.inasan.karchevsky.lib.service.BigFilesSplitterByHours;
import ru.inasan.karchevsky.lib.storage.GlobalPoolOfIdentifiers;
import ru.inasan.karchevsky.lib.tools.namingRulesImplementation.SysTreeNamesGenerator;
import ru.inasan.karchevsky.lib.tools.resolvingRulesImplementation.pairToPairRules.PairIsComponentOfOtherRuleImplementation;
import ru.inasan.karchevsky.model.StarSystemGrouped;
import ru.inasan.karchevsky.model.SystemAbstractElement;
import ru.inasan.karchevsky.processor.SystemGroupingRules;
import ru.inasan.karchevsky.repository.StarSystemGroupedRepository;
import ru.inasan.karchevsky.repository.SystemAbstractElementRepository;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class InitListener implements ApplicationListener<ApplicationReadyEvent>, SharedConstants {

    private final SystemAbstractElementRepository systemAbstractElementRepository;
    private final StarSystemGroupedRepository starSystemGroupedRepository;

    public static InterpreterProxy storage = new InterpreterProxy();

    @Autowired
    public InitListener(SystemAbstractElementRepository systemAbstractElementRepository, StarSystemGroupedRepository starSystemGroupedRepository) {
        this.systemAbstractElementRepository = systemAbstractElementRepository;
        this.starSystemGroupedRepository = starSystemGroupedRepository;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        preProcessing();
        HashMap<String, List<NodeForParsedCatalogue>> systems = new SystemGroupingRules().systemGrouping(storage);


        systems.forEach((key, value) -> {

            System.out.println(key);
            for (NodeForParsedCatalogue node : value) {

                storage.setListBinCep(Lists.newArrayList());
                storage.setListCCDMPairs(Lists.newArrayList());
                storage.setListCEV(Lists.newArrayList());
                storage.setListINT4(Lists.newArrayList());
                storage.setListLMX(Lists.newArrayList());
                storage.setListSB9(Lists.newArrayList());
                storage.setListSCO(Lists.newArrayList());
                storage.setListTDSC(Lists.newArrayList());
                storage.setListWDS(Lists.newArrayList());

                if (node instanceof NodeWDS) {
                    storage.getListWDS().add((NodeWDS) node);
                }
                if (node instanceof NodeCCDM) {
                    storage.getListCCDMPairs().add((NodeCCDM) node);
                }
                if (node instanceof NodeTDSC) {
                    storage.getListTDSC().add((NodeTDSC) node);
                }
                if (node instanceof NodeINT4) {
                    storage.getListINT4().add((NodeINT4) node);
                }
                if (node instanceof NodeCEV) {
                    storage.getListCEV().add((NodeCEV) node);
                }
                if (node instanceof NodeSB9) {
                    storage.getListSB9().add((NodeSB9) node);
                }
                if (node instanceof NodeORB6) {
                    storage.getListSCO().add((NodeORB6) node);
                }
            }
            storage.setS(new StarSystem());
            System.out.println("WDS GO");
            storage.interprWDS();
            System.out.println("CCDM GO");
            storage.interprCCDM();
            System.out.println("TDSC GO");
            storage.interprTDSC();
            System.out.println("INT4 GO");
            storage.interprINT4();

            SysTreeNamesGenerator.generateNames(storage);
            PairIsComponentOfOtherRuleImplementation.rebuildTree(storage);
            SysTreeNamesGenerator.generateILBSystemNames(storage);
            CustomWriter.writeAllCachedData(key, storage);

            storage.setListCCDMPairs(Lists.newArrayList());
            storage.setListINT4(Lists.newArrayList());
            storage.setListTDSC(Lists.newArrayList());
            storage.setListWDS(Lists.newArrayList());

        });

        System.out.println("WRITE SUCCESSFUL");

        systems.keySet().stream().forEach(z -> {
            StarSystemGrouped starSystemGrouped = new StarSystemGrouped();
            starSystemGrouped.setSystemId(z);
            starSystemGroupedRepository.save(starSystemGrouped);
        });

        systems.values().stream().flatMap(Collection::stream).forEach(z -> {
            SystemAbstractElement systemAbstractElement = new SystemAbstractElement();
            systemAbstractElement.setValue(z);
            systemAbstractElementRepository.save(systemAbstractElement);
        });

        //postProcessing();

    }

    private static void preProcessing() {
        System.out.println("PHASE 1: SMALL CACHE GENERATION STARTED");
        //split large files on small (current algorithm complexity O(n^3))
        split();
        //adding in cache small catalogs
        ParserORB6.parseORB6(storage.getListSCO());
        ParserCEV.parseCEV(storage.getListCEV());
        ParserBinCep.parseBinCep(storage.getListBinCep());
        ParserSB9.parseSB9(storage.getListSB9());
        ParserSB9.parseSB9params(storage);
        ParserXR.parseLMX(storage.getListLMX());
        System.err.println("LMX size:" + storage.getListLMX().size());
        System.out.println("PHASE 1: SUCCESS");
    }

    private static void oldProcessing() {
        System.out.println();
        System.out.println("PHASE 2: PROCESSING STARTED");
        for (int i = 0; i < 24; i++) {
            for (int j = 0; j < 6; j++) {
                System.out.println("    zone " + i + "h" + j + "(" + (i * 6 + j) + " from 144 (each zone - 10min))");
                //clearCache();
                //solve("" + i + j);
                analyze();
            }
        }
        System.out.println("PHASE 2: SUCCESS");
    }

    private static void postProcessing() {
        System.out.println();
        System.out.println("PHASE 3: VALIDATION STARTED");

        System.err.println("fail on matching SCO nodes: " + storage.getListSCO().size());
        for (int i = 0; i < storage.getListSCO().size(); i++) {
            System.err.println("" + storage.getListSCO().get(i).source);
        }
        System.err.println("fail on matching CEV nodes: " + storage.getListCEV().size());
        for (int i = 0; i < storage.getListCEV().size(); i++) {
            System.err.println("" + storage.getListCEV().get(i).source);
        }
        System.err.println("fail on matching SB9 nodes: " + storage.getListSB9().size());
        for (int i = 0; i < storage.getListSB9().size(); i++) {
            System.err.println("" + storage.getListSB9().get(i).source);
        }
        System.err.println("fail on matching CCDM nodes: " + GlobalPoolOfIdentifiers.unresolvedCCDM + "/" + GlobalPoolOfIdentifiers.totalCCDM + " " + 1.0 * GlobalPoolOfIdentifiers.unresolvedCCDM / GlobalPoolOfIdentifiers.totalCCDM * 100 + "%");
        System.err.println("fail on matching TDSC nodes: " + GlobalPoolOfIdentifiers.unresolvedTDSC + "/" + GlobalPoolOfIdentifiers.totalTDSC + " " + 1.0 * GlobalPoolOfIdentifiers.unresolvedTDSC / GlobalPoolOfIdentifiers.totalTDSC * 100 + "%");
        System.err.println("fail on matching INT4 nodes: " + GlobalPoolOfIdentifiers.unresolvedINT4 + "/" + GlobalPoolOfIdentifiers.totalINT4 + " " + 1.0 * GlobalPoolOfIdentifiers.unresolvedINT4 / GlobalPoolOfIdentifiers.totalINT4 * 100 + "%");

        System.err.println("success on matching LMX nodes: " + GlobalPoolOfIdentifiers.resolvedLMX);
        System.err.println("success on matching SB9 nodes: " + GlobalPoolOfIdentifiers.resolvedSB9);
        System.err.println("success on matching CEV nodes: " + GlobalPoolOfIdentifiers.resolvedCEV);
        System.err.println("success on matching SCO nodes: " + GlobalPoolOfIdentifiers.resolvedSCO);

        GlobalPoolOfIdentifiers.validateDuplicates();
        GlobalPoolOfIdentifiers.printCount();
        BigFilesSplitterByHours.concatenator();
    }

    public static void split() {
        System.out.println("  Splitting large files on 10min-zones");
        if (WDS_SPLIT_BEFORE_PROCESSING) {
            System.out.println("  WDS split-flag:TRUE");
            SplitRule splitRule = (string, hour, decade) -> {
                if (Integer.parseInt("" + string.charAt(0) + string.charAt(1) + string.charAt(2)) == hour * 10 + decade) {
                    return true;
                }
                return false;
            };
            BigFilesSplitterByHours.splitLargeFile(WDS_SOURCE_FILES, WDS_GENERATED_STUFF, splitRule);
            System.out.println("  WDS splitting success");
        } else {
            System.out.println("  WDS split-flag:FALSE");
        }
        if (CCDM_SPLIT_BEFORE_PROCESSING) {
            System.out.println("  CCDM split-flag:TRUE");
            SplitRule splitRule = (string, hour, decade) -> {
                if (Integer.parseInt("" + string.charAt(1) + string.charAt(2) + string.charAt(3)) == hour * 10 + decade) {
                    return true;
                }
                return false;
            };
            BigFilesSplitterByHours.splitLargeFile(CCDM_SOURCE_FILES, CCDM_GENERATED_STUFF, splitRule);
            System.out.println("  CCDM splitting SUCCESS");
        } else {
            System.out.println("  CCDM split-flag:FALSE");
        }
        if (TDSC_SPLIT_BEFORE_PROCESSING) {
            System.out.println("  TDSC split-flag:TRUE");
            SplitRule splitRule = (string, hour, decade) -> {
                try {
                    String[] cache = string.split("\\|");
                    if ((int) (Double.parseDouble(cache[4]) / 15) == hour && decade == (int) (Double.parseDouble(cache[4]) % 15 * 6 / 15)) {
                        //System.err.println(""+cache[4]+" "+(int)(Double.parseDouble(cache[4])%15*6/15));
                        return true;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return false;
            };
            BigFilesSplitterByHours.splitLargeFile(TDSC_SOURCE_FILES, TDSC_GENERATED_STUFF, splitRule);
            System.out.println("  TDSC splitting SUCCESS");
        } else {
            System.out.println("  TDSC split-flag:FALSE");
        }
        if (INT4_SPLIT_BEFORE_PROCESSING) {
            System.out.println("  INT4 split-flag:TRUE");
            SplitRule splitRule = (string, hour, decade) -> {
                try {
                    if (string.length() > 2 && string.charAt(0) != ' ' && Integer.parseInt("" + string.charAt(0) + string.charAt(1) + string.charAt(2)) == hour * 10 + decade) {
                        return true;
                    }
                } catch (Exception e) {
                    System.err.print("ERR p1c101: unresolved line in INT4: " + string);
                }
                return false;
            };
            BigFilesSplitterByHours.splitLargeFile(INT4_SOURCE_FILES, INT4_GENERATED_STUFF, splitRule);
            System.out.println("  INT4 splitting SUCCESS");
        } else {
            System.out.println("  INT4 split-flag:FALSE");
        }
    }


    private static void analyze() {
        for (StarSystem system : storage.sysList) {
            system.pairs.stream()
                    .map(z -> z.getParamsForByKey(KeysDictionary.DM))
                    .filter(z -> z != null && !z.equals(""))
                    .filter(z -> !z.isEmpty())
                    .forEach(collect -> {
                        List<String> collect1 = collect.stream()
                                .filter(z -> z != null && !z.equals(""))
                                .collect(Collectors.toList());
                        if (collect1.size() > 1) {
                            System.out.println(system.ILBId + " " + "Found wrong attribute " + KeysDictionary.DM);
                            collect1.forEach(c -> System.out.println("    " + c));
                        }
                    });


            system.pairs.stream()
                    .map(z -> z.getParamsForByKey(KeysDictionary.HIP))
                    .filter(z -> z != null && !z.equals(""))
                    .filter(z -> !z.isEmpty())
                    .forEach(collect -> {
                        List<String> collect1 = collect.stream()
                                .filter(z -> z != null && !z.equals(""))
                                .collect(Collectors.toList());
                        if (collect1.size() > 1) {
                            System.out.println(system.ILBId + " " + "Found wrong attribute " + KeysDictionary.HIP);
                            collect1.forEach(c -> System.out.println("    " + c));
                        }
                    });

            system.pairs.stream()
                    .map(z -> z.getParamsForByKey(KeysDictionary.HD))
                    .filter(z -> z != null && !z.equals(""))
                    .filter(z -> !z.isEmpty())
                    .forEach(collect -> {
                        List<String> collect1 = collect.stream()
                                .filter(z -> z != null && !z.equals(""))
                                .collect(Collectors.toList());
                        if (collect1.size() > 1) {
                            System.out.println(system.ILBId + " " + "Found wrong attribute " + KeysDictionary.HD);
                            collect1.forEach(c -> System.out.println("    " + c));
                        }
                    });

            system.pairs.stream()
                    .map(z -> z.getParamsForByKey(KeysDictionary.ADS))
                    .filter(z -> z != null && !z.equals(""))
                    .filter(z -> !z.isEmpty())
                    .forEach(collect -> {
                        List<String> collect1 = collect.stream()
                                .filter(z -> z != null && !z.equals(""))
                                .collect(Collectors.toList());
                        if (collect1.size() > 1) {
                            System.out.println(system.ILBId + " " + "Found wrong attribute " + KeysDictionary.ADS);
                            collect1.forEach(c -> System.out.println("    " + c));
                        }
                    });

            system.pairs.stream()
                    .map(z -> z.getParamsForByKey(KeysDictionary.BAYER))
                    .filter(z -> z != null && !"".equals(z))
                    .filter(z -> !z.isEmpty())
                    .forEach(collect -> {
                        List<String> collect1 = collect.stream()
                                .filter(z -> z != null && !z.equals(""))
                                .collect(Collectors.toList());
                        if (collect1.size() > 1) {
                            System.out.println(system.ILBId + " " + "Found wrong attribute " + KeysDictionary.BAYER);
                            collect1.forEach(c -> System.out.println("    " + c));
                        }
                    });
        }

        for (StarSystem system : storage.sysList) {

            List<String> systemIds = system.pairs.stream()
                    .map(NodeILB::getAllSystemIds)
                    .flatMap(Collection::stream)
                    .distinct()
                    .collect(Collectors.toList());

            systemIds.forEach(System.out::println);

            if (systemIds.size() > 2) {
                System.err.println("Found complex system:");
                systemIds.forEach(System.err::println);
            }

            if (systemIds.size() == 2) {
                System.out.println("Found complex system:");
                systemIds.forEach(System.err::println);
            }
        }
    }
}
