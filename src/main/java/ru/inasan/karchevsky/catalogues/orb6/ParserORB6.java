package ru.inasan.karchevsky.catalogues.orb6;

import ru.inasan.karchevsky.configuration.SharedConstants;
import ru.inasan.karchevsky.lib.model.NodeILB;
import ru.inasan.karchevsky.lib.pattern.NodeForParsedCatalogue;
import ru.inasan.karchevsky.storage.CachedStorageILB;

import java.io.File;
import java.io.FileReader;
import java.util.Collection;

public class ParserORB6 implements SharedConstants {
    public static void parseORB6(Collection<NodeORB6> nodes) {
        System.out.println("       parse SCO");
        try {
            File dataFile = new File(INPUT_FOLDER + SCO_SOURCE_FILE);
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
                    NodeORB6 star = new NodeORB6(s);
                    nodes.add(star);
                    ss = new StringBuffer();
                }
            }
            if (LOGGING_LEVEL_VERBOSE_ENABLED) System.out.println("       Success. fileLength=" + dataFile.length());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
