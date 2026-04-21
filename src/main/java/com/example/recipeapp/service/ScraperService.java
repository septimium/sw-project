package com.example.recipeapp.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class ScraperService {

    private static final String[] CUISINES = {"Italian", "Asian", "Mexican", "French", "Indian", "American", "British"};
    private static final String[] DIFFICULTIES = {"Beginner", "Intermediate", "Advanced"};

    public void scrapeAndCreateDataFile(String filePath) throws IOException {
        String url = "https://www.bbcgoodfood.com/recipes/collection/budget-autumn";
        Document doc = Jsoup.connect(url).get();
        Elements recipeElements = doc.select(".card__content h2");
        
        List<String> titles = new ArrayList<>();
        for (Element e : doc.select("h2, h3")) {
            String t = e.text();
            if (t.length() > 5 && !t.contains("App only") && !t.contains("premium") && titles.size() < 30) {
                titles.add(t);
            }
        }

        if (titles.isEmpty()) {
            titles.add("Default Recipe 1");
            titles.add("Default Recipe 2");
        }

        Random random = new Random();

        StringBuilder xml = new StringBuilder();
        xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        xml.append("<appData xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"data.xsd\">\n");
        
        xml.append("  <users>\n");
        xml.append("    <user>\n");
        xml.append("      <name>John</name>\n");
        xml.append("      <surname>Doe</surname>\n");
        xml.append("      <skillLevel>Beginner</skillLevel>\n");
        xml.append("      <preferredCuisine>Italian</preferredCuisine>\n");
        xml.append("    </user>\n");
        xml.append("  </users>\n");
        
        xml.append("  <recipes>\n");
        for (int i = 0; i < 30 && i < titles.size(); i++) {
            String title = titles.get(i).replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
            String c1 = CUISINES[random.nextInt(CUISINES.length)];
            String c2 = CUISINES[random.nextInt(CUISINES.length)];
            String diff = DIFFICULTIES[random.nextInt(DIFFICULTIES.length)];

            xml.append("    <recipe>\n");
            xml.append("      <title>").append(title).append("</title>\n");
            xml.append("      <cuisine1>").append(c1).append("</cuisine1>\n");
            xml.append("      <cuisine2>").append(c2).append("</cuisine2>\n");
            xml.append("      <difficulty>").append(diff).append("</difficulty>\n");
            xml.append("    </recipe>\n");
        }
        xml.append("  </recipes>\n");
        xml.append("</appData>\n");

        File file = new File(filePath);
        if (!file.getParentFile().exists()) {
             file.getParentFile().mkdirs();
        }
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(xml.toString());
        }
    }
}

