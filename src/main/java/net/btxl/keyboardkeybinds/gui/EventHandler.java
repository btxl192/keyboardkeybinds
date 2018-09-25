package net.btxl.keyboardkeybinds.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiOptions;
import net.minecraftforge.client.event.GuiScreenEvent.ActionPerformedEvent;
import net.minecraftforge.client.event.GuiScreenEvent.InitGuiEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber
public class EventHandler
{
	
	private static final Integer buttonID = 192;
	
	@SubscribeEvent
	public static void onGuiInit(InitGuiEvent.Post event)
	{
		if (event.getGui() instanceof GuiOptions)
		{
			GuiButton b = new GuiButton(buttonID, 0, 0, 100, 20, "Keyboard Keybinds");
			event.getButtonList().add(b);
		}
	}
	
	@SubscribeEvent
	public static void onGUIActionPerformed(ActionPerformedEvent event)
	{
		if (event.getGui() instanceof GuiOptions && event.getButton().id == buttonID)
		{
			Minecraft.getMinecraft().displayGuiScreen(new GUIKeyboardKeybinds(event.getGui()));
		}
	}
}
