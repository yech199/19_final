package Model.ChanceCards;

import Model.Fields.Field;
import Model.Fields.PropertyField;
import Model.GameBoard;
import Model.GlobalValues;
import Model.Player;

public class BuildingTaxCard extends ChanceCard {
    private final int HOUSE_PRICE;
    private final int HOTEL_PRICE;
    public BuildingTaxCard(String name, String cardText, int HOUSE_PRICE, int HOTEL_PRICE) {
        super(name, cardText);
        this.HOUSE_PRICE = HOUSE_PRICE;
        this.HOTEL_PRICE = HOTEL_PRICE;
    }

    @Override
    public void cardAction(Player player, GameBoard gameBoard) {
        int houseCounter = 0;
        int hotelCounter = 0;
        for (int i = 0; i < gameBoard.fields.length; i++) {
            Field field = gameBoard.fields[i];
            if (field instanceof PropertyField p && player == p.owner && p.amountOfBuildings > 0) {
                if (p.amountOfBuildings == GlobalValues.MAX_AMOUNT_OF_BUILDINGS) {
                    hotelCounter++;
                }
                else {
                    for (int j = 0; j < p.amountOfBuildings; j++) {
                        houseCounter++;
                    }
                }
            }
        }

        player.addAmountToBalance(-(hotelCounter * this.HOTEL_PRICE));
        player.addAmountToBalance(-(houseCounter * this.HOUSE_PRICE));
    }
}
