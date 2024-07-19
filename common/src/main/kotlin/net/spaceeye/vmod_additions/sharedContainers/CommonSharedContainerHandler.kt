package net.spaceeye.vmod_additions.sharedContainers

import net.spaceeye.vmod.utils.ServerClosable
import kotlin.math.max

abstract class CommonSharedContainerHandler<T: CommonContainer>(val containerConstructor: () -> T, val getBufferSize: () -> Long): ServerClosable() {
    private val data = mutableMapOf<Int, T>()
    private val defaultContainer = containerConstructor().withCapacity(0L)
    private var counter = -1

    fun createContainer(update: () -> Boolean): Int {
        counter++

        val container = containerConstructor().withCapacity(getBufferSize()) as T
        data.getOrPut(counter) {container}
        container.registerOnChange { update() }

        return counter
    }

    fun loadContainer(id: Int, container: T, update: () -> Boolean) {
        val container = data.getOrPut(id) {container}
        counter = max(counter, id)

        container.registerOnChange { update }
    }

    fun getContainer(id: Int): T = (data[id] ?: defaultContainer) as T

    fun removeContainer(id: Int) {
        data.remove(id)
    }

    override fun close() {
        data.clear()
        counter = -1
    }
}