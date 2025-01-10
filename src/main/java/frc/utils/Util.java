package frc.utils;


import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.ClosedLoopConfig.FeedbackSensor;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.SparkFlex;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.SparkClosedLoopController;

import edu.wpi.first.wpilibj.I2C.Port;
import frc.robot.Constants;

public class Util {

    /**
     * Create a CANSparkMax with current limiting enabled
     * 
     * @param id the ID of the Spark MAX
     * @param motortype the type of motor the Spark MAX is connected to 
     * @param stallLimit the current limit to set at stall
     * 
     * @return a fully configured CANSparkMAX
     */
    public static SparkMax createSparkMAX(int id, MotorType motortype, int stallLimit, boolean inverted) {
        SparkMax sparkMAX = new SparkMax(id, motortype);
        SparkMaxConfig config = new SparkMaxConfig();
        config
            .inverted(inverted)
            .idleMode(IdleMode.kBrake);
        sparkMAX.configure(config, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
        return sparkMAX;
    }

    private static int sparkMAXDefaultCurrentLimit = 40;
    //40 amps - v1.1. | 20-30 amps - 550

    /**
     * Create a CANSparkMax with default current limiting enabled
     * 
     * @param id the ID of the Spark MAX
     * @param motortype the type of motor the Spark MAX is connected to
     * 
     * @return a fully configured CANSparkMAX
     */
    public static SparkMax createSparkMAX(int id, MotorType motortype, boolean inverted) {
        return createSparkMAX(id, motortype, sparkMAXDefaultCurrentLimit, inverted);
    }

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