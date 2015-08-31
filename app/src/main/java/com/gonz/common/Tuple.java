package com.gonz.common;

import java.io.Serializable;

public class Tuple implements Comparable<Tuple>, Serializable {

    String name;
    float mark;

    public Tuple(String n, float m) {
        this.name = n;
        this.mark = m;
    }

    @Override
    public int compareTo(Tuple o) {
        Tuple other = (Tuple) o;
        if(this.mark > other.mark)
            return 1;
        else if (this.mark < other.mark)
            return -1;
        return 0;
    }

    public String getName() {   return this.name;   }
    public float getMark() {  return this.mark;   }

    @Override
    public String toString() {
        return this.name + " :  " + this.mark;
    }

}