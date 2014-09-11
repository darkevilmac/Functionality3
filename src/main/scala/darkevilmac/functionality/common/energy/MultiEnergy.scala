package darkevilmac.functionality.common.energy

import darkevilmac.functionality.common.lib.Values

/**
 * Allows quick conversion between RF EU and MJ
 */
class MultiEnergy(var rawEnergy: Double) {

  def addEU(amount: Double) {
    rawEnergy = rawEnergy + (amount * Values.euConversion)

    if (rawEnergy < 0) {
      rawEnergy = 0
    }
  }

  def useEU(amount: Double) {
    var rawEnergyClone: Double = rawEnergy

    while (rawEnergyClone >= Values.euConversion) {
      rawEnergyClone = rawEnergyClone - Values.euConversion
    }

    rawEnergy = rawEnergyClone

    if (rawEnergy < 0) {
      rawEnergy = 0
    }
  }

  def getEU(): Double = {
    if (rawEnergy < 0) {
      rawEnergy = 0
    }
    return (rawEnergy / Values.euConversion).asInstanceOf[Int]
  }

  def setEU(amount: Double) {
    rawEnergy = Values.euConversion * amount
    if (rawEnergy < 0) {
      rawEnergy = 0
    }
  }

  def addRF(amount: Int) {
    rawEnergy = rawEnergy + (amount * Values.rfConversion)
    if (rawEnergy < 0) {
      rawEnergy = 0
    }
  }

  def useRF(amount: Int) {
    var rawEnergyClone: Double = rawEnergy

    while (rawEnergyClone >= Values.rfConversion) {
      rawEnergyClone = rawEnergyClone - Values.rfConversion
    }

    rawEnergy = rawEnergyClone

    if (rawEnergy < 0) {
      rawEnergy = 0
    }
  }

  def getRF(): Int = {
    if (rawEnergy < 0) {
      rawEnergy = 0
    }
    return (rawEnergy / Values.rfConversion).asInstanceOf[Int]
  }

  def setRF(amount: Int) {
    rawEnergy = Values.rfConversion * amount
    if (rawEnergy < 0) {
      rawEnergy = 0
    }
  }

}
