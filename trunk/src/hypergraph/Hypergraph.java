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
		for (Node node : arc.getHead()) {
			addHead(node, arc);
		}
		for (Node node : arc.getTail()) {
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
					node = n;
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

		Iterator<Node> iter = nodes.iterator();
		// System.out.println(nodes.size());
		Node node = null;
		// System.out.println(node);

		while (iter.hasNext() && !start.equals(node)) {
			node = iter.next();
			// System.out.println("veo " + node);
		}
		if (node.equals(start)) {
			System.out.println("inicia " + node);
			node.init();
		}

		minPath(start, end);
		Hypergraph g = new Hypergraph();
		g.setStart(this.start);
		g.setEnd(this.end);
		Hyperarc arc = end.getMinArc().last;
		return tracePath(arc, g);
	}

	private Hypergraph tracePath(Hyperarc arc, Hypergraph graph) {
		if (arc == null) {
			// System.out.println("volviendo!");
			return null;
		}
		graph.addHyperarc(arc);
		if (!arc.getTail().isEmpty()) {
			if (arc.getTail().contains(start)) {
				return graph;
			}
		}
		for (Node n : arc.getTail()) {
			// System.out.println("arco " + arc + " y nodo" + n);
			tracePath(n.getMinArc().last, graph);
		}

		return graph;
	}

	private void minPath(Node ini, Node fin) {
		if (ini.equals(fin)) {
			// System.out.println("vuelvoo!");
			return;
		}
		for (Hyperarc arc : ini.adj) {
			System.out.println("arco " + arc);
			if (arc.isMarked()) {
				System.out.println(arc + " está marcada!");
				for (Node n : arc.getHead()) {
					System.out.println("nodo " + n + " para arco " + arc);
					n.addMark(arc);
					if (n.isMarked()) {
						System.out.println(n + " está marcada!");
						minPath(n, fin);
					}
				}
			}
		}
		System.out.println("retornando");
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
