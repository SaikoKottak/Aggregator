package com.javarush.task.task28.task2810.model;

import com.javarush.task.task28.task2810.vo.Vacancy;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HHStrategy implements Strategy {

    private static final String URL_FORMAT = "http://hh.ua/search/vacancy?text=java+%s&page=%d";

    @Override
    public List<Vacancy> getVacancies(String searchString) {
        String vacancyQuery = "[data-qa=\"vacancy-serp__vacancy\"]";
        String titleQuery = "[data-qa=\"vacancy-serp__vacancy-title\"]";
        String compensationQuery = "[data-qa=\"vacancy-serp__vacancy-compensation\"]";
        String addressQuery = "[data-qa=\"vacancy-serp__vacancy-address\"]";
        String employerQuery = "[data-qa=\"vacancy-serp__vacancy-employer\"]";

        List<Vacancy> vacancies = new ArrayList<>();
        Vacancy vacancy;
        int i = 0;
        try {
            while (true) {
                Document doc = getDocument(searchString, i);

                Elements vacancyElement = doc.select(vacancyQuery);

                if (!vacancyElement.isEmpty()) {

                    for (Element element : vacancyElement) {

                        vacancy = new Vacancy();
                        vacancy.setTitle(element.select(titleQuery).text());

                        vacancy.setCity(element.select(addressQuery).text());
                        vacancy.setCompanyName(element.select(employerQuery).text());

                        vacancy.setSiteName("http:/hh.ua/");
                        vacancy.setUrl(element.select(titleQuery).attr("href"));

                        if (!element.select(compensationQuery).isEmpty()) {
                            vacancy.setSalary(element.select(compensationQuery).text());

                        } else {
                            vacancy.setSalary("");

                        }
                        vacancies.add(vacancy);
                    }
                } else {
                    break;
                }
                i++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return vacancies;
    }

    protected Document getDocument(String searchString, int page) throws IOException {
        String url = String.format(URL_FORMAT, searchString, page);
        return Jsoup.connect(url)
                .userAgent("Chrome/58.0.3029.110")
                .referrer("http:/hh.ua/")
                .get();
    }
}
