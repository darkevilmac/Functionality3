package darkevilmac.functionality.common.tileentity

import cofh.api.energy.IEnergyStorage
import cpw.mods.fml.common.{FMLCommonHandler, Optional}
import darkevilmac.functionality.common.energy.MultiBattery
import darkevilmac.functionality.common.util.IC2EnergyUtils
import ic2.api.energy.event.{EnergyTileLoadEvent, EnergyTileUnloadEvent}
import ic2.api.energy.tile.IEnergySink
import ic2.api.info.Info
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.tileentity.TileEntity
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.common.util.ForgeDirection

@Optional.Interface(iface = "IEnergySink", modid = "IC2")
class TileMultiPowerReceptor(var battery: MultiBattery, parent: TileEntity, var acceptableDirections: Array[ForgeDirection]) extends TileEntity with IEnergySink with IEnergyStorage {

  var addedToEnet: Boolean = false

  override def updateEntity {
    if (worldObj == null) {
      return
    }
    if (worldObj.isRemote) {
      return
    }

    if (!addedToEnet) loadEnet
  }

  def setAcceptableDirections(newAcceptableDirections: Array[ForgeDirection]) {
    unloadEnet
    acceptableDirections = newAcceptableDirections
    loadEnet
  }

  def loadEnet() {
    if (!addedToEnet && !FMLCommonHandler.instance.getEffectiveSide.isClient && Info.isIc2Available) {
      worldObj = parent.getWorldObj
      xCoord = parent.xCoord
      yCoord = parent.yCoord
      zCoord = parent.zCoord
      if (acceptableDirections == null) {
        acceptableDirections = ForgeDirection.VALID_DIRECTIONS
      } else if (acceptableDirections.isEmpty) {
        acceptableDirections = ForgeDirection.VALID_DIRECTIONS
      }

      MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this))
      addedToEnet = true
    }
  }

  def unloadEnet() {
    if (addedToEnet && Info.isIc2Available) {
      MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this))
      addedToEnet = false
    }
  }

  override def invalidate {
    super.invalidate
    onChunkUnload
  }

  override def onChunkUnload {
    unloadEnet
  }

  override def readFromNBT(nbt: NBTTagCompound) {
    battery.readFromNBT(nbt)
  }

  override def writeToNBT(nbt: NBTTagCompound) {
    battery.writeToNBT(nbt)
  }

  /**
   * Redstone Flux's energy reception
   *
   * @param maxReceive
	 * Maximum amount of energy to receive.
   * @param simulate
	 * If TRUE, the charge will only be simulated.
   **/
  override def receiveEnergy(maxReceive: Int, simulate: Boolean): Int = {
    battery.addRF(maxReceive, simulate)
    0
  }

  /**
   * Redstone Flux's energy extraction
   *
   * @param maxExtract
	 * Maximum amount of energy to extract.
   * @param simulate
	 * If TRUE, the extraction will only be simulated.
   **/
  override def extractEnergy(maxExtract: Int, simulate: Boolean): Int = {
    0
  }

  /**
   * Redstone Flux's current energy storage
   *
   * @return
   */
  override def getEnergyStored(): Int = {
    battery.getRF
  }

  /**
   * Redstone Flux's max energy
   *
   * @return
   */
  override def getMaxEnergyStored(): Int = {
    battery.getMaxRF
  }

  override def getDemandedEnergy: Double = {
    Math.max(0, battery.getMaxEU - battery.getEU)
  }

  override def injectEnergy(directionFrom: ForgeDirection, amount: Double, voltage: Double): Double = {
    battery.addEU(amount, false)
    0.0
  }

  override def acceptsEnergyFrom(emitter: TileEntity, direction: ForgeDirection): Boolean = {
    acceptableDirections.contains(direction)
  }

  def canConnectEnergy(from: ForgeDirection): Boolean = {
    acceptableDirections.contains(from)
  }

  override def getSinkTier: Int = {
    IC2EnergyUtils.getIC2TierFromEnergyRate(battery.transferRate)
  }


}
