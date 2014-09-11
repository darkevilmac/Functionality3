package darkevilmac.functionality.common

import cpw.mods.fml.common.event.{FMLInitializationEvent, FMLPostInitializationEvent, FMLPreInitializationEvent}
import cpw.mods.fml.common.{Loader, Mod}
import darkevilmac.functionality.common.configuration.ModConfiguration
import darkevilmac.functionality.common.lib.{Reference, Values}
import net.minecraftforge.common.config.Configuration

@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, version = Reference.MOD_VERSION, modLanguage = "scala")
object Functionality {

  var config: Configuration = null

  @Mod.EventHandler
  def onPreInit(e: FMLPreInitializationEvent) {
    ModConfiguration.init(e)
  }

  @Mod.EventHandler
  def onInit(e: FMLInitializationEvent) {
  }

  @Mod.EventHandler
  def onPostInit(e: FMLPostInitializationEvent) {
    if (Loader.isModLoaded("IC2")) {
      Values.ic2Loaded = true
    }
  }

}
