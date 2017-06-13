package com.javarush.task.task28.task2810.model;

import com.javarush.task.task28.task2810.vo.Vacancy;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MoikrugStrategy implements Strategy {

    private static final String URL_FORMAT = "https://moikrug.ru/vacancies?page=%d&q=%s";
    @Override
    public List<Vacancy> getVacancies(String searchString) {
        List<Vacancy> vacancies = new ArrayList<>();
        int page = 1;
        Document doc;
        try {
            while (true) {
                doc = getDocument(searchString, page);
                Elements vacancyElement = doc.getElementsByClass("job");
                if(!vacancyElement.isEmpty()){
                    for(Element element: vacancyElement){
                        Vacancy vacancy = new Vacancy();
                        vacancy.setTitle(element.getElementsByAttributeValue("class","title").text());
                        vacancy.setCompanyName(element.getElementsByAttributeValue("class","company_name").text());
                        vacancy.setSiteName("https://moikrug.ru/");
                        vacancy.setUrl("https://moikrug.ru" + element.select("a[class=job_icon]").attr("href"));
                        String salary = element.getElementsByAttributeValue("class", "salary").text();
                        String city = element.getElementsByAttributeValue("class", "location").text();
                        vacancy.setSalary(salary.length()==0 ? "" : salary);
                        vacancy.setCity(city.length()==0 ? "" : city);
                        vacancies.add(vacancy);
                    }
                } else {
                    break;
                }
                page++;
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        return vacancies;
    }

    private Document getDocument(String form, int page) throws IOException {
        String url = String.format(URL_FORMAT,page,form);
        return Jsoup.connect(url)
                .userAgent("Chrome/58.0.3029.110")
                .referrer("https://moikrug.ru")
                .get();
    }
}
