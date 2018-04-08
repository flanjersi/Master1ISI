package fr.master1ISI.xmlParser;

import fr.master1ISI.fileDownloader.FileDownloader;
import fr.master1ISI.fileDownloader.GitHubFileDownloader;
import fr.master1ISI.fileDownloader.KaggleFileDownloader;
import fr.master1ISI.wrapperConception2.ConfigurationWrapper;
import fr.master1ISI.wrapperConception2.WrapperCSVDynamics;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ConfigParser {

    private String nameFile;

    private Document document;

    private Node configRoot;

    private List<Source> sourcesList;
    private int iteratorSourcesCpt;

    private List<View> viewsList;
    private int iteratorViewCpt;


    public ConfigParser(String nameFile){
        this.nameFile = nameFile;
        this.iteratorSourcesCpt = 0;
        this.iteratorViewCpt = 0;
        this.sourcesList = null;
        this.viewsList = null;
    }

    public void initParser() throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        document = builder.parse(getClass().getResourceAsStream(nameFile));
        configRoot = document.getElementsByTagName("config").item(0);
    }

    public boolean parseSources(){

        NodeList nodeList = configRoot.getChildNodes();
        Node node;

        Node sources = null;

        for(int i = 0 ; i < nodeList.getLength() ; i++){
            node = nodeList.item(i);

            if(node.getNodeType() != Node.ELEMENT_NODE) continue;
            if(!node.getNodeName().equals("sources")) continue;

            sources = node;
        }

        if(sources == null) return false;

        sourcesList = new ArrayList<>();

        nodeList = sources.getChildNodes();

        for(int i = 0 ; i < nodeList.getLength() ; i++){
            node = nodeList.item(i);

            if(node.getNodeType() != Node.ELEMENT_NODE) continue;
            if(!node.getNodeName().equals("source")) continue;

            sourcesList.add(parseSource(node));
        }

        return true;
    }

    private Source parseSource(Node sourceNode) {

        ConfigurationWrapper configurationWrapper = new ConfigurationWrapper();

        FileDownloader fileDownloader = null;

        Element elementSource = (Element) sourceNode;

        String nameTable = elementSource.getElementsByTagName("nameTable").item(0).getTextContent();
        String pathFileDest = elementSource.getElementsByTagName("pathFileDest").item(0).getTextContent();

        String type = elementSource.getAttribute("type");

        Element typeSource = (Element) elementSource.getElementsByTagName(type).item(0);

        if(type.equalsIgnoreCase("github")){
            String ownerName = typeSource.getElementsByTagName("ownerName").item(0).getTextContent();
            String repoName = typeSource.getElementsByTagName("repoName").item(0).getTextContent();
            String pathFileGithub = typeSource.getElementsByTagName("pathFileGithub").item(0).getTextContent();

            fileDownloader = new GitHubFileDownloader(ownerName, repoName, pathFileGithub, pathFileDest);

        }
        else if(type.equalsIgnoreCase("kaggle")){
            String nameDataSet = typeSource.getElementsByTagName("nameDataSet").item(0).getTextContent();
            String nameFileData = typeSource.getElementsByTagName("nameFileData").item(0).getTextContent();

            String pathFolderDest = pathFileDest.substring(0, pathFileDest.lastIndexOf('/'));
            fileDownloader = new KaggleFileDownloader(nameDataSet, nameFileData, pathFolderDest);
        }

        configurationWrapper.setNameTable(nameTable);
        configurationWrapper.setFile(new File(pathFileDest));
        WrapperCSVDynamics wrapperCSVDynamics = new WrapperCSVDynamics(configurationWrapper);

        return new Source(nameTable.toUpperCase(), wrapperCSVDynamics, fileDownloader);
    }


    public boolean parseViews(){
        NodeList nodeList = configRoot.getChildNodes();
        Node node;

        Node views = null;

        for(int i = 0 ; i < nodeList.getLength() ; i++){
            node = nodeList.item(i);

            if(node.getNodeType() != Node.ELEMENT_NODE) continue;
            if(!node.getNodeName().equals("views")) continue;

            views = node;
        }

        if(views == null) return false;

        sourcesList = new ArrayList<>();

        nodeList = views.getChildNodes();

        viewsList = new ArrayList<>();

        for(int i = 0 ; i < nodeList.getLength() ; i++){
            node = nodeList.item(i);

            if(node.getNodeType() != Node.ELEMENT_NODE) continue;
            if(!node.getNodeName().equals("view")) continue;

            viewsList.add(parseView(node));
        }

        return true;
    }

    private View parseView(Node viewNode){
        Element elementSource = (Element) viewNode;

        String nameView = elementSource.getElementsByTagName("name").item(0).getTextContent();
        String request = elementSource.getElementsByTagName("request").item(0).getTextContent();

        Node node = elementSource.getElementsByTagName("tables").item(0);

        NodeList nodeList = node.getChildNodes();

        List<String> tables = new ArrayList<>();

        Node nodeTable;
        for(int i = 0 ; i < nodeList.getLength() ; i++){
            nodeTable = nodeList.item(i);

            if(nodeTable.getNodeType() != Node.ELEMENT_NODE) continue;
            if(!nodeTable.getNodeName().equals("table")) continue;

            tables.add(nodeTable.getTextContent());
        }

        return new View(nameView, request, tables);
    }


    public boolean hasNextSource(){
        if(sourcesList == null) return false;
        if(iteratorSourcesCpt == sourcesList.size()) return false;

        return true;
    }

    public boolean hasNextView(){
        if(viewsList == null) return false;
        if(iteratorViewCpt == viewsList.size()) return false;

        return true;
    }

    public Source nextSource(){
        if(!hasNextSource()) return null;

        Source source = sourcesList.get(iteratorSourcesCpt);
        iteratorSourcesCpt++;

        return source;
    }

    public View nextView(){
        if(!hasNextView()) return null;

        View view = viewsList.get(iteratorViewCpt);
        iteratorViewCpt++;

        return view;
    }




}
