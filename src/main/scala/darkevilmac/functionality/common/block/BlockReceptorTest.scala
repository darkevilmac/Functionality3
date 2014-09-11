package darkevilmac.functionality.common.block

import darkevilmac.functionality.common.tileentity.{TileTester, TileMultiPowerReceptor}
import net.minecraft.block.BlockContainer
import net.minecraft.block.material.Material
import net.minecraft.tileentity.TileEntity
import net.minecraft.world.World

/**
 * Created by Darkevilmac on 7/30/2014.
 */
class BlockReceptorTest() extends BlockContainer(Material.iron) {

  setBlockName("BlockReceptorTest")

  def createNewTileEntity(world: World, var2: Int): TileEntity = {
    return new TileTester
  }

}