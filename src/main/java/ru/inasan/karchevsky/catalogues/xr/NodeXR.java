package ru.inasan.karchevsky.catalogues.xr;

import ru.inasan.karchevsky.configuration.KeysDictionary;
import ru.inasan.karchevsky.lib.pattern.NodeForParsedCatalogue;

/**
 * Created by Karchevsky on 20.09.2017.
 */
public class NodeXR extends NodeForParsedCatalogue {
    public static String uniqueCatalogueID = "XR";
    public String coordinates ="";
    boolean isEclipsing;
    boolean isSB;

    public NodeXR(String source){
        this.source=source;
        int xh = Integer.parseInt(source.substring(79,81));
        int xm = Integer.parseInt(source.substring(82,84));
        double xs = Double.parseDouble(source.substring(85,92));

        double yh  = Integer.parseInt(source.substring(94,97));
        double ym = Integer.parseInt(source.substring(98,100));
        double ys = Double.parseDouble(source.substring(101, 107).replaceAll(" ","0"));

        if(yh<0){
            ym*=-1;
            ys*=-1;
        }

        String xhs =""+xh;
        if(xh<10){
            xhs="0"+xhs;
        }
        String xms =""+xm;
        if(xm<10){
            xms="0"+xms;
        }
        String xss =""+xs;
        if(xs<10){
            xss="0"+xss;
        }
        params.put(KeysDictionary.COORD_I1_1,""+xhs+xms+xss);

        double X = (((xh*60+xm)*60+xs))/24.0/60/60*360;
        double Y = (((yh*60+ym)*60+ys))/60.0/60;

        //System.err.println(xhs+xms+xss+ " " + X + " " + Y);

        params.put(KeysDictionary.X,X+"");
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
