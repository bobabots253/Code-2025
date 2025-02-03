package frc.robot;

// Class to refer to all arm and hook states
public final class States {
    public enum ElevatorPos {
        STOW(0), L1Score(1), L2Score(2), L3Score (3), NOTHING(99); //Add All Pos Later
        public int val;
        private ElevatorPos(int val) {
            this.val = val;
        }
    }

}
