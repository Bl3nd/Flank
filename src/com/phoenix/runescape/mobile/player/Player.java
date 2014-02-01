package com.phoenix.runescape.mobile.player;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import com.phoenix.runescape.Constants;
import com.phoenix.runescape.World;
import com.phoenix.runescape.item.ItemContainer;
import com.phoenix.runescape.item.container.BankingInterfaceContainer;
import com.phoenix.runescape.item.container.EquipmentInterfaceContainer;
import com.phoenix.runescape.item.container.InventoryInterfaceContainer;
import com.phoenix.runescape.mobile.Entity;
import com.phoenix.runescape.mobile.EntityIndex;
import com.phoenix.runescape.mobile.EntityUpdateListener;
import com.phoenix.runescape.mobile.mob.Mob;
import com.phoenix.runescape.mobile.player.file.PlayerReadFileEvent;
import com.phoenix.runescape.mobile.player.file.PlayerSaveFileEvent;
import com.phoenix.runescape.mobile.player.skill.SkillSet;
import com.phoenix.runescape.mobile.player.skill.SkillSet.Skill;
import com.phoenix.runescape.mobile.player.skill.SkillingAction;
import com.phoenix.runescape.network.AsynchronousConnection;
import com.phoenix.runescape.network.packet.OutgoingPacket;
import com.phoenix.runescape.network.packet.dispatch.LogOutPacket;
import com.phoenix.runescape.pulse.PulseScheduler;

/**
 * @author Dylan Vicchiarelli
 *
 * Represents a single player in the virtual world.
 */
public final class Player extends Entity {
	
	/**
	 * The login credentials of the player.
	 */
	private final String accountName, accountPassword;
	
	/**
	 * The skilling action being performed by a player.
	 */
	private SkillingAction skilling;
	
	/**
	 * The asynchronous connection of the player.
	 */
	private final AsynchronousConnection connection;
	
    /**
     * The string of chat broken down into byte format.
     */
    private byte chatText[] = new byte[256];

    /**
     * The effects and color in integer form.
     */
    private int chatTextEffects = 0, chatTextColor = 0;
	
	/**
	 * The players local to our player.
	 */
	private final List<Player> localPlayers = new LinkedList<Player>();
	
	/**
	 * The mobs local to our player.
	 */
	private final List<Mob> localMobs = new LinkedList<Mob>();
	
	/**
	 * The listener for the player updating procedure.
	 */
	private PlayerUpdateListener listener = new PlayerUpdateListenerEvent();
	
	/**
	 * The instance for the private messaging system.
	 */
	private final PrivateMessaging privateMessaging = new PrivateMessaging(this);
	
	/**
	 * The skill set belonging to the player.
	 */
	private final SkillSet skills = new SkillSet(this);

	/**
	 * The container for the inventory of the player.
	 */
	private final ItemContainer inventoryContainer = new InventoryInterfaceContainer(28, this);
	
	/**
	 * The container for the worn equipment of the player.
	 */
	private final ItemContainer equipmentContainer = new EquipmentInterfaceContainer(14, this);
	
	/**
	 * The container for the items in the player's bank account.
	 */
	private final ItemContainer bankingContainer = new BankingInterfaceContainer(352, this);
	
	/**
	 * The appearance values of the player.
	 */
	private int[] appearanceValues = new int[13];

	/**
	 * The color indexes of the player.
	 */
	private int[] colorValues = new int[5];

	/**
	 * The gender of the player.
	 */
	private int gender = 0;
	
	private int firstLogin = 0;
	
	/**
	 * The default class constructor.
	 * 
	 * @param connection The asynchronous connection.
	 * 
	 * @param name The name of the player's account.
	 * 
	 * @param password The password of the player's account.
	 */
	public Player(AsynchronousConnection connection, String name, String password) {
		this.connection = connection;
		this.accountName = name;
		this.accountPassword = password;
		assignDefaultAppearance();
		assignDefaultMovementIndices();
		assignDefulatSkillIndices();
		putAttribute("player-rights", 0);
		
		if (getAccountName().equalsIgnoreCase("cody")) {
			putAttribute("player-rights", 2);
		}
	}
	
	/**
	 * Assigns the default movement indices for the player.
	 */
	public final void assignDefaultMovementIndices() {
		putAttribute("stand-index", 0x328);
		putAttribute("stand-turn-index", 0x337);
		putAttribute("walk-index", 0x333);
		putAttribute("run-index", 0x338);
	}
	
	/**
	 * Assigns the default skill indices for the player.
	 */
	public final void assignDefulatSkillIndices() {
		for(int i = 0; i < 21; i ++) {
			
			getSkills().getSkills()[i] = new Skill(1, 0);
		}
		
		getSkills().getSkills()[Constants.HITPOINTS] = new Skill(10, 1184);
	}
	
	/**
	 * Assigns the default appearance indices for the player.
	 */
	public final void assignDefaultAppearance() {
		getAppearanceValues()[0] = 18;
		getAppearanceValues()[1] = 26;
		getAppearanceValues()[2] = 36;
		getAppearanceValues()[3] = 0;
		getAppearanceValues()[4] = 33;
		getAppearanceValues()[5] = 42;
		getAppearanceValues()[6] = 10;
		getColorValues()[0] = 7;
		getColorValues()[1] = 8;
		getColorValues()[2] = 9;
		getColorValues()[3] = 5;
		getColorValues()[4] = 0;
	}
	
	/**
	 * Returns the asynchronous connection of the player.
	 * 
	 * @return The returned asynchronous connection.
	 */
	public final AsynchronousConnection getConnection() {
		
		return connection;
	}
	
	/**
	 * Returns a list of players local to ours.
	 * 
	 * @return The returned player list.
	 */
	public final List<Player> getLocalPlayers() {
		
		return localPlayers;
	}

	/**
	 * Returns the account password of the player.
	 * 
	 * @return The returned name.
	 */
	public final String getAccountPassword() {
		
		return accountPassword;
	}

	/**
	 * Returns the account name of the player.
	 * 
	 * @return The returned account name.
	 */
	public final String getAccountName() {
		
		return accountName;
	}
	
	/**
	 * Dispatches an outgoing packet.
	 * 
	 * @param packet The packet to be dispatched.
	 */
	public final void send(OutgoingPacket packet) {
		
		getConnection().write(packet.dispatch(this).getBuffer());
	}
	
	/**
	 * Executes the saving event for a player's account file.
	 */
	public final boolean save() {
		
		return activateFileEvent(new PlayerSaveFileEvent(this));
	}
	
	/**
	 * Executes the loading event for a player's account file.
	 * 
	 * @return The result of the event operation.
	 */
	public final boolean load() {
		
		return activateFileEvent(new PlayerReadFileEvent(this));
	}
	
	/**
	 * Activates an event in correlation to the player's account file.
	 * 
	 * @param event The event being activated.
	 * 
	 * @return The result of the event operation.
	 */
	public final boolean activateFileEvent(PlayerFileEvent event) {
		
		return event.parseFile();
	}
	
	/**
	 * Resets the update flags after the updating is complete.
	 */
	public final void reset() {
		
		getUpdateFlags().clear();
	}
	
	/**
	 * Prepares a prior update method to the main update block.
	 */
	public final void prepare() {
		
		getMovement().handleEntityMovement();
	}
	
	/**
	 * Returns the color indexes of the player.
	 * 
	 * @return The returned index.
	 */
	public final int[] getColorValues() {
		
		return colorValues;
	}

	/**
	 * Returns the appearance values of the player.
	 * 
	 * @return The returned valued.
	 */
	public final int[] getAppearanceValues() {
		
		return appearanceValues;
	}
	
	/**
	 * Returns the interface container for the player's bank account.
	 * 
	 * @return The returned interface item container.
	 */
	public final ItemContainer getBankingContainer() {
		
		return bankingContainer;
	}
	
	/**
	 * Returns the updating listener for this entity.
	 * 
	 * @return The returned listener.
	 */
	public final PlayerUpdateListener getListener() {
		
		return listener;
	}

	/**
	 * Returns the chat effects of the spoken message.
	 * 
	 * @return The returned effects.
	 */
	public final int getChatTextEffects() {
		
		return chatTextEffects;
	}

	/**
	 * Modifies the chat effects of the spoken message.
	 * 
	 * @param chatTextEffects The new modification.
	 */
	public final void setChatTextEffects(int chatTextEffects) {
		
		this.chatTextEffects = chatTextEffects;
	}

	/**
	 * Returns the char color of the spoken message.
	 * 
	 * @return The returned chat color.
	 */
	public final int getChatTextColor() {
		
		return chatTextColor;
	}

	/**
	 * Modifies the chat color of the spoken message.
	 * 
	 * @param chatTextColor The new modification.
	 */
	public final void setChatTextColor(int chatTextColor) {
		
		this.chatTextColor = chatTextColor;
	}

	/**
	 * Returns the byte format of the chat text.
	 * 
	 * @return The returned byte format.
	 */
	public final byte[] getChatText() {
		
		return chatText;
	}

	/**
	 * Modifies the byte format of the chat text.
	 * 
	 * @param chatText The new modification.
	 */
	public final void setChatText(byte chatText[]) {
		
		this.chatText = chatText;
	}
	
	/**
	 * Returns the instance for the private messaging system.
	 * 
	 * @return The returned instance.
	 */
	public final PrivateMessaging getPrivateMessaging() {
		
		return privateMessaging;
	}
	
	/**
	 * Returns the inventory container of the player.
	 * 
	 * @return The returned container.
	 */
	public final ItemContainer getInventoryContainer() {
		
		return inventoryContainer;
	}
	
	/**
	 * Returns the equipment container of the player.
	 * 
	 * @return The returned container.
	 */
	public final ItemContainer getEquipmentContainer() {
		
		return equipmentContainer;
	}
	
	/**
	 * Returns the skill set.
	 * 
	 * @return The returned skill set.
	 */
	public final SkillSet getSkills() {
		
		return skills;
	}
	
	/**
	 * Returns the collection of local mobs.
	 * 
	 * @return The returned collection.
	 */
	public final List<Mob> getLocalMobs() {
		
		return localMobs;
	}
	
	/**
	 * Returns the players gender in integer format.
	 * 
	 * @return The returned gender.
	 */
	public int getGender() {
		
		return gender;
	}

	/**
	 * Modifier the gender of the player.
	 * 
	 * @param gender The new modification;
	 */
	public void setGender(int gender) {
		
		this.gender = gender;
	}
	
	/**
	 * Returns the current skilling action of the player.
	 * 
	 * @return The returned skilling action.
	 */
	public SkillingAction getSkilling() {
		
		return skilling;
	}

	/**
	 * Modifies the current skilling action of the player.
	 * 
	 * @param skilling The new modification.
	 */
	public void setSkilling(SkillingAction skilling) {
		
		this.skilling = skilling;
	}

	@Override
	public EntityUpdateListener<Player> getUpdateListener() {
		
		return getListener();
	}
	
	@Override
	public void remove() {
		try {
			
			putAttribute("cancel-walk-to-actions");
			getMovement().resetMovement();
			getPrivateMessaging().remove();
			World.getInstance().getPlayers().remove(getIndex()).save();
			EntityIndex.discardPlayerIndex(getIndex());
			PulseScheduler.getInstance().destoryPulsesForOwner(this);
			send(new LogOutPacket());
			getConnection().getSocketChannel().close();
		} catch (IOException exception) {
			
			exception.printStackTrace();
		}
	}
	
	public int getFirstLogin() {
		return firstLogin;
	}
	
	public void setFirstLogin(int firstLog) {
		this.firstLogin = firstLog;
	}
}