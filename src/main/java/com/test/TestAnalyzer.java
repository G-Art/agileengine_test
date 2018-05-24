package com.test;

import com.test.service.AnalizerService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.Properties;

import static java.lang.System.out;

public class TestAnalyzer {

    private AnalizerService analizerService = new AnalizerService();

    private static final String MAKE_EVERYTHING_OK_BUTTON = "make-everything-ok-button";

    public static void main(String[] args) throws IOException {
        TestAnalyzer testAnalyzer = new TestAnalyzer();
        testAnalyzer.process(args[0], args[1], testAnalyzer.getTargetElementId());
    }

    public void process(String origin, String diff, String targetElementId) throws IOException {
        Document originDoc = Jsoup.parse(new File(origin), "UTF-8");
        HtmlElement etalonElement = new HtmlElement(originDoc.getElementById(targetElementId));
        Document diffDoc = Jsoup.parse(new File(diff), "UTF-8");

        out.println( String.format("Looking for similar element: [ %s ]", etalonElement.getElement().toString()));

        Optional<HtmlElement> element = analizerService.findElement(diffDoc, etalonElement);

        if(element.isPresent()){
            out.println("Element found!");
            out.println(element.get().getPath());
        }else{
            out.println("Nothing is found!!!");
        }

    }

    private String getTargetElementId() {
        Properties prop = new Properties();
        String filename = "config.properties";
        try(InputStream input = this.getClass().getClassLoader().getResourceAsStream(filename);) {

            if (input == null) {
                out.println("Sorry, unable to find " + filename);
                return MAKE_EVERYTHING_OK_BUTTON;
            }

            prop.load(input);

            return Optional.ofNullable(prop.getProperty("target.element.id")).orElse(MAKE_EVERYTHING_OK_BUTTON);

        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return MAKE_EVERYTHING_OK_BUTTON;
    }
}
