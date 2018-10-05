package ru.inasan.karchevsky.catalogues.wds;

import ru.inasan.karchevsky.configuration.SharedConstants;

import java.io.File;
import java.io.FileReader;
import java.util.Collection;

public class ParserWDS implements SharedConstants {
    public static void parseWDS(String xxx, Collection<NodeWDS> nodes) {
        try {
            String[] fileName = {"WDS" + xxx + ".txt"};//all zones;
            for (int h = 0; h < fileName.length; h++) {
                if (LOGGING_LEVEL_VERBOSE_ENABLED) System.out.println("Stage:" + h);
                File dataFile = new File(OUTPUT_FOLDER + WDS_GENERATED_STUFF + "/" + fileName[h]);
                FileReader in = new FileReader(dataFile);
                char c;
                StringBuffer ss = new StringBuffer();
                long d = dataFile.length();
                for (long i = 0; i < d; i++) {
                    c = (char) in.read();
                    ss.append(c);
                    if (c == 10) {
                        String s = ss.toString();
                        if (s.length() > 10) {
                            s = s.substring(0, s.length() - 2);
                            NodeWDS star = new NodeWDS(s);
                            nodes.add(star);
                            ss = new StringBuffer();
                        } else {
                            ss = new StringBuffer();
                        }
                    }
                    if (d - i < 1) {
                        String s = ss.toString();
                        NodeWDS star = new NodeWDS(s);
                        nodes.add(star);
                        ss = new StringBuffer();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
