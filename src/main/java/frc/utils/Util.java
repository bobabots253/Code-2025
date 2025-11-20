package frc.utils;

public class Util {

    /**
     * Returns value if greater than deadband and 0 if not above threshold
     * @param val
     * @param deadband
     * @return Afflicted Deadband Value
     */
    public static double deadBand(double val, double deadband) {
		return (Math.abs(val) > Math.abs(deadband)) ? val : 0.0;
	}

}