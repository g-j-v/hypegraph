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
	private int cost;
	private Map<Node, Node.Hmark> origins;

	public Hyperarc(String name, Integer value) {
		this.tail = new HashSet<Node>();
		this.head = new HashSet<Node>();
		this.value = value;
		this.name = name;
		this.mark = false;
		this.origins = new HashMap<Node, Node.Hmark>();
	}

	public Set<Node> getTail() {
		return tail;
	}

	public Set<Node> getHead() {
		return head;
	}

	public boolean equals(Object o) {
		if(o == null){
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
	}

	public boolean isMarked() {
		boolean flag = true;

		if (mark == true) {
			return true;
		}

		Iterator<Node> iter = this.tail.iterator();
		Node node;
		while(iter.hasNext()){
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

	public int calculateCost() {
	
		Map<Node, Node.Hmark> mapa = new HashMap<Node, Node.Hmark>();

		for (Node n : tail) {
			mapa.put(n, n.mark.get(0));
		}

		origins = mapa;

		Iterator<Node> iter = mapa.keySet().iterator();

		if (iter.hasNext()) {
			varyMark(mapa, iter);
		}

		return sumMarks(mapa);

	}

	public int Cost() {
		return cost;
	}

	public void varyMark(Map<Node, Node.Hmark> map, Iterator<Node> iter) {
		int i;
		Node node = iter.next();

		for (i = 0; i < node.mark.size(); i++) {

			map.put(node, node.mark.get(i));

			if (iter.hasNext()) {
				varyMark(map, iter);
			} else {
				if (sumMarks(map) < sumMarks(origins)) {
					origins = map;
				}
			}
		}
	}

	private static int sumMarks(Map<Node, Node.Hmark> map) {
		int res = 0;
		Set<Hyperarc> parents = new HashSet<Hyperarc>();
		for (Node n : map.keySet()) {
			if (!parents.contains(map.get(n).arc)) {
				res += map.get(n).cost;
			}
			parents.add(map.get(n).arc);
		}

		return res;
	}
	
}
