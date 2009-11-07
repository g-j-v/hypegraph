package hypergraph;
import java.util.ArrayList;
import java.util.List;

public class Node {
	String name;
	Integer tag;
	List<Hyperarc> adj;
	List<Hmark> mark;
	int prevHyps;

	public Node(String name) {
		this.name = name;
		this.tag = null;
		this.adj = new ArrayList<Hyperarc>();
		this.mark=new ArrayList<Hmark>();
		this.prevHyps = 0;
	}
	
	

	public String toString(){
		return name;
	}
	
	public boolean equals(Object o) {
		if(o == null){
			return false;
		}
		if (o.getClass() != this.getClass()) {
			return false;
		}
		return this.name.equals(((Node) o).name);
	}
	
	public void addArc(Hyperarc arc){
		if(!adj.contains(arc)){
			adj.add(arc);
		}
	}
	
	public boolean contains(Hyperarc arc){
		return adj.contains(arc);
	}
	
	public int hashCode(){
		return name.hashCode();
	}

	public String getName() {
		return name;
	}
	
	public void clearMark(){
		mark.clear();
	}
	
	public boolean isMarked(){
		return !mark.isEmpty() && mark.size()==prevHyps;
	}
	
	public void addMark(Hyperarc arc){
		int aux = arc.getValue() + arc.calculateCost();
		mark.add(new Hmark(arc, aux));
	}
	
	
	
	public void mark(Hyperarc arc){
		Hmark tmp = new Hmark(arc, arc.Cost());
		mark.add(tmp);
	}
	
	public int getMin(){
		Integer i=null;
		for(Hmark m : mark){
			if(i == null || m.cost < i){
				i=m.cost;
			}
		}
		System.out.println("Peso mínimo para " + this + "=" + i);
		return i;
	}
	
	public Hyperarc getMinArc(){
		int mini=getMin();
		for(Hmark m : mark){
			if(m.cost == mini){
				return m.arc;
			}
		}
		return null;
	}
	
	public void init(){
		/*
		 * Esta función sería usada para inicializar el peso del nodo inicial. 
		 */
		
		addMark(new Hyperarc("", 0));
		prevHyps=1;
	}
	
	public List<Hmark> getOrigins(){
		return mark;
	}
	
	public void growHyp(){
		prevHyps++;
	}
	
	public class Hmark {
		Hyperarc arc;
		int cost;
		
		Hmark(Hyperarc arc, int cost){
			this.arc=arc;
			this.cost=cost;
		}
		
		public int getCost(){
			return cost;
		}
		
		public Hyperarc getOrigin(){
			return arc;
		}

		public boolean equals(Hmark mark){
			return arc.equals(mark.arc);
		}
		
		public int hashCode(){
			return arc.hashCode();
		}
	}
	
	
}
