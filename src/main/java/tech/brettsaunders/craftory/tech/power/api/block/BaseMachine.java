package tech.brettsaunders.craftory.tech.power.api.block;

import dev.lone.itemsadder.api.FontImages.FontImageWrapper;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import org.bukkit.Location;
import org.bukkit.inventory.Inventory;
import tech.brettsaunders.craftory.tech.power.api.guiComponents.GBattery;
import tech.brettsaunders.craftory.tech.power.api.interfaces.IEnergyReceiver;

public abstract class BaseMachine extends PoweredBlock implements IEnergyReceiver, Externalizable {

  /* Static Constants Protected */

  /* Static Constants Private */
  private static final long serialVersionUID = 10007L;
  /* Per Object Variables Saved */
  protected int maxReceive;

  /* Per Object Variables Not-Saved */


  /* Construction */
  public BaseMachine(Location location, byte level, int maxReceive) {
    super(location, level);
    this.maxReceive = maxReceive;
    energyStorage.setMaxReceive(maxReceive);
    init();
  }

  /* Saving, Setup and Loading */
  public BaseMachine() {
    super();
    init();
  }

  /* Common Load and Construction */
  private void init() {
    isReceiver = true;
  }

  @Override
  public void writeExternal(ObjectOutput out) throws IOException {
    super.writeExternal(out);
    out.writeInt(maxReceive);
  }

  @Override
  public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
    super.readExternal(in);
    maxReceive = in.readInt();
  }

  /* IEnergyReceiver */
  @Override
  public int receiveEnergy(int maxReceive, boolean simulate) {
    return energyStorage.receiveEnergy(Math.min(maxReceive, this.maxReceive), simulate);
  }

  /* IEnergyHandler */
  @Override
  public int getEnergyStored() {
    return energyStorage.getEnergyStored();
  }

  @Override
  public int getMaxEnergyStored() {
    return energyStorage.getMaxEnergyStored();
  }

  @Override
  public int getEnergySpace() {
    return Math.max(energyStorage.getMaxEnergyStored() - energyStorage.getEnergyStored(), maxReceive);
  }

  /* IEnergyConnection */
  @Override
  public boolean canConnectEnergy() {
    return true;
  }

  /* External Methods */
  public int maxReceiveEnergy() {
    return maxReceive;
  }

  @Override
  public void setupGUI() {
    Inventory inventory = setInterfaceTitle("Machine", new FontImageWrapper("extra:cell"));
    addGUIComponent(new GBattery(inventory, energyStorage));
  }
}
