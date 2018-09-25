package net.btxl.keyboardkeybinds;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;


@Mod(modid = keyboardkeybinds.MOD_ID, name = keyboardkeybinds.MOD_NAME, version = keyboardkeybinds.VERSION)
public class keyboardkeybinds
{
	//constants
	public static final String MOD_ID = "keyboardkeybinds";
	public static final String MOD_NAME = "BTXL's Keyboard Keybinds";
	public static final String VERSION = "1.0";
	public static final String RESOURCE_PREFIX = MOD_ID.toLowerCase() + ":";
	
	
	@Instance(MOD_ID)
	public static keyboardkeybinds instance;
	
	@SidedProxy(clientSide = "net.btxl.keyboardkeybinds.ClientProxy", serverSide = "net.btxl.keyboardkeybinds.CommonProxy")
	public static CommonProxy proxy;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		proxy.preInit(event);
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		proxy.init(event);
	}
	
	@EventHandler
	public void postinit(FMLPostInitializationEvent event)
	{
		proxy.postInit(event);
	}
	
	
}
