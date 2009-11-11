package hypergraph;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Set;
/*import java.io.ObjectInputStream.GetField;
import java.util.SortedSet;*/

public class dotConverter {
	static Integer suma = 0;
	
	public static void toDot(Hypergraph hypergrafo,String arch,Hypergraph min) throws IOException{
		
		BufferedWriter output;
		try {
			output = new BufferedWriter(new FileWriter(arch));
		} catch (IOException e) {
			System.out.println("No se puede escribir");
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
			//String gris = "color=grey";
		
			output.write("[" +"shape=box, height00.18, fontsize=12,");
			if(isInMinPath(arc,min)){
				output.write(" color=red, style=filled, fillcolor=red ");
			}else{
				output.write("color=grey,");
			}
			output.write("label="+ '\"' + arc.getName()+ " (" + arc.getValue() + ") "+ "\"" +"];");
			suma+=arc.getValue();
			output.newLine();
		}
		output.write("//// Arcos");
		output.newLine();
	////Arquetes
		
		String flechitacomun = "[style=bold, color=red]";
		String flechitagris = "[color=\"#000000\", color=grey, arrowhead=vee]";
		
	//	Set<Hyperarc> hypArcNoinic = hypergrafo.getHyperArcs();
		//hypArcNoinic.remove(hypergrafo.getStart().adj);
		
		for(Hyperarc arc: hypergrafo.getHyperArcs()){
			
			for(Node n: arc.getTail()){
				output.write("Node"+n.getName() + "->" + arc.getName() + " ");
				if(!isInMinPath(n,min) || !isInMinPath(arc, min)){
					output.write(flechitagris);
				}else{
					output.write(flechitacomun);
				}
				output.newLine();
			}
			for(Node n: arc.getHead()){
				output.write( arc.getName() + "->"  + "Node"+n.getName() );
				if (!isInMinPath(n,min) || !isInMinPath(arc, min)){
					output.write(flechitagris);
				}else{
					output.write(flechitacomun);
				}
				output.newLine();
			}
		}
		
		output.write("}");
		output.flush();
	}

	private static boolean isInMinPath(Hyperarc ha, Hypergraph min) {
		return min.getHyperArcs().contains(ha);
	}
/*	private static boolean isInMinPath(Node node, Hypergraph min) {
		return min.getNodes().contains(node);
	}*/
	private static boolean isInMinPath(Node node, Hypergraph min) {
		return min.getNodes().contains(node);
	}
}
