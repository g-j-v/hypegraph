package hypergraph;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class hgConverter {

	Set<Node> nodes;
	static InputStream file;
	static BufferedReader stream;

	public static List<Node> readHeaders() {

		List<Node> ret = new ArrayList<Node>();

		try {

			String line = stream.readLine();
			String[] tokens = line.split(" ");

			while (tokens[0].startsWith("#") || tokens[0] == "") {
				line = stream.readLine();
				tokens = line.split(" ");
			}

			if (tokens.length > 1 && !tokens[1].startsWith("#")) {
				// System.out.println("Formato de archivo inválido.8");
				return null;
			}

			ret.add(new Node(tokens[0]));

			line = stream.readLine();
			tokens = line.split(" ");

			while (tokens[0].startsWith("#") || tokens[0] == "") {
				line = stream.readLine();
				tokens = line.split(" ");
			}

			if (tokens.length > 1 && !tokens[1].startsWith("#")) {
				// System.out.println("Formato de archivo inválido.8");
				return null;
			}
			ret.add(new Node(tokens[0]));

		} catch (IOException e) {
			// System.out.println("Error al leer el archivo.");
			return null;
		}

		return ret;
	}

	public static boolean readHyperArcs(Hypergraph graph) {

		String name, line;
		String[] tokens;
		int weight, cycles, i;

		try {
			line = stream.readLine();
			while (line != null) {
				tokens = line.split(" ");

				while (tokens[0].startsWith("#") || tokens[0] == "\n") {
					line = stream.readLine();
					tokens = line.split(" ");
				}

				name = tokens[0];
				// System.out.println(name);

				weight = Integer.valueOf(tokens[1]);
				// System.out.println(weight);

				Hyperarc aux = new Hyperarc(name, weight);
				graph.addHyperarc(aux);

				cycles = Integer.valueOf(tokens[2]);
				// System.out.println("cycles = " + cycles);

				if (cycles + 5 > tokens.length) {
					// System.out.println("Formato de archivo inválido.");
					return false;
				}

				for (i = 3; i < 3 + cycles; i++) {
					if (tokens[i].contains("#")) {
						// System.out.println("Formato de archivo inválido.");
						return false;
					}
					graph.addHead(new Node(tokens[i]), aux);
				}

				int marker = cycles + 3;
				cycles = Integer.valueOf(tokens[marker]);
				// System.out.println("cycles = " + cycles);

				for (i = marker + 1; i < marker + 1 + cycles; i++) {
					if (tokens[i].contains("#")) {
						// System.out.println("Formato de archivo inválido.");
						return false;
					}
					graph.addTail(new Node(tokens[i]), aux);
				}
				line = stream.readLine();
			}

		} catch (IOException e) {
			// System.out.println("Error al leer el archivo.");
			return false;
		}

		return true;
	}

	public static Hypergraph read(String archivo) {

		Hypergraph ret = new Hypergraph();

		try {
			stream = new BufferedReader(new InputStreamReader(
					new FileInputStream(archivo)));
		} catch (FileNotFoundException e) {
			// System.out.println("El archivo no se ha encontrado.");
		}

		// stream.commentChar('#');
		// stream.eolIsSignificant(true);

		List<Node> header = readHeaders();
		if (header == null) {
			return null;
		}
		ret.setStart(header.get(0));
		ret.setEnd(header.get(1));

		if (!(readHyperArcs(ret))) {
			return null;
		}
		// System.out.println(ret);
		return ret;
	}

	public static void toHg(Hypergraph min, String fileName) throws IOException{
		
		BufferedWriter output;
		try {
			output = new BufferedWriter(new FileWriter(fileName));
		} catch (IOException e) {
			System.out.println("No se puede escribir");
			return;
		}
		
		output.write(min.getStart().toString());
		output.newLine();
		output.write(min.getEnd().toString());
		output.newLine();
		for(Hyperarc ha: min.getHyperArcs()){
			output.write(ha.toString() + " ");
			output.write(ha.getValue() + " ");
			output.write(ha.getHead().size() + " ");
			for(Node n: ha.getHead()){
				output.write(n.toString() + " ");
			}
			output.write(ha.getTail().size() + " ");
			for(Node n: ha.getTail()){
				output.write(n.toString() + " ");
			}
			output.newLine();
		}
		
	
	}

}
