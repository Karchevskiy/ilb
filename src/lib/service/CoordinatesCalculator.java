package lib.service;

import javafx.util.Pair;


public class CoordinatesCalculator{

    /**
     * Get values in radians and return in radians*/
    public static Pair<Double,Double> calculateByRhoTheta(double a1, double d1, double rh, double th){
        //f1=Math.PI/2-f1;
        if(th<0){
            th+=2.0d*Math.PI;
        }
        if(th>2.0d*Math.PI){
            th-=2.0d*Math.PI;
        }
        double dd =rh/(Math.sqrt(1.0d+tan(th)*tan(th)*cos(d1)));
        double da=Math.sqrt(rh*sin(th)/cos(d1)*rh*sin(th)/cos(d1));
        if(th>=0 && th<Math.PI/2){
            //da+
            if(d1>0){
                //dd+
            }else{
                dd=-dd;
            }
        }else if(th>=Math.PI/2 && th<Math.PI){
            //da+
            if(d1>0){
                dd=-dd;
            }else{
            }
        }else if(th>=Math.PI && th<Math.PI*3/2){
            da=-da;
            if(d1>0){
                dd=-dd;
            }else{
            }
        }else if(th<2*Math.PI && th>=Math.PI*3/2){
            da=-da;
            if(d1>0){
            }else{
                dd=-dd;
            }
        }
        double a2=a1+da;
        double d2=d1+dd;
        return new Pair<>(a2,d2);
    }
    private static double sin(double o){
        return Math.sin(o);
    }
    private static double cos(double o){
        return Math.cos(o);
    }
    private static double tan(double o){
        return Math.tan(o);
    }
}
