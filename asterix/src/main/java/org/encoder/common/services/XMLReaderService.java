package org.encoder.common.services;

import org.encoder.common.AsterixFlightData;
import org.encoder.common.Constants;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.Set;

@Service
public class XMLReaderService {

    private final AsterixFlightData asterixFlightData;

    public XMLReaderService(AsterixFlightData asterixFlightData) {
        this.asterixFlightData = asterixFlightData;
    }

    public void readAsterixData(Set<String> asterixIds, Set<Integer> asterixSubfieldIds) {

        Document doc = readXml();

        if (null != doc) {

            NodeList nList = doc.getElementsByTagName("dataitem");
            boolean isSubfield = false;
            String parentId = null;
            parseXml(nList, asterixIds, asterixSubfieldIds, isSubfield, parentId);
        }
    }

    private void parseXml(NodeList elements,
                          Set<String> asterixIds,
                          Set<Integer> asterixSubfieldIds,
                          boolean isSubfield,
                          String parentId) {

        for (int index = 0; index < elements.getLength(); index++) {
            Node node = elements.item(index);

            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                String elementId = element.getAttribute("id");

                if (!isSubfield && asterixIds.contains(elementId)) {

                    int frn = Integer.parseInt(element.getAttribute("frn"));
                    String format = element.getAttribute("format");
                    int length = Integer.parseInt(element.getAttribute("length"));
                    String name = element.getElementsByTagName("name").item(0).getTextContent();

                    asterixFlightData.addAsterixField(frn, length, name, elementId, format);

                    if (format.equals("compound") && !asterixSubfieldIds.isEmpty()) {
                        NodeList subfields = element.getElementsByTagName("subfield");
                        parseXml(subfields, asterixIds, asterixSubfieldIds, true, elementId);
                    }
                } else if (asterixSubfieldIds.contains(index + 1)) {

                    int subfieldFrn = Integer.parseInt(element.getAttribute("frn"));
                    int subfieldLength = Integer.parseInt(element.getAttribute("length"));
                    String subfieldName = element.getElementsByTagName("name").item(0).getTextContent();
                    String subfieldFormat = element.getAttribute("format");

                    if (null != parentId) {

                        asterixFlightData.addAsterixSubfield(parentId, subfieldFrn, subfieldLength, subfieldName, subfieldFormat);
                    }
                }
            }
        }
    }



    private Document readXml() {

        try {
            File xmlFile = new File(Constants.ASTERIX_DATA_FILE);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);
            doc.getDocumentElement().normalize();
            return doc;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return null;
    }
}
