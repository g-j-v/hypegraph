package hypergraph;

public class test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Hypergraph hg = new Hypergraph();
		Node source = new Node("SOURCE");
		Node n1 = new Node("N1");
		Node n2 = new Node("N2");
		Node n3 = new Node("N3");
		Node n4 = new Node("N4");
		Node n5 = new Node("N5");
		Node n6 = new Node("N6");
		Node n7 = new Node("N7");
		Node n8 = new Node("N8");
		Node n9 = new Node("N9");
		Node dest = new Node("DEST");
		
		Hyperarc ha = new Hyperarc("a", 3);
		ha.addHead(source);
		source.next.add(ha);
		ha.addTail(n1);
		n1.next.add(ha);
		
		
	}

}
