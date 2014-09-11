/**
 * TileBuffer from Buildcraft converted to scala.
 */
package darkevilmac.functionality.common.tileentity

import darkevilmac.functionality.common.util.SafeTimeTracker
import net.minecraft.block.Block
import net.minecraft.tileentity.TileEntity
import net.minecraft.world.World
import net.minecraftforge.common.util.ForgeDirection

object TileBuffer {

  def makeBuffer(world: World, x: Int, y: Int, z: Int, loadUnloaded: Boolean): Array[TileBuffer] = {
    val buffer = Array.ofDim[TileBuffer](6)
    for (i <- 0 until 6) {
      val d = ForgeDirection.getOrientation(i)
      buffer(i) = new TileBuffer(world, x + d.offsetX, y + d.offsetY, z + d.offsetZ, loadUnloaded)
    }
    buffer
  }
}

class TileBuffer(private val world: World, private val x: Int, private val y: Int, private val z: Int, private val loadUnloaded: Boolean) {

  private val tracker = new SafeTimeTracker(20, 5)
  private var block: Block = null
  private var tile: TileEntity = _

  refresh()

  def set(block: Block, tile: TileEntity) {
    this.block = block
    this.tile = tile
    tracker.markTime(world)
  }

  def getBlock(): Block = {
    if ((tile != null && tile.isInvalid) || (tile == null && tracker.markTimeIfDelay(world))) {
      refresh()
    }
    block
  }

  def refresh() {
    tile = null
    block = null
    if (!loadUnloaded && !world.blockExists(x, y, z)) {
      return
    }
    block = world.getBlock(x, y, z)
    if (block != null &&
      block.hasTileEntity(world.getBlockMetadata(x, y, z))) {
      tile = world.getTileEntity(x, y, z)
    }
  }

  def getTile(): TileEntity = {
    if (tile != null && !tile.isInvalid) {
      return tile
    }
    if (tracker.markTimeIfDelay(world)) {
      refresh()
      if (tile != null && !tile.isInvalid) {
        return tile
      }
    }
    null
  }

  def exists(): Boolean = {
    if (tile != null && !tile.isInvalid) {
      return true
    }
    world.blockExists(x, y, z)
  }
}