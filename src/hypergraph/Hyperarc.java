package hypergraph;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class Hyperarc {
	private String name;
	private Set<Node> tail;
	private Set<Node> head;
	private Integer value;
	private boolean mark;
	private Map<Node, Node.Hmark> origins;
	Set<Hyperarc> cost;

	public Hyperarc(String name, Integer value) {
		this.tail = new HashSet<Node>();
		this.head = new HashSet<Node>();
		this.value = value;
		this.name = name;
		this.mark = false;
		this.origins = new HashMap<Node, Node.Hmark>();
		this.cost = new HashSet<Hyperarc>();
	}

	public Set<Node> getTail() {
		return tail;
	}

	public Set<Node> getHead() {
		return head;
	}

	public Map<Node, Node.Hmark> getMarks() {
		return origins;
	}

	public boolean equals(Object o) {
		if (o == null) {
			return false;
		}
		if (o.getClass() != this.getClass()) {
			return false;
		}
		return this.name.equals(((Hyperarc) o).name);
	}

	public boolean setTail(Set<Node> tail) {
		this.tail = tail;
		for (Node n : tail) {
			if (!n.adj.contains(this))
				n.adj.add(this);
		}
		return true;
	}

	public boolean setHead(Set<Node> head) {
		this.head = head;
		return true;// ???
	}

	public boolean addHead(Node node) {
		return head.add(node);
	}

	public boolean addTail(Node node) {
		return tail.add(node);
	}

	public String toString() {
		return name;
	}

	public String getName() {
		return name;
	}

	public Integer getValue() {
		return value;
	}

	public int hashCode() {
		return name.hashCode();
	}

	public void clearMark() {
		mark = false;
		cost.clear();
		origins.clear();
	}

	public boolean isMarked() {
		boolean flag = true;

		if (mark == true) {
			return true;
		}

		Iterator<Node> iter = this.tail.iterator();
		Node node;
		while (iter.hasNext()) {
			node = iter.next();
			if (flag == false) {
				return false;
			}
			flag = flag && node.isMarked();
		}

		if (flag == true) {
			mark = true;
		}

		return flag;
	}

	public Set<Hyperarc> getCost() {
		if (cost.isEmpty()) {
			cost = calculateCost();
		}
		return cost;
	}

	public Set<Hyperarc> calculateCost() {

		Set<Hyperarc> ret = new HashSet<Hyperarc>();
		Map<Node, Node.Hmark> mapa = new HashMap<Node, Node.Hmark>();

		for (Node n : tail) {
			mapa.put(n, n.mark.get(0));
		}

		for (Node n : mapa.keySet()) {
			origins.put(n.clone(), mapa.get(n).clone());
		}
		// origins = (HashMap<Node, Node.Hmark>)((HashMap<Node,
		// Node.Hmark>)mapa).clone();

		Iterator<Node> iter = mapa.keySet().iterator();

		if (iter.hasNext()) {
			Node first = iter.next();
			varyMark(mapa, first, iter);
		}

		System.out.println("tamaño del mapa: " + mapa.keySet().size());
		for (Node n : origins.keySet()) {
			ret.addAll(origins.get(n).getAncestors());
		}

		// System.out.println("Ancestros de "+ this + ": " + ret.size());
		String anc = "";
		for (Hyperarc a : ret) {
			anc += ", " + a;
		}

		System.out.println("Nosotros, " + anc + ", somos ancestros de " + this);

		return ret;

	}

	public void varyMark(Map<Node, Node.Hmark> map, Node current,
			Iterator<Node> iter) {

		int i;
		Node next = null;

		if (iter.hasNext()) {
			next = iter.next();
		}

		System.out.println("curr " + current + "next " + next);

		for (i = 0; i < current.mark.size(); i++) {

			System.out.print("map: ");
			for (Node n : map.keySet()) {
				System.out.print(map.get(n) + ", ");
			}
			System.out.print("origins: ");
			for (Node n : map.keySet()) {
				System.out.print(origins.get(n) + ", ");
			}
			
			map.put(current, current.mark.get(i));

			if (next != null) {
				varyMark(map, next, iter);
			} else {
				System.out.print("map: ");
				for (Node n : map.keySet()) {
					System.out.print(map.get(n) + ", ");
				}
				System.out.print("origins: ");
				for (Node n : map.keySet()) {
					System.out.print(origins.get(n) + ", ");
				}
				System.out.println("lalala " + current + "-" + sumMarks(map)
						+ "-" + sumMarks(origins));
				if (sumMarks(map) < sumMarks(origins)) {
					System.out.println("Viejo: " + sumMarks(origins)
							+ ", Nuevo : " + sumMarks(map));
					origins.putAll(map);
				}
				System.out.print("map: ");
				for (Node n : map.keySet()) {
					System.out.print(map.get(n) + ", ");
				}
				System.out.print("origins: ");
				for (Node n : map.keySet()) {
					System.out.print(origins.get(n) + ", ");
				}
			}
		}
	}

	public static int sumMarks(Map<Node, Node.Hmark> map) {
		int res = 0;
		Set<Hyperarc> parents = new HashSet<Hyperarc>();
		for (Node n : map.keySet()) {
			parents.addAll(map.get(n).getAncestors());
		}
		for (Hyperarc a : parents) {
			res += a.getValue();
		}
		return res;
	}

}
