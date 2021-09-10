package com.openclassrooms.realestatemanager.data.room.sample;

import com.openclassrooms.realestatemanager.data.room.model.Agent;

public class SampleAgent {
    public Agent[] getSample(){
        return new Agent[]{
                new Agent(1, "Williams"),
                new Agent(2, "Johnson"),
                new Agent(3, "Miller"),
                new Agent(4, "Jones"),
                new Agent(5, "Rodriguez"),
                new Agent(6, "Lee"),
                new Agent(7, "Lee"),
                new Agent(8, "Davis"),
                new Agent(9, "Rivera"),
                new Agent(10, "Smith"),
                new Agent(11, "Brown")
        };
    }
}
