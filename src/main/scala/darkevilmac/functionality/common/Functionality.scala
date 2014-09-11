package darkevilmac.functionality.common

import cpw.mods.fml.common.event.{FMLInitializationEvent, FMLPostInitializationEvent, FMLPreInitializationEvent}
import cpw.mods.fml.common.registry.GameRegistry
import cpw.mods.fml.common.{Loader, Mod}
import darkevilmac.functionality.client.sound.{PositionedSoundPos, Functionality3SoundManagerThread, Functionality3SoundManager}
import darkevilmac.functionality.common.block.BlockReceptorTest
import darkevilmac.functionality.common.configuration.ModConfiguration
import darkevilmac.functionality.common.lib.{Reference, Values}
import net.minecraft.client.Minecraft
import net.minecraft.util.ResourceLocation
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
    GameRegistry.registerBlock(new BlockReceptorTest, "BlockReceptorTest")
  }

  @Mod.EventHandler
  def onPostInit(e: FMLPostInitializationEvent) {
    if (Loader.isModLoaded("IC2")) {
      Values.ic2Loaded = true
    }
  }

}
