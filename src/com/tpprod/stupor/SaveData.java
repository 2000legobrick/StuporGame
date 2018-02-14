package com.tpprod.stupor;

public class SaveData implements java.io.Serializable {
	
		private static final long serialVersionUID = 1L;
		private int playerCurrentX = 0;
		private int playerCurrentY = 0;
		private int playerEXP = 0;
		private int playerHealth = 0;
		private int playerMana = 0;
		
		public int getPlayerCurrentX() {
			return playerCurrentX;
		}
		public void setPlayerCurrentX(int playerCurrentX) {
			this.playerCurrentX = playerCurrentX;
		}
		public int getPlayerCurrentY() {
			return playerCurrentY;
		}
		public void setPlayerCurrentY(int playerCurrentY) {
			this.playerCurrentY = playerCurrentY;
		}
		public int getPlayerEXP() {
			return playerEXP;
		}
		public void setPlayerEXP(int playerEXP) {
			this.playerEXP = playerEXP;
		}
		public int getPlayerHealth() {
			return playerHealth;
		}
		public void setPlayerHealth(int playerHealth) {
			this.playerHealth = playerHealth;
		}
		public int getPlayerMana() {
			return playerMana;
		}
		public void setPlayerMana(int playerMana) {
			this.playerMana = playerMana;
		}
		
}
