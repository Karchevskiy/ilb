package ru.inasan.karchevsky.catalogues.int4;

import ru.inasan.karchevsky.configuration.SharedConstants;
import ru.inasan.karchevsky.lib.pattern.NodeForParsedCatalogue;
import ru.inasan.karchevsky.storage.CachedStorageILB;

import javax.management.InvalidAttributeValueException;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collection;

public class ParserINT4 implements SharedConstants {

    public static void parseINT4(String xxx, Collection<NodeINT4> nodes) {
        try {
            String[] fileName = {"INT" + xxx + ".txt"};//all zones;
            for (int h = 0; h < fileName.length; h++) {
                if (LOGGING_LEVEL_VERBOSE_ENABLED) System.out.println("Stage:" + h);
                File dataFile = new File(OUTPUT_FOLDER + INT4_GENERATED_STUFF + "/" + fileName[h]);
                FileReader in = new FileReader(dataFile);
                char c;
                StringBuffer ss = new StringBuffer();
                long d = dataFile.length();
                ArrayList<String> sources = new ArrayList<>();
                for (long i = 0; i < d; i++) {
                    c = (char) in.read();
                    ss.append(c);
                    if (c == 10) {
                        String s = ss.toString();
                        if (s.length() > 10) {
                            s = s.substring(0, s.length() - 2);
                            if (s.charAt(0) != ' ') {
                                if (sources.size() >= 2) {
                                    try {
                                        NodeINT4 star = new NodeINT4(sources);
                                        nodes.add(star);
                                    } catch (InvalidAttributeValueException e) {
                                        if (LOGGING_LEVEL_VERBOSE_ENABLED)
                                            System.err.println("INT4 parse error:" + e.getMessage());
                                    }
                                    sources = new ArrayList<>();
                                }
                                sources.add(s);
                            } else {
                                sources.add(s);
                            }
                            ss = new StringBuffer();
                        } else {
                            ss = new StringBuffer();
                        }
                    }
                    if (d - i < 1) {
                        String s = ss.toString();
                        sources.add(s);
                        NodeINT4 star = new NodeINT4(sources);
                        nodes.add(star);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
