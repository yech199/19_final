package Model.Fields;

import java.awt.*;

public final class FieldFactory {

    /**
     * Laver et array af vores felter. Hvert felt er et objekt, som er instatieret vha. den tilhørende klasse
     * Fx StartField klassens constructer bruges til at lave feltet Start
     */
    public static Field[] createFields() { // How to makes an array: type[] variabelName
        return new Field[]{
                // Index 0-4
                new StartField("START", "2$", "Modtag 2$ når du passerer start",
                        new Color(224, 224, 224)),
                new AmusementField("Burgerbaren", "1$", "Burgerbaren", 1,
                        new Color(169, 100, 31)),
                new AmusementField("Pizzariaet", "1$", "Pizzariaet", 1,
                        new Color(169, 100, 31)),
                new ChanceField("?", "Chance", "Tag et chancekort.",
                        new Color(224, 224, 224)),
                new AmusementField("Slikbutikken", "1$", "Slikbutikken", 1,
                        new Color(149, 223, 243)),
                // Index 5-9
                new AmusementField("Iskiosken", "1$", "Iskiosken", 1,
                        new Color(149, 223, 243)),
                new JailField("Fængsel", "Fængsel", "På besøg i fængslet",
                        new Color(125, 125, 125)),
                new AmusementField("Museet", "2$", "Museet", 2,
                        new Color(255, 105, 180)),
                new AmusementField("Biblioteket", "2$", "Biblioteket", 2,
                        new Color(255, 105, 180)),
                new ChanceField("?", "Chance", "Tag et chancekort.",
                        new Color(224, 224, 224)),
                // Index 10-14
                new AmusementField("Skaterparken", "2$", "Skaterparken", 2,
                        new Color(255, 110, 0)),
                new AmusementField("Swimming-\npoolen", "2$", "Swimmingpoolen", 2,
                        new Color(255, 110, 0)),
                new FreeParkingField("Parkering", "Gratis parkering", "Gratis parkering",
                        new Color(224, 224, 224)),
                new AmusementField("Spillehallen", "3$", "Spillehallen", 3,
                        new Color(235, 22, 43)),
                new AmusementField("Biografen", "3$", "Biografen", 3,
                        new Color(235, 22, 43)),
                // Index 15-19
                new ChanceField("?", "Chance", "Tag et chancekort.",
                        new Color(224, 224, 224)),
                new AmusementField("Legetøjs-\nbutikken", "3$", "Legetøjsbutikken", 3,
                        new Color(255, 255, 0)),
                new AmusementField("Dyrehandlen", "3$", "Dyrehandlen", 3,
                        new Color(255, 255, 0)),
                new GoToJailField("Gå i fængsel", "Gå i fængsel",
                        "Du fængsles\nSlå to ens for at komme ud", new Color(125, 125, 125)),
                new AmusementField("Bowlinghallen", "4$", "Bowlinghallen", 4,
                        new Color(64, 185, 68)),
                // Index 20-23
                new AmusementField("Zoo", "4$", "Zoo", 4,
                        new Color(64, 185, 68)),
                new ChanceField("?", "Chance", "Tag et chancekort.",
                        new Color(224, 224, 224)),
                new AmusementField("Vandlandet", "5$", "Vandlandet", 5,
                        new Color(75, 155, 225)),
                new AmusementField("Strand-\npromenaden", "5$", "Strandpromenaden", 5,
                        new Color(75, 155, 225)),
        };
    }
}