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
import edu.wpi.first.wpilibj.motorcontrol.Spark;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.States;
import frc.robot.Constants.ClimbConstants;
import frc.robot.Configs;
import frc.utils.Util;


public class ClimbSubsystem extends SubsystemBase {
    private static final SparkMax masterClimbSparkMax = new SparkMax(ClimbConstants.slaveClimbCanID, MotorType.kBrushless);
    private static final SparkMax followerClimbSparkMax = new SparkMax(ClimbConstants.masterClimbCanID, MotorType.kBrushless); //ID,MotorType
    //private final RelativeEncoder relativeEncoder;
    private final AbsoluteEncoder m_climbAbsoluteEncoder;
    /* READ ME:
    * Creates a PID Controller which we use to control the motors movement
    */
    //private SparkClosedLoopController pidController;

    private static ClimbSubsystem instance;
    public static ClimbSubsystem getInstance() {
        if(instance == null) instance = new ClimbSubsystem();
        return instance;


    }

    private ClimbSubsystem() {
        //absoluteEncoder = masterClimbSparkMax.getAbsoluteEncoder();
        //pidController = masterClimbSparkMax.getClosedLoopController();
        m_climbAbsoluteEncoder = masterClimbSparkMax.getAbsoluteEncoder();
        masterClimbSparkMax.configure(Configs.ClimbSubsystem.climbMasterConfig, ResetMode.kResetSafeParameters,
        PersistMode.kPersistParameters);
       followerClimbSparkMax.configure(Configs.ClimbSubsystem.climbFollowerCoastConfig, ResetMode.kResetSafeParameters,
        PersistMode.kPersistParameters);
        register(); //Register Subsystem for Command Scheduluer to call in periodic
    }

    /**
     * Runs motors at a value [-1 to 1]. Log current value on SmartDashboard
     */
    public void setLazyOpenLoop(double OpenLoopValue) {
        if (OpenLoopValue != 0){
        masterClimbSparkMax.configure(Configs.ClimbSubsystem.climbMasterConfig, ResetMode.kResetSafeParameters,
        PersistMode.kPersistParameters);
       followerClimbSparkMax.configure(Configs.ClimbSubsystem.climbFollowerCoastConfig, ResetMode.kResetSafeParameters,
        PersistMode.kPersistParameters);
        masterClimbSparkMax.set(OpenLoopValue);
        followerClimbSparkMax.set(-OpenLoopValue);
        } else{
        followerClimbSparkMax.configure(Configs.ClimbSubsystem.climbFollowerConfig, ResetMode.kResetSafeParameters,
        PersistMode.kPersistParameters);
        }
        SmartDashboard.putNumber("Climb /setOpenLoop", OpenLoopValue);
    }

    public void setConditionalBrake(boolean setConditionalBrake){
        followerClimbSparkMax.configure(Configs.ClimbSubsystem.climbFollowerConfig, ResetMode.kResetSafeParameters,
        PersistMode.kPersistParameters);
    }
    /**
     * Runs motors at a value 0 (stop).
     */
    public void stop() {
        setLazyOpenLoop(0);
    }

    @Override
    public void periodic() {
        //SmartDashboard.putNumber("Relative Encoder value [Rots]", absoluteEncoder.getPosition());
        SmartDashboard.putNumber("Climb /masterClimbOutputCurrent", masterClimbSparkMax.getOutputCurrent());
        SmartDashboard.putNumber("Climb /slaveClimbOutputCurrent", followerClimbSparkMax.getOutputCurrent());
        SmartDashboard.putNumber("Climb /absoluteEncoder", m_climbAbsoluteEncoder.getPosition());
    }
}