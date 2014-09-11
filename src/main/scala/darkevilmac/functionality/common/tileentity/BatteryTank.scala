package darkevilmac.functionality.common.tileentity

import darkevilmac.functionality.common.energy.MultiBattery
import darkevilmac.functionality.common.util.FluidUtils
import net.minecraft.tileentity.TileEntity
import net.minecraftforge.common.util.ForgeDirection
import net.minecraftforge.fluids.{Fluid, FluidStack, FluidTankInfo, IFluidTank}

/**
 * For use with TileMultiPower( Emitter, Receptor, Storage )
 */
class BatteryTank(var battery: MultiBattery, var parent: TileEntity, var receptorFluid: Fluid, var emitterFluid: Fluid, var receptorFluidConversionRate: Int, var emitterFluidConversionRate: Int, var fluidTransferRate: Int, var tankSize: Int) extends TileEntity with IFluidTank {

  var tileBuffer: Array[TileBuffer] = null

  /**
   * Call on updateEntity
   *
   * @param battery
   */
  def updateBattery(battery: MultiBattery) {
    this.battery = battery

    pushToConsumers
  }

  def pushToConsumers() {
    if (tileBuffer == null) {
      worldObj = parent.getWorldObj
      xCoord = parent.xCoord
      yCoord = parent.yCoord
      zCoord = parent.zCoord
      tileBuffer = TileBuffer.makeBuffer(worldObj, xCoord, yCoord, zCoord, false)
    }

    FluidUtils.pushFluidToConsumers(this, fluidTransferRate, tileBuffer)
  }

  def getTile(side: ForgeDirection): TileEntity = {
    if (tileBuffer == null) {
      tileBuffer = TileBuffer.makeBuffer(worldObj, xCoord, yCoord, zCoord, false)
    }
    tileBuffer(side.ordinal()).getTile
  }

  override def getFluid: FluidStack = {
    new FluidStack(receptorFluid, battery.getFluid(receptorFluidConversionRate))
  }

  override def drain(maxDrain: Int, doDrain: Boolean): FluidStack = {
    new FluidStack(emitterFluid, battery.useFluid(maxDrain, emitterFluidConversionRate, if (doDrain) {
      false
    } else {
      true
    }))
  }

  override def getFluidAmount: Int = {
    battery.getFluid(receptorFluidConversionRate)
  }

  override def getInfo: FluidTankInfo = {
    new FluidTankInfo(new FluidStack(receptorFluid, battery.getFluid(receptorFluidConversionRate)), tankSize)
  }

  override def getCapacity: Int = {
    tankSize
  }

  override def fill(resource: FluidStack, doFill: Boolean): Int = {
    if (resource.getFluid == receptorFluid) {
      battery.addFluid(resource.amount, receptorFluidConversionRate, if (doFill) {
        false
      } else {
        true
      })
    }
    0
  }
}
