package ru.inasan.karchevsky.catalogues.tdsc;

import ru.inasan.karchevsky.configuration.SharedConstants;
import ru.inasan.karchevsky.lib.pattern.NodeForParsedCatalogue;
import ru.inasan.karchevsky.storage.CachedStorageILB;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collection;

public class ParserTDSC implements SharedConstants {

    public static void parseTDSC(String xxx, Collection<NodeTDSC> nodes) {
        try {
            String[] fileName = {"TDSC" + xxx + ".txt"};//all zones;
            File dataFile = new File(OUTPUT_FOLDER + TDSC_GENERATED_STUFF + "/" + fileName[0]);
            FileReader in = new FileReader(dataFile);
            char c;
            StringBuffer ss = new StringBuffer();
            ArrayList<NodeTDSCHelperComponent> listTDSCComponents = new ArrayList<NodeTDSCHelperComponent>();
            String currentSystem = "initial";
            long d = dataFile.length();
            for (long i = 0; i < d; i++) {
                c = (char) in.read();
                ss.append(c);
                if (c == 10) {
                    String s = ss.toString();
                    if (s.length() > 1) {
                        try {
                            NodeTDSCHelperComponent star = new NodeTDSCHelperComponent(s);
                            if (currentSystem.equals(star.tdscID)) {
                                listTDSCComponents.add(star);
                            } else {
                                if (!currentSystem.equals("initial")) {
                                    NodeTDSCHelperComponent.translateToPairs(listTDSCComponents, nodes);
                                }
                                listTDSCComponents = new ArrayList<>();
                                listTDSCComponents.add(star);
                                currentSystem = star.tdscID;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    ss = new StringBuffer();
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
