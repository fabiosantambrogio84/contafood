package com.contafood.xTemp;

public interface Interface1 {

    default void log(){
        System.out.println("LOG");
    }

    static int sum(){
        return 1;
    }
}
