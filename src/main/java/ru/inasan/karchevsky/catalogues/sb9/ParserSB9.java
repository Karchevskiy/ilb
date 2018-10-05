package ru.inasan.karchevsky.catalogues.sb9;

import ru.inasan.karchevsky.configuration.SharedConstants;
import ru.inasan.karchevsky.lib.storage.CachedStorage;
import ru.inasan.karchevsky.storage.CachedStorageILB;

import java.io.File;
import java.io.FileReader;
import java.util.Collection;

public class ParserSB9 implements SharedConstants {

    public static void parseSB9(Collection<NodeSB9> nodes) {
        System.out.println("       parse SB9");
        try {
            File dataFile = new File(INPUT_FOLDER + SB9_SOURCE_FILE);
            FileReader in = new FileReader(dataFile);
            char c;
            StringBuffer ss = new StringBuffer();
            long d = dataFile.length();
            for (long i = 0; i < d; i++) {
                c = (char) in.read();
                ss.append(c);
                if (c == 10) {
                    String s = ss.toString();
                    s = s.substring(0, s.length() - 2);
                    NodeSB9 star = new NodeSB9(s);
                    nodes.add(star);
                    ss = new StringBuffer();
                }
            }
            if (LOGGING_LEVEL_VERBOSE_ENABLED) System.out.println("       Success. fileLength=" + dataFile.length());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void parseSB9params(CachedStorageILB storage) {
        System.out.println("       parse SB9 params");
        try {
            File dataFile = new File(INPUT_FOLDER + SB9_PARAMS_FILE);
            FileReader in = new FileReader(dataFile);
            char c;
            StringBuffer ss = new StringBuffer();
            long d = dataFile.length();
            for (long i = 0; i < d; i++) {
                c = (char) in.read();
                ss.append(c);
                if (c == 10) {
                    String s = ss.toString();
                    s = s.substring(0, s.length() - 1);
                    NodeSB9Params star = new NodeSB9Params(s, storage);
                    ss = new StringBuffer();
                }
            }
            if (LOGGING_LEVEL_VERBOSE_ENABLED) System.out.println("       Success. fileLength=" + dataFile.length());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
