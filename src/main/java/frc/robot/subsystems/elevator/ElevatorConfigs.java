package frc.robot.subsystems.elevator;

import com.revrobotics.spark.config.ClosedLoopConfig.FeedbackSensor;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.config.SparkMaxConfig;

public class ElevatorConfigs {
    public static final SparkMaxConfig masterLiftingConfig = new SparkMaxConfig(); //Left (relative to swerve)
    public static final SparkMaxConfig slaveLiftingConfig = new SparkMaxConfig(); ////Right (relative to swerve)
    //public static final SparkBaseConfig baseMasterLiftingConfig = new SparkMaxConfig();
    public static final SparkMaxConfig masterLiftingCoastModeConfig = new SparkMaxConfig();
    public static final SparkMaxConfig slaveLiftingCoastModeConfig = new SparkMaxConfig();
    static{
    masterLiftingConfig
                .inverted(false)
                .idleMode(IdleMode.kBrake)
                .smartCurrentLimit(ElevatorConstants.kUniversalSoftLimit);
    masterLiftingConfig
            .closedLoop
                .feedbackSensor(FeedbackSensor.kPrimaryEncoder)
                .pid(ElevatorConstants.kIncrementalPostionP,
                     ElevatorConstants.kIncrementalPostionI,
                     ElevatorConstants.kIncrementalPositionD)
                .outputRange(ElevatorConstants.kUniversalPIDOutputLow, ElevatorConstants.kUniversalPIDOutputHigh)
                .velocityFF(0) //1/ElevatorConstants.kV
            .maxMotion
                .maxVelocity(1100) //rpm
                .maxAcceleration(2000) //rpm
                .allowedClosedLoopError(0.012);
    slaveLiftingConfig
                .follow(ElevatorConstants.masterLiftingCANId, true)
                .idleMode(IdleMode.kBrake)
                .smartCurrentLimit(ElevatorConstants.kUniversalSoftLimit);
    masterLiftingCoastModeConfig
                .idleMode(IdleMode.kCoast)
                .smartCurrentLimit(ElevatorConstants.kUniversalSoftLimit);
    masterLiftingCoastModeConfig.closedLoop
                .feedbackSensor(FeedbackSensor.kPrimaryEncoder)
                .pid(ElevatorConstants.kIncrementalPostionP,
                     ElevatorConstants.kIncrementalPostionI,
                     ElevatorConstants.kIncrementalPositionD)
                .outputRange(ElevatorConstants.kUniversalPIDOutputLow, ElevatorConstants.kUniversalPIDOutputHigh);
    slaveLiftingCoastModeConfig.follow(ElevatorConstants.masterLiftingCANId);
    slaveLiftingCoastModeConfig
                .inverted(true)
                .idleMode(IdleMode.kCoast)
                .smartCurrentLimit(ElevatorConstants.kUniversalSoftLimit);
    }
}

