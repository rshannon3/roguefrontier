package game;

import java.awt.event.MouseEvent;

import mapUnit.Player;

public class InspectionInventoryGUI extends InventoryGUI {

	public InspectionInventoryGUI(Player player) {
		super(player);
	}
	
	@Override
	protected void setInventory() {
		this.inventory = p.getInspectionInventory();
	}
	
	@Override
	public void testPress(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON3) {
			int index = e.getX() / tileSize + (e.getY() / tileSize) * numRows;
			if (inventory.getItem(index) != null) {
				inventory.transferItemTo(index, p.getInventory());
			}
		}else{
			super.testPress(e);
		}
	}

}
