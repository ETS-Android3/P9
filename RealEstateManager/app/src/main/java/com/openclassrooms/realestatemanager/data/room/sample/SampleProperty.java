package com.openclassrooms.realestatemanager.data.room.sample;

import com.openclassrooms.realestatemanager.data.room.model.Property;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SampleProperty {
    public Property[] getSample() throws ParseException {
        Date date1 = new SimpleDateFormat("dd/MM/yyy").parse("09/09/2021");
        Date date2 = new SimpleDateFormat("dd/MM/yyy").parse("23/09/2021");
        Date date3 = new SimpleDateFormat("dd/MM/yyy").parse("28/09/2021");
        Date date4 = new SimpleDateFormat("dd/MM/yyy").parse("19/10/2021");

        return new Property[]{
                new Property(1,
                        29995000,
                        8350,
                        "Live your dream lifestyle in this ultimate modern home designed by Foster + Partners. Residence 15 consists of the full private floor featuring 11 ft ceilings throughout the over 8000 sqft residence. The surrounding floor to ceiling windows maximize the light and sweeping 360-degree views of the most sought-after iconic New York City Landmarks and the Hudson River.",
                        "CHELSEA",
                        "551 W 21ST ST, 15THFLOOR, CHELSEA, NEW YORK, NY 10011",
                        "High Line, Art Galeries",
                        date1,
                        null,
                        4,
                        1,
                         20,
                        40.747559, -74.007069),
                new Property(2,
                        29900000,
                        4296,
                        "Wrap yourself in the ultimate ambience of luxury, style and sophistication in this expansive residence occupying half of the 86th floor at the coveted new Central Park Tower. Boasting impeccably-designed, finely-finished 4,295 sf interiors and 70+ of stunning Central Park frontage, there is truly no other living destination like this, with views beyond compare. The generous 4 bedroom, 4.5 bathroom",
                        "MIDTOWN MANHATTAN",
                        "86E CENTRAL PARK TOWER, 217 WEST 57TH ST, MIDTOWN MANHATTAN, NEW YORK, NY",
                        "Central Park",
                        date1,
                        date2,
                        4,
                        1,
                        18,
                        40.766166, -73.980937),
                new Property(3,
                        23000000,
                        20000,
                        "Located on one of the best town house blocks on the upper east side, this historic single-family home has been newly renovated and is ready to move in. Built in 1869, and reimagined in 1899 by master builder Michael Reed, this home",
                        "LENOX HILL",
                        "116 EAST 70TH ST, LENOX HILL, NEW YORK, NY",
                        "Historic District",
                        date1,
                        null,
                        5,
                        2,
                        16,
                        40.769413, -73.964072),
                new Property(4,
                        135000000,
                        8055,
                        "Among the clouds, at one thousand feet, is the Floating Inner Garden. A one-of-a-kind Hiroshi Sugimoto-designed masterpiece. This serene full-floor residence offers 8,055 square feet (748 square meters) of living space, five bedrooms, five bathrooms, two powder rooms, unobstructed helicopter views in all directions, soaring ceilings, two private elevator landings, custom-built mechanical, electrical, plumbing, and HVAC systems, and a hand selection of the finest natural",
                        "PARK AVENUE",
                        "432 Park Ave, #79, NEW_YORK, NY",
                        "Central Park",
                        date1,
                        date3,
                        1,
                        3,
                        14,
                        40.761749, -73.971821),
                new Property(5,
                        775000,
                        150,
                        "Spacious two bedroom apartment in a beautifully maintained condop located in the most desirable part of Harlem. Besides the generously sized bedrooms, there is a large full bath and an en suite half bath. The kitchen has good counter space and is well designed for cooking. There are new stainless steel appliances - dishwasher, stove and refrigerator. Additionally, there is a washer and dryer in the apartment, tons of closet space including a walk-in, hardwood floors, video/Butterfly intercom system and thru the wall A/Cs in all of the rooms. The apartment gets great light from the west and overlooks gardens and the building's landscaped courtyard. There is a live in super, a great gym and a parking garage with a waiting list. Sublets are allowed for a period of 2 years out of every 4 years once you have occupied the apartment as your primary residence. Come live in this thriving neighborhood which is across Morningside Park from Columbia University and near express transportation to midtown and offers great food, great shopping and great entertainment.",
                        "CENTRAL HARLEM",
                        "2235 Frederick Douglass Blvd Apt 3A, Manhattan, NY 10027",
                        "Columbia University, Nearby schools, Transit 2, 3 A B C D",
                        date4,
                        null,
                        2,
                        6,
                        8,
                        40.807604635958675, -73.95357030181226)
        };
    }
}
