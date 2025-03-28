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
    private static final SparkMax masterClimbSparkMax = Util.createSparkMAX(ClimbConstants.masterClimbCanID, MotorType.kBrushless, false); //ID,MotorType
    private static final SparkMax followerClimbSparkMax = Util.createSparkMAX(ClimbConstants.followerClimbCanID, MotorType.kBrushless, false); //ID,MotorType
    //private final RelativeEncoder relativeEncoder;
    private final AbsoluteEncoder absoluteEncoder;
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
        resetEncoders();
        absoluteEncoder = masterClimbSparkMax.getAbsoluteEncoder();
        //pidController = masterClimbSparkMax.getClosedLoopController();
        masterClimbSparkMax.configure(Configs.ClimbSubsystem.climbConfig, ResetMode.kResetSafeParameters,
        PersistMode.kPersistParameters);
       followerClimbSparkMax.configure(Configs.ClimbSubsystem.climbFollowerConfig, ResetMode.kResetSafeParameters,
        PersistMode.kPersistParameters);
        register(); //Register Subsystem for Command Scheduluer to call in periodic
    }
    /**
     * Runs motors at a value [-1 to 1]. Log current value on SmartDashboard
     */
    public void setOpenLoop(double value) {
        masterClimbSparkMax.setVoltage(value);
        System.out.println(masterClimbSparkMax.getBusVoltage());
        SmartDashboard.putNumber("Climb Open-loop Value", value);
    }
    
    /**
     * Runs motors at a value 0 (stop).
     */
    public void stop() {
        setOpenLoop(0);
    }
    
    /**
     * Resets encoders to zero
     */
    public void resetEncoders() {
        //relativeEncoder.setPosition(0);
    }
    
    @Override
    public void periodic() {
        SmartDashboard.putNumber("Relative Encoder value [Rots]", absoluteEncoder.getPosition());
        SmartDashboard.putNumber("Climb / Right Motor Current [Amps]", masterClimbSparkMax.getOutputCurrent());
        SmartDashboard.putNumber("Climb / left Motor Current [Amps]", followerClimbSparkMax.getOutputCurrent());
    
    }
    
    /* READ ME:
     * Select the Setpoint aka reference point for the PID Controller
     */
    public void setPosition(double position) {
        //pidController.setReference(position, ControlType.kPosition);
        SmartDashboard.putNumber("Current SetPoint", position);
    }

    /* READ ME:
     * Depending on what we input in (ie: button matching), we can select which setpoint we want to go to.
     */
    public void setClimbState(States.ClimbPos state) {
        SmartDashboard.putNumber("Position", state.val);
        switch (state) {
            case STOW:
                setPosition(ClimbConstants.stow);
                break;
            case CLIMB:
                setPosition(ClimbConstants.climb);
            default:
                setPosition(ClimbConstants.stow);
                break;
        }
    }

}
