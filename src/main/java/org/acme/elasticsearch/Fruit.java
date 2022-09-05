package org.acme.elasticsearch;

import lombok.Builder;

@Builder
public class Fruit {
    public String id;
    public String name;
    public String color;
}