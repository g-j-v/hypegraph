package hypergraph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Hyperarc {
	private String name;
	private Set<Node> tail;
	private Set<Node> head;
	private Integer value;
	private boolean mark;
	private List<HashMap<Node, Node.Hmark>> origins;
	List<HashSet<Hyperarc>> cost;

	public Hyperarc(String name, Integer value) {
		this.tail = new HashSet<Node>();
		this.head = new HashSet<Node>();
		this.value = value;
		this.name = name;
		this.mark = false;
		this.origins = new ArrayList<HashMap<Node, Node.Hmark>>();
		this.cost = new ArrayList<HashSet<Hyperarc>>();
	}

	public Set<Node> getTail() {
		return tail;
	}

	public Set<Node> getHead() {
		return head;
	}

	public List<HashMap<Node, Node.Hmark>> getMarks() {
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
			if (!n.next.contains(this))
				n.next.add(this);
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
			flag = flag && node.isTotallyMarked();
		}

		if (flag == true) {
			mark = true;
		}

		return flag;
	}

	public List<HashSet<Hyperarc>> getCost() {
		if (cost.isEmpty()) {
			cost = calculateCost();
		}
		return cost;
	}

	public List<HashSet<Hyperarc>> calculateCost() {

		List<HashSet<Hyperarc>> ret = new ArrayList<HashSet<Hyperarc>>();
		Map<Node, Node.Hmark> mapa = new HashMap<Node, Node.Hmark>();
		Map<Node, Node.Hmark> map2 = new HashMap<Node, Node.Hmark>();
		for (Node n : tail) {
			mapa.put(n, n.mark.get(0));
			map2.put(n, n.mark.get(0));
		}	
		

		origins.add((HashMap<Node, Node.Hmark>) map2);

		// origins = (HashMap<Node, Node.Hmark>)((HashMap<Node,
		// Node.Hmark>)mapa).clone();

		Iterator<Node> iter = mapa.keySet().iterator();

		if (iter.hasNext()) {
			Node first = iter.next();
			varyMark(mapa, first, iter);
		}

		System.out.println("tamaño del mapa: " + mapa.keySet().size());

		Set<Hyperarc> tmp = new HashSet<Hyperarc>();

		for (int i = 0; i < origins.size(); i++) {
			for (Node n : origins.get(i).keySet()) {
				tmp.addAll((HashSet<Hyperarc>) origins.get(i).get(n)
						.getAncestors());
			}
			ret.add((HashSet<Hyperarc>) tmp);
			String anc = "";
			for (Hyperarc a : tmp) {
				anc += ", " + a;
			}

			System.out.println("Nosotros, " + anc + ", somos ancestros de "
					+ this);
		}

		System.out.println("Grupos de ancestros de "+ this + ": " + origins.size());

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
			System.out.println(current.mark.size());
			
			 System.out.print("map: ");
			 for (Node n : map.keySet()) {
			 System.out.print(map.get(n) + ", ");
			 }
			 System.out.print("origins: ");
			 for (Node n : map.keySet()) {
			 System.out.print(origins.get(0).get(n) + ", ");
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
				 for (Map<Node, Node.Hmark> m : origins) {
				 for (Node n : m.keySet()) {
				 System.out.print(m.get(n) + ", ");
				 }
				 System.out.println(";;;;;");
				 }
				System.out.println("lalala " + current + "-" + sumMarks(map)
						+ "-" + sumMarks(origins.get(0)));
				
				if (sumMarks(map) == sumMarks(origins.get(0))) {
					Map<Node, Node.Hmark> mp = new HashMap<Node, Node.Hmark>();
					mp.putAll(map);
					if (!origins.contains((HashMap<Node, Node.Hmark>) mp)) {
						origins.add((HashMap<Node, Node.Hmark>) mp);
					}
				}else if (sumMarks(map) < sumMarks(origins.get(0))) {
					System.out.println("Viejo: " + sumMarks(origins.get(0))
							+ ", Nuevo : " + sumMarks(map));
					origins.clear();
					Map<Node, Node.Hmark> mp = new HashMap<Node, Node.Hmark>();
					mp.putAll(map);
					origins.add((HashMap<Node, Node.Hmark>) mp);
				} 
				//				
				// System.out.print("map: ");
				// for (Node n : map.keySet()) {
				// System.out.print(map.get(n) + ", ");
				// }
				// System.out.print("origins: ");
				// for (Map<Node, Node.Hmark> m : origins) {
				// for (Node n : m.keySet()) {
				// System.out.print(m.get(n) + ", ");
				// }
				// System.out.println(";;;;;");
				// }
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
