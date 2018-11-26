package ru.inasan.karchevsky.catalogues.ccdm;

import com.google.common.collect.Lists;
import ru.inasan.karchevsky.configuration.SharedConstants;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collection;

public class ParserCCDM implements SharedConstants {

    public static void parseCCDM(String xxx, Collection<NodeCCDM> nodes, Collection<NodeCCDMAstrometric> nodesAstrometric) {
        try {
            String[] fileName = {"CCDM" + xxx + ".txt"};
            File dataFile = new File(OUTPUT_FOLDER + CCDM_GENERATED_STUFF + "/" + fileName[0]);
            FileReader in = new FileReader(dataFile);
            char c;
            long d = dataFile.length();
            StringBuffer ss = new StringBuffer();
            ArrayList<CCDMHelperComponent> listCCDMComponents = Lists.newArrayList();
            String currentSystem = "initial";
            for (long i = 0; i < d; i++) {
                c = (char) in.read();
                ss.append(c);
                if (c == 10) {
                    String s = ss.toString();
                    if (s.length() > 1) {
                        try {
                            CCDMHelperComponent star = new CCDMHelperComponent(s);
                            if (currentSystem.equals(star.ccdmID)) {
                                listCCDMComponents.add(star);
                            } else {
                                if (!currentSystem.equals("initial")) {
                                    CCDMHelperComponent.translateToPairs(listCCDMComponents, nodes);
                                }
                                listCCDMComponents = new ArrayList<>();
                                listCCDMComponents.add(star);
                                currentSystem = star.ccdmID;
                            }

                            if (s.charAt(14) == '%' || s.charAt(14) == '&') {
                                NodeCCDMAstrometric starAstrometric = new NodeCCDMAstrometric(s);
                                nodesAstrometric.add(starAstrometric);
                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    ss = new StringBuffer();
                }
            }
            CCDMHelperComponent.translateToPairs(listCCDMComponents, nodes);
            if (LOGGING_LEVEL_VERBOSE_ENABLED) System.out.println("       Success. fileLength=" + dataFile.length());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
