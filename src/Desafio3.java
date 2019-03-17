
//Thainá França - RA: 082160022

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Desafio3 {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		String direcIn = "", direcOut = "", done = "{\r\n";

		try {
			System.out.println("Digite o caminho do arquivo XML: ");
			direcIn = sc.next();
			sc.close();
			direcOut = direcIn.replace(".xml", ".json");

			// Open file and parse to XML object
			File xmlFile = new File(direcIn);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(xmlFile);
			doc.getDocumentElement().normalize();

			// Read XML file nodes
			String root = doc.getDocumentElement().getNodeName();
			NodeList nodes = doc.getElementsByTagName(root).item(0).getChildNodes();
			done += "\"" + root + "\": {\r\n";
			done += getNodeValue(nodes, false);
			done = done.substring(0, done.length() - 3) + "\r\n}\r\n}";

			Writer fw = new FileWriter(new File(direcOut), false);
			fw.write(done);
			fw.close();
			System.out.print("O arquivo de saída foi salvo!");

		} catch (Exception fex) {
			System.out.println(fex.getMessage());
			fex.printStackTrace();
		}
	}

	public static String getNodeValue(NodeList lista, boolean child) {
		String ret = "";

		for (int i = 0; i < lista.getLength(); i++) {
			Node linha = lista.item(i);

			if (linha.getNodeType() == Node.ELEMENT_NODE) {
				// Open node
				if (!child) {
					ret += "\"" + linha.getNodeName() + "\": {\r\n";
				}
				// Get node name and value
				else {
					ret += "\"" + linha.getNodeName() + "\": ";
					ret += "\"" + linha.getTextContent() + "\",\r\n";
				}

				// Loop again if has child nodes
				if (linha.hasChildNodes()) {
					ret += getNodeValue(linha.getChildNodes(), true);
				}

				// Close opened nodes
				if (!child) {
					if (ret.endsWith(",\r\n")) {
						ret = ret.substring(0, ret.length() - 3) + "\r\n";
					}
					ret += "},\r\n";
				}
			}
		}
		return ret;
	}
}
