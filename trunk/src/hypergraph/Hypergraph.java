package hypergraph;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class Hypergraph {

	private Node start;
	private Node end;
	private Set<Node> nodes;
	private Set<Hyperarc> hyperArcs;

	public Hypergraph() {
		nodes = new HashSet<Node>();
		hyperArcs = new HashSet<Hyperarc>();
	}

	public boolean addHyperarc(String name, Integer value) {
		Hyperarc tmp = new Hyperarc(name, value);
		if (hyperArcs.contains(tmp)) {
			return false;
		}
		return hyperArcs.add(tmp);
	}

	public boolean addHyperarc(Hyperarc arc) {
		if (hyperArcs.contains(arc)) {
			return false;
		}
		hyperArcs.add(arc);
		for(Node node: arc.getHead()){
			addHead(node, arc);
		}
		for(Node node: arc.getTail()){
			addTail(node, arc);
		}
		return true;
	}

	public boolean addNode(Node node) {
		return nodes.add(node);
	}

	public boolean addHead(Node node, Hyperarc arc) {
		if (hyperArcs.contains(arc)) {
			if (!nodes.contains(node)) {
				nodes.add(node);
			}
			
			for (Node n : nodes) {
				if (n.equals(node)) {
					node=n;
					n.growHyp();
				}
			}
			
			arc.getHead().add(node);
			return true;
		}
		return false;
	}

	public boolean addTail(Node node, Hyperarc arc) {
		if (hyperArcs.contains(arc)) {
			if (!nodes.contains(node)) {
				nodes.add(node);
			}
			
			for (Node n : nodes) {
				if (n.equals(node)) {
					node = n;
					n.addArc(arc);
				}
			}
			
			arc.addTail(node);
			return true;
		}
		return false;
	}

	public Set<Hyperarc> getHyperArcs() {
		return hyperArcs;
	}

	public Set<Node> getNodes() {
		return nodes;
	}

	public boolean hasNode(Node node) {
		return nodes.contains(node);
	}

	public Hypergraph minPath() {

		clearMarks();

		Iterator<Node> iter= nodes.iterator();
		for(Node node=iter.next(); !iter.hasNext() || node.equals(start) ; node=iter.next()){
		
			if(node.equals(start)){
				node.init();
			}
		}
		minPath(start, end);
		Hypergraph g = new Hypergraph();
		g.setStart(this.start);
		g.setEnd(this.end);
		Hyperarc arc = end.getMinArc();
		return tracePath(arc, g);
	}

	private Hypergraph tracePath(Hyperarc arc, Hypergraph graph) {
		if(arc == null){
			return null;
		}
		graph.addHyperarc(arc);
		if (!arc.getTail().isEmpty()) {
			if (arc.getTail().contains(start)) {
				return graph;
			}
		}
		for (Node n : arc.getTail()) {
			tracePath(n.getMinArc(), graph);
		}

		return graph;
	}

	private void minPath(Node ini, Node fin) {
		if (ini.equals(fin)) {
			return;
		}
		for (Hyperarc arc : ini.adj) {
			if (arc.isMarked()) {
				for (Node n : arc.getHead()) {
					n.addMark(arc);
					if (n.isMarked()) {
						minPath(n, fin);
					}
				}
			}
		}
	}

	public void setStart(Node start) {
		if (!nodes.contains(start)) {
			nodes.add(start);
		}
		this.start = start;
	}

	public Node getLast() {
		return end;
	}

	public void setEnd(Node end) {
		if (!nodes.contains(end)) {
			nodes.add(end);
		}
		this.end = end;
	}

	public String toString() {
		String aux = "Inicio: " + start + ", Fin: " + end + "\n Nodes cant: "
				+ nodes.size() + "\nNodes:";
		for (Node node : nodes) {
			aux += " " + node;
		}
		aux += "\nHyperarcs: ";
		for (Hyperarc arc : hyperArcs) {
			aux += " " + arc;
		}

		return aux;
	}

	public void clearMarks() {
		for (Node node : nodes) {
			node.clearMark();
		}

		for (Hyperarc arc : hyperArcs) {
			arc.clearMark();
		}
	}

}
