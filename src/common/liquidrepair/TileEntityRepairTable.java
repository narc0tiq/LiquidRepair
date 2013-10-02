package liquidrepair;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.liquids.LiquidStack;
import net.minecraftforge.liquids.LiquidTank;
import net.minecraftforge.liquids.ILiquidTank;
import net.minecraftforge.liquids.ITankContainer;

import mods.tinker.tconstruct.TConstruct;
import mods.tinker.tconstruct.library.crafting.CastingRecipe;
import mods.tinker.tconstruct.library.tools.ToolCore;

public class TileEntityRepairTable extends TileEntity implements ISidedInventory, ITankContainer {
    public ItemStack content;
    public LiquidTank tank;
    public LiquidStack repairLiquid;

    public TileEntityRepairTable() {
        super();
        tank = new LiquidTank(null, 0, this);
    }

    public boolean isRepairable(ItemStack is) {
        if(is == null) { return false; }

        Item item = is.getItem();
        // TODO: This is WRONG! Tinker tools keep their damage value in NBT!
        // However, for now, it'll do.
        return item instanceof ToolCore && item.getDamage(is) > 0;
    }

    public ToolCore getTool() {
        if(content == null || !(content.getItem() instanceof ToolCore)) {
            return null;
        }
        return (ToolCore) content.getItem();
    }

    public boolean isContentRepaired() {
        ToolCore tool = getTool();
        if(tool == null) { return true; }

        // TODO: This is still WRONG! Just like above.
        return tool.getDamage(content) < 1;
    }

    @SuppressWarnings("unchecked")
    public LiquidStack findLiquidFor(Item headItem) {
        if(headItem == null) {
            return null;
        }

        // The only reason I need the typecast is because I'm developing
        // against a deobfuscated TConstruct; remove it (and the
        // @SuppressWarnings above) if you're using the real source.
        List<CastingRecipe> recipes = (List<CastingRecipe>)
                                      TConstruct.tableCasting.getCastingRecipes();
        for(CastingRecipe recipe: recipes) {
            if(recipe.output.itemID == headItem.itemID) {
                return recipe.castingMetal;
            }
        }

        return null;
    }

    public void resetTank() {
        tank.setCapacity(0);
        repairLiquid = null;

        if(content == null || !(content.getItem() instanceof ToolCore) || isContentRepaired()) {
            return;
        }

        ToolCore tool = (ToolCore) content.getItem();
        repairLiquid = findLiquidFor(tool.getHeadItem());
        // TODO: Scale repairLiquid's quantity by tool damage, use it to set
        // tank's capacity.
    }

//public interface ISidedInventory extends IInventory {
    @Override public int[] getAccessibleSlotsFromSide(int side) { return new int[]{ 0 }; }
    @Override public boolean canInsertItem(int slot, ItemStack is, int side) { return slot == 0; }

    @Override
    public boolean canExtractItem(int slot, ItemStack is, int side) {
        return slot == 0 && isContentRepaired();
    }
//}

//public interface IInventory {
    // Informational, fixed-implementation parts:
    @Override public int getInventoryStackLimit() { return 1; }
    @Override public String getInvName() { return CommonProxy.NAME_REPAIR_TABLE; }
    @Override public int getSizeInventory() { return 1; }
    @Override public ItemStack getStackInSlotOnClosing(int slot) { return null; }
    @Override public boolean isInvNameLocalized() { return false; }
    @Override public boolean isUseableByPlayer(EntityPlayer player) { return true; }


    // User parts:
    @Override public void openChest() { /* nothing to do */ }
    @Override public void closeChest() { /* nothing to do */ }

    @Override
    public ItemStack decrStackSize(int slot, int count) {
        if(content == null || slot != 0) {
            // There's nothing there, man.
            return null;
        }

        ItemStack ret = content;
        if(count >= content.stackSize) {
            // You took all of it!
            content = null;
        } else {
            // You took just some of it.
            ret = content.copy();
            ret.stackSize = count;
            content.stackSize -= count;
        }
        onInventoryChanged();
        return ret;
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        if(slot != 0) {
            return null;
        } else {
            return content;
        }
    }

    @Override
    public boolean isStackValidForSlot(int slot, ItemStack is) {
        if(is == null) {
            return true;
        }

        if(slot != 0 || content != null) {
            return false;
        }

        return isRepairable(is);
    }

    @Override
    public void onInventoryChanged() {
        worldObj.markBlockForRenderUpdate(xCoord, yCoord, zCoord);
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack is) {
        content = is;
        if(is != null && is.stackSize > getInventoryStackLimit()) {
            content.stackSize = getInventoryStackLimit();
        }
        onInventoryChanged();
    }
//}

    public boolean isRepairLiquid(LiquidStack resource) {
        if(resource == null || repairLiquid == null) {
            return false;
        }
        return resource.isLiquidEqual(repairLiquid);
    }

//public interface ITankContainer {
    public ILiquidTank getTank(ForgeDirection direction, LiquidStack type) {
        // We have no tanks for the wrong liquid (but null liquids are fine)
        if(!isRepairLiquid(type)) {
            return null;
        }
        return tank;
    }

    public ILiquidTank[] getTanks(ForgeDirection direction) {
        return new ILiquidTank[] { tank };
    }

    public int fill(ForgeDirection from, LiquidStack resource, boolean doFill) {
        return fill(0, resource, doFill);
    }

    public int fill(int tankIndex, LiquidStack resource, boolean doFill) {
        if(tankIndex != 0 || !isRepairLiquid(resource)) {
            return 0;
        }
        return tank.fill(resource, doFill);
    }

    public LiquidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
        return drain(0, maxDrain, doDrain);
    }

    public LiquidStack drain(int tankIndex, int maxDrain, boolean doDrain) {
        if(tankIndex != 0) {
            return null;
        }
        return tank.drain(maxDrain, doDrain);
    }
//}
}
