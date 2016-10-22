package com.rosendal.elevkarrosendal.fragments.food;

import com.rosendal.elevkarrosendal.fragments.refreshable_fragment.RefreshTask;
import com.rosendal.elevkarrosendal.fragments.refreshable_fragment.RefreshTaskHandler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;


class RefreshFoodTask extends RefreshTask<FoodData> {
    private int minTime;

    RefreshFoodTask(RefreshTaskHandler<FoodData> handler, int minTime) {
        super(handler);
        this.minTime = minTime;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected FoodData doWork() {
        try {
            ArrayList<String>[] result = new ArrayList[5];
            String url = "https://sites.google.com/feeds/content/rosendalsgymnasiet.se/rosnet?path=/veckans-mat";
            Document doc = Jsoup.connect(url).get();
            Elements e = doc.select("table[cellspacing].sites-layout-name-two-column");
            ArrayList<String[]> extras = new ArrayList<>();
            String title = "";
            if (e.size() > 0) {
                Elements el2 = e.get(0).getElementsByAttributeValueMatching("color", "#b45f06");
                title =  el2.text();
                Elements el3 = el2.get(0).parent().siblingElements();
                Element el4 = el3.get(1);
                Elements elements = el4.children().get(0).children();
                int day = 0;
                result[0] = new ArrayList<>();
                boolean b = false, passed = false;
                String name = "", value = "";

                for (Element element : elements) {
                    if (element.text().startsWith("Varje dag serveras")) break;
                    if (b) {
                        if (!element.text().isEmpty() && !element.text().equals(" ") && element.children().size() == 0){
                            value = element.text();
                        } else {
                            b = false;
                            String[] obj = new String[2];
                            obj[0] = name;
                            obj[1] = value;
                            extras.add(obj);
                        }
                    }
                    if (!element.text().isEmpty() && !element.text().equals(" ") && !(element.toString().contains("&nbsp")) && !passed) {
                        if (checkDay(element.html()) != 5) {
                            day = checkDay(element.html());
                            result[day] = new ArrayList<>();
                        } else if (element.children().size() == 0) {
                            result[day].add(element.text());
                        } else if (!(element.child(0).tagName().equals("strong") || element.child(0).tagName().equals("b") || element.child(0).tagName().equals("br"))) {
                            result[day].add(element.text());
                        }
                    }
                    if (element.text().toLowerCase().contains("veckans soppa")) {
                        passed = true;
                        b = true;
                        name = "Veckans soppa:";
                        //value = element.text().substring("Veckans soppa:".length(), element.text().length());
                    } else if (element.text().toLowerCase().contains("veckans sallad")) {
                        passed = true;
                        b = true;
                        name = "Veckans sallad:";
                        //value = element.text().substring("Veckans sallad:".length(), element.text().length());
                    }
                }
            }
            ArrayList<String>[] food = new ArrayList[5];
            for (int i = 0; i < 5; i++) {
                ArrayList list = result[i];
                food[i] = (list != null ? list : new ArrayList<>());
            }
            FoodData toReturn = new FoodData();
            toReturn.title = title;
            toReturn.extras = extras;
            toReturn.food = food;
            return toReturn;
        } catch (IOException e1) {
            e1.printStackTrace();
            return new FoodData();
        }
    }

    private static int checkDay(String html) {
        if (html.contains("<b>Måndag</b>") || html.contains("<b>Måndag</b>")) {
            return 0;
        } else if (html.contains("<b>Tisdag</b>") || html.contains("<b>Tisdag</b>")) {
            return 1;
        } else if (html.contains("<b>Onsdag</b>") || html.contains("<b>Onsdag</b>")) {
            return 2;
        } else if (html.contains("<b>Torsdag</b>") || html.contains("<b>Torsdag</b>")) {
            return 3;
        } else if (html.contains("<b>Fredag</b>") || html.contains("<b>Fredag</b>")) {
            return 4;
        }
        return 5;
    }

}
