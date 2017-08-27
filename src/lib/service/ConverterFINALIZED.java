package lib.service;

import ILBprocessing.configuration.KeysDictionary;
import lib.pattern.NodeForParsedCatalogue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;

public class ConverterFINALIZED {
	public static double hrsToRad(double I, double F){
		double HRS=(int) (I/10000);
		double MIN=(int) ((I-HRS*10000)/100);
		double sec=I-HRS*10000-MIN*100+F/100;
		double rad = (((HRS*60)+MIN)*60+sec)*2*Math.PI/24/60/60;
		return rad;
	}
	public static double grToRad(double I, double F){
		double GRAD = (int)I/10000;
		double MIN = (int)((I-GRAD*10000)/100);
		double sec= I-GRAD*10000-MIN*100+F/10;
		double rad = (((GRAD*60)+MIN)*60+sec)*2*Math.PI/360/60/60;
		return rad;
	}
	public static double radToHrs(double rad){
		double I=rad/2/Math.PI*24*60*60;
		if(I<0){
			I+=24*60*60;
		}
		if(I>24*60*60){
			I-=24*60*60;
		}

		int HRS=(int) (I/3600);
		int MIN=(int) ((I-HRS*3600)/60);
		double sec=(I-HRS*3600-MIN*60);

		String g=""+HRS;
		if(g.length()==1){
			g="0"+g;
		}
		String m=""+Math.abs(MIN);
		if(m.length()==1){
			m="0"+m;
		}
		String s=""+Math.abs(sec);
		if(sec<10){
			s="0"+s;
		}
		I=Double.parseDouble(""+g+m+s);
		return I;
	}
	public static double radToGr(double rad){
		/*while(rad<0){
			rad+=Math.PI*2;
		}
		while(rad>Math.PI*2){
			rad-=Math.PI*2;
		}*/
		double I=rad/2/Math.PI*360*60*60;
		int GRS=(int) (I/3600);
		String minusFlag="";
		if(GRS==0 && I<0){
			minusFlag="-";
		}
		int MIN=(int) ((I-GRS*3600)/60);
		double sec=(I-GRS*3600-MIN*60);
		String g=""+GRS;
		if(g.length()==1){
			g="0"+g;
		}
		String m=""+Math.abs(MIN);
		if(m.length()==1){
			m="0"+m;
		}
		String s=""+Math.abs(sec);
		if(Math.abs(sec)<10){
			s="0"+s;
		}
		I=Double.parseDouble(minusFlag+g+m+s);
		return I;
	}
	public static ArrayList<Integer> sortByValue(Map<Integer, String> map ) {
		ArrayList list = new ArrayList(map.keySet());
		Collections.sort(list, new Comparator<Integer>() {
			@Override
			public int compare(Integer a, Integer b) {
				return (b - a);
			}
		});
		return list;
	}
	public static void calculateRhoTheta(NodeForParsedCatalogue e){
		double x1=Double.parseDouble(e.params.get(KeysDictionary.COORD_I1_1))+Double.parseDouble(e.params.get(KeysDictionary.COORD_F1_1))/100;
        double x2=Double.parseDouble(e.params.get(KeysDictionary.COORD_I2_1))+Double.parseDouble(e.params.get(KeysDictionary.COORD_F2_1))/100;
        double y1=Double.parseDouble(e.params.get(KeysDictionary.COORD_I1_2))+Double.parseDouble(e.params.get(KeysDictionary.COORD_F1_2))/10;
        double y2=Double.parseDouble(e.params.get(KeysDictionary.COORD_I2_2))+Double.parseDouble(e.params.get(KeysDictionary.COORD_F2_2))/10;
        double x = x2-x1;
        double y = y2-y1;
        double rho=Math.sqrt(x*x+y*y);
        double theta=Math.atan2(x,y)/Math.PI*180;

        //e.params.put(KeysDictionary.RHO,""+rho);
        //e.params.put(KeysDictionary.THETA,""+theta);
        e.params.put(KeysDictionary.X,""+x1);
        e.params.put(KeysDictionary.Y,""+y1);
	}
}