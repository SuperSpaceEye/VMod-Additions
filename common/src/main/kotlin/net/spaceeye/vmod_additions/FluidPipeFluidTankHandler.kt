package net.spaceeye.vmod_additions

import net.spaceeye.vmod_additions.utils.CommonFluidTank
import kotlin.math.max


object FluidPipeFluidTankHandler {
    private val data = mutableMapOf<Int, CommonFluidTank>()

    private var counter = -1

    private val defaultTank = PlatformUtils.getFluidTank().withCapacity(0L)

    fun createTank(update: () -> Boolean): Int {
        counter++

        val tank = PlatformUtils.getFluidTank()
        data.getOrPut(counter) {tank}
        tank.registerOnChange { update() }

        return counter
    }

    fun loadTank(id: Int, tank: CommonFluidTank) {
        data.getOrPut(id) {tank}
        counter = max(counter, id)
    }

    fun getTank(id: Int) = data[id] ?: defaultTank

    fun removeTank(id: Int) {
        data.remove(id)
    }
}