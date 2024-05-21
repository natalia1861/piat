package piat.opendatasearch;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import com.google.gson.stream.JsonWriter;

import piat.opendatasearch.XPATH_Evaluador.Propiedad;

/** @author Natalia Agüero Knauf 47230975S
 * Clase usada para crear un fichero JSON
 * Contiene un método estático, llamado generar() que se encarga de generar un String en formato JSON a partir de la lista de propiedades que se le pasa como parámetro 
 */
public class GenerarJSON {

	/**
	 * Método que se encarga de generar un String en formato JSON a partir de la lista de propiedades que se le pasa como parámetro
	 * @param	ficheroJSONSalida	Nombre del fichero JSON de salida
	 * @param	listaPropiedades	Lista de propiedades
	 */
	public static void generar(String ficheroJSONSalida,List<Propiedad> listaPropiedades) throws IOException{
		// TODO:
		// - Crear objeto de la clase JsonWriter usando como fichero el parámetro ficheroJSONSalida
		// - Crear los arrays y objetos que debe tener el fichero JSON de salida
		int posicion;
		JsonWriter jsonWriter;
		

			jsonWriter=new JsonWriter(new FileWriter (ficheroJSONSalida));
			jsonWriter.setIndent("\t");	// indentacion
            jsonWriter.beginObject(); //----> {
            
			if ((posicion=buscarPropiedad("query", listaPropiedades))>=0) {
				 jsonWriter.name(listaPropiedades.get(posicion).nombre).value(listaPropiedades.get(posicion).valor); //query
			}
			
			if ((posicion=buscarPropiedad("numeroResources", listaPropiedades))>=0) {
				 jsonWriter.name(listaPropiedades.get(posicion).nombre).value(Integer.parseInt(listaPropiedades.get(posicion).valor)); //numeroResources
			}
			
			 if ((posicion=buscarPropiedad("id", listaPropiedades))>=0){
				 jsonWriter.name("infDatasets");			// ----> "infDatasets"
				 jsonWriter.beginArray();                   // ----> [
				 
				 jsonWriter.beginObject();					// ----> {
				 jsonWriter.name("id").value(listaPropiedades.get(posicion).valor); // ----> id: https://
				 jsonWriter.name("num").value(listaPropiedades.get(buscarPropiedad(listaPropiedades.get(posicion).valor, listaPropiedades)).valor); // num: 5
				 jsonWriter.endObject();					// ----> {
				 
				 posicion++; 
				 while (posicion<listaPropiedades.size()){
					 if (listaPropiedades.get(posicion).nombre.equals("id")) {
						 jsonWriter.beginObject();					// ----> {
						 jsonWriter.name("id").value(listaPropiedades.get(posicion).valor); // ----> id: https://
						 jsonWriter.name("num").value(listaPropiedades.get(buscarPropiedad(listaPropiedades.get(posicion).valor, listaPropiedades)).valor); // num: 5
						 jsonWriter.endObject();					// ----> {
					 }
					 posicion++;
	                }
				 jsonWriter.endArray();     // ----> ]
			 }
			 
			 if ((posicion=buscarPropiedad("ubicacion", listaPropiedades))>=0){
				 jsonWriter.name("ubicaciones");
				 jsonWriter.beginArray();                 // ----> [
				 
				 jsonWriter.value(listaPropiedades.get(posicion).valor); //----> Centro...
				 posicion++;
				 while (posicion<listaPropiedades.size() && listaPropiedades.get(posicion).nombre.equals("ubicacion")){
	                    jsonWriter.value(listaPropiedades.get(posicion).valor);  //  -----> Centro.., Centro..., Teatro...
	                    posicion++;
	             } 
				 jsonWriter.endArray();     // ----> ]
			 }
			
			jsonWriter.endObject();    // ----> }
			jsonWriter.close();
	}

	 // Metodo auxiliar para buscar en el ArrayList el primer elemento que contenga el String pasado como parÃ¡metro en su nombre. Devuelve -1 si no lo encuentra
	private static int buscarPropiedad(String nombre, List<Propiedad> listaPropiedades){
		for (int i=0;i<listaPropiedades.size();i++)
			if(listaPropiedades.get(i).nombre.equals(nombre)) return i;
		return -1;
	}

}

