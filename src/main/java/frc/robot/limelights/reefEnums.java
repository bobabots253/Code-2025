package frc.robot.limelights;

public final class reefEnums {
    public enum branch {
        A(1), B(2);
        public int val;
        private branch(int val) {
            this.val = val;
        }
    }
    public enum side {
        LEFT(1), RIGHT(2);
        public int val;
        private side(int val){
            this.val = val;
        }
    }
}