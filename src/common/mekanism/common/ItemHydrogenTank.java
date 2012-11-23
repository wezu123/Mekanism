package mekanism.common;

import mekanism.api.EnumGas;
import net.minecraft.src.*;

public class ItemHydrogenTank extends ItemStorageTank
{
	public ItemHydrogenTank(int id)
	{
		super(id, 1600, 16, 16);
	}
	
	public EnumGas gasType()
	{
		return EnumGas.HYDROGEN;
	}
}
