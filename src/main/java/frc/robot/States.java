package frc.robot;

// Class to refer to all arm and hook states
public final class States {
    public enum ElevatorPos {
        STOW(0), L1(1), L2(2), L3(3);
        public int level;
        private ElevatorPos(int val) {
            this.level = val;
        }
    }


}
