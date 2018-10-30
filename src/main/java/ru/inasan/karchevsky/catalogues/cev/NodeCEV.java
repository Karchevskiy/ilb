package ru.inasan.karchevsky.catalogues.cev;

import ru.inasan.karchevsky.configuration.KeysDictionary;
import ru.inasan.karchevsky.lib.pattern.NodeForParsedCatalogue;
import ru.inasan.karchevsky.storage.HelperComponent;

import java.io.Serializable;

/**
 * Created by Алекс on 06.04.2017.
 */
public class NodeCEV extends NodeForParsedCatalogue implements Serializable {
    public static String uniqueCatalogueID = "CEV";

    public double theta;
    public double rho;
    public HelperComponent el1 = new HelperComponent();
    public HelperComponent el2 = new HelperComponent();

    public NodeCEV(String s) {
        source = s;
        params.put(KeysDictionary.CEV, s.substring(0, 9));
        String crd = s.substring(123, 139);
        params.put(KeysDictionary.COORD_I1_1, crd);

        int c1 = Integer.parseInt(crd.substring(0, 2));
        int c2 = Integer.parseInt(crd.substring(2, 4));
        double c3 = Double.parseDouble(crd.substring(4, 8));
        boolean minus = crd.charAt(9) == '-';
        //System.err.print(c1+""+c2+""+c3+"   ");
        double x = (c1 * 60 + c2) * 60 + c3;
        double X = x / 24 / 60 / 60 * 360;
        int d1 = Integer.parseInt(crd.substring(10, 12));
        int d2 = Integer.parseInt(crd.substring(12, 14));
        int d3 = Integer.parseInt(crd.substring(14, 16));

        double y = (d1 * 60 + d2) * 60 + d3;
        if (minus) {
            y *= -1;
        }
        double Y = y / 60 / 60;

        params.put(KeysDictionary.X, X + "");
        params.put(KeysDictionary.Y, Y + "");

    };

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