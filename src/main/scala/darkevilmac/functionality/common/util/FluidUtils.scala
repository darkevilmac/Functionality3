package darkevilmac.functionality.common.util

import darkevilmac.functionality.common.tileentity.TileBuffer
import net.minecraftforge.common.util.ForgeDirection
import net.minecraftforge.fluids.{IFluidHandler, IFluidTank}

/**
 * Created by Darkevilmac on 9/10/2014.
 */
object FluidUtils {

  def pushFluidToConsumers(tank: IFluidTank, flowCap: Int, tileBuffer: Array[TileBuffer]) {
    var amountToPush = flowCap
    for (side <- ForgeDirection.VALID_DIRECTIONS) {
      val fluidStack = tank.drain(amountToPush, false)
      if (fluidStack != null && fluidStack.amount > 0) {
        val tile = tileBuffer(side.ordinal()).getTile
        if (tile.isInstanceOf[IFluidHandler]) {
          val used = tile.asInstanceOf[IFluidHandler].fill(side.getOpposite, fluidStack, true)
          if (used > 0) {
            amountToPush -= used
            tank.drain(used, true)
            if (amountToPush <= 0) {
              return
            }
          }
        }
      }
    }
  }

}
