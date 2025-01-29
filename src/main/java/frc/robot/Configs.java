package frc.robot;

import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.config.ClosedLoopConfig.FeedbackSensor;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;

import frc.robot.Constants.ModuleConstants;

public final class Configs {
    public static final class MAXSwerveModule {
        public static final SparkMaxConfig drivingConfig = new SparkMaxConfig();
        public static final SparkMaxConfig turningConfig = new SparkMaxConfig();

        static {
            // Use module constants to calculate conversion factors and feed forward gain.
            double drivingFactor = ModuleConstants.kWheelDiameterMeters * Math.PI
                    / ModuleConstants.kDrivingMotorReduction;
            double turningFactor = 2 * Math.PI;
            double drivingVelocityFeedForward = 1 / ModuleConstants.kDriveWheelFreeSpeedRps;

            drivingConfig
                    .idleMode(IdleMode.kBrake)
                    .smartCurrentLimit(45);
            drivingConfig.encoder
                    .positionConversionFactor(drivingFactor) // meters
                    .velocityConversionFactor(drivingFactor / 60.0); // meters per second
            drivingConfig.closedLoop
                    .feedbackSensor(FeedbackSensor.kPrimaryEncoder)
                    .pid(0.04, 0, 0)
                    .velocityFF(drivingVelocityFeedForward)
                    .outputRange(-1, 1);

            turningConfig
                    .idleMode(IdleMode.kBrake)
                    .smartCurrentLimit(25);
            turningConfig.absoluteEncoder
                    // Invert the turning encoder, since the output shaft rotates in the opposite
                    // direction of the steering motor in the MAXSwerve Module.
                    .inverted(true)
                    .positionConversionFactor(turningFactor) // radians
                    .velocityConversionFactor(turningFactor / 60.0); // radians per second
            turningConfig.closedLoop
                    .feedbackSensor(FeedbackSensor.kAbsoluteEncoder)
                    // These are example gains you may need to them for your own robot!
                    .pid(1, 0, 0)
                    .outputRange(-1, 1)
                    .positionWrappingEnabled(true)
                    .positionWrappingInputRange(0, turningFactor);
        }
    }

    public static final class ElevatorSubsystem {
        public static final SparkMaxConfig motorRConfig = new SparkMaxConfig();
        public static final SparkMaxConfig motorLConfig = new SparkMaxConfig();
        
        static{
                motorLConfig.follow(Constants.ElevatorConstants.rightMotorID);
                motorLConfig
                                .idleMode(IdleMode.kBrake)
                                .smartCurrentLimit(45);
                motorLConfig.closedLoop
                                .feedbackSensor(FeedbackSensor.kPrimaryEncoder)
                                .pid(0, 0, 0)
                                .outputRange(-1, 1);
                motorRConfig
                                .idleMode(IdleMode.kBrake)
                                .smartCurrentLimit(45);
                motorRConfig.closedLoop
                                .feedbackSensor(FeedbackSensor.kPrimaryEncoder)
                                .pid(0, 0, 0)
                                .outputRange(-1, 1);
        }
        
    }

    public static final class TestSubsystem {
        public static final SparkMaxConfig TestConfig = new SparkMaxConfig();
        public static final SparkMaxConfig TestConfig2 = new SparkMaxConfig();

        static{
        TestConfig.follow(Constants.TestSubsystemConstants.rightArmMotorID);
        TestConfig
                    .idleMode(IdleMode.kBrake)
                    .smartCurrentLimit(45);
        TestConfig.closedLoop
                    .feedbackSensor(FeedbackSensor.kPrimaryEncoder)
                    .pid(1.2, 0, 0.75)
                    .outputRange(-1, 1);
        TestConfig2
                    .idleMode(IdleMode.kBrake)
                    .smartCurrentLimit(25);
        TestConfig2.absoluteEncoder
                    // Invert the turning encoder, since the output shaft rotates in the opposite
                    // direction of the steering motor in the MAXSwerve Module.
                    .inverted(false);
        TestConfig2.closedLoop
                    .feedbackSensor(FeedbackSensor.kAbsoluteEncoder)
                    // These are example gains you may need to them for your own robot!
                    .pid(1, 0, .5)
                    .outputRange(-1, 1)
                    .positionWrappingEnabled(false);
        }
    }

}