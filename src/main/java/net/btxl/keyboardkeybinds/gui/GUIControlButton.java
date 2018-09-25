package net.btxl.keyboardkeybinds.gui;

import java.util.List;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.settings.KeyBindingMap;
import net.minecraftforge.client.settings.KeyModifier;

public class GUIControlButton extends GuiButton
{
	
	private final String key;
	private KeyBindingMap keyBindMap = new KeyBindingMap();
	private Integer keyIndex = -1;
	
	public GUIControlButton(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText, String Key)
	{		
		super(buttonId, x, y, widthIn, heightIn, buttonText);
		key = Key;
		keyIndex = org.lwjgl.input.Keyboard.getKeyIndex(GUIKeyboardKeybinds.getKeyCode(key));
		checkColour();
	}

	public String getKey()
	{
		return key;
	}
	
	public Integer getKeyIndex()
	{
		return keyIndex;
	}
	
	public void checkColour()
	{		
		List<KeyBinding> kb = keyBindMap.lookupAll(keyIndex);
		
		if (kb.size() == 0)
		{
			//displayString = "§a" + displayString;			
		}	
		if (kb.size() == 1 && !displayString.contains("("))
		{
			displayString = "(" + displayString + ")";
		}
		if (kb.size() > 0)
		{
			Boolean modMatches = false;
			for (KeyBinding k : kb)
			{
				if (k.getKeyModifier() == GUIKeyboardKeybinds.currentKeyModifier)
				{
					modMatches = true;
				}
			}	
			if (!modMatches)
			{
				//displayString = "§6" + displayString;	
				if (!displayString.contains("?"))
				{
					displayString = "?" + displayString;
				}
				
			}
		}
		if (kb.size() > 1)
		{
			
			for (KeyBinding k : kb)
			{
				if (k.getKeyModifier() == GUIKeyboardKeybinds.currentKeyModifier)
				{
					//displayString = "§c" + displayString;
					if (!displayString.contains("*"))
					{
						displayString = "*" + displayString + "*";
					}
				}
			}	
		}
		this.width = GUIKeyboardKeybinds.getButtonWidth(displayString);
	}
}
