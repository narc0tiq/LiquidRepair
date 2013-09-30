package liquidrepair;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

import net.minecraftforge.common.Configuration;

public class CommonProxy {
    public int repairTableBlockID = 1899;
    public BlockRepairTable repairTableBlock;

    public void init(Configuration config) {
        initConfig(config);
        initBlocks();
        initLanguage();
        initTConstruct();
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
        GameRegistry.registerBlock(repairTableBlock, ItemBlockRepairTable.class, "RepairTable");
    }

    public void initLanguage() {
        String langDir = "/lang/";
        String[] languages = { "en_US" };
        LanguageRegistry lr = LanguageRegistry.instance();

        for(String language: languages) {
            lr.loadLocalization(langDir + language + ".xml", language, true);
        }
    }

    public void initTConstruct() {
    }

    public void initRecipes() {
    }
}
