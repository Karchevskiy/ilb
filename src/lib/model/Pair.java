package lib.model;

public class Pair extends Component {

	public Component el1;//reference to pair or component
	public Component el2;//reference to pair or component
	public Component equalNodeOnNextLevel = null;//reference to component

	public Pair(){
		el1=new Component();
		el2=new Component();
	}
}
