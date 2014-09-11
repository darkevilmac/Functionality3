package darkevilmac.functionality.common.energy

import net.minecraft.nbt.NBTTagCompound

/**
 * Created by Darkevilmac on 8/23/2014.
 */
class MultiBattery(var energy: MultiEnergy, var maxEnergy: MultiEnergy, var transferRate: MultiEnergy) {

  def readFromNBT(nbt: NBTTagCompound) {
    val batteryData: NBTTagCompound = nbt.getCompoundTag("Functionality3Battery")

    energy.rawEnergy = batteryData.getDouble("energy")
    maxEnergy.rawEnergy = batteryData.getDouble("maxEnergy")
    transferRate.rawEnergy = batteryData.getDouble("energyTransferRate")
  }

  def writeToNBT(nbt: NBTTagCompound) {
    val batteryData: NBTTagCompound = new NBTTagCompound

    batteryData.setDouble("energy", energy.rawEnergy)
    batteryData.setDouble("maxEnergy", maxEnergy.rawEnergy)
    batteryData.setDouble("energyTransferRate", transferRate.rawEnergy)

    nbt.setTag("Functionality3Battery", batteryData)
  }

  /**
   * Add energy to the battery in EU.
   *
   * @param amount the amount to add in EU.
   * @param simulate if true energy will not be added.
   * @return amount added.
   */
  def addEU(amount: Double, simulate: Boolean): Double = {
    var amountAdded: Double = amount
    var amountAddedClone: Double = amountAdded


    if (amountAdded > transferRate.getEU) {
      amountAdded = transferRate.getEU
      amountAddedClone = amountAdded
    }


    if ((new QuickMultiEnergy(0.0).addEU(amountAddedClone).rawEnergy + energy.rawEnergy) > maxEnergy.rawEnergy) {
      amountAdded = amountAddedClone - (amountAddedClone + energy.getEU() - maxEnergy.getEU())
    }

    if (!simulate) {
      energy.addEU(amountAdded)
    }
    amountAdded
  }

  /**
   * Use energy in the battery in EU.
   *
   * @param amount the amount to use in EU.
   * @param simulate if true energy will not be used.
   * @return amount used.
   */
  def useEU(amount: Double, simulate: Boolean): Double = {
    var amountUsed: Double = amount

    if (amountUsed > transferRate.getEU) {
      amountUsed = transferRate.getEU
    }

    if ((energy.rawEnergy - new QuickMultiEnergy(0.0).addEU(amount).rawEnergy) < 0.0) {
      amountUsed = energy.getEU
    }

    if (!simulate) {
      energy.useEU(amountUsed)
    }
    amountUsed
  }

  /**
   * Gets the amount of energy in the battery in EU.
   *
   * @return The amount of energy the battery currently contains in EU.
   */
  def getEU(): Double = {
    energy.getEU
  }

  /**
   * Set the energy in the battery in EU.
   *
   * @param amount the amount to set it to in EU
   */
  def setEU(amount: Double) {
    energy.setEU(amount)
  }

  /**
   * Gets the maximum amount of energy that can be stored in the battery in EU.
   *
   * @return The amount of energy the battery currently contains in EU.
   */
  def getMaxEU(): Double = {
    maxEnergy.getEU
  }

  /**
   * Add energy to the battery in RF.
   *
   * @param amount the amount to add in RF.
   * @param simulate if true energy will not be added.
   * @return amount added.
   */
  def addRF(amount: Int, simulate: Boolean): Int = {
    var amountAdded: Int = amount
    var amountAddedClone: Int = amountAdded

    if (amountAdded > transferRate.getRF) {
      amountAdded = transferRate.getRF
      amountAddedClone = amountAdded
    }

    if ((new QuickMultiEnergy(0.0).addRF(amountAddedClone).rawEnergy + energy.rawEnergy) > maxEnergy.rawEnergy) {
      amountAdded = amountAddedClone - (amountAddedClone + energy.getRF() - maxEnergy.getRF())
    }

    if (!simulate) {
      energy.addRF(amountAdded)
    }
    amountAdded
  }

  /**
   * Use energy in the battery in RF.
   *
   * @param amount the amount to use in RF.
   * @param simulate if true energy will not be used.
   * @return amount used.
   */
  def useRF(amount: Int, simulate: Boolean): Int = {
    var amountUsed: Int = amount

    if (amountUsed > transferRate.getRF) {
      amountUsed = transferRate.getRF
    }

    if ((energy.rawEnergy - new QuickMultiEnergy(0.0).addRF(amount).rawEnergy) < 0.0) {
      amountUsed = energy.getRF
    }

    if (!simulate) {
      energy.useRF(amountUsed)
    }
    amountUsed
  }

  /**
   * Gets the amount of energy in the battery in RF.
   *
   * @return The amount of energy the battery currently contains in RF.
   */
  def getRF(): Int = {
    energy.getRF
  }

  /**
   * Set the energy in the battery in RF.
   *
   * @param amount the amount to set it to in RF
   */
  def setRF(amount: Int) {
    energy.setRF(amount)
  }

  /**
   * Gets the maximum amount of energy that can be stored in the battery in RF.
   *
   * @return The amount of energy the battery currently contains in RF.
   */
  def getMaxRF(): Int = {
    maxEnergy.getRF
  }


}
