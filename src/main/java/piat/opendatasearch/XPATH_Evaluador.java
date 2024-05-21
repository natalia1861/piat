package piat.opendatasearch;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


/** @author Natalia Agüero Knauf 47230975S
 * Clase para evaluar las expresiones XPath
 * Contiene un método estático, llamado evaluar() que se encarga de realizar las consultas XPath al fichero XML que se le pasa como parámetro 
 */
public class XPATH_Evaluador{

	/**
	 * Método que se encarga de evaluar las expresiones XPath sobre el fichero XML generado en la práctica 4
	 * @param	ficheroXML	Fichero XML a evaluar
	 * @return	Una lista con la propiedad resultante de evaluar cada expresión XPath
	 * @throws	IOException
	 * @throws	XPathExpressionException 
	 * @throws	ParserConfigurationException 
	 * @throws	SAXException 
	 */
	public static List<Propiedad> evaluar (String ficheroXML) throws IOException, XPathExpressionException, ParserConfigurationException, SAXException {
		// TODO: 
		// Realiza las 4 consultas XPath al documento XML de entrada que se indican en el enunciado en el apartado "3.2 Búsqueda de información y generación del documento de resultados."
		// Cada consulta devolverá una información que se añadirá a la colección List <Propiedad>, que es la que devuelve este método
		// Una consulta puede devolver una propiedad o varias

		List <Propiedad> listaPropiedades = new ArrayList<Propiedad> ();
		Propiedad propiedad;
        String resultado;
        
		//En primer lugar se crea el arbol DOM a partir del fichero XML fuente
        final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(false);
        final DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(ficheroXML);
        
        //se crea el objeto XPATH que evaluará las expresiones
        XPath xPath = XPathFactory.newInstance().newXPath();
        
        //contenido textual del elemento <query>
        String expresionPathQuery = "//summary/query/text()";
        String contentQuery = (String) xPath.evaluate (expresionPathQuery, doc, XPathConstants.STRING);
        propiedad=new Propiedad ("query", contentQuery);
        listaPropiedades.add(propiedad);
        
        //Numero de elementos <reource> hijos de <resources>
        String expresionPathNumElemResource = "count(//resources/resource)";
        Double numeroResource = (Double) xPath.evaluate(expresionPathNumElemResource, doc, XPathConstants.NUMBER);
        resultado= String.valueOf(numeroResource.intValue());	// Convertir a int para quitar decimales, y luego a String
        propiedad=new Propiedad ("numeroResources", resultado);
		listaPropiedades.add(propiedad);
		
        //contenido de cada uno de los elementos <eventLocation> descendientes de <resource>.
        //si su contenido esta vacio no se tendra en cuenta
        //si una ubicacion esta repetida solo se añadira una vez al documento de salida
        String expresionPatheventLocation = "//resource//eventLocation/text()";
        NodeList eventLocationList = (NodeList) xPath.evaluate (expresionPatheventLocation, doc, XPathConstants.NODESET);
        List <String> finalListEventLocation= new ArrayList<>();
        String eventLocationAux;
        for (int i= 0; i < eventLocationList.getLength(); i++) {
        	eventLocationAux = eventLocationList.item(i).getNodeValue();
        	if (!finalListEventLocation.contains(eventLocationAux) && !Objects.isNull(eventLocationAux) && !eventLocationAux.isBlank()) {
        		finalListEventLocation.add(eventLocationList.item(i).getNodeValue());
        		propiedad=new Propiedad ("ubicacion", eventLocationAux);
        		listaPropiedades.add(propiedad);
        	}
        }
        
        //por cada elemento <dataset>, hijo de <datasets>, numero de elementos <resource> 
        //cuyo atributo id es igual al atributo id del elemento <dataset>
        String expressionPathDatasetId = "//datasets/dataset/@id";					//expresion para encontrar todos los id de cada dataset
        String expresionPathNumResDat = "count(//resources/resource[@id=\"#ID#\"])"; //expresion para posteriormente encontrar el numero de resource con cierto id
        
        StringBuilder expresionFinal;
        Double numeroResourceDataset;
        NodeList datasets = (NodeList) xPath.evaluate (expressionPathDatasetId, doc, XPathConstants.NODESET);
        
        for (int i = 0; i < datasets.getLength(); i++) {
        	expresionFinal = new StringBuilder();
        	expresionFinal.append(expresionPathNumResDat.replace("#ID#", datasets.item(i).getNodeValue()));
        	numeroResourceDataset = (Double) xPath.evaluate(expresionFinal.toString(), doc, XPathConstants.NUMBER);
        	resultado= String.valueOf(numeroResourceDataset.intValue());	// Convertir a int para quitar decimales, y luego a String
        	
        	propiedad=new Propiedad ("id", datasets.item(i).getNodeValue());
    		listaPropiedades.add(propiedad);
    		
    		propiedad=new Propiedad (datasets.item(i).getNodeValue(), resultado);
    		listaPropiedades.add(propiedad);
        }
        
		return listaPropiedades;
	}
	/**
	 * Esta clase interna define una propiedad equivalente a "nombre":"valor" en JSON
	 */
	public static class Propiedad {
		public final String nombre;
		public final String valor;

		public Propiedad (String nombre, String valor){
			this.nombre=nombre;
			this.valor=valor;		
		}

		@Override
		public String toString() {
			return this.nombre+": "+this.valor;

		}

	} //Fin de la clase interna Propiedad

} //Fin de la clase XPATH_Evaluador
