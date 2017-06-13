package com.javarush.task.task28.task2810;


import com.javarush.task.task28.task2810.model.*;
import com.javarush.task.task28.task2810.view.HtmlView;

public class Aggregator {
    public static void main(String[] args) {
        HtmlView view = new HtmlView();
        Provider headHunter = new Provider(new HHStrategy());
        Provider moiKrug = new Provider(new MoikrugStrategy());
        Provider rab66 = new Provider(new Rabota66Strategy());
        Model model = new Model(view,headHunter, moiKrug);
        Controller controller = new Controller(model);
        view.setController(controller);
        view.userRequest("Java junior");
    }
}
