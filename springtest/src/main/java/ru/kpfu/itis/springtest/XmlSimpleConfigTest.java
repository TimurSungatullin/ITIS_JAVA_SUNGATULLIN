package ru.kpfu.itis.springtest;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import java.util.Scanner;

public class XmlSimpleConfigTest {

    public static void main(String[] args) {
        new XmlSimpleConfigTest();
    }

    public XmlSimpleConfigTest(){
        System.out.println("XML config context configuration test");

        ApplicationContext context =
                new FileSystemXmlApplicationContext(getClass().getResource("/simpleConfig.xml").toString());

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


        //Example ComponentScan 3.2 task
        ApplicationContext contextScan = new ClassPathXmlApplicationContext("simpleScanConfig.xml");
        SimpleScan scan = (SimpleScan) contextScan.getBean("simplescan");
        System.out.println(scan.getNewCurrency(10));
    }
}
