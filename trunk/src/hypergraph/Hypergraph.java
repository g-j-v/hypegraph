package hypergraph;

import java.util.ArrayList;
import java.util.Deque;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class Hypergraph {

	private Node start;
	private Node end;
	private Set<Node> nodes;
	private Set<Hyperarc> hyperArcs;
	public static Set<Hyperarc> minPath;
	public static int minWeight;

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
					n.addPrev(arc);
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
					n.addNext(arc);
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

		possMinPath(start, end);
		Hypergraph g = new Hypergraph();
		g.setStart(this.start);
		g.setEnd(this.end);

		Hyperarc arc = end.getMinArc().last;
		System.out.println("key0: " + arc);
		minPath = tracePath(arc, g).hyperArcs;
		System.out.println("key: " + Hypergraph.minPath);
		minWeight = calculateWeight(minPath);

		clearMarks();

		// Set<Hyperarc> ret = new HashSet<Hyperarc>();
		// ret = (HashSet<Hyperarc>)realMinPath(hg.start, hg.end, hg,
		// calculateWeight());

		realMinPathWrap(this.start, this.end);

		Hypergraph hg = new Hypergraph();
		hg.setStart(this.start);
		hg.setEnd(this.end);

		for (Hyperarc ha : minPath) {
			hg.addHyperarc(ha);
		}
		return hg;

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

			tracePath(n.getMinArc().last, graph);

		}

		return graph;
	}

	private void possMinPath(Node ini, Node fin) {
		if (ini.equals(fin)) {
			System.out.println("vuelvoo!");
			return;
		}
		for (Hyperarc arc : ini.next) {
			System.out.println("arco " + arc);
			if (arc.isMarked()) {
				System.out.println(arc + " está marcada!");
				for (Node n : arc.getHead()) {
					System.out.println("nodo " + n + " para arco " + arc);
					n.addMark(arc);
					if (n.isTotallyMarked()) {
						System.out.println(n + " está marcada!");
						possMinPath(n, fin);
					}
				}
			}
		}
		System.out.println("retornando");
	}

	private void realMinPathWrap(Node start, Node end) {
		Set<Hyperarc> hs1 = new HashSet<Hyperarc>();
		Set<Hyperarc> hs2 = new HashSet<Hyperarc>();
		Set<Node> set = new HashSet<Node>();
		set.add(end);

		for (Hyperarc arc : end.prev) {
			hs2.add(arc);
			realMinPath(start, set, hs1, hs2);
			hs2.clear();
		}
	}

	private void realMinPath(Node start, Set<Node> ends, Set<Hyperarc> acum,
			Set<Hyperarc> last) {

		int weight = calculateWeight(acum) + calculateWeight(last);

		/*
		 * if (weight > Hypergraph.minWeight) {
		 * System.out.println("no sigo porque");
		 * System.out.println(acum.toString() + last.toString());
		 * System.out.println("era largo"); return; }
		 */
		System.out.println("@@@@@@@@@@@@@@@@@@@@@@");
		System.out.println("last = " + last);
		System.out.println("ends = " + ends);
		System.out.println("ends.size() " + ends.size());
		System.out.println(start);
		System.out.println(ends.toArray()[0]);
		System.out.println("@@@@@@@@@@@@@@@@@@@@@@");
		if (ends.size() == 1 && start.equals(ends.toArray()[0])) {
			System.out.println("entroooooo");
			System.out.println("actual peso: " + weight);
			System.out.println("minimo: " + minWeight);
			System.out.println("camino" + minPath);
			if (weight < minWeight) {
				Hypergraph.minWeight = weight;
				Hypergraph.minPath.clear();
				Hypergraph.minPath.addAll(acum);
				Hypergraph.minPath.addAll(last);
			}
			return;
		}

		Set<Hyperarc> newAcum = new HashSet<Hyperarc>();
		newAcum.addAll(acum);
		newAcum.addAll(last);

		Set<Node> nodes = new HashSet<Node>();

		for (Hyperarc ha : last) {
			nodes.addAll(ha.getTail());
		}

		System.out.println("nodos = " + nodes);
		Deque<Set<Hyperarc>> queue = new LinkedList<Set<Hyperarc>>();

		Iterator<Node> iter = nodes.iterator();
		if (iter.hasNext()) {
			Node n = iter.next();
			// System.out.println("es el siguiente? = " + n);
			// System.out.println("cola a encolar = " + queue);
			arcPermut(n, iter, new ArrayList<Hyperarc>(), queue);
			System.out.println("cola encolada = " + queue);
		}
		/*
		 * Ahora copiar el código de Hyperarc.varyMark para hacer las
		 * combinatorias de aristas y agregarlas al deque de sets de aristas
		 */
		System.out.println(queue);
		while (!queue.isEmpty()) {
			Set<Hyperarc> haset = queue.poll();
			// sSystem.out.println(queue);
			// (System.out.println(haset);
			realMinPath(start, nodes, newAcum, haset);
		}

	}

	private void arcPermut(Node current, Iterator<Node> iter,
			List<Hyperarc> arcs, Deque<Set<Hyperarc>> queue) {

		if (current == null) {
			queue.offer(new HashSet<Hyperarc>(arcs));
			return;
		}

		Node next = null;
		if (iter.hasNext()) {
			next = iter.next();
		}

		System.out.println(current);
		if (current.equals(start)) {
			arcPermut(next, iter, arcs, queue);
		} else {
			for (Hyperarc arc : current.prev) {
				boolean added = arcs.add(arc);
				arcPermut(next, iter, arcs, queue);
				if (added) {
					arcs.remove(arcs.size() - 1);
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

	private static int calculateWeight(Set<Hyperarc> arcs) {
		int w = 0;
		for (Hyperarc arc : arcs) {
			w += arc.getValue();
		}
		return w;
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
