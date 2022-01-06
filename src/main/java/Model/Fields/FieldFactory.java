package Model.Fields;

import java.awt.*;
import java.security.KeyStore;

public final class FieldFactory {

    /**
     * Laver et array af vores felter. Hvert felt er et objekt, som er instatieret vha. den tilhørende klasse
     * Fx StartField klassens constructer bruges til at lave feltet Start
     */
    public static Field[] createFields() { // How to makes an array: type[] variabelName
        return new Field[]{
                // Index 0-4
                new StartField("START", "Modtag 4000", "Modtag kr. 4000 hver gang du passerer start",
                        Color.RED),
                new PropertyField("Rødovrevej", "Pris: kr. 1200", "Rødovrevej", 50, 1200,
                        new Color(0, 0, 102), new Color(255, 255, 255)),
                new ChanceField(),
                new PropertyField("Hvidovrevej", "Pris: kr. 1200", "Hvidovrevej", 50, 1200,
                        new Color(0, 0, 102), new Color(255, 255, 255)),
                new TaxField("Betal\nindkomst-\nskat", "10% el. 4000", "Betal indkomstskat\n10% eller kr. 4000,-"),
                new ShippingField("Øresund", "Pris: kr. 4000", "Øresundsredderiet", 500),

                // Index 5-9
                new PropertyField("Roskildevej", "Pris: kr. 2000", "Roskildevej", 100, 2000,
                        new Color(255, 128, 0), new Color(0, 0, 0)),
                new ChanceField(),
                new PropertyField("Valby\nLanggade", "Pris: kr. 2000", "Valby Langgade", 100, 2000,
                        new Color(255, 128, 0), new Color(0, 0, 0)),
                new PropertyField("Allégade", "Pris: kr. 2400", "Allégade", 150, 2400,
                        new Color(255, 128, 0), new Color(0, 0, 0)),

                // Index 10-14
                new JailField("Fængsel", "Fængsel", "På besøg i fængslet"),
                new PropertyField("Frederiks-\nberg Allé", "Pris: kr. 2800", "Frederiksberg Allé", 200, 2800,
                        new Color(102, 204, 0), new Color(0, 0, 0)),
                new BreweryField("Tuborg", "Pris: kr. 3000", "Tuborg bryggeri"),
                new PropertyField("Bülowsvej", "Pris: kr. 2800", "Bülowsvej", 200, 2800,
                        new Color(102, 204, 0), new Color(0, 0, 0)),
                new PropertyField("Gammel Kongevej", "Pris: kr. 3200", "Gammel Kongevej", 250, 3200,
                        new Color(102, 204, 0), new Color(0, 0, 0)),

                // Index 15-19
                new ShippingField("D.F.D.S.", "Pris: kr. 4000", "D.F.D.S.", 500),
                new PropertyField("Bernstorffsvej", "Pris: kr. 3600", "Bernstorffsvej", 300, 3600,
                        new Color(96, 96, 96), new Color(0, 0, 0)),
                new ChanceField(),
                new PropertyField("Hellerupvej", "Pris: kr. 3600", "Hellerupvej", 300, 3600,
                        new Color(96, 96, 96), new Color(0, 0, 0)),
                new PropertyField("Strandvejen", "Pris: kr. 4000", "Strandvejen", 350, 4000,
                        new Color(96, 96, 96), new Color(0, 0, 0)),

                // Index 20-24
                new FreeParkingField("Parkering", "Parkering", "Parkering er et fristed, indtil man skal kaste igen"),
                new PropertyField("Trianglen", "Pris: kr. 4400", "Trianglen", 350, 4400,
                        new Color(153, 0, 0), new Color(255, 255, 255)),
                new ChanceField(),
                new PropertyField("Østerbro-\ngade", "Pris: kr. 4400", "Østerbrogade", 350, 4400,
                        new Color(153, 0, 0), new Color(255, 255, 255)),
                new PropertyField("Grønningen", "Pris: kr. 4800", "Grønningen", 400, 4800,
                        new Color(153, 0, 0), new Color(255, 255, 255)),

                // Index 25-29
                new ShippingField("Ø.S.", "Pris: kr. 4000", "Ø.S. redderiet", 500),
                new PropertyField("Bredgade", "Pris: kr. 5200", "Bredgade", 450, 5200,
                        new Color(255, 255, 255), new Color(0, 0, 0)),
                new PropertyField("Kgs. Nytorv", "Pris: kr. 5200", "Kongens Nytorv", 450, 5200,
                        new Color(255, 255, 255), new Color(0, 0, 0)),
                new BreweryField("Carlsberg", "Pris: kr. 3000", "Carlsberg bryggeri"),
                new PropertyField("Østergade", "Pris: kr. 5600", "Østergade", 500, 5600,
                        new Color(255, 255, 255), new Color(0, 0, 0)),

                // Index 30-34
                new GoToJailField("Gå i fængsel", "Gå i fængsel",
                        "Du fængsles\nSlå to ens for at komme ud"),
                new PropertyField("Amagertorv", "Pris: kr. 6000", "Amagertorv", 550, 6000,
                        new Color(255, 255, 50), new Color(0, 0, 0)),
                new PropertyField("Vimmel-\nskaftet", "Pris: kr. 6000", "Vimmelskaftet", 550, 6000,
                        new Color(255, 255, 50), new Color(0, 0, 0)),
                new ChanceField(),
                new PropertyField("Nygade", "Pris: kr. 6400", "Nygade", 600, 6400,
                        new Color(255, 255, 50), new Color(0, 0, 0)),

                // Index 35-39
                new ShippingField("Bornholm", "Pris: kr. 4000", "Bornholms redderi", 500),
                new ChanceField(),
                new PropertyField("Frederiks-\nberggade", "Pris: kr. 7000", "Frederiksberggade", 700, 7000,
                        new Color(102, 0, 102), new Color(255, 255, 255)),
                new TaxField("Ekstra-\nordinær\nstatsskat", "Betal 2000 kr.", "Betal ekstraordinær\nstatsskat: kr. 2000,-"),
                new PropertyField("Rådhuspladsen", "Pris: kr. 8000", "Rådhuspladsen", 1000, 8000,
                        new Color(102, 0, 102), new Color(255, 255, 255)),
        };
    }
}