package frc.robot;

// Class to refer to all arm and hook states
public final class States {
    public enum ElevatorPos {
        STOW(0), L1Score(1), L2Score(2), L3SCORE (3), NOTHING(99); //Add All Pos Later
        public int val;
        private ElevatorPos(int val) {
            this.val = val;
        }
    }

    public enum EndEffectorPos {
        STOW(0), L1Score(1), L2Score(2), L3Score (3), INTAKE(4), PUSH(5), FLY_BIRDY_FLY(6), HARD_REMOVE(7), SMART_INTAKE(8), SOFT_REMOVE(9), NOTHING(99), ; //Add All Pos Later
        public int val;
        private EndEffectorPos(int val) {
            this.val = val;
        }
    }

}
