package darkevilmac.functionality.common.energy

import darkevilmac.functionality.common.lib.Values

/**
 * Created by Darkevilmac on 8/25/2014.
 */
class QuickMultiEnergy(var rawEnergy: Double) {

  def addEU(amount: Double): QuickMultiEnergy = {
    rawEnergy = rawEnergy + (amount * Values.euConversion)

    this
  }

  def useEU(amount: Double): QuickMultiEnergy = {
    var rawEnergyClone: Double = rawEnergy

    while (rawEnergyClone >= Values.rfConversion) {
      rawEnergyClone = rawEnergyClone - Values.euConversion
    }

    this
  }

  def setEU(amount: Double): QuickMultiEnergy = {
    rawEnergy = Values.euConversion * amount

    this
  }

  def getEU(): Double = {
    var returnDouble: Double = 0
    var rawEnergyClone = rawEnergy

    while (rawEnergyClone >= Values.euConversion) {
      returnDouble = returnDouble + 1
      rawEnergyClone = rawEnergy - Values.euConversion
    }

    returnDouble
  }

  def addRF(amount: Int): QuickMultiEnergy = {
    rawEnergy = rawEnergy + (amount * Values.rfConversion)

    this
  }

  def useRF(amount: Int): QuickMultiEnergy = {
    var rawEnergyClone: Int = rawEnergy.asInstanceOf[Int]

    while (rawEnergyClone >= Values.rfConversion) {
      rawEnergyClone = rawEnergyClone - Values.rfConversion
    }

    this
  }

  def setRF(amount: Int): QuickMultiEnergy = {
    rawEnergy = Values.rfConversion * amount

    this
  }

  def getRF(): Int = {
    var returnInt: Int = 0
    var rawEnergyClone = rawEnergy

    while (rawEnergyClone >= Values.rfConversion) {
      returnInt = returnInt + 1
      rawEnergyClone = rawEnergy - Values.rfConversion
    }

    returnInt
  }

}
