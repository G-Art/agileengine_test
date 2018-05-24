package com.test.service;

import com.test.HtmlElement;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class AnalizerService {

    public Optional<HtmlElement> findElement(Document diffDoc, HtmlElement etalonElement) {
        List<HtmlElement> htmlElementList = findCandidates(diffDoc, etalonElement);

        return htmlElementList.stream()
                .map(htmlElement -> fillScore(htmlElement, etalonElement))
                .sorted(Comparator.comparingInt(HtmlElement::getScore).reversed()).findFirst();
    }

    private List<HtmlElement> findCandidates(Document diffDoc, HtmlElement etalonElement) {
        Elements elements = diffDoc.select(createSelectors(etalonElement));

        return elements.stream()
                .distinct()
                .map(HtmlElement::new)
                .collect(Collectors.toList());
    }

    private String createSelectors(HtmlElement etalonElement) {
        List<String> selectors = new ArrayList<>();
        selectors.add(etalonElement.getTag());

        etalonElement.getAttributes().forEach(attribute -> {
            selectors.add(String.format("[%s='%s']", attribute.getKey(), attribute.getValue()));
            selectors.add(String.format("%s[%s='%s']", etalonElement.getTag(), attribute.getKey(), attribute.getValue()));
        });
        return String.join(", ", selectors);
    }

    private HtmlElement fillScore(HtmlElement htmlElement, HtmlElement etalonElement) {
        int score = 0;
        if (htmlElement.getTag().equals(etalonElement.getTag())) {
            score++;
        }
        if (htmlElement.getText().equals(etalonElement.getText())) {
            score++;
        }

        Map<String, String> attributes = etalonElement.getAttributes().stream().collect(Collectors.toMap(Attribute::getKey, Attribute::getValue));

        for (Attribute attribute : htmlElement.getAttributes()) {

            if (attributes.get(attribute.getKey()) != null &&
                    (attribute.getValue().equals(attributes.get(attribute.getKey())) ||
                    attribute.getValue().contains(attributes.get(attribute.getKey())))) {
                score++;
            }
        }

        htmlElement.setScore(score);
        return htmlElement;
    }

}
