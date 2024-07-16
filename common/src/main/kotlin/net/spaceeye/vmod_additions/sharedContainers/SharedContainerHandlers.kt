package net.spaceeye.vmod_additions.sharedContainers

import net.spaceeye.vmod_additions.PlatformUtils
import net.spaceeye.vmod_additions.VAConfig

object ItemPipeStacksHandler: CommonSharedContainerHandler<SingleItemContainer>({SingleItemContainer()}, {1})
object FluidPipeFluidTankHandler: CommonSharedContainerHandler<CommonFluidTank>({PlatformUtils.getFluidTank()}, {VAConfig.SERVER.PIPES.BUFFER_SIZE.FLUID_PIPE.toLong()})
object EnergyPipeEnergyTankHandler: CommonSharedContainerHandler<CommonEnergyTank>({PlatformUtils.getEnergyTank()}, {VAConfig.SERVER.PIPES.BUFFER_SIZE.ENERGY_PIPE.toLong()})