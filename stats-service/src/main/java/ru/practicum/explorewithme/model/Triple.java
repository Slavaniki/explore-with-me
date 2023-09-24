package ru.practicum.explorewithme.model;

public class Triple<T, U, V> {
    private final T first;
    private final U second;
    private final V third;

    public Triple(T first, U second, V third) {
        this.first = first;
        this.second = second;
        this.third = third;
    }

    public V getThird() {
        return third;
    }

    public U getSecond() {
        return second;
    }

    public T getFirst() {
        return first;
    }
}