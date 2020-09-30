package iDiamondhunter.morebowsmod;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
//import cpw.mods.fml.common.SidedProxy;
//import iDiamondhunter.morebowsmod.proxy.*;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import iDiamondhunter.morebowsmod.bows.*;
import iDiamondhunter.morebowsmod.entities.*;

import org.apache.logging.log4j.Logger;

@Mod(modid = MoreBowsMod.MOD_ID, name = MoreBowsMod.MOD_NAME, version = MoreBowsMod.MOD_VERSION)
public class MoreBowsMod
{
	public static final String MOD_ID = "${archivesBaseName}";
	public static final String MOD_NAME	= "${modName}";
	public static final String MOD_VERSION	= "${version}";
	
	@Instance("${archivesBaseName}")
	public static MoreBowsMod instance;
	
    public static Logger modLog;
    
    /* TODO: Make proxies, re-evaluate how & where stuff should be declared & initialized. */
    
    public final static String DiamondBowName = "DiamondBow";
    public final static String GoldBowName = "GoldBow";
    public final static String EnderBowName = "EnderBow";
    public final static String StoneBowName = "StoneBow";
    public final static String IronBowName = "IronBow";
    public final static String MultiBowName = "MultiBow";
    public final static String FlameBowName = "FlameBow";
    public final static String FrostBowName = "FrostBow";
    
    /* This is super janky, rethink. */
    private final static String modSeperator = "morebowsmod:";
    
    public final static Item DiamondBow = new ItemDiamondBow().setUnlocalizedName(DiamondBowName).setTextureName(modSeperator + DiamondBowName);
	public final static Item GoldBow = new ItemGoldBow().setUnlocalizedName(GoldBowName).setTextureName(modSeperator + GoldBowName);
	public final static Item EnderBow = new ItemBowEnder().setUnlocalizedName(EnderBowName).setTextureName(modSeperator + EnderBowName);
	public final static Item StoneBow = new ItemStoneBow().setUnlocalizedName(StoneBowName).setTextureName(modSeperator + StoneBowName);
	public final static Item IronBow = new ItemIronBow().setUnlocalizedName(IronBowName).setTextureName(modSeperator + IronBowName);
	public final static Item MultiBow = new ItemMultiBow().setUnlocalizedName(MultiBowName).setTextureName(modSeperator + MultiBowName);
	public final static Item FlameBow = new ItemFlameBow().setUnlocalizedName(FlameBowName).setTextureName(modSeperator + FlameBowName);
	public final static Item FrostBow = new ItemFrostBow().setUnlocalizedName(FrostBowName).setTextureName(modSeperator + FrostBowName);
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		
		modLog = event.getModLog();
		modLog.info("Too many bows, or not enough?");
		
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event) {
		
		registerItems();
		
	}
	
	private void registerItems() {
		
		/* TODO check if this is the right way of doing things. */
		
        GameRegistry.registerItem(DiamondBow, DiamondBowName);
        GameRegistry.registerItem(GoldBow, GoldBowName);
        GameRegistry.registerItem(EnderBow, EnderBowName);
        GameRegistry.registerItem(StoneBow, StoneBowName);
        GameRegistry.registerItem(IronBow, IronBowName);
        GameRegistry.registerItem(MultiBow, MultiBowName);
        GameRegistry.registerItem(FlameBow, FlameBowName);
        GameRegistry.registerItem(FrostBow, FrostBowName);
		
		GameRegistry.addRecipe(new ItemStack(StoneBow, 1), new Object[] //TEST TO SEE IF THE RECIPES WORK
                {
                    " Ss", "TBs", " Ss", 'T', Items.stick, 's', Items.string, 'S', Blocks.stone, 'B', Items.bow
                });
        GameRegistry.addRecipe(new ItemStack(StoneBow, 1), new Object[]
                {
                    "sS ", "sBT", "sS ", 'T', Items.stick, 's', Items.string, 'S', Blocks.stone, 'B', Items.bow
                });
        GameRegistry.addRecipe(new ItemStack(IronBow, 1), new Object[]
                {
                    " Is", "IBs", " Is", 's', Items.string, 'I', Items.iron_ingot, 'B', Items.bow
                });
        GameRegistry.addRecipe(new ItemStack(IronBow, 1), new Object[]
                {
                    "sI ", "sBI", "sI ", 's', Items.string, 'I', Items.iron_ingot, 'B', Items.bow
                });
        GameRegistry.addRecipe(new ItemStack(GoldBow, 1), new Object[]
                {
                    " Gs", "GBs", " Gs", 's', Items.string, 'G', Items.gold_ingot, 'B', Items.bow
                });
        GameRegistry.addRecipe(new ItemStack(GoldBow, 1), new Object[]
                {
                    "sG ", "sBG", "sG ", 's', Items.string, 'G', Items.gold_ingot, 'B', Items.bow
                });
        GameRegistry.addRecipe(new ItemStack(DiamondBow, 1), new Object[]
                {
                    " Ds", "IBs", " Ds", 's', Items.string, 'D', Items.diamond, 'I', Items.iron_ingot, 'B', Items.bow
                });
        GameRegistry.addRecipe(new ItemStack(DiamondBow, 1), new Object[]
                {
                    "sD ", "sBI", "sD ", 's', Items.string, 'D', Items.diamond, 'I', Items.iron_ingot, 'B', Items.bow
                });
        GameRegistry.addRecipe(new ItemStack(MultiBow, 1), new Object[]
                {
                    " Bs", "I s", " Bs", 's', Items.string, 'I', Items.iron_ingot, 'B', IronBow
                });
        GameRegistry.addRecipe(new ItemStack(MultiBow, 1), new Object[]
                {
                    "sB ", "s I", "sB ", 's', Items.string, 'I', Items.iron_ingot, 'B', IronBow
                });
        GameRegistry.addRecipe(new ItemStack(FlameBow, 1), new Object[]
                {"NB ", "GI ", "NB ",   'G', Items.gold_ingot, 'B', Items.blaze_rod,  'I', IronBow, 'N',Blocks.netherrack
        	    });
        GameRegistry.addRecipe(new ItemStack(FlameBow, 1), new Object[]
                {" NB", " GI", " NB",   'G', Items.gold_ingot, 'B', Items.blaze_rod, 'I', IronBow, 'N', Blocks.netherrack
        	    });
        GameRegistry.addRecipe(new ItemStack(EnderBow, 1), new Object[]
                {
                    "GP ", "EI ", "GP ",   'G', Items.gold_ingot, 'P', Items.ender_pearl,  'I', IronBow, 'E',Items.ender_eye
                });
        GameRegistry.addRecipe(new ItemStack(EnderBow, 1), new Object[]
                {
                    " GP", " EI", " GP",  'G', Items.gold_ingot, 'P', Items.ender_pearl,  'I', IronBow, 'E', Items.ender_eye
                });
        GameRegistry.addRecipe(new ItemStack(FrostBow, 1), new Object[]
        		{
        			" IR", "SER", " IR", 'R', Items.string, 'I', Blocks.ice, 'S', Items.snowball, 'E', IronBow
        		});
		
        /* I'm not sure how this works. */
		EntityRegistry.registerModEntity(EntityiDiamondhunterFireArrow.class, "iDiamondhunterFireArrow", 1, this, 64, 20, true);
		EntityRegistry.registerModEntity(EntityiDiamondhunterFrostArrow.class, "iDiamondhunterFrostArrow", 2, this, 64, 20, true);
		EntityRegistry.registerModEntity(EntityiDiamondhunterEnderArrow.class, "iDiamondhunterEnderArrow", 3, this, 64, 20, true);
		
	}
}
