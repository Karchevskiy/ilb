package lib.model;

public class Pair extends Component {

	public Component el1;//reference to pair or component
	public Component el2;//reference to pair or component
	public Component equalNodeOnNextLevel = null;//reference to component
	public char[] modifier;  // [VO_____]

	public Pair(){
		el1=new Component();
		el2=new Component();
		modifier =new char[10];
		for(int i=0;i<10;i++){
			modifier[i]=0;
		}
	}
}
