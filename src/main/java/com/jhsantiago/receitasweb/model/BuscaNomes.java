package com.jhsantiago.receitasweb.model;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;
/**
 *
 * @author jhons
 */
public class BuscaNomes {
    private Document doc;

    public BuscaNomes(String caminho) throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory fabrica = DocumentBuilderFactory.newInstance();
        DocumentBuilder construtor = fabrica.newDocumentBuilder();
        doc = construtor.parse(caminho);
    }

    private String serealizar(Node no) throws TransformerConfigurationException, TransformerException {
        TransformerFactory fabrica = TransformerFactory.newInstance();
        Transformer transformador = fabrica.newTransformer();
        DOMSource fonte = new DOMSource(no);
        ByteArrayOutputStream fluxo = new ByteArrayOutputStream();
        StreamResult saida = new StreamResult(fluxo);
        transformador.transform(fonte, saida);
        return fluxo.toString();
    }

    private Document newDocReceitas() throws ParserConfigurationException {
        Document newDoc;
        DocumentBuilderFactory fabrica = DocumentBuilderFactory.newInstance();
        DocumentBuilder construtor = fabrica.newDocumentBuilder();
        newDoc = construtor.newDocument();
        Element receita = newDoc.createElement("receita");
        newDoc.appendChild(receita);
        return newDoc;
    }
  
    public String FiltroNome() throws TransformerException, ParserConfigurationException {
        Document newDoc = newDocReceitas();
        //Node noReceita = null;
        NodeList receitas = doc.getElementsByTagName("nome");
        int tam = receitas.getLength();
        for (int i = 0; i < tam; i++) {
            Node noReceita = receitas.item(i);
            if (noReceita != null) {
                Element newTag = newDoc.createElement(noReceita.getNodeName());
                Text newText = newDoc.createTextNode(noReceita.getFirstChild().getNodeValue());
                newTag.appendChild(newText);
                newDoc.getDocumentElement().appendChild(newTag);
            }
        }
        return serealizar(newDoc);
    }

    public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException, TransformerException {
        BuscaNomes r = new BuscaNomes("src/main/java/com/jhsantiago/receitasweb/model/Receitas.xml");
        System.out.println(r.FiltroNome());

    }
}
