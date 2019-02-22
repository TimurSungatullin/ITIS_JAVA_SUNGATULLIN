package ru.kpfu.itis.springtest;


import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Scanner;

public class JavaSimpleConfigTest {

    public static void main(String[] args) {
        System.out.println("Java config context configuration test");

        ApplicationContext context
                = new AnnotationConfigApplicationContext(SimpleConfig.class);

        Converter obj = null;
        Scanner sc = new Scanner(System.in);
        System.out.println("В какую валюты хотите перевести: (U)SD, (E)uro");
        String currency = sc.nextLine();
        if (currency.equals("U")) {
           obj = (Converter) context.getBean("converterToUSD");
        }
        if (currency.equals("E")) {
           obj = (Converter) context.getBean("converterToEuro");
        }
        try {
            System.out.println("Сколько хотитет перевести?");
            float count = sc.nextFloat();
            System.out.println(obj.getNewCurrency(count));
        }
        catch (NullPointerException ex) {
            System.out.println("Я так не умею");
        }
    }

}
