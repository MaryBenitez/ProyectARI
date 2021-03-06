import com.google.gson.*;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;
import java.awt.*;
import java.io.*;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Scanner;

public class Main extends Component {

    /**
     * @param args the command line arguments
     */

    private static StreamResult out; //Salida
    private static TransformerHandler th;
    private static AttributesImpl atts;

    private static String clave = "";
    private static Scanner claveI = new Scanner(System.in);

    private static String deli = "";
    private static Scanner deliI = new Scanner(System.in);


    public static void main(String[] args) {

        //Vigenere vigenere3 = new Vigenere("4567467811114","CiAri");


            try {
                //convertirTXTtoXML();
                //convertirTXTtoJson();
                convertirXMLtoTXT();
                //convertirJSONtoTXT();


            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        //Funcion para convetir TXT a XML
        public static void convertirTXTtoXML () {

            //SELECIONA ARCHIVO A LEER
            JFileChooser selectorArchivos = new JFileChooser();
            selectorArchivos.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            FileNameExtensionFilter filtro = new FileNameExtensionFilter("Archivos de texto", "txt");
            selectorArchivos.setFileFilter(filtro);
            selectorArchivos.showOpenDialog(selectorArchivos);
            File archivo = selectorArchivos.getSelectedFile(); // obtiene el archivo seleccionado

// -------------------------------------------------------------------------------------------
            //ELIGE DONDE GUARDAR
            JFileChooser archivoG = new JFileChooser();
            archivoG.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            //FileNameExtensionFilter filtroG = new FileNameExtensionFilter("Archivos XML", "xml");
            //selectorArchivos.setFileFilter(filtroG);
            archivoG.showSaveDialog(archivoG);
            File guarda = archivoG.getSelectedFile();
//--------------------------------------------------------------------------------------------

            if ((archivo == null) || (archivo.getName().equals(""))) {
                JOptionPane.showMessageDialog(selectorArchivos, "Nombre de archivo inválido",
                        "Nombre de archivo inválido", JOptionPane.ERROR_MESSAGE);
            } else {

                Scanner entrada = null;

                try {
                    String ruta = selectorArchivos.getSelectedFile().getAbsolutePath();
                    File f = new File(ruta);
                    entrada = new Scanner(f);

                    out = new StreamResult(guarda + ".xml");
                    openXml();//---> Funcion que abre xml (Estructura)

                    System.out.println ("Introduzca clave con la que desea cifrar");
                    clave = claveI.nextLine();
                    System.out.println ("Introduzca delimitador");
                    deli = deliI.nextLine();

                    while (entrada.hasNext()) {
                        proceso(entrada.nextLine());
                    }

                    System.out.println ("Número de trajetas cifradas");

                    closeXml();//---> Funcion que cierra xml (Estructura)
                    entrada.close();

                } catch (FileNotFoundException e) {
                    System.out.println(e.getMessage());
                } catch (NullPointerException e) {
                    System.out.println("No se ha seleccionado ningún fichero");
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }

            }

        }

        //Abre el archivo XML (Estructura) para empezar a crearlo
        public static void openXml () throws ParserConfigurationException, TransformerConfigurationException, SAXException {

            SAXTransformerFactory tf = (SAXTransformerFactory) SAXTransformerFactory.newInstance();
            th = tf.newTransformerHandler();

            // SALIDA XML
            Transformer serializer = th.getTransformer();
            serializer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            serializer.setOutputProperty(OutputKeys.INDENT, "yes");

            //Se empiezan a poner las etiquetas
            th.setResult(out);
            th.startDocument();
            atts = new AttributesImpl();
            th.startElement("", "", "Clientes", atts);
        }

        //FUNCION de Proceso para crear el archivo.xml a traves de la informacion del archivo.txt
        public static void proceso (String s) throws SAXException {

            String[] elements = s.split(deli); //--->Delimitador
            atts.clear();
            th.startElement("", "", "cliente", atts);

            th.startElement("", "", "documento", atts);
            th.characters(elements[1].toCharArray(), 0, elements[1].length());
            th.endElement("", "", "documento");

            th.startElement("", "", "primer-nombre", atts);
            th.characters(elements[2].toCharArray(), 0, elements[2].length());
            th.endElement("", "", "primer-nombre");

            th.startElement("", "", "apellido", atts);
            th.characters(elements[3].toCharArray(), 0, elements[3].length());
            th.endElement("", "", "apellido");

            boolean ban = true;
            th.startElement("", "", "credit-card", atts);
            while (ban){
                VigenereCifrado vigenereCifrado = new VigenereCifrado(elements[4],clave);
                th.characters(vigenereCifrado.cifrado, 0, elements[4].length());
                System.out.println(vigenereCifrado.cifrado);
                ban= false;
            }
            th.endElement("", "", "credit-card");

            th.startElement("", "", "tipo", atts);
            th.characters(elements[5].toCharArray(), 0, elements[5].length());
            th.endElement("", "", "tipo");

            th.startElement("", "", "telefono", atts);
            th.characters(elements[6].toCharArray(), 0, elements[6].length());
            th.endElement("", "", "telefono");

            th.endElement("", "", "cliente");

        }

        //Cierra el archivo.xml (Estructura)
        public static void closeXml () throws SAXException {
            th.endElement("", "", "Clientes");
            th.endDocument();
        }
//-------------------------------------------------------------------------
        //Funcion para convetir TXT a JSON
        public static void convertirTXTtoJson () throws IOException {

            //Crea una matriz donde se almacenaran los datos
            JsonArray datasets = new JsonArray();

            //SELECCIONA ARCHIVO A LEER
            JFileChooser selectorArchivos = new JFileChooser();
            selectorArchivos.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            FileNameExtensionFilter filtro = new FileNameExtensionFilter("Archivos de texto", "txt");
            selectorArchivos.setFileFilter(filtro);
            selectorArchivos.showOpenDialog(selectorArchivos);
            File archivo = selectorArchivos.getSelectedFile(); // obtiene el archivo seleccionado
//---------------------------------------------------------------------------------------------
            //ELIGE DONDE GUARDAR
            //JFileChooser archivoG = new JFileChooser();
            //archivoG.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            //FileNameExtensionFilter filtroG = new FileNameExtensionFilter("Archivos JSON", "json");
            //selectorArchivos.setFileFilter(filtroG);
            //archivoG.showSaveDialog(archivoG);
            //File guarda = archivoG.getSelectedFile();

            JFileChooser archivoG = new JFileChooser();
            archivoG.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            archivoG.showSaveDialog(archivoG);
            File guarda = archivoG.getSelectedFile();
//---------------------------------------------------------------------------------------------

            if ((archivo == null) || (archivo.getName().equals(""))) {
                JOptionPane.showMessageDialog(selectorArchivos, "Nombre de archivo inválido",
                        "Nombre de archivo inválido", JOptionPane.ERROR_MESSAGE);
            } else {

                Scanner entrada = null;
                String ruta = selectorArchivos.getSelectedFile().getAbsolutePath();
                File f = new File(ruta);
                entrada = new Scanner(f);
                //Titlos para el JSON
                String titulo = "id;documento;primer-nombre;apellido;credit-card;tipo;telefono";
                String line;//--> Para las linea que leera del txt
                boolean flag = true;
                List<String> columns = null;
                System.out.println ("Introduzca delimitador");
                deli = deliI.nextLine();
                while (entrada.hasNext()) {
                    if (flag) {
                        //process Titulos;
                        columns = Arrays.asList(titulo.split("\\;|\\,|\\^|\\$|\\?|\\+|\\(|\\)|\\:|\\[|\\{")); //---> delimitador
                        //Se crea el objeto JSON y lo almacena temporalmente
                        JsonObject obj = new JsonObject();
                        //Información del cliente (Linea por linea del archivo)
                        List<String> chunks = Arrays.asList(entrada.nextLine().split(deli)); //---> delimitador
                        for (int i = 0; i < columns.size(); i++) {
                            obj.addProperty(columns.get(i), chunks.get(i));
                        }
                        //Agrega los datos a la matriz
                        datasets.add(obj);
                    } else {
                        flag = false;
                    }
                }

                generateJWT(datasets);

                //Aqui se le da el formato de JSON y se empieza a crear
                Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE).create();
                Writer writer = null;
                try {
                    //Se crea el fichero JSON en la ruta establecida
                    writer = new FileWriter(guarda + ".json");
                    //Se crea el JSON y lo escribimos en el archivo.
                    gson.toJson(datasets, writer);

                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    // Cerramos el archivo
                    try {
                        //Verificamos que no este nulo
                        if (null != writer) {
                            writer.flush();
                            writer.close();
                        }
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                }
            }

        }

        //Funcion para convertir XML a TXT
        public static void convertirXMLtoTXT () throws IOException {

            //SELECCIONA ARCHIVO A LEER
            JFileChooser selectorArchivos = new JFileChooser();
            selectorArchivos.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            FileNameExtensionFilter filtro = new FileNameExtensionFilter("Archivos XML", "xml");
            selectorArchivos.setFileFilter(filtro);
            selectorArchivos.showOpenDialog(selectorArchivos);
            File archivo = selectorArchivos.getSelectedFile(); // obtiene el archivo seleccionado
//---------------------------------------------------------------------------------------------
            //ELIGE DONDE GUARDAR
            JFileChooser archivoG = new JFileChooser();
            archivoG.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            FileNameExtensionFilter filtroG = new FileNameExtensionFilter("Archivos de Texto", "txt");
            selectorArchivos.setFileFilter(filtroG);
            archivoG.showSaveDialog(archivoG);
            File guarda = archivoG.getSelectedFile();
//---------------------------------------------------------------------------------------------

            if ((archivo == null) || (archivo.getName().equals(""))) {
                JOptionPane.showMessageDialog(selectorArchivos, "Nombre de archivo inválido",
                        "Nombre de archivo inválido", JOptionPane.ERROR_MESSAGE);
            } else {
                try {
                    //Archivo.txt que va a crear
                    BufferedWriter writer = new BufferedWriter(new FileWriter(guarda + ".txt"));

                    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                    DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                    Document doc = dBuilder.parse(archivo); //Archivo elegido a leer

                    System.out.println ("Introduzca clave con la que desea descifrar");
                    clave = claveI.nextLine();
                    System.out.println ("Introduzca delimitador");
                    deli = deliI.nextLine();

                    //Etiqueta va a leer para pasarla a texto
                    NodeList nList = doc.getElementsByTagName("cliente");
                    int cont=1;
                    //Nodo Padre
                    for (int i = 0; i < nList.getLength(); i++) {
                        Node node = nList.item(i);
                        if (node.getNodeType() == Node.ELEMENT_NODE) {
                            //Creo un elemento que obtendra los hijos
                            Element eElement = (Element) node;
                            if (eElement.hasChildNodes()) {
                                NodeList nl = node.getChildNodes();
                                for (int j = 0; j < nl.getLength(); j++) {
                                    Node nd = nl.item(j);
                                    String name = nd.getTextContent();
                                    if(j==0){
                                            writer.write(cont+deli);
                                            cont++;
                                    }

                                    //Compruebo que no este vacio y escribo en el archivo.txt
                                    if (name != null && !name.trim().equals("")) {

                                        if(j == 7){
                                            VigenereDescifrado vigenereDescifrado = new VigenereDescifrado(name,clave);
                                            writer.write(vigenereDescifrado.descifrado);
                                        }else{
                                            writer.write(nd.getTextContent().trim());
                                        }
                                        //Para que el último dato no tenga el delimitador
                                        if (j < nl.getLength() - 2) {
                                            writer.write(deli);
                                        }

                                    }
                                }
                            }
                        }
                        writer.write("\n");
                    }
                    System.out.println ("Tarjetas descifradas");
                    System.out.println ("Delimitador colocado");
                    writer.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }

        //Funcion para convertir JSON a TXT
        public static void convertirJSONtoTXT () throws IOException, ParseException {

            //SELECCIONA ARCHIVO A LEER
            JFileChooser selectorArchivos = new JFileChooser();
            selectorArchivos.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            FileNameExtensionFilter filtro = new FileNameExtensionFilter("Archivos JSON", "json");
            selectorArchivos.setFileFilter(filtro);
            selectorArchivos.showOpenDialog(selectorArchivos);
            File archivo = selectorArchivos.getSelectedFile(); // obtiene el archivo seleccionado
//---------------------------------------------------------------------------------------------
            //ELIGE DONDE GUARDAR
            JFileChooser archivoG = new JFileChooser();
            archivoG.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            FileNameExtensionFilter filtroG = new FileNameExtensionFilter("Archivos de Texto", "txt");
            selectorArchivos.setFileFilter(filtroG);
            archivoG.showSaveDialog(archivoG);
            File guarda = archivoG.getSelectedFile();
//---------------------------------------------------------------------------------------------

            if ((archivo == null) || (archivo.getName().equals(""))) {
                JOptionPane.showMessageDialog(selectorArchivos, "Nombre de archivo inválido",
                        "Nombre de archivo inválido", JOptionPane.ERROR_MESSAGE);
            } else {
                //Archivo al que vamos a reescribir el JSON
                Writer writer;
                writer = new FileWriter(guarda + ".txt");

                JSONParser parser = new JSONParser();
                JSONArray a = (JSONArray) parser.parse(new FileReader(archivo));

                System.out.println ("Introduzca delimitador");
                deli = deliI.nextLine();

                try {
                    for (Object o : a) {

                        JSONObject jsonObject = (JSONObject) o;

                        String id = (String) jsonObject.get("id");
                        String doc = (String) jsonObject.get("documento");
                        String nombre = (String) jsonObject.get("primer-nombre");
                        String apellido = (String) jsonObject.get("apellido");
                        String nTarjeta = (String) jsonObject.get("credit-card");
                        String tipo = (String) jsonObject.get("tipo");
                        String telefono = (String) jsonObject.get("telefono");
                        if (jsonObject.get("id") == id) {
                            writer.write(id);
                            writer.write(deli);
                        }
                        if (jsonObject.get("documento") == doc) {
                            writer.write(doc);
                            writer.write(deli);
                        }
                        if (jsonObject.get("primer-nombre") == nombre) {
                            writer.write(nombre);
                            writer.write(deli);
                        }
                        if (jsonObject.get("apellido") == apellido) {
                            writer.write(apellido);
                            writer.write(deli);
                        }
                        if (jsonObject.get("credit-card") == nTarjeta) {
                            writer.write(nTarjeta);
                            writer.write(deli);
                        }
                        if (jsonObject.get("tipo") == tipo) {
                            writer.write(tipo);
                            writer.write(deli);
                        }
                        if (jsonObject.get("telefono") == telefono) {
                            writer.write(telefono);
                            writer.write("\n");
                        }
                    }
                } finally {
                    // Cerramos el archivo
                    try {
                        //Verificamos que no este nulo
                        if (null != writer) {
                            writer.flush();
                            writer.close();
                        }
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                }
            }


        }

    public static void generateJWT(JsonArray dataset){
        String clave = "EstaEsUnaClaveSuperSecretaYLargaParaQueEstoFuncione";
        byte[] decodedKey = Base64.getDecoder().decode(clave);
        String jwt = Jwts.builder()
                .setPayload(dataset.toString())
                .signWith(Keys.hmacShaKeyFor(decodedKey))
                .compact();

        System.out.println(jwt);
        System.out.println();

        String result = Jwts.parser()
                .setSigningKey(Keys.hmacShaKeyFor(decodedKey))
                .parsePlaintextJws(jwt).getBody();

        System.out.println(result);
    }

    }



