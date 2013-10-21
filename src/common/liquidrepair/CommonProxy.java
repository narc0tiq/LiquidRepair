package liquidrepair;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

import net.minecraftforge.common.Configuration;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import mods.tinker.tconstruct.common.TContent;

public class CommonProxy {
    public int repairTableBlockID = 1899;
    public BlockRepairTable repairTableBlock;
    public static final String NAME_REPAIR_TABLE = "liquidrepair.repairtable";

    public void init(Configuration config) {
        initConfig(config);
        initBlocks();
        initLanguage();
        initRecipes();
    }

    public void initConfig(Configuration config) {
        try {
            config.load();
        }
        catch(RuntimeException e) { /* and ignore it */ }

        // Read config stuff here.
        repairTableBlockID = config.getBlock("repairTable", repairTableBlockID)
                                   .getInt(repairTableBlockID);

        try {
            config.save();
        }
        catch(RuntimeException e) {
            System.out.println("LiquidRepair: Unable to save config!");
            e.printStackTrace();
        }
    }

    public void initBlocks() {
        repairTableBlock = new BlockRepairTable(repairTableBlockID);
        GameRegistry.registerBlock(repairTableBlock, ItemBlockRepairTable.class, NAME_REPAIR_TABLE);
        GameRegistry.registerTileEntity(TileEntityRepairTable.class, "liquidrepair.repairtable");
    }

    public void initLanguage() {
        String langDir = "/lang/";
        String[] languages = { "en_US" };
        LanguageRegistry lr = LanguageRegistry.instance();

        for(String language: languages) {
            lr.loadLocalization(langDir + language + ".xml", language, true);
        }
    }

    public void initRecipes() {
        ItemStack searedBrick = new ItemStack(TContent.materials, 1, 2);
        ItemStack anvil = new ItemStack(Block.anvil, 1);
        ItemStack castingTable = new ItemStack(TContent.searedBlock, 1, 0);
        ItemStack repairTable = new ItemStack(repairTableBlock, 1);

        GameRegistry.addRecipe(new ShapedOreRecipe(repairTable,
                                                   "bbb",
                                                   "bab",
                                                   "b b",
                                                   'b', searedBrick,
                                                   'a', anvil));

        GameRegistry.addRecipe(new ShapelessOreRecipe(repairTable,
                                                      castingTable,
                                                      anvil));
    }
}
