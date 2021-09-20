package com.openclassrooms.realestatemanager.data.room.sample;

import com.openclassrooms.realestatemanager.data.room.model.Property;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SampleProperty {
    public Property[] getSample() throws ParseException {
        Date date1 = new SimpleDateFormat("dd/MM/yyy").parse("09/09/2021");

        return new Property[]{
                new Property(1,
                        29995000,
                        8350,
                        "Live your dream lifestyle in this ultimate modern home designed by Foster + Partners. Residence 15 consists of the full private floor featuring 11 ft ceilings throughout the over 8000 sqft residence. The surrounding floor to ceiling windows maximize the light and sweeping 360-degree views of the most sought-after iconic New York City Landmarks and the Hudson River.",
                        "551 W 21ST ST, 15THFLOOR, CHELSEA, NEW YORK, NY 10011",
                        "High Line, Art Galeries",
                        true,
                        date1,
                        null,
                        4,
                        1,
                        1),
                new Property(2,
                        29900000,
                        4296,
                        "Wrap yourself in the ultimate ambience of luxury, style and sophistication in this expansive residence occupying half of the 86th floor at the coveted new Central Park Tower. Boasting impeccably-designed, finely-finished 4,295 sf interiors and 70+ of stunning Central Park frontage, there is truly no other living destination like this, with views beyond compare. The generous 4 bedroom, 4.5 bathroom",
                        "86E CENTRAL PARK TOWER, 217 WEST 57TH ST, MIDTOWN MANHATTAN, NEW YORK, NY",
                        "Central Park",
                        true,
                        date1,
                        null,
                        4,
                        1,
                        1),
                new Property(3,
                        23000000,
                        20000,
                        "Located on one of the best town house blocks on the upper east side, this historic single-family home has been newly renovated and is ready to move in. Built in 1869, and reimagined in 1899 by master builder Michael Reed, this home",
                        "116 EAST 70TH ST, LENOX HILL, NEW YORK, NY",
                        "Historic District",
                        true,
                        date1,
                        null,
                        5,
                        1,
                        2),
                new Property(4,
                        135000000,
                        8055,
                        "Among the clouds, at one thousand feet, is the Floating Inner Garden. A one-of-a-kind Hiroshi Sugimoto-designed masterpiece. This serene full-floor residence offers 8,055 square feet (748 square meters) of living space, five bedrooms, five bathrooms, two powder rooms, unobstructed helicopter views in all directions, soaring ceilings, two private elevator landings, custom-built mechanical, electrical, plumbing, and HVAC systems, and a hand selection of the finest natural",
                        "432 Park Ave, #79, NEW_YORK, NY",
                        "Central Park",
                        true,
                        date1,
                        null,
                        1,
                        1,
                        1)
        };
    }
}
