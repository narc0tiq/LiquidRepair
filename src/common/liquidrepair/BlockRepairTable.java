package liquidrepair;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import mods.tinker.tconstruct.library.TConstructRegistry;

public class BlockRepairTable extends BlockContainer {
    public BlockRepairTable(int id, Material material) {
        super(id, material);

        this.setUnlocalizedName("liquidrepair.block.repairtable");
        this.setHardness(2.0f);
        this.setCreativeTab(TConstructRegistry.blockTab);
    }

    public BlockRepairTable(int id) {
        this(id, Material.iron);
    }

    public TileEntity createNewTileEntity(World world) {
        return null;
    }
}
