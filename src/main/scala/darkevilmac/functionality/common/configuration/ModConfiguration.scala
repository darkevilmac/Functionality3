package darkevilmac.functionality.common.configuration

import cpw.mods.fml.common.event.FMLPreInitializationEvent
import darkevilmac.functionality.common.Functionality
import net.minecraftforge.common.config.Configuration

/**
 * Created by Darkevilmac on 7/24/2014.
 */
object ModConfiguration {

  def init(e: FMLPreInitializationEvent) {
    Functionality.config = new Configuration(e.getSuggestedConfigurationFile)
  }

}
