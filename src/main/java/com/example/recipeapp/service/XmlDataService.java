package com.example.recipeapp.service;

import com.example.recipeapp.model.Recipe;
import com.example.recipeapp.model.User;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import jakarta.annotation.PostConstruct;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

@Service
public class XmlDataService {

    @Autowired
    private ScraperService scraperService;

    private List<Recipe> recipesInMemory = new ArrayList<>();
    private List<User> usersInMemory = new ArrayList<>();
    private final String FILE_PATH = "src/main/resources/data.xml";

    @PostConstruct
    public void init() {
        loadDataIntoMemory();
    }

    public void loadDataIntoMemory() {
        recipesInMemory.clear();
        usersInMemory.clear();
        try {
            File xmlFile = new File(FILE_PATH);
            if (!xmlFile.exists()) {
                System.out.println("Data XML not found, scraping...");
                scraperService.scrapeAndCreateDataFile(FILE_PATH);
            }

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);
            doc.getDocumentElement().normalize();

            NodeList userList = doc.getElementsByTagName("user");
            for (int i = 0; i < userList.getLength(); i++) {
                Node node = userList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element el = (Element) node;
                    String name = getElementText(el, "name");
                    String surname = getElementText(el, "surname");
                    String skillLevel = getElementText(el, "skillLevel");
                    String cuisine = getElementText(el, "preferredCuisine");
                    usersInMemory.add(new User(name, surname, skillLevel, cuisine));
                }
            }

            NodeList recipeList = doc.getElementsByTagName("recipe");
            for (int i = 0; i < recipeList.getLength(); i++) {
                Node node = recipeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element el = (Element) node;
                    String title = getElementText(el, "title");
                    String cuisine1 = getElementText(el, "cuisine1");
                    String cuisine2 = getElementText(el, "cuisine2");
                    String difficulty = getElementText(el, "difficulty");
                    recipesInMemory.add(new Recipe(title, cuisine1, cuisine2, difficulty));
                }
            }
            System.out.println("Loaded " + recipesInMemory.size() + " recipes and " + usersInMemory.size() + " users into memory.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getElementText(Element parent, String tagName) {
        NodeList list = parent.getElementsByTagName(tagName);
        if (list != null && list.getLength() > 0) {
            return list.item(0).getTextContent();
        }
        return "";
    }

    public void addRecipe(Recipe recipe) {
        recipesInMemory.add(recipe);
        saveDocument();
    }

    public void addUser(User user) {
        usersInMemory.add(0, user);
        saveDocument();
    }

    private void saveDocument() {
        try {
            File xmlFile = new File(FILE_PATH);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);
            doc.getDocumentElement().normalize();

            Document newDoc = dBuilder.newDocument();
            Element rootElement = newDoc.createElement("appData");
            rootElement.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
            rootElement.setAttribute("xsi:noNamespaceSchemaLocation", "data.xsd");
            newDoc.appendChild(rootElement);

            Element usersElement = newDoc.createElement("users");
            rootElement.appendChild(usersElement);
            for (User u : usersInMemory) {
                Element userEl = newDoc.createElement("user");
                userEl.appendChild(createElementWithValue(newDoc, "name", u.getName()));
                userEl.appendChild(createElementWithValue(newDoc, "surname", u.getSurname()));
                userEl.appendChild(createElementWithValue(newDoc, "skillLevel", u.getSkillLevel()));
                userEl.appendChild(createElementWithValue(newDoc, "preferredCuisine", u.getPreferredCuisine()));
                usersElement.appendChild(userEl);
            }

            Element recipesElement = newDoc.createElement("recipes");
            rootElement.appendChild(recipesElement);
            for (Recipe r : recipesInMemory) {
                Element recipeEl = newDoc.createElement("recipe");
                recipeEl.appendChild(createElementWithValue(newDoc, "title", r.getTitle()));
                recipeEl.appendChild(createElementWithValue(newDoc, "cuisine1", r.getCuisine1()));
                recipeEl.appendChild(createElementWithValue(newDoc, "cuisine2", r.getCuisine2()));
                recipeEl.appendChild(createElementWithValue(newDoc, "difficulty", r.getDifficulty()));
                recipesElement.appendChild(recipeEl);
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

            DOMSource source = new DOMSource(newDoc);
            StreamResult result = new StreamResult(xmlFile);
            transformer.transform(source, result);

            System.out.println("Successfully saved data to XML!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Element createElementWithValue(Document doc, String name, String value) {
        Element el = doc.createElement(name);
        el.appendChild(doc.createTextNode(value));
        return el;
    }

    public List<Recipe> getRecipesInMemory() {
        return recipesInMemory;
    }

    public List<User> getUsersInMemory() {
        return usersInMemory;
    }

    public List<Recipe> getRecipesBySkill() {
        return queryRecipesByXPath("//recipe[difficulty = /appData/users/user[1]/skillLevel]");
    }

    public List<Recipe> getRecipesBySkillAndCuisine() {
        return queryRecipesByXPath("//recipe[difficulty = /appData/users/user[1]/skillLevel and (cuisine1 = /appData/users/user[1]/preferredCuisine or cuisine2 = /appData/users/user[1]/preferredCuisine)]");
    }

    private List<Recipe> queryRecipesByXPath(String expression) {
        List<Recipe> resultList = new ArrayList<>();
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(new File(FILE_PATH));
            doc.getDocumentElement().normalize();

            XPath xPath = XPathFactory.newInstance().newXPath();
            NodeList nodeList = (NodeList) xPath.compile(expression).evaluate(doc, XPathConstants.NODESET);

            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element el = (Element) node;
                    String title = getElementText(el, "title");
                    String cuisine1 = getElementText(el, "cuisine1");
                    String cuisine2 = getElementText(el, "cuisine2");
                    String difficulty = getElementText(el, "difficulty");
                    resultList.add(new Recipe(title, cuisine1, cuisine2, difficulty));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultList;
    }

    public String getRecipesXslt() {
        try {
            File xmlFile = new File(FILE_PATH);
            File xsltFile = new File("src/main/resources/recipes.xsl");
            
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer(new javax.xml.transform.stream.StreamSource(xsltFile));
            
            java.io.StringWriter writer = new java.io.StringWriter();
            transformer.transform(new javax.xml.transform.stream.StreamSource(xmlFile), new javax.xml.transform.stream.StreamResult(writer));
            return writer.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "Error transforming XML";
        }
    }
}
