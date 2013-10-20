package liquidrepair;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import mods.tinker.tconstruct.library.TConstructRegistry;
import mods.tinker.tconstruct.library.tools.AbilityHelper;

public class BlockRepairTable extends BlockContainer {
    public BlockRepairTable(int id, Material material) {
        super(id, material);

        this.setUnlocalizedName(CommonProxy.NAME_REPAIR_TABLE);
        this.setHardness(2.0f);
        this.setCreativeTab(TConstructRegistry.blockTab);
    }

    public BlockRepairTable(int id) {
        this(id, Material.iron);
    }

    @Override
    public TileEntity createNewTileEntity(World world) {
        return new TileEntityRepairTable();
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player,
            int side, float clickX, float clickY, float clickZ) {
        if(world.isRemote) {
            return true;
        }

        TileEntityRepairTable te = (TileEntityRepairTable) world.getBlockTileEntity(x, y, z);
        if(te == null) {
            return true;
        }

        if(te.isStackInSlot(0)) {
            // Use up the last of the liquid inside
            te.attemptRepair();

            // Get it out!
            ItemStack stack = te.decrStackSize(0, 1);
            if(stack != null) {
                AbilityHelper.spawnItemAtPlayer(player, stack);
            }
        } else {
            // Put it in!
            ItemStack stack = player.inventory.decrStackSize(player.inventory.currentItem, 1);
            if(stack != null) {
                te.setInventorySlotContents(0, stack);
            }
        }

        world.markBlockForUpdate(x, y, z);
        return true;
    }
}
