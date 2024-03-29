package hypergraph;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Node implements Cloneable {
	String name;
	List<Hyperarc> prev;
	List<Hyperarc> next;
	List<Hmark> mark;

	public Node(String name) {
		this.name = name;
		this.prev =  new ArrayList<Hyperarc>();
		this.next = new ArrayList<Hyperarc>();
		this.mark = new ArrayList<Hmark>();
	}

	public Node clone() {
		Node ret = new Node(this.name);
		ret.next = this.next;
		ret.mark = this.mark;
		return ret;
	}

	public String toString() {
		return name;
	}

	public boolean equals(Object o) {
		if (o == null) {
			return false;
		}
		if (o.getClass() != this.getClass()) {
			return false;
		}
		return this.name.equals(((Node) o).name);
	}

	public void addNext(Hyperarc arc) {
		if (!next.contains(arc)) {
			next.add(arc);
		}
	}

	public void addPrev(Hyperarc arc) {
		if (!prev.contains(arc)) {
			prev.add(arc);
		}
	}
	
	public boolean contains(Hyperarc arc) {
		return next.contains(arc);
	}

	public int hashCode() {
		return this.name.hashCode();
	}

	public String getName() {
		return name;
	}

	public void clearMark() {
		mark.clear();
	}

	public boolean isTotallyMarked() {
		Set<Hyperarc> prevs = new HashSet<Hyperarc>();
		for (Hmark m : mark) {
			prevs.add(m.last);
		}
		return !mark.isEmpty() && prevs.size() == mark.size();
	}

	public boolean isMarked(){
		return !mark.isEmpty();
	}
	
	public void addMark(Hyperarc arc) {
		List<HashSet<Hyperarc>> aux;
		aux = arc.getCost();
		Hmark tmp;
		for (HashSet<Hyperarc> hs : aux) {
			tmp = new Hmark(hs, arc);
			//System.out.println("marqué " + this + " con " + arc + " con "
//					+ tmp.getCost());
			mark.add(tmp);
		}
	}


	public int getMin() {
		Integer i = null;
		for (Hmark m : mark) {
//			//System.out.println(m.cost);
			if (i == null || m.cost < i) {
				//System.out.println(m.cost);
				i = m.cost;
			}
		}
		//System.out.println("Peso mínimo para " + this + "=" + i);
		return i;
	}

	public Hmark getMinArc() {
		int mini = getMin();
		for (Hmark m : mark) {
			if (m.cost == mini) {
				return m;
			}
		}
		return null;
	}

	public void init() {
		/*
		 * Esta función sería usada para inicializar el peso del nodo inicial.
		 */

		addMark(new Hyperarc("", 0));
	}

	public List<Hmark> getOrigins() {
		return mark;
	}

	public class Hmark implements Cloneable {
		Set<Hyperarc> arcs = new HashSet<Hyperarc>();
		Hyperarc last;
		int cost;

		Hmark(Set<Hyperarc> arcs, Hyperarc arc) {
			this.arcs = arcs;
			this.arcs.add(arc);
			for (Hyperarc a : arcs) {
				cost += a.getValue();
			}
			this.last = arc;
		}

		public int getCost() {
			return cost;
		}

		public Hyperarc getOrigin() {
			return last;
		}

		public Set<Hyperarc> getAncestors() {
			return arcs;
		}

		public boolean equals(Hmark mark) {
			return arcs.equals(mark.arcs);
		}

		public int hashCode() {
			return arcs.hashCode();
		}

		public Hmark clone() {
			return new Hmark(this.arcs, this.last);
		}

		public String toString() {
			return this.last.toString();
		}
	}

}
