package com.company;

import java.io.Serializable;
import java.util.NavigableMap;
import java.util.Random;
import java.util.TreeMap;


/**
 * Generic helper class to generate random next value based on weights
 * */
public  class RandomCollection<E> implements Serializable {
    private final NavigableMap<Double, E> map = new TreeMap<Double, E>();
    private final Random random;
    private double total = 0;

    /**
     * constructor
     * */
    public RandomCollection() {
        this(new Random());
    }

    public RandomCollection(Random random) {
        this.random = random;
    }

    /**
     * insert weight with the generic object
     * */
    public RandomCollection<E> add(double weight, E result) {
        if (weight <= 0) return this;
        total += weight;
        map.put(total, result);
        return this;
    }

    /**
     * get next random value
     * */
    public E next() {
        double value = random.nextDouble() * total;
        return map.higherEntry(value).getValue();
    }
}