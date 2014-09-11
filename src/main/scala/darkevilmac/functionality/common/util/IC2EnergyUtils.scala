package darkevilmac.functionality.common.util

import darkevilmac.functionality.common.energy.MultiEnergy

/**
 * Created by Darkevilmac on 8/22/2014.
 */
object IC2EnergyUtils {

  def getIC2TierFromEnergyRate(energyRate: MultiEnergy): Int = {
    var returnVal: Int = 0
    if (energyRate.getEU >= 33) {
      returnVal = returnVal + 1
    }
    if (energyRate.getEU >= 512) {
      returnVal = returnVal + 1
    }
    if (energyRate.getEU >= 2048) {
      returnVal = returnVal + 1
    }
    if (energyRate.getEU >= 8192) {
      returnVal = returnVal + 1
    }
    return returnVal
  }

}
