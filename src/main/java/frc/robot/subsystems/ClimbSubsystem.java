package frc.robot.subsystems;

import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.ClosedLoopConfig.FeedbackSensor;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.SparkFlex;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.AbsoluteEncoder;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.spark.SparkBase.ControlType;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.SparkClosedLoopController;
import java.text.DecimalFormat;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj.motorcontrol.Spark;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.States;
import frc.robot.Constants.ClimbConstants;
import frc.robot.Configs;
import frc.utils.Util;


public class ClimbSubsystem extends SubsystemBase {
    public static SparkMax masterClimbSparkMax;
    public static SparkMax followerClimbSparkMax; 
    private final RelativeEncoder m_masterEncoder;
    private final RelativeEncoder m_followerEncoder;

    /* READ ME:
    * Creates a PID Controller which we use to control the motors movement
    */
    //private SparkClosedLoopController pidController;

    private static class ClimbSubsystemHandler {
        private static final ClimbSubsystem instance = new ClimbSubsystem();
    }

    public static ClimbSubsystem getInstance() {
        return ClimbSubsystemHandler.instance;
    }

    // private static ClimbSubsystem instance;
    // public static ClimbSubsystem getInstance() {
    //     if(instance == null) instance = new ClimbSubsystem();
    //     return instance;
    // }

    private ClimbSubsystem() {
        masterClimbSparkMax = new SparkMax(ClimbConstants.masterClimbCanID, MotorType.kBrushless);
        followerClimbSparkMax = new SparkMax(ClimbConstants.slaveClimbCanID, MotorType.kBrushless);
        m_masterEncoder = masterClimbSparkMax.getEncoder();
        m_followerEncoder = followerClimbSparkMax.getEncoder();
        //absoluteEncoder = masterClimbSparkMax.getAbsoluteEncoder();
        //pidController = masterClimbSparkMax.getClosedLoopController();
        masterClimbSparkMax.configure(Configs.ClimbSubsystem.climbMasterConfig, ResetMode.kResetSafeParameters,
        PersistMode.kPersistParameters);
       followerClimbSparkMax.configure(Configs.ClimbSubsystem.climbFollowerCoastConfig, ResetMode.kResetSafeParameters,
        PersistMode.kPersistParameters);

        SmartDashboard.putNumber("Climb /masterClimbOutputCurrent", masterClimbSparkMax.getOutputCurrent());
        SmartDashboard.putNumber("Climb /slaveClimbOutputCurrent", followerClimbSparkMax.getOutputCurrent());
        SmartDashboard.putNumber("Climb /m_masterEncoder", m_masterEncoder.getPosition());
        SmartDashboard.putNumber("Climb /m_followerEncoder", m_followerEncoder.getPosition());
    }

    /**
     * Runs motors at a value [-1 to 1]. Log current value on SmartDashboard
     */
    public void setLazyOpenLoop(double OpenLoopValue) {
        if (OpenLoopValue != 0){
        masterClimbSparkMax.configure(Configs.ClimbSubsystem.climbMasterConfig, ResetMode.kResetSafeParameters,
        PersistMode.kNoPersistParameters);
       followerClimbSparkMax.configure(Configs.ClimbSubsystem.climbFollowerCoastConfig, ResetMode.kResetSafeParameters,
        PersistMode.kNoPersistParameters);
        masterClimbSparkMax.set(OpenLoopValue);
        followerClimbSparkMax.set(-OpenLoopValue);
        } else{
        followerClimbSparkMax.configure(Configs.ClimbSubsystem.climbFollowerBrakeConfig, ResetMode.kNoResetSafeParameters,
        PersistMode.kNoPersistParameters);
        masterClimbSparkMax.set(0);
        followerClimbSparkMax.set(0);
        }
        //SmartDashboard.putNumber("Climb /setOpenLoop", OpenLoopValue);
    }

    public void setHorizonPosition(){
        var currentRevs = m_masterEncoder.getPosition();
        double arbRevHorizonSetpoint = 0.0; // tune
        if (!MathUtil.isNear(arbRevHorizonSetpoint, currentRevs, 0.1)){
            if (currentRevs < arbRevHorizonSetpoint){
                setLazyOpenLoop(-1.0);
            } else if (currentRevs > arbRevHorizonSetpoint){
                setLazyOpenLoop(1.0);
            }
        }
    }

    
    public void setPullUpPosition(){
        var currentRevs = m_masterEncoder.getPosition();
        double arbRevPullUpSetpoint = 0.0; // tune
        if (!MathUtil.isNear(arbRevPullUpSetpoint, currentRevs, 0.1)){
            if (currentRevs < arbRevPullUpSetpoint){
                setLazyOpenLoop(-1.0);
            } else if (currentRevs > arbRevPullUpSetpoint){
                setLazyOpenLoop(1.0);
            }
        }
    }

    // public void setConditionalBrake(boolean setConditionalBrake){
    //     followerClimbSparkMax.configure(Configs.ClimbSubsystem.climbFollowerConfig, ResetMode.kResetSafeParameters,
    //     PersistMode.kPersistParameters);
    // }
    /**
     * Runs motors at a value 0 (stop).
     */
    public void stop() {
        setLazyOpenLoop(0);
    }

    @Override
    public void periodic() {
        //SmartDashboard.putNumber("Relative Encoder value [Rots]", absoluteEncoder.getPosition())

    }
}