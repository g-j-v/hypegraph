package hypergraph;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream.GetField;
import java.util.SortedSet;

public class dotConverter {
	
	public static void toDot(Hypergraph hypergrafo,String arch) throws IOException{
		
		BufferedWriter output;
		try {
			output = new BufferedWriter(new FileWriter(arch));
		} catch (IOException e) {
			System.out.println("catch2");
			return;
		}
	
		output.write("digraph { ");
		output.newLine();
		output.write("//// Nodos");
		output.newLine();
	/*	SortedSet<Node> nombresordenados =  new SortedSet<Node>();*/
	//// Nodos	
		for(Node n: hypergrafo.getNodes()){
			output.write("Node"+n.getName() + ' ' );
			output.write("[label="+ '\"' + n.getName() + "\"];");
			output.newLine();
		}
		output.write("//// ejes");
		output.newLine();
	//// Ejes
		for(Hyperarc arc: hypergrafo.getHyperArcs()){
			output.write(arc.getName() + ' ' );
			output.write("[" +
					"shape=box, height00.18, fontsize=12,"+
					//+ isintheminpath()?"color=grey":""+
					"label="+ '\"' + arc.getName()+ " (" + arc.getValue() + ") "+ "\"" +
							"];");
			output.newLine();
		}
		output.write("//// Arcos");
		output.newLine();
	////Arquetes
		String flechitacomun = "[style=bold, color=black]";
		String flechitagris = "[color=\"#000000\", style=dotted, arrowhead=vee]";
		for(Hyperarc arc: hypergrafo.getHyperArcs()){
			for(Node n: arc.getTail()){
				output.write("Node"+n.getName() + "->" + arc.getName() + " ");
				System.out.println(n);
				if (isInMinPath(n)){
					output.write(flechitacomun);
				}else{
					output.write(flechitagris);
				}
				output.newLine();
			}
			for(Node n: arc.getHead()){
				output.write( arc.getName() + "->"  + "Node"+n.getName() );
				if (isInMinPath(n)){
					output.write(flechitacomun);
				}else{
					output.write(flechitagris);
				}
				output.newLine();
			}
		}
		
		output.write("}");
		output.flush();
	}

	private static boolean isInMinPath(Node n) {
		return true;
	}

}
