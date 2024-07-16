package net.spaceeye.vmod_additions

//TODO make it actually work
object VAConfig {
    val SERVER = Server()

    class Server() {
        val PIPES = Pipes()

        class Pipes() {
            val MAX_DIST = MaxDist()
            val BUFFER_SIZE = BufferSize()

            class MaxDist {
                val ITEM_PIPE = 7.0
                val FLUID_PIPE = 7.0
                val ENERGY_PIPE = 7.0
            }

            class BufferSize {
                val FLUID_PIPE = 1
                val ENERGY_PIPE = 1000
            }
        }
    }
}