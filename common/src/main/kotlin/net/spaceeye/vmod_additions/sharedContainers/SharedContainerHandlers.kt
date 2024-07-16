package net.spaceeye.vmod_additions.sharedContainers

import net.spaceeye.vmod_additions.PlatformUtils

object ItemPipeStacksHandler: CommonSharedContainerHandler<SingleItemContainer>({ SingleItemContainer() })
object FluidPipeFluidTankHandler: CommonSharedContainerHandler<CommonFluidTank>({PlatformUtils.getFluidTank()})
object EnergyPipeEnergyTankHandler: CommonSharedContainerHandler<CommonEnergyTank>({PlatformUtils.getEnergyTank()})