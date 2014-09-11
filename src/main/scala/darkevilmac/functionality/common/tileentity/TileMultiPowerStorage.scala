package darkevilmac.functionality.common.tileentity

import cofh.api.energy.IEnergyStorage
import cpw.mods.fml.common.FMLCommonHandler
import darkevilmac.functionality.common.energy.MultiBattery
import darkevilmac.functionality.common.util.{RFEnergyUtils, IC2EnergyUtils}
import ic2.api.energy.EnergyNet
import ic2.api.energy.event.{EnergyTileLoadEvent, EnergyTileUnloadEvent}
import ic2.api.energy.tile.{IEnergySink, IEnergySource}
import ic2.api.info.Info
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.tileentity.TileEntity
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.common.util.ForgeDirection

class TileMultiPowerStorage(var battery: MultiBattery, parent: TileEntity, var acceptableInputDirections: Array[ForgeDirection], var acceptableOutputDirections: Array[ForgeDirection]) extends TileEntity with IEnergySink with IEnergySource with IEnergyStorage {

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
      if (acceptableInputDirections == null) {
        acceptableInputDirections = ForgeDirection.VALID_DIRECTIONS
      } else if (acceptableInputDirections.isEmpty) {
        acceptableInputDirections = ForgeDirection.VALID_DIRECTIONS
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

  def getTile(side: ForgeDirection): TileEntity = {
    if (tileBuffer == null) {
      tileBuffer = TileBuffer.makeBuffer(worldObj, xCoord, yCoord, zCoord, false)
    }
    tileBuffer(side.ordinal()).getTile
  }

  def setAcceptableInputDirections(newAcceptableInputDirections: Array[ForgeDirection]) {
    unloadEnet
    acceptableInputDirections = newAcceptableInputDirections
    loadEnet
  }

  def setAcceptableOutputDirections(newAcceptableOutputDirections: Array[ForgeDirection]) {
    unloadEnet
    acceptableOutputDirections = newAcceptableOutputDirections
    loadEnet
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

  def canConnectInputEnergy(from: ForgeDirection): Boolean = {
    acceptableInputDirections.contains(from)
  }

  def canConnectOutputEnergy(from: ForgeDirection): Boolean = {
    acceptableOutputDirections.contains(from)
  }

  override def getDemandedEnergy: Double = {
    Math.max(0, battery.getMaxEU - battery.getEU)
  }

  override def injectEnergy(directionFrom: ForgeDirection, amount: Double, voltage: Double): Double = {
    battery.addEU(amount, false)
    0.0
  }

  override def acceptsEnergyFrom(emitter: TileEntity, direction: ForgeDirection): Boolean = {
    acceptableInputDirections.contains(direction)
  }

  override def emitsEnergyTo(receiver: TileEntity, direction: ForgeDirection): Boolean = {
    acceptableOutputDirections.contains(direction)
  }

  override def getOfferedEnergy: Double = {
    Math.min(battery.getEU, EnergyNet.instance.getPowerFromTier(IC2EnergyUtils.getIC2TierFromEnergyRate(battery.transferRate)))
  }

  override def drawEnergy(amount: Double) {
    battery.useEU(amount, false)
  }

  override def getSinkTier: Int = {
    IC2EnergyUtils.getIC2TierFromEnergyRate(battery.transferRate)
  }

  override def getSourceTier: Int = {
    IC2EnergyUtils.getIC2TierFromEnergyRate(battery.transferRate)
  }

}
