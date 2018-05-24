package com.test;

import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Element;

import java.util.List;

public class HtmlElement {

    private Element element;
    private String path;

    private int score = 0;

    public HtmlElement(Element element) {
        this.element = element;
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(element.toString());
        stringBuilder.append(" : ");

        Element el = element.parent();
        do {
            stringBuilder.append(el.tagName());
            if(el.hasParent()){
                stringBuilder.append(" : ");
                el = el.parent();
            }else {
                el = null;
            }

        } while (el != null);

        path = stringBuilder.toString();
    }

    public Element getElement() {
        return element;
    }

    public List<Attribute> getAttributes(){
        return element.attributes().asList();
    }

    public String getText(){
        return element.text();
    }

    public String getTag(){
        return element.tag().getName();
    }

    public int getScore() {
        return score;
    }

    public String getPath() {
        return path;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
