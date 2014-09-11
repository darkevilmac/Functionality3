package darkevilmac.functionality.common.asm

import java.util

import codechicken.core.launch.DepLoader
import cpw.mods.fml.relauncher.IFMLLoadingPlugin

class LoadingHook extends IFMLLoadingPlugin {

  DepLoader.load

  override def getASMTransformerClass(): Array[String] = {
    null
  }

  override def injectData(data: util.Map[String, AnyRef]) {}

  override def getModContainerClass(): String = {
    null
  }

  override def getAccessTransformerClass(): String = {
    null
  }

  override def getSetupClass(): String = {
    null
  }
}
