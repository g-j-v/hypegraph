package hypergraph;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Node implements Cloneable {
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
	
	public Node clone(){
		Node ret = new Node(this.name);
		ret.adj=this.adj;
		ret.mark=this.mark;
		ret.prevHyps=this.prevHyps;
		ret.tag= this.tag;
		return ret;
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
		return this.name.hashCode();
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
		Set<Hyperarc> aux;
		aux = arc.getCost();
		Hmark tmp = new Hmark(aux, arc);
		System.out.println("marqué "+ this + " con " + arc + " con " + tmp.getCost());
		mark.add(tmp);
	}
	
	public int getMin(){
		Integer i=null;
		for(Hmark m : mark){
			System.out.println(m.cost);
			if(i == null || m.cost < i){
				System.out.println(m.cost);
				i=m.cost;
			}
		}
		System.out.println("Peso mínimo para " + this + "=" + i);
		return i;
	}
	
	public Hmark getMinArc(){
		int mini=getMin();
		for(Hmark m : mark){
			if(m.cost == mini){
				return m;
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
	
	public class Hmark implements Cloneable{
		Set<Hyperarc> arcs = new HashSet<Hyperarc>();
		Hyperarc last;
		int cost;
		
		Hmark(Set<Hyperarc> arcs, Hyperarc arc){
			this.arcs = arcs;
			this.arcs.add(arc);
			for(Hyperarc a:arcs){
				cost+=a.getValue();
			}
			this.last=arc;		
		}
		
		public int getCost(){
				return cost;
		}
		
		public Hyperarc getOrigin(){
			return last;
		}
		
		public Set<Hyperarc> getAncestors(){
			return arcs;
		}

		public boolean equals(Hmark mark){
			return arcs.equals(mark.arcs);
		}
		
		public int hashCode(){
			return arcs.hashCode();
		}
		
		public Hmark clone(){
			return new Hmark(this.arcs, this.last);
		}
		
		public String toString(){
			return this.last.toString();
		}
	}
	
	
}
