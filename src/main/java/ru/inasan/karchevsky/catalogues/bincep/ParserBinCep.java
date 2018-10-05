package ru.inasan.karchevsky.catalogues.bincep;

import ru.inasan.karchevsky.configuration.SharedConstants;

import java.io.File;
import java.io.FileReader;
import java.util.Collection;


public class ParserBinCep implements SharedConstants {
    public static void parseBinCep(Collection<NodeBinCep> nodes) {
        System.out.println("       parse BinCEP");
        try {
            File dataFile = new File(INPUT_FOLDER + BIN_CEP_SOURCE_FILE);
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
                    NodeBinCep star = new NodeBinCep(s);
                    nodes.add(star);
                    ss = new StringBuffer();
                }
            }
            if (LOGGING_LEVEL_VERBOSE_ENABLED)
                System.out.println("       Success NodeBinCep. fileLength=" + dataFile.length());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}