package darkevilmac.functionality.common.util

import net.minecraft.world.World

class SafeTimeTracker {

  private var lastMark: Long = Long.MinValue
  private var duration: Long = -1
  private var randomRange: Long = 0
  private var lastRandomDelay: Long = 0
  private var internalDelay: Long = 1

  def this(delay: Long) {
    this()
    internalDelay = delay
  }

  def this(delay: Long, random: Long) {
    this()
    internalDelay = delay
    randomRange = random
  }

  def markTimeIfDelay(world: World): Boolean = markTimeIfDelay(world, internalDelay)

  def markTimeIfDelay(world: World, delay: Long): Boolean = {
    if (world == null) {
      return false
    }
    val currentTime = world.getTotalWorldTime
    if (currentTime < lastMark) {
      lastMark = currentTime
      false
    } else if (lastMark + delay + lastRandomDelay <= currentTime) {
      duration = currentTime - lastMark
      lastMark = currentTime
      lastRandomDelay = (Math.random() * randomRange).toInt
      true
    } else {
      false
    }
  }

  def durationOfLastDelay(): Long = if (duration > 0) duration else 0

  def markTime(world: World) {
    lastMark = world.getTotalWorldTime
  }
}
