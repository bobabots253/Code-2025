package frc.robot.subsystems.endeffector;

import com.revrobotics.spark.config.ClosedLoopConfig.FeedbackSensor;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.config.SparkMaxConfig;

public class EndeffectorConfigs {
    public static final SparkMaxConfig endEffectorConfig = new SparkMaxConfig();

    /*
     * After Sparkmaxs are created we apply a SparkMaxConfig to apply custom parameters.
     * Universally, we MUST set the type of <Idle Mode> and <Current Limit>.
     * idleMode: [kBrake], prevents free rotation of the motor when no command is being given to it.
     * smartCurrentLimit: custom [kUniversalSoftLimit] = 20 Amps. We apply this change to prevent too much power being requested by the motors.
     * This current limit is a soft limit because it is defined within code and the main current limit is the physical 30 Amp breaker on the robot.
     * In theory, the soft limit prevents the motor controller from requesting over the limit (20 Amps) from the PDH, preventing the 30 Amp breaker from ever tripping.
     * 
     * Not Used but Useful:
     * closedLoop: defines the parameters when using the internal <PID controller>. 
     * Closed Loop as a whole are finitely split between <Velocity Control> or <Position Control>
     * Remember: In order for a Closed Loop to run, we need a sensor to record <Error>, where:
     *  <Error> = Requested Position/Velocity (target, goal) - Current Position/Velocity
     * PID Controller parameters:
     * P = Proportional Hyperparameter, linear rate of change, where P * <Error> = requestedChange
     * I = Intergral Hyperparameter, prevent steady state error and thresholding, usually not reaching the target in time.
     * D = Derivative Hyperparameter, compensate for high rate of change, ususally overshooting the target.
     *  outputRange, prevents the motors from requesting to high of a motor output where 1.0 = 100%, and vice versa.
     * Reminder: PID values are heuristic and while safe parameters can be predicted at a high level of confidence,
     *  values should be started low (ie: 0.001)
     * Note: In this exercise, we are not using the PID controller for velocity because a basic output via the [set()] method is sufficient.
     */
    static{
    endEffectorConfig
                .idleMode(IdleMode.kBrake)
                .smartCurrentLimit(EndeffectorConstants.kUniversalSoftLimit);
    endEffectorConfig.closedLoop
                .feedbackSensor(FeedbackSensor.kPrimaryEncoder)
                .pid(EndeffectorConstants.kIntakeVelocityP, 
                EndeffectorConstants.kIntakeVelocityI, 
                     EndeffectorConstants.kIntakeVelocityD)
                .outputRange(EndeffectorConstants.kUniversalPIDOutputLow, EndeffectorConstants.kUniversalPIDOutputHigh);
    }   
 }