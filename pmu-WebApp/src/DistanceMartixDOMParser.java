import java.io.File;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class DistanceMartixDOMParser {


    private static final String XML_FILENAME = "/Users/sid/Desktop/5th year/Project/TEST4AWS/Jython/src/distance.xml";

    public static String getDistance() {

        String distanceBt2Points = null;
        try {
            File inputFile = new File(XML_FILENAME);
            DocumentBuilderFactory dFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = dFactory.newDocumentBuilder();
            Document doc = documentBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();


            System.out.println(doc.getDocumentElement().getElementsByTagName("origin_address").item(0).getTextContent());
            System.out.println(doc.getDocumentElement().getElementsByTagName("destination_address").item(0).getTextContent());
            NodeList nodeList = doc.getElementsByTagName("row");

            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    element.getAttribute("element");
                    float distance = Integer.parseInt(element.getElementsByTagName("distance").item(0).getChildNodes().item(1).getLastChild().getTextContent().toString(), 10);
                    distanceBt2Points = String.valueOf((distance / 1000));
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (DOMException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
        return distanceBt2Points;
    }

    public static String getJourneyDuration() {

        String journeyTime = null;
        try {
            File inputFile = new File(XML_FILENAME);
            DocumentBuilderFactory dFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = dFactory.newDocumentBuilder();
            Document doc = documentBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();


            System.out.println(doc.getDocumentElement().getElementsByTagName("origin_address").item(0).getTextContent());
            System.out.println(doc.getDocumentElement().getElementsByTagName("destination_address").item(0).getTextContent());
            NodeList nodeList = doc.getElementsByTagName("row");

            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    element.getAttribute("element");
                    journeyTime = element.getElementsByTagName("text").item(0).getTextContent();

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return journeyTime;
    }
}
