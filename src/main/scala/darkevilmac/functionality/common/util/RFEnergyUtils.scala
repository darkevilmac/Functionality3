package darkevilmac.functionality.common.util

import cofh.api.energy.{IEnergyHandler, IEnergyStorage}
import darkevilmac.functionality.common.tileentity.TileBuffer
import net.minecraftforge.common.util.ForgeDirection

object RFEnergyUtils {

  /**
   * A port of buildcraft's pushFluidToConsumers for use with RF
   *
   * @param storage
   * @param energyCap
   * @param tileBuffer
   */
  def pushRFToConsumers(storage: IEnergyStorage, energyCap: Int, tileBuffer: Array[TileBuffer]) {
    var amountToPush = energyCap
    for (side <- ForgeDirection.VALID_DIRECTIONS) {
      val energy:Int = storage.extractEnergy(amountToPush, true)
      if (energy > 0) {
        val tile = tileBuffer(side.ordinal()).getTile
        if (tile.isInstanceOf[IEnergyHandler]) {
          val used = tile.asInstanceOf[IEnergyHandler].receiveEnergy(side.getOpposite, energy, false)
          if (used > 0) {
            amountToPush -= used
            storage.extractEnergy(energy, false)
            if (amountToPush <= 0) {
              return
            }
          }
        }
      }
    }
  }

}
