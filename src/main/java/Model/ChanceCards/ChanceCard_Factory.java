package Model.ChanceCards;

public class ChanceCard_Factory {
    public static ChanceCard[] createChanceCards() {
        return new ChanceCard[]{
                // Specific fields
                new MovementCard("Start", "Ryk frem til START. Modtag 2$", 0, MovementCard.MovementType.INDEX),
                new MovementCard("Strandpromenaden", "Ryk frem til STRANDPROMENADEN", 23, MovementCard.MovementType.INDEX), // FIXME: Ikke køb
                new MovementCard("Skaterparken", "Ryk frem til SKATERPARKEN", 10, MovementCard.MovementType.INDEX),

                // Number of fields TODO: Tag et chancekort mere??
                new MovementCard("5 felter frem", "Ryk 5 felter frem", 5, MovementCard.MovementType.NUMBER),
                new MovementCard("1 felt frem", "Ryk 1 felt frem", 1, MovementCard.MovementType.NUMBER),

                // Pay the bank
                new PayTheBankCard("Slik", "Du har spist for meget slik. BETAL 2$ til banken.", 2),

                // Receive money
                new ReceiveMoneyCard("Fødselsdag", "Det er din fødselsdag! Modtag 2$ fra banken. TILLYKKE MED FØDSELSDAGEN!", 2), // FIXME: fra andre spillere
                new ReceiveMoneyCard("Lektier", "Du har lavet alle dine lektier! Modtag 2$ fra banken", 2), // FIXME: fra banken

                // Release from prison
                new ReleaseFromPrisonCard("Løsladelse", "Du løslades uden omkostninger. Behold dette kort, indtil du får brug for det"),
        };
    }
}
