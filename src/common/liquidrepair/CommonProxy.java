package liquidrepair;

import net.minecraftforge.common.Configuration;

public class CommonProxy {
    public int repairTableBlockID = 1899;

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
}
