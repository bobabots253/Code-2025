package frc.robot.subsystems.endeffector;

import com.revrobotics.spark.config.ClosedLoopConfig.FeedbackSensor;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.config.SparkMaxConfig;

public class EndeffectorConfigs {
    public static final SparkMaxConfig pivotConfig = new SparkMaxConfig();
    public static final SparkMaxConfig intakeRollerConfig = new SparkMaxConfig();
    public static final SparkMaxConfig algaeRollerConfig = new SparkMaxConfig();
    public static final SparkMaxConfig pivotCoastModeConfig = new SparkMaxConfig();

    static{
    pivotConfig
                .idleMode(IdleMode.kBrake)
                .smartCurrentLimit(EndeffectorConstants.kUniversalSoftLimit);
    pivotConfig.closedLoop
                .feedbackSensor(FeedbackSensor.kAbsoluteEncoder)
                .pid(EndeffectorConstants.kPivotAbsolutePositionP,
                    EndeffectorConstants.kPivotAbsolutePositionI,
                    EndeffectorConstants.kPivotAbsolutePositionD)
                .outputRange(EndeffectorConstants.kUniversalPIDOutputLow, EndeffectorConstants.kUniversalPIDOutputHigh)
                .positionWrappingEnabled(true);
    pivotConfig.absoluteEncoder
                .inverted(false);
    pivotCoastModeConfig
                .idleMode(IdleMode.kCoast)
                .smartCurrentLimit(EndeffectorConstants.kUniversalSoftLimit);
    pivotCoastModeConfig.closedLoop
                .feedbackSensor(FeedbackSensor.kAbsoluteEncoder)
                .pid(EndeffectorConstants.kPivotAbsolutePositionP,
                EndeffectorConstants.kPivotAbsolutePositionI,
                EndeffectorConstants.kPivotAbsolutePositionD)
                .outputRange(EndeffectorConstants.kUniversalPIDOutputLow, EndeffectorConstants.kUniversalPIDOutputHigh)
                .positionWrappingEnabled(false);
    pivotCoastModeConfig.absoluteEncoder
                .inverted(false);
    intakeRollerConfig
                .idleMode(IdleMode.kBrake)
                .smartCurrentLimit(EndeffectorConstants.kUniversalSoftLimit);
    intakeRollerConfig.closedLoop
                .feedbackSensor(FeedbackSensor.kPrimaryEncoder)
                .pid(EndeffectorConstants.kIntakeVelocityP, 
                EndeffectorConstants.kIntakeVelocityI, 
                     EndeffectorConstants.kIntakeVelocityD)
                .outputRange(EndeffectorConstants.kUniversalPIDOutputLow, EndeffectorConstants.kUniversalPIDOutputHigh);
    }   
 }