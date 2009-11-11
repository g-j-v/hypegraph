package hypergraph;

import java.io.IOException;
import java.util.List;

public class Core {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Hypergraph hg;
		List<Hypergraph> minHg;

		if(args.length == 0){
			System.out.println("Escriba el nombre del archivo");
			return;
		}
		
		String s = "sarasasa.hg";
		String[] asd = s.split(".");
		
		
		
		/*char[] aux = args[0].toCharArray();
		
		aux.toString();
		String[] name=aux.split(".");*/
		System.out.println(asd.length);
		String name = "A.out";
		System.out.println("Calculando el camino minimo de " + name);
		hg = hgConverter.read(args[0]);
		
		

		// System.out.println("aefsgd!");
		int i = 0;

		minHg = hg.minPath();
		try {
			dotConverter.toDot(hg, name + ".dot", hg);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		for (Hypergraph hyp : minHg) {
			System.out.println("aaaaaaaaaaaaa");
			System.out.println(hyp.getHyperArcs());
			System.out.println(hyp.minWeight);
			System.out.println("aaaaaaaaaaaaa");

			try {
				hgConverter.toHg(hyp, name+".min.hg");
				//dotConverter.toDot(hg, name + i + ".min.dot", hyp);

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// //System.out.println(minHg);
			// //System.out.println(hg.getLast().getMin());
			i++;
		}
	}
}
