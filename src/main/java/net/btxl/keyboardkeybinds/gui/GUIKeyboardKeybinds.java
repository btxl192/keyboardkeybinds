package net.btxl.keyboardkeybinds.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.input.Mouse;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.settings.KeyBindingMap;
import net.minecraftforge.client.settings.KeyModifier;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GUIKeyboardKeybinds extends GuiScreen
{
	private final GuiScreen parentScreen;
	private final Integer backButtonID = 193;
	private final String topRow = "`1234567890-=";
	private final String firstRow = "QWERTYUIOP[]\\";
	private final String secondRow = "ASDFGHJKL;'";
	private final String thirdRow = "ZXCVBNM,./";
	private static final Map<String, String> keyCodeDict;
	static
	{
		keyCodeDict = new HashMap<String, String>();
		keyCodeDict.put("-", "MINUS");
		keyCodeDict.put("=", "EQUALS");
		keyCodeDict.put("[", "LBRACKET");
		keyCodeDict.put("]", "RBRACKET");
		keyCodeDict.put(";", "SEMICOLON");
		keyCodeDict.put("'", "APOSTROPHE");
		keyCodeDict.put("`", "GRAVE");
		keyCodeDict.put("\\", "BACKSLASH");
		keyCodeDict.put(",", "COMMA");
		keyCodeDict.put(".", "PERIOD");
		keyCodeDict.put("/", "SLASH");
		keyCodeDict.put("CAPS", "CAPITAL");
	};
	private static Map<Integer, Integer> rowCount = new HashMap<Integer, Integer>();
	private static Map<Integer, Integer> rowLength = new HashMap<Integer, Integer>();
	private final KeyBindingMap keyBindMap = new KeyBindingMap();
	private final List<KeyBinding> keyBinds = new ArrayList<KeyBinding>();
	private final List<GuiButton> currentKeyBinds = new ArrayList<GuiButton>();	
	private final List<GuiButton> otherKeyBinds = new ArrayList<GuiButton>();
	private Map<GuiButton, KeyBinding> buttonKeyBindMap = new HashMap<GuiButton, KeyBinding>();
	private Integer currentKBLength = 0;
	private Integer otherKBLength = 0;
	private static Integer lastKeyCode = -1;
	private static GuiButton lastButton = null;
	private GuiTextField searchBar;
	private final Integer buttonSpacing = getButtonHeight() + 35;
	private final float heightOffsetMult = 0.025f;
	public static KeyModifier currentKeyModifier = KeyModifier.NONE;
	private Map<GuiButton, KeyModifier> modifierButtons = new HashMap<GuiButton, KeyModifier>();
	
	public GuiButton getLastButtonInList()
	{
		return buttonList.get(buttonList.size()-1);
	}
	
	private float getHeightMult()
	{
		return (float)this.height / 550f; 
	}
	
	private float getWidthMult()
	{
		return (float)this.width / 500f;
	}
	
	private Integer getButtonHeight()
	{
		Integer i = (int) (30 * getHeightMult());
		if (i > 20)
		{
			return 20;
		}
		else
		{
			return i;
		}
	}	
	
	public static Integer getLastKeyCode()
	{
		return lastKeyCode;
	}
	
	public static GuiButton getLastButton()
	{
		return lastButton;
	}
	
	public GUIKeyboardKeybinds(GuiScreen screen)
    {
        this.parentScreen = screen;
        for (Integer i = 0; i <= 255; i++)
        {
        	keyBinds.addAll(keyBindMap.lookupAll(i));
        }
    }
	
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRenderer, "Keyboard Keybinds", this.width / 2, 15, 16777215);
        if (lastButton != null)
        {
            this.drawString(this.fontRenderer, "Bound keys: ", 20, getRowY(7), 16777215);
            this.drawString(this.fontRenderer, "Unbound keys: ", 20, getRowY(9), 16777215);
        }
        this.drawString(this.fontRenderer, "Key modifiers: ", 20, getRowY(11), 16777215);
        super.drawScreen(mouseX, mouseY, partialTicks);
        Integer mousewheel = Integer.signum(Mouse.getDWheel()) * 20;
        if (lastButton != null)
        {
        	 searchBar.drawTextBox();
        }       
        for (GuiButton b : buttonList)
        {
        	if (otherKeyBinds.contains(b))
        	{
        		b.x += mousewheel;
        	}
        	if (b.id > 193 && b.isMouseOver())
        	{
        		
        		if (currentKeyBinds.contains(b))
        		{
        			List<String> text = new ArrayList<String>();
    				text.add("---" + I18n.format(buttonKeyBindMap.get(b).getKeyDescription()) + "---");
        			text.add("---Click to unbind---");
        			drawHoveringText(text, mouseX, mouseY);
        		}
        		else if (otherKeyBinds.contains(b))
        		{
        			List<String> text = new ArrayList<String>();
    				text.add("---" + I18n.format(buttonKeyBindMap.get(b).getKeyDescription()) + "---");
    				text.add("Bound to " + buttonKeyBindMap.get(b).getDisplayName());
        			text.add("---Click to bind---");
        			drawHoveringText(text, mouseX, mouseY);
        		}
        		else if (b.id < 392)
        		{
            		String buttonKey = ((GUIControlButton)b).getKey();
            		List<String> text = new ArrayList<String>();
            		text.add(buttonKey + " bound to:");
            		String key = getKeyCode(buttonKey);
            		Integer keyIndex = org.lwjgl.input.Keyboard.getKeyIndex(key);
        			if (keyBindMap.lookupAll(keyIndex).size() > 0)
        			{
        				for (KeyBinding k : keyBindMap.lookupAll(keyIndex))
        				{       				
        					String desc = "";
        					if (k.getKeyModifier() != KeyModifier.NONE)
        					{
        						desc = "(" + k.getKeyModifier() + ") " + I18n.format(k.getKeyDescription());
        					}
        					else
        					{
        						desc = I18n.format(k.getKeyDescription());
        					}
        					text.add(desc);
        				}
        			}
        			else
        			{
        				text.add("None");
        			}
        			text.add("---Click to edit---");
            		drawHoveringText(text, mouseX, mouseY);
        		}

        	}
        }
    }
    
    public Integer getRowY(Integer rowNum)
    {
    	return (int) ((int) ((40 + buttonSpacing * rowNum) * getHeightMult()) + this.height * heightOffsetMult);
    }
    
    public void initGui()
    {
		searchBar = new GuiTextField(292, fontRenderer, 20, getRowY(6), 200, getButtonHeight());
    	drawKeyboard();
    }
    
    public void drawModifierButtons()
    {
    	Integer spacing = 20;
    	modifierButtons.clear();
    	modifierButtons.put(new GuiButton(392, spacing, getRowY(12), getButtonWidth("NONE"), getButtonHeight(), "NONE"), KeyModifier.NONE);
    	spacing += getButtonWidth("NONE") + 5;
    	modifierButtons.put(new GuiButton(393, spacing, getRowY(12), getButtonWidth("SHIFT"), getButtonHeight(), "SHIFT"), KeyModifier.SHIFT);
    	spacing += getButtonWidth("SHIFT") + 5;
    	modifierButtons.put(new GuiButton(394, spacing, getRowY(12), getButtonWidth("ALT"), getButtonHeight(), "ALT"), KeyModifier.ALT);
    	spacing += getButtonWidth("ALT") + 5;
    	modifierButtons.put(new GuiButton(395, spacing, getRowY(12), getButtonWidth("CTRL"), getButtonHeight(), "CTRL"), KeyModifier.CONTROL);
    	spacing += getButtonWidth("CTRL") + 5;
    	for (GuiButton b : modifierButtons.keySet()) 
    	{
    		this.buttonList.add(b);
    		if (modifierButtons.get(b) == currentKeyModifier)
    		{
    			b.enabled = false;
    		}
    		else
    		{
    			b.enabled = true;
    		}
    	}
    }
    
    public void drawKeyboard()
    {
    	buttonList.clear();
    	rowLength.clear();
    	this.buttonList.add(new GuiButton(backButtonID, 0, 0, 100, 20, "Back to options"));
    	drawRow(topRow, 0, -15);
    	drawRow(firstRow, 1, 0);
    	drawRow(secondRow, 2, 15);
    	drawRow(thirdRow, 3, 30);  	
    	placeControlButton(80, 4, "LCTRL", "LCONTROL", 25);
    	disableBadButton();
    	placeControlButton(80, 4, "SPACEBAR", "SPACE", 125);
    	disableBadButton();
    	placeControlButton(80, 4, "RCTRL", "RCONTROL", 225);
    	disableBadButton();
    	if (lastButton != null)
    	{   		
			showKeyBinds(lastButton);	
    	}
    	drawModifierButtons();
    }
    
    public void disableBadButton()
    {
		if (currentKeyModifier != KeyModifier.NONE)
		{
			getLastButtonInList().enabled = false;
		}
    }
    
    public void drawRow(String row, Integer rowNum, Integer offset)
    {   	
    	offset += 50;
		rowCount.put(rowNum, 1);
    	for (char key : row.toCharArray())
    	{
    		addControlButton(offset, rowNum, String.valueOf(key), String.valueOf(key));
        	//LONGER KEYS   		
    		if (key == row.charAt(row.length()-1))
    		{
            	if (row.equals(topRow))
            	{
            		addControlButton(offset, rowNum, "BACK", "BACK");
            		disableBadButton();          		
            	}
            	else if (row.equals(firstRow))
            	{
            		placeControlButton(offset, rowNum, "TAB", "TAB", offset - 45);
            		disableBadButton();
            	}
            	else if (row.equals(secondRow))
            	{
            		addControlButton(offset, rowNum, "RETURN", "RETURN");
            		disableBadButton();
            		placeControlButton(offset, rowNum, "CAPS", "CAPS", offset - 50);   
            		disableBadButton();
            	}
            	else if (row.equals(thirdRow))
            	{
            		addControlButton(offset, rowNum, "RSHIFT", "RSHIFT");
            		disableBadButton();
            		placeControlButton(offset, rowNum, "LSHIFT", "LSHIFT", offset - 60);
            		disableBadButton();
            	}
    		}
    	}

    }
    
    public void showKeyBinds(GuiButton button)
    {    	
		button.enabled = false;
		String keyCode = getKeyCode(((GUIControlButton)button).getKey());
		lastKeyCode = org.lwjgl.input.Keyboard.getKeyIndex(keyCode);
		if (lastButton != null)
		{
			lastButton.enabled = true;
		}   
		lastButton = button;	
		currentKeyBinds.addAll(otherKeyBinds);
		for (GuiButton b : currentKeyBinds)
		{
			buttonList.remove(b);
		}
		currentKeyBinds.clear();
		otherKeyBinds.clear();
		List<KeyBinding> currentKeys = keyBindMap.lookupAll(org.lwjgl.input.Keyboard.getKeyIndex(keyCode));
		rowCount.put(8, 0);
		buttonKeyBindMap.clear();
		for (KeyBinding k : currentKeys)
		{
			addCurrentKBButton(20, 8, I18n.format(k.getKeyDescription()));
			buttonKeyBindMap.put(buttonList.get(this.buttonList.size()-1), k);
		}
		
		rowCount.put(10, 0);
		for (KeyBinding k : keyBinds)
		{    			
			if (k.getKeyDescription().contains(searchBar.getText()) || searchBar.getText().trim() == "" || I18n.format(k.getKeyDescription()).contains(searchBar.getText()))
			{
				addOtherKBButton(20, 10, I18n.format(k.getKeyDescription()));
				buttonKeyBindMap.put(buttonList.get(this.buttonList.size()-1), k);
				if (currentKeys.contains(k))
				{
					buttonList.get(this.buttonList.size()-1).enabled = false;
				}
				if (k.getKeyCode() != 0)
				{
					buttonList.get(this.buttonList.size()-1).displayString = "*" + buttonList.get(this.buttonList.size()-1).displayString + "*";
				}
			}
		
		}
    }
    
    protected void actionPerformed(GuiButton button) throws IOException
    {
    	if (button.id == backButtonID)
    	{
    		lastButton = null;
    		Minecraft.getMinecraft().displayGuiScreen(parentScreen);   		
    	}
    	if (button.id > 193  && button.id < 392)
    	{   	
    		if (currentKeyBinds.contains(button))
    		{
        		bindKey(button, 0, KeyModifier.NONE);
    		}
    		else if (otherKeyBinds.contains(button))
    		{   
        		bindKey(button, lastKeyCode, currentKeyModifier);
    		}
    		else
    		{   				
    			showKeyBinds(button);       			
    		} 
    		drawKeyboard();
    	} 
    	if (modifierButtons.keySet().contains(button))
    	{
    		currentKeyModifier = modifierButtons.get(button);
    		button.enabled = false;
    		lastButton = null;
    		drawKeyboard();
    	}
    }

    public void bindKey(GuiButton b, Integer keyCode, KeyModifier keyModifier)
    {
		buttonKeyBindMap.get(b).setKeyModifierAndCode(keyModifier, keyCode);
		KeyBinding.resetKeyBindingArrayAndHash();
		drawKeyboard();
    }
    
    public void addCurrentKBButton(Integer offset, Integer rowNum, String buttonText)
    {
    	try
    	{
        	if (rowCount.get(rowNum) == 0)
        	{
        		currentKBLength = 0;
        	}
        	else
        	{
        		currentKBLength += getButtonWidth(buttonList.get(this.buttonList.size()-1).displayString) + 5;
        	}   	
        	GuiButton b = new GuiButton(backButtonID + buttonList.size(), (currentKBLength) + offset, getRowY(rowNum), getButtonWidth(buttonText), getButtonHeight(), buttonText);
        	currentKeyBinds.add(b);
        	for (KeyBinding k : keyBindMap.lookupAll(org.lwjgl.input.Keyboard.getKeyIndex(getKeyCode(buttonText))))
        	{
        		if (k.getKeyModifier() == currentKeyModifier)
        		{
                	this.buttonList.add(b);   		
            		rowCount.put(rowNum, rowCount.get(rowNum) + 1);
        		}
        	}
    	}
    	catch (Exception e)
    	{
    		
    	}


    }
    
    public void addOtherKBButton(Integer offset, Integer rowNum, String buttonText)
    {
    	try
    	{
        	if (rowCount.get(rowNum) == 0)
        	{
        		otherKBLength = 0;
        	}
        	else
        	{    		
        		otherKBLength += getButtonWidth(buttonList.get(this.buttonList.size()-1).displayString) + 5;
        	}
    		this.buttonList.add(new GuiButton(backButtonID + buttonList.size(), (otherKBLength) + offset, getRowY(rowNum), getButtonWidth(buttonText), getButtonHeight(), buttonText));
    		otherKeyBinds.add(buttonList.get(this.buttonList.size()-1));
    		rowCount.put(rowNum, rowCount.get(rowNum) + 1);
    	}
		catch (Exception e)
		{
			
		}

    }
    
    public void addControlButton(Integer offset, Integer rowNum, String buttonText, String key)
    {       	
    	try
    	{
    		Integer width = getButtonWidth(buttonText);
    		if (rowLength.get(rowNum) == null)
    		{
    			rowLength.put(rowNum, 0);
    		}
    		this.buttonList.add(new GUIControlButton(backButtonID + buttonList.size(), rowLength.get(rowNum) + offset + rowCount.get(rowNum), getRowY(rowNum), width, getButtonHeight(), buttonText, key));
    		if (lastButton != null)
    		{			
    			if (key.equals(((GUIControlButton)lastButton).getKey()))
    			{
    				getLastButtonInList().enabled = false;
    			}
    		}
    		rowCount.put(rowNum, rowCount.get(rowNum) + 1);
    		rowLength.put(rowNum, rowLength.get(rowNum) + width);
    	}
    	catch (Exception e)
    	{
    		System.out.println("trouble adding " + buttonText);
    	}

    }
    
    public void placeControlButton(Integer offset, Integer rowNum, String buttonText, String key, Integer x)
    {
    	try
    	{
    		Integer width = getButtonWidth(buttonText);
    		this.buttonList.add(new GUIControlButton(backButtonID + buttonList.size(), x, getRowY(rowNum), width, getButtonHeight(), buttonText, key));
    		if (lastButton != null)
    		{   			
    			if (key.equals(((GUIControlButton)lastButton).getKey()))
    			{
    				getLastButtonInList().enabled = false;
    			}
    		}
    		rowCount.put(rowNum, rowCount.get(rowNum) + 1);	
    		
    	}
    	catch (Exception e)
    	{
    		System.out.println("trouble placing " + buttonText);
    	}
    	

    }
    
    public static String getKeyCode(String s)
    {
    	String output = keyCodeDict.get(s);
    	if (output != null)
    	{
    		return output;
    	}
    	else
    	{
    		return s;
    	}
    }
    
    public Integer getButtonWidth(String s)
    {
    	return (int) ((20 + 5 * (s.length() - 1)) * getWidthMult());
    }
    
    @Override
    protected void keyTyped(char c, int keyCode) throws IOException
    {
        super.keyTyped(c, keyCode);
        if (searchBar != null)
        {
        	searchBar.textboxKeyTyped(c, keyCode);
        	drawKeyboard();
        }
        
    }
    
    @Override
    protected void mouseClicked(int x, int y, int button) throws IOException
    {
        super.mouseClicked(x, y, button);
        if (searchBar != null)
        {
            searchBar.mouseClicked(x, y, button);
            if (button == 1 && x >= searchBar.x && x < searchBar.x + searchBar.width && y >= searchBar.y && y < searchBar.y + searchBar.height) {
            	searchBar.setText("");
            }
        }

    }
    
    @Override
    public void updateScreen()
    {
        super.updateScreen();
        if (searchBar != null)
        {
        	searchBar.updateCursorCounter();
        }
        
    }
    
}
