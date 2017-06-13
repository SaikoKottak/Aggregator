package com.javarush.task.task28.task2810.view;

import com.javarush.task.task28.task2810.Controller;
import com.javarush.task.task28.task2810.vo.Vacancy;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class HtmlView implements View {

    private final String filePath = "./src/" + this.getClass().getPackage().getName().replace('.', '/') + "/vacancies.html";

    private Controller controller;


    @Override
    public void update(List<Vacancy> vacancies) {

        try {
            updateFile(getUpdatedFileContent(vacancies));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void setController(Controller controller) {
        this.controller = controller;
    }


    public void userRequest(String request) {
        controller.onRequestSelect(request);
    }


    private String getUpdatedFileContent(List<Vacancy> vacancies) {

        try {
            Document document = getDocument();

            Element templateElement = document.getElementsByClass("template").first();
            Element cleanElement = templateElement.clone();
            cleanElement.removeAttr("style");
            cleanElement.removeClass("template");
            document.select("tr[class=vacancy]").remove();

            for (Vacancy vacancy : vacancies) {
                Element vacancyElement = cleanElement.clone();

                vacancyElement.getElementsByClass("city").first().text(vacancy.getCity());
                vacancyElement.getElementsByClass("companyName").first().text(vacancy.getCompanyName());
                vacancyElement.getElementsByClass("salary").first().text(vacancy.getSalary());

                Element link = vacancyElement.getElementsByTag("a").first();
                link.text(vacancy.getTitle());
                link.attr("href", vacancy.getUrl());

                templateElement.before(vacancyElement.outerHtml());
            }

            return document.html();

        } catch (IOException e) {
            e.printStackTrace();
            return "Some exception occurred";
        }
    }


    private void updateFile(String fileContent) throws IOException {

        FileWriter fileWriter = new FileWriter(filePath);
        fileWriter.write(fileContent);
        fileWriter.close();
    }


    protected Document getDocument() throws IOException {
        return Jsoup.parse(
                new File(filePath), "UTF-8");
    }
}