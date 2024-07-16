package net.spaceeye.vmod_additions

//TODO
object VAConfig {
    val SERVER = Server()

    class Server() {
        val PIPES = Pipes()

        class Pipes() {
            val ITEM_PIPE_MAX_DIST = 7.0
            val FLUID_PIPE_MAX_DIST = 7.0
            val ENERGY_PIPE_MAX_DIST = 7.0
        }
    }
}