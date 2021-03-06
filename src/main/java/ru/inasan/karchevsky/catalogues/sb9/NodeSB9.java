package ru.inasan.karchevsky.catalogues.sb9;

import ru.inasan.karchevsky.configuration.KeysDictionary;
import ru.inasan.karchevsky.lib.pattern.NodeForParsedCatalogue;
import ru.inasan.karchevsky.storage.HelperComponent;

import java.io.Serializable;

/**
 * Created by Алекс on 06.04.2017.
 */
public class NodeSB9 extends NodeForParsedCatalogue implements Serializable {
    public static String uniqueCatalogueID = "SB9";

    public double theta;
    public double rho;
    public boolean updated = false;
    public int key = 0;
    public HelperComponent el1 = new HelperComponent();
    public HelperComponent el2 = new HelperComponent();

    public NodeSB9(String s) {
        source = s;
        String[] values = s.split("\\|");

        key = Integer.parseInt(values[0]);
        params.put(KeysDictionary.SB9, values[0] + "");
        params.put(KeysDictionary.WDSSYSTEM, values[1]);

        params.put(KeysDictionary.RHO, "" + 0.0000000001d);
        params.put(KeysDictionary.THETA, "" + 0.0000000001d);

        if (values.length > 4) {
            params.put(KeysDictionary.UBV, values[4]);
        }
        String x, y;
        boolean plus = false;
        if (values[2].contains("+")) {
            String[] coords = values[2].split("\\+");
            x = coords[0];
            y = coords[1];
            plus = true;
        } else {
            String[] coords = values[2].split("-");
            x = coords[0];
            y = coords[1];
        }

        params.put(KeysDictionary.COORD_I1_1, x);

        int xh = Integer.parseInt(x.substring(0, 2));
        int yh = Integer.parseInt(y.substring(0, 2));


        int xm = Integer.parseInt(x.substring(2, 4));
        int ym = Integer.parseInt(y.substring(2, 4));


        int xs = Integer.parseInt(x.substring(4, 6));
        int ys = Integer.parseInt(y.substring(4, 6));


        int xms = Integer.parseInt(x.substring(6, x.length()));
        int yms = 0;
        int dx = x.length() - 6;
        int dy = y.length() - 6;
        if (y.length() > 6) {
            yms = Integer.parseInt(y.substring(6, y.length()));
        }


        double X = (((xh * 60 + xm) * 60 + xs) * 100 + 100 * xms / Math.pow(10, dx)) / 24 / 60 / 60 / 100 * 360;
        double Y = (((yh * 60 + ym) * 60 + ys) * 100 + 100 * yms / Math.pow(10, dy)) / 60 / 60 / 100;

        params.put(KeysDictionary.X, X + "");
        if (plus) {
            params.put(KeysDictionary.Y, Y + "");
        } else {
            params.put(KeysDictionary.Y, -Y + "");
        }
    }

    public void update(String key, String value) {
        switch (key) {
            case "HIP":
                params.put(KeysDictionary.HIP, value.replaceAll("\\\\", "").replaceAll("\\$", ""));
                break;
            case "Flamsteed":
                params.put(KeysDictionary.FLAMSTEED, value.replaceAll("\\\\", "").replaceAll("\\$", ""));
                break;
            case "Bayer":
                params.put(KeysDictionary.BAYER, value.replaceAll("\\\\", "").replaceAll("\\$", ""));
                break;
            case "BD":
                params.put(KeysDictionary.DM, key + value.replaceAll("\\\\", "").replaceAll("\\$", ""));
                break;
            case "CD":
                params.put(KeysDictionary.DM, key + value.replaceAll("\\\\", "").replaceAll("\\$", ""));
                break;
            case "CPD":
                params.put(KeysDictionary.DM, key + value.replaceAll("\\\\", "").replaceAll("\\$", ""));
                break;
            case "HD":
                params.put(KeysDictionary.HD, value.replaceAll("\\\\", "").replaceAll("\\$", ""));
                break;
            case "GCVS":
                params.put(KeysDictionary.CEV, value.replaceAll("\\\\", "").replaceAll("\\$", ""));
                break;
        }
        updated = true;
    }

    @Override
    public Double getXel1() {
        return null;
    }

    @Override
    public Double getYel1() {
        return null;
    }

    @Override
    public Double getXel2() {
        return null;
    }

    @Override
    public Double getYel2() {
        return null;
    }
}