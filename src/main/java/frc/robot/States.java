package frc.robot;

// Class to refer to all arm and hook states
public final class States {
    public enum TestPos {
        STOW(0), POS1(1);
        public int val;
        private TestPos(int val) {
            this.val = val;
        }
    }

}
