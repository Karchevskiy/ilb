package ru.inasan.karchevsky.catalogues.bincep;

import ru.inasan.karchevsky.configuration.KeysDictionary;
import ru.inasan.karchevsky.lib.pattern.NodeForParsedCatalogue;
import ru.inasan.karchevsky.storage.HelperComponent;

public class NodeBinCep extends NodeForParsedCatalogue {
    public static String uniqueCatalogueID = "BINCEP";

    public double theta;
    public double rho;
    public HelperComponent el1 = new HelperComponent();
    public HelperComponent el2 = new HelperComponent();

    public NodeBinCep(String s) {
        source = s;
        params.put(KeysDictionary.CEV, s.substring(0, 9));

        int hh = Integer.parseInt(s.substring(13, 15));
        int mm = Integer.parseInt(s.substring(16, 18));
        double ss = Double.parseDouble(s.substring(19, 27));


        params.put(KeysDictionary.COORD_I1_1, "" + s.substring(13, 15) + s.substring(16, 18) + s.substring(19, 23));

        boolean minus = s.charAt(28) == '-';

        double x = (hh * 60 + mm) * 60 + ss;
        double X = x / 24 / 60 / 60 * 360;

        int d1 = Integer.parseInt(s.substring(29, 31));
        int d2 = Integer.parseInt(s.substring(32, 34));
        double d3 = Double.parseDouble(s.substring(35, 42));

        double y = (d1 * 60 + d2) * 60 + d3;
        if (minus) {
            y *= -1;
        }
        double Y = y / 60 / 60;

        params.put(KeysDictionary.X, X + "");
        params.put(KeysDictionary.Y, Y + "");

    }

    @Override
    public Double getXel1() {
        return Double.valueOf(params.get(KeysDictionary.X));
    }

    @Override
    public Double getYel1() {
        return Double.valueOf(params.get(KeysDictionary.Y));
    }

    @Override
    public Double getXel2() {
        return Double.valueOf(params.get(KeysDictionary.X));
    }

    @Override
    public Double getYel2() {
        return Double.valueOf(params.get(KeysDictionary.Y + 0.0000000000001d));
    }
}