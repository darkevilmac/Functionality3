package darkevilmac.functionality.common.tileentity

import cofh.api.energy.IEnergyStorage
import cpw.mods.fml.common.{FMLCommonHandler, Optional}
import darkevilmac.functionality.common.energy.MultiBattery
import darkevilmac.functionality.common.util.{IC2EnergyUtils, RFEnergyUtils}
import ic2.api.energy.EnergyNet
import ic2.api.energy.event.{EnergyTileLoadEvent, EnergyTileUnloadEvent}
import ic2.api.energy.tile.IEnergySource
import ic2.api.info.Info
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.tileentity.TileEntity
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.common.util.ForgeDirection

/**
 * A class you can add to your tileentity to emit energy from IC2, Buildcraft, and mods that support Redstone Flux
 *
 * @param parent the the tile this is in
 */
@Optional.Interface(iface = "IEnergySource", modid = "IC2")
class TileMultiPowerEmitter(var battery: MultiBattery, parent: TileEntity, var acceptableDirections: Array[ForgeDirection]) extends TileEntity with IEnergySource with IEnergyStorage {

  var addedToEnet: Boolean = false
  var tileBuffer: Array[TileBuffer] = null

  override def updateEntity {
    if (worldObj == null) {
      worldObj = parent.worldObj
      if (worldObj == null) {
        return
      }
    }
    if (worldObj.isRemote) {
      return
    }

    if (!addedToEnet) loadEnet

    if (tileBuffer == null) {
      tileBuffer = TileBuffer.makeBuffer(worldObj, xCoord, yCoord, zCoord, false)
    }
    pushToConsumers
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

  def pushToConsumers() {
    if (tileBuffer == null) {
      tileBuffer = TileBuffer.makeBuffer(worldObj, xCoord, yCoord, zCoord, false)
    }
    RFEnergyUtils.pushRFToConsumers(this, battery.transferRate.getRF, tileBuffer)
  }

  def setAcceptableDirections(newAcceptableDirections: Array[ForgeDirection]) {
    unloadEnet
    acceptableDirections = newAcceptableDirections
    loadEnet
  }

  def unloadEnet() {
    if (addedToEnet && Info.isIc2Available) {
      MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this))
      addedToEnet = false
    }
  }

  def getTile(side: ForgeDirection): TileEntity = {
    if (tileBuffer == null) {
      tileBuffer = TileBuffer.makeBuffer(worldObj, xCoord, yCoord, zCoord, false)
    }
    tileBuffer(side.ordinal()).getTile
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
    battery.useRF(maxExtract, simulate)
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

  override def emitsEnergyTo(receiver: TileEntity, direction: ForgeDirection): Boolean = {
    acceptableDirections.contains(direction)
  }

  def canConnectEnergy(from: ForgeDirection): Boolean = {
    acceptableDirections.contains(from)
  }

  override def getOfferedEnergy: Double = {
    Math.min(battery.getEU, EnergyNet.instance.getPowerFromTier(IC2EnergyUtils.getIC2TierFromEnergyRate(battery.transferRate)))
  }

  override def drawEnergy(amount: Double) {
    battery.useEU(amount, false)
  }

  override def getSourceTier: Int = {
    IC2EnergyUtils.getIC2TierFromEnergyRate(battery.transferRate)
  }

}
