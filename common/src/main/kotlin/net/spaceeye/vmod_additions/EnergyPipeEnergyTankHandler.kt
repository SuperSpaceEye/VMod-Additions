package net.spaceeye.vmod_additions

import net.spaceeye.vmod_additions.utils.CommonEnergyTank
import kotlin.math.max

object EnergyPipeEnergyTankHandler {
    private val data = mutableMapOf<Int, CommonEnergyTank>()

    private var counter = -1

    private val defaultTank = PlatformUtils.getEnergyTank().withCapacity(0L)

    fun createTank(update: () -> Boolean): Int {
        counter++

        val tank = PlatformUtils.getEnergyTank()
        data.getOrPut(counter) {tank}
        tank.registerOnChange { update() }

        return counter
    }

    fun loadTank(id: Int, tank: CommonEnergyTank) {
        data.getOrPut(id) {tank}
        counter = max(counter, id)
    }

    fun getTank(id: Int) = data[id] ?: defaultTank

    fun removeTank(id: Int) {
        data.remove(id)
    }
}