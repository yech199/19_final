package Model.ChanceCards;

public class ChanceCard_Factory {
    public static ChanceCard[] createChanceCards() {
        return new ChanceCard[]{
                // Specific fields
                new MovementCard("Start", "Ryk frem til START", 0, MovementCard.MovementType.INDEX),
                new MovementCard("Start", "Ryk frem til START", 0, MovementCard.MovementType.INDEX),
                new MovementCard("Rådhuspladsen", "Tag til Rådhuspladsen", 39, MovementCard.MovementType.INDEX),

                new MovementCard("Frederiksberg Allé", "Ryk frem til Frederiksberg Allé. Hvis De passere START, indkasser da 4000 kr.", 11, MovementCard.MovementType.INDEX),
                new MovementCard("Mols-Linien", "Tag med Mols-Linien, flyt brikken frem og hvis De passerer START indkassér da kr 4000.", 15, MovementCard.MovementType.INDEX),
                new MovementCard("Grønningen", "Ryk frem til Grønningen, hvis De passerer start indkasser da kr 4000", 24, MovementCard.MovementType.INDEX),
                new MovementCard("Vimmelskaftet", "Ryk frem til Vimmelskaftet, hvis de passerer start indkasser da kr 4000", 32, MovementCard.MovementType.INDEX),
                new MovementCard("Strandvejen", "Ryk frem til Strandvejen. Hvis De passere START, indkasser da 4000 kr.", 19, MovementCard.MovementType.INDEX),

                // fieldIndex er sat til -1, for alle der bruger NEAREST
                new MovementCard("Nærmeste rederi", "Ryk frem til det nærmeste rederi og betal ejeren to gange den leje han ellers er berettiget\n" +
                        "til, hvis selskabet ikke ejes af nogen kan De købe det af banken.", -1, MovementCard.MovementType.NEAREST),
                new MovementCard("Nærmeste rederi", "Ryk frem til det nærmeste rederi og betal ejeren to gange den leje han ellers er berettiget\n" +
                        "til, hvis selskabet ikke ejes af nogen kan De købe det af banken.", -1, MovementCard.MovementType.NEAREST),
                new MovementCard("Færge", "Tag med den nærmeste færge, hvis de passerer start indkasser da kr 4000", -1, MovementCard.MovementType.NEAREST),

                // Number of fields
                new MovementCard("3 felter frem", "Ryk tre felter frem", 3, MovementCard.MovementType.NUMBER),
                new MovementCard("3 felter tilbage" ,"Ryk tre felter tilbage", -3, MovementCard.MovementType.NUMBER),
                new MovementCard("3 felter tilbage" ,"Ryk tre felter tilbage", -3, MovementCard.MovementType.NUMBER),

                new GoToJailCard("Fængsel", "Gå i fængsel, De indkasserer ikke 4000 kr for at passere start."),
                new GoToJailCard("Fængsel", "Gå i fængsel, De indkasserer ikke 4000 kr for at passere start."),


                // Pay the bank
                new PayTheBankCard("Fuldt stop", "De har kørt frem for “fuldt stop”, Betal 1000 kroner i bøde.", 1000),
                new PayTheBankCard("vognvask og smøring", "Betal for vognvask og smøring kr 300.", 300),
                new PayTheBankCard("Øl-levering", "Betal kr 200 for levering af 2 kasser øl.", 200),
                new PayTheBankCard("Reparation", "Betal 3000 for reparation af deres vogn.", 3000),
                new PayTheBankCard("Reparation", "Betal 3000 for reparation af deres vogn.", 3000),
                new PayTheBankCard("Nye dæk", "De har købt 4 nye dæk til Deres vogn, betal kr 1000.", 1000),
                new PayTheBankCard("Parkeringsbøde", "De har fået en parkeringsbøde, betal kr 200 i bøde.", 200),
                new PayTheBankCard("Bilforsikring", "Betal deres bilforsikring, kr 1000.", 1000),
                new PayTheBankCard("Told", "De har været udenlands og købt for mange smøger, betal kr 200 i told.", 200),
                new PayTheBankCard("Tandlæge", "Tandlægeregning, betal kr 2000.", 2000),

                // Receive money from the bank
                new ReceiveMoneyFromBankCard("Lotteri", "De har vundet i klasselotteriet. Modtag 500 kr.", 500),
                new ReceiveMoneyFromBankCard("Lotteri", "De har vundet i klasselotteriet. Modtag 500 kr.", 500),
                new ReceiveMoneyFromBankCard("Aktieudbytte", "De modtager Deres aktieudbytte. Modtag kr 1000 af banken.", 1000),
                new ReceiveMoneyFromBankCard("Aktieudbytte", "De modtager Deres aktieudbytte. Modtag kr 1000 af banken.", 1000),
                new ReceiveMoneyFromBankCard("Aktieudbytte", "De modtager Deres aktieudbytte. Modtag kr 1000 af banken.", 1000),
                new ReceiveMoneyFromBankCard("Skat", "Kommunen har eftergivet et kvartals skat. Hæv i banken 3000 kr.", 3000),
                new ReceiveMoneyFromBankCard("Tipning", "De har en række med elleve rigtige i tipning, modtag kl 1000.", 1000),
                new ReceiveMoneyFromBankCard("Gageforhøjelse", "Grundet dyrtiden har De fået gageforhøjelse, modtag kr 1000.", 1000),
                new ReceiveMoneyFromBankCard("Præmieobligation", "Deres præmieobligation er udtrykket. De modtager 1000 kr af banken.", 1000),
                new ReceiveMoneyFromBankCard("Præmieobligation", "Deres præmieobligation er udtrykket. De modtager 1000 kr af banken.", 1000),
                new ReceiveMoneyFromBankCard("Auktion", "De har solg nogle gamle møbler på auktion. Modtag 1000 kr af banken.", 1000),
                new ReceiveMoneyFromBankCard("Avl", "Værdien af egen avl fra nyttehaven udgør 200 som de modtager af banken", 200),

                // FIXME: Receive money from other players
                new ReceiveMoneyFromOtherPlayersCard("Fødselsdag", "Det er deres fødselsdag. Modtag af hver medspiller 200 kr.",
                        200),
                new ReceiveMoneyFromOtherPlayersCard("Sammenskudsgilde", "De har lagt penge ud til et sammenskudsgilde. Mærkværdigvis betaler alle straks.\n" +
                        "Modtag fra hver medspiller 500 kr.", 500),
                new ReceiveMoneyFromOtherPlayersCard("Familiefest", "De skal holde familiefest og får et tilskud fra hver medspiller på 500 kr.",
                        500),

                // Release from prison
                new ReleaseFromPrisonCard("Kongens fødselsdag", "I anledning af kongens fødselsdag benådes De herved for fængsel. Dette kort kan\n" +
                        "opbevares indtil De får brug for det, eller De kan sælge det."),
                new ReleaseFromPrisonCard("Kongens fødselsdag", "I anledning af kongens fødselsdag benådes De herved for fængsel. Dette kort kan\n" +
                        "opbevares indtil De får brug for det, eller De kan sælge det."),

                new EndowmentCard("Matedor-legat", "De modtager “Matador-legatet” på kr 40.000, " +
                        "men kun hvis værdier ikke overstiger 15.000 kr", 40000, 1500),

                new BuildingTaxCard("Oliepriser", "Oliepriserne er steget, " +
                        "og De skal betale kr 500 pr hus og kr 2000 pr hotel.", 500, 2000),
                new BuildingTaxCard("Ejendomsskat", "Ejendomsskatten er steget. " +
                        "Ekstraudgifterne er: 800 kr pr hus, 2300 kr pr hotel.", 800, 2300)
        };
    }
}
