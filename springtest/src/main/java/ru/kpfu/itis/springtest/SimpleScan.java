package ru.kpfu.itis.springtest;

import org.springframework.beans.factory.annotation.Autowired;

public class SimpleScan {

    @Autowired
    private Converter converter;


    public float getNewCurrency(float count) {
        count += 10;
        return count * converter.getRate();
    }


}
