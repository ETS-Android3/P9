package com.openclassrooms.realestatemanager.data.room.sample;

import com.openclassrooms.realestatemanager.data.room.model.Agent;

public class SampleAgent {
    public Agent[] getSample(){
        return new Agent[]{
                new Agent(1, "Williams", "Williams@realestate.com", "646-268-3154"),
                new Agent(2, "Johnson", "Johnson@realestate.com", "607-624-9366"),
                new Agent(3, "Miller", "Miller@realestate.com", "716-804-7724"),
                new Agent(4, "Jones", "Jones@realestate.com", "518-752-3860"),
                new Agent(5, "Rodriguez", "Rodriguez@realestate.com", "646-446-9258"),
                new Agent(6, "Line", "Line@realestate.com", "914-321-0734"),
                new Agent(7, "Lee Vihn", "Lee.Vihn@realestate.com", "646-785-9722"),
                new Agent(8, "Davis", "Davis@realestate.com", "716-488-6426"),
                new Agent(9, "Rivera", "Rivera@realestate.com", "917-742-2959"),
                new Agent(10, "Smith", "Smith@realestate.com", "585-275-6747"),
                new Agent(11, "Brown", "Brown@realestate.com", "516-428-9478")
        };
    }
}
