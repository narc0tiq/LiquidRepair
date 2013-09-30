package liquidrepair;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;

import net.minecraftforge.common.Configuration;

@Mod(modid = "LiquidRepair",
     name = "Liquid Repair",
     version = "%conf:VERSION%")
@NetworkMod(serverSideRequired = false,
            clientSideRequired = true,
            versionBounds="%conf:VERSION_BOUNDS%")
public class LiquidRepair {
    @SidedProxy(clientSide="liquidrepair.ClientProxy",
                serverSide="liquidrepair.CommonProxy")
    public static CommonProxy proxy;

    public static Configuration config = null;

    @Mod.PreInit
    public void preInit(FMLPreInitializationEvent event) {
        config = new Configuration(event.getSuggestedConfigurationFile());
    }

    @Mod.Init
    public void init(FMLInitializationEvent event) {
        proxy.initConfig(config);
    }
}
