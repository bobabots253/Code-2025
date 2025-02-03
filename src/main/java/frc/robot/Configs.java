package frc.robot;

import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.config.ClosedLoopConfig.FeedbackSensor;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;

import frc.robot.Constants.ElevatorConstants;
import frc.robot.Constants.EndEffectorConstants;
import frc.robot.Constants.ModuleConstants;

public final class Configs {
    public static final class MAXSwerveModule {
        public static final SparkMaxConfig drivingConfig = new SparkMaxConfig();
        public static final SparkMaxConfig turningConfig = new SparkMaxConfig();

        static {
            // Use module constants to calculate conversion factors and feed forward gain.
            //DO NOT TOUCH THESE CONSTANTS UNLESS YOU KNOW WHAT YOU ARE DOING
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
        public static final SparkMaxConfig masterLiftingConfig = new SparkMaxConfig(); //Left (relative to swerve)
        public static final SparkMaxConfig slaveLiftingConfig = new SparkMaxConfig(); ////Right (relative to swerve)

        static{
        masterLiftingConfig
                    .idleMode(IdleMode.kBrake)
                    .smartCurrentLimit(ElevatorConstants.kUniversalSoftLimit);
        masterLiftingConfig.closedLoop
                    .feedbackSensor(FeedbackSensor.kPrimaryEncoder)
                    .pid(ElevatorConstants.kIncrementalPostionP,
                         ElevatorConstants.kIncrementalPostionI,
                         ElevatorConstants.kIncrementalPositionD)
                    .outputRange(ElevatorConstants.kUniversalPIDOutputLow, ElevatorConstants.kUniversalPIDOutputHigh);

        slaveLiftingConfig.follow(ElevatorConstants.masterLiftingCANId);
        slaveLiftingConfig
                    .idleMode(IdleMode.kBrake)
                    .smartCurrentLimit(ElevatorConstants.kUniversalSoftLimit);
        }
    }
    public static final class EndEffectorSubsystem {
        public static final SparkMaxConfig pivotConfig = new SparkMaxConfig();
        public static final SparkMaxConfig intakeRollerConfig = new SparkMaxConfig();
        public static final SparkMaxConfig algaeRollerConfig = new SparkMaxConfig();

        static{
        pivotConfig
                    .idleMode(IdleMode.kBrake)
                    .smartCurrentLimit(EndEffectorConstants.kUniversalSoftLimit);
        pivotConfig.closedLoop
                    .feedbackSensor(FeedbackSensor.kAbsoluteEncoder)
                    .pid(EndEffectorConstants.kPivotAbsolutePositionP,
                         EndEffectorConstants.kPivotAbsolutePositionI,
                         EndEffectorConstants.kPivotAbsolutePositionD)
                    .outputRange(EndEffectorConstants.kUniversalPIDOutputLow, EndEffectorConstants.kUniversalPIDOutputHigh)
                    .positionWrappingEnabled(false);
        pivotConfig.absoluteEncoder
                    .inverted(false);

        intakeRollerConfig
                    .idleMode(IdleMode.kBrake)
                    .smartCurrentLimit(EndEffectorConstants.kUniversalSoftLimit);
        intakeRollerConfig.closedLoop
                    .feedbackSensor(FeedbackSensor.kPrimaryEncoder)
                    .pid(EndEffectorConstants.kIntakeVelocityP, 
                         EndEffectorConstants.kIntakeVelocityI, 
                         EndEffectorConstants.kIntakeVelocityD)
                    .outputRange(EndEffectorConstants.kUniversalPIDOutputLow, EndEffectorConstants.kUniversalPIDOutputHigh);
        
        algaeRollerConfig
                    .idleMode(IdleMode.kBrake)
                    .smartCurrentLimit(EndEffectorConstants.kUniversalSoftLimit);
        algaeRollerConfig.closedLoop
                    .feedbackSensor(FeedbackSensor.kPrimaryEncoder)
                    .pid(EndEffectorConstants.kAlgaeVelocityP, 
                         EndEffectorConstants.kAlgaeVelocityI, 
                         EndEffectorConstants.kAlgaeVelocityD)
                    .outputRange(EndEffectorConstants.kUniversalPIDOutputLow, EndEffectorConstants.kUniversalPIDOutputHigh);
        }
    }
//     public static final class TestSubsystem {
//         public static final SparkMaxConfig TestConfig = new SparkMaxConfig();
//         public static final SparkMaxConfig TestConfig2 = new SparkMaxConfig();

//         static{
//         TestConfig.follow(Constants.TestSubsystemConstants.rightArmMotorID);
//         TestConfig
//                     .idleMode(IdleMode.kBrake)
//                     .smartCurrentLimit(45);
//         TestConfig.closedLoop
//                     .feedbackSensor(FeedbackSensor.kPrimaryEncoder)
//                     .pid(1.2, 0, 0.75)
//                     .outputRange(-1, 1);
//         TestConfig2
//                     .idleMode(IdleMode.kBrake)
//                     .smartCurrentLimit(25);
//         TestConfig2.absoluteEncoder
//                     // Invert the turning encoder, since the output shaft rotates in the opposite
//                     // direction of the steering motor in the MAXSwerve Module.
//                     .inverted(false);
//         TestConfig2.closedLoop
//                     .feedbackSensor(FeedbackSensor.kAbsoluteEncoder)
//                     // These are example gains you may need to them for your own robot!
//                     .pid(1, 0, .5)
//                     .outputRange(-1, 1)
//                     .positionWrappingEnabled(false);
//         }
//     }

}