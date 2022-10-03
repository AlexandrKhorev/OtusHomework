package ru.otus;


import com.google.common.base.Objects;

import java.util.ArrayList;
import java.util.List;

public class HelloOtus {
    public static void main(String[] args) {
        List<String> firstList = null;
        List<String> secondList = new ArrayList<>(){{
            add("Hello");
            add("Otus");
        }};

        System.out.println(Objects.equal(firstList, secondList));
    }
}
