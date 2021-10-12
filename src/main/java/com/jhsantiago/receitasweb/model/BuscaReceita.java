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
public class BuscaReceita {

    private Document doc;

    public BuscaReceita(String caminho) throws ParserConfigurationException, SAXException, IOException {
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

    private void CopyForNewDoc(Node noReceita, Document newDoc) {
        NodeList filhos = noReceita.getChildNodes();
        int tam = filhos.getLength();
        for (int i = 0; i < tam; i++) {
            Node filho = filhos.item(i);
            if (filho.getNodeType() == Node.ELEMENT_NODE) {
                Element newTag = newDoc.createElement(filho.getNodeName());
                Text newText = newDoc.createTextNode(filho.getFirstChild().getNodeValue());
                newTag.appendChild(newText);
                NodeList filhosDoF = filho.getChildNodes();
                int tamF = filhosDoF.getLength();
                for (int o = 0; o < tamF; o++) {
                    Node filhoDoF = filhosDoF.item(o);
                    if (filhoDoF.getNodeType() == Node.ELEMENT_NODE) {
                        Element newTagF = newDoc.createElement(filhoDoF.getNodeName());
                        Text newTextF = newDoc.createTextNode(filhoDoF.getFirstChild().getNodeValue());
                        newTagF.appendChild(newTextF);
                        newTag.appendChild(newTagF);
                    }
                }
                newDoc.getDocumentElement().appendChild(newTag);
            }
        }
    }
  
    public String FiltroNome(String nome) throws TransformerException, ParserConfigurationException {
        Document newDoc = newDocReceitas();
        Node noReceita = null;
        NodeList filhos = doc.getElementsByTagName("nome");
        int tam = filhos.getLength();
        for (int i = 0; i < tam; i++) {
            Node noFilho = filhos.item(i);
            if (noFilho != null) {
                if (noFilho.getFirstChild().getNodeValue().equals(nome)) {
                    noReceita = noFilho.getParentNode();
                    CopyForNewDoc(noReceita, newDoc);
                }
            }
        }
        return serealizar(newDoc);
    }

    public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException, TransformerException {
        BuscaReceita r = new BuscaReceita("src/main/java/com/jhsantiago/receitasweb/model/Receitas.xml");
        System.out.println(r.FiltroNome("Bolo de Milho sem Farinha"));

    }
}
