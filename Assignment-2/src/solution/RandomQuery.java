package solution;

public class RandomQuery {
	private Node node;
	private boolean narrowPassage;
	
	public RandomQuery (Node node, boolean isNarrow){
		this.node = node;
		narrowPassage = isNarrow;
	}
	
	//Queries
	public Node getNode(){
		return node;
	}
	public boolean isNarrowPassage(){
		return narrowPassage;
	}
}
