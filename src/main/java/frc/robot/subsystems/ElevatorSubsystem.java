package frc.robot.subsystems;

import java.util.ResourceBundle.Control;

import com.revrobotics.RelativeEncoder;
import com.revrobotics.spark.SparkBase.ControlType;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.ClosedLoopSlot;
import com.revrobotics.spark.SparkClosedLoopController;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.ElevatorFeedforward;
import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.Configs;
import frc.robot.Constants;
import frc.robot.Constants.ElevatorConstants;
import frc.robot.States;


public class ElevatorSubsystem extends SubsystemBase{

private static SparkMax m_masterLiftingSparkMax;
private static SparkMax m_slaveLiftingSparkMax;
private final RelativeEncoder m_LiftingEncoder;
private final RelativeEncoder m_followerEncoder;
private static DigitalInput masterHallEffectSensor;
private static DigitalInput slaveHallEffectSensor;
private static SparkClosedLoopController m_LiftingPIDController;
private static ProfiledPIDController m_profiledPIDController;

//Tunable Values
// public final String kTunableP = "Tunable_P";
// public final String kTunableI = "Tunable_I";
// public final String kTunableD = "Tunable_D";
public int currentIntSetpointElevator;
// private static ElevatorSubsystem instance;
public static ElevatorFeedforward m_feedForward;
public static double trapezoid;

public double volting;

    private static class ElevatorSubsystemHandler {
        private static final ElevatorSubsystem instance = new ElevatorSubsystem();
    }

    public static ElevatorSubsystem getInstance() {
        return ElevatorSubsystemHandler.instance;
    }

private ElevatorSubsystem() {
    m_masterLiftingSparkMax = new SparkMax(ElevatorConstants.masterLiftingCANId, MotorType.kBrushless);
    m_slaveLiftingSparkMax = new SparkMax(ElevatorConstants.slaveLiftingCANId, MotorType.kBrushless);

    // Setup encoders and PID controllers for the driving SPARKS MAX.
    m_LiftingEncoder = m_masterLiftingSparkMax.getEncoder();
    m_followerEncoder = m_slaveLiftingSparkMax.getEncoder();
    m_LiftingPIDController = m_masterLiftingSparkMax.getClosedLoopController();

    m_feedForward = new ElevatorFeedforward(0.45, .75, .0, 0.0);
    m_profiledPIDController = new ProfiledPIDController(
        Constants.ElevatorConstants.profiledP, 
        Constants.ElevatorConstants.profiledI, 
        Constants.ElevatorConstants.profiledD, 
        new TrapezoidProfile.Constraints(Constants.ElevatorConstants.elevatorMaxVelocity, Constants.ElevatorConstants.elevaotrMaxAccerleration), 0.02
    );
    m_profiledPIDController.setIZone(0);

    m_masterLiftingSparkMax.configure(Configs.ElevatorSubsystem.masterLiftingConfig, ResetMode.kResetSafeParameters,
    PersistMode.kPersistParameters);
    m_slaveLiftingSparkMax.configure(Configs.ElevatorSubsystem.slaveLiftingConfig, ResetMode.kResetSafeParameters,
    PersistMode.kPersistParameters);

//     //Homing & Safe Code Stop
//     masterHallEffectSensor = new DigitalInput(ElevatorConstants.pivotMasterHallEffectDIO);
//     slaveHallEffectSensor = new DigitalInput(ElevatorConstants.pivotSlaveHallEffectDIO);
//     //Preferences.putDouble(kTunableP , ElevatorConstants.kIncrementalPostionP);
//     resetEncoders();
    //setCoastMode(true);
    }

// @Override
    public void periodic() {
        SmartDashboard.putNumber("Elevator /relativePosition", m_LiftingEncoder.getPosition());
        SmartDashboard.putNumber("Elevator /relativePositionMEters", rotToMeters(m_LiftingEncoder.getPosition()));
        SmartDashboard.putNumber("Elevator /relativePosition", m_LiftingEncoder.getPosition());
        SmartDashboard.putNumber("Elevator /relativePositionMEters", rotToMeters(m_LiftingEncoder.getPosition()));
        SmartDashboard.putNumber("Elevator /masterCurrent", m_masterLiftingSparkMax.getOutputCurrent());
        SmartDashboard.putNumber("Elevator /followerCurrent", m_slaveLiftingSparkMax.getOutputCurrent());
        SmartDashboard.putBoolean("Elevator /withinExtensionRange", isWithinExtensionRange());
        SmartDashboard.putNumber("Elevator /requestedPosition", currentIntSetpointElevator);
        SmartDashboard.putNumber("Elevator /trapezoid", trapezoid);
        SmartDashboard.putNumber("Elevator /masterInputCurrent", m_masterLiftingSparkMax.getAppliedOutput());
        SmartDashboard.putNumber("Elevator/secondStageVelocity ", rpmToVelocity(m_LiftingEncoder.getVelocity()));
    }

//     if (!isWithinExtensionRange()){
//         System.out.println("Elevator Hitting Code Stop");
//         stopElevator();
//     }

//     SmartDashboard.putNumber("Elevator /elevatorVel",getElevatorVelocity());
    


    


    public void setLazyPercentageOpenLoop(double OpenLoopPercentage) {
        SmartDashboard.putNumber("Elevator /Raw Output Speed (#.##)", OpenLoopPercentage);
        m_masterLiftingSparkMax.set(OpenLoopPercentage);
    }
    public void setElevatorVoltage(double volts) {
        SmartDashboard.putNumber("Elevator /Manual Voltage", volts);
        m_masterLiftingSparkMax.setVoltage(volts);
        // System.out.println("the current output" + m_masterLiftingSparkMax.getAppliedOutput());
        // System.out.println("the current volts" + m_masterLiftingSparkMax.getBusVoltage());
        // System.out.println("the current " + m_masterLiftingSparkMax.getOutputCurrent());
    }

    public void setSafePercentageOpenLoop(double OpenLoopPercentage){
        SmartDashboard.putNumber("Elevator / Safe Output Speed (#.##)", OpenLoopPercentage);
        if (isWithinExtensionRange() && !MathUtil.isNear(18.85, getEncoder(), 0.15)){
            m_masterLiftingSparkMax.set(
                MathUtil.clamp(OpenLoopPercentage,
                 ElevatorConstants.ELEVATOR_OUTPUT_LOW, ElevatorConstants.ELEVATOR_OUTPUT_HIGH));
            m_slaveLiftingSparkMax.set(
                MathUtil.clamp(OpenLoopPercentage,
                 ElevatorConstants.ELEVATOR_OUTPUT_LOW, ElevatorConstants.ELEVATOR_OUTPUT_HIGH));
            }else{
            while(MathUtil.isNear(18.85, getEncoder(), 0.15)){
                m_masterLiftingSparkMax.set(-0.05);
                m_slaveLiftingSparkMax.set(-0.05);
                }
            while(MathUtil.isNear(-0.1, getEncoder(), 0.05)){
                m_masterLiftingSparkMax.set(0.05);
                m_slaveLiftingSparkMax.set(0.05);
            }
            }        
        }

    public void stopElevator() {
        setLazyPercentageOpenLoop(0);
    }

    public double getEncoder(){
        return m_LiftingEncoder.getPosition();
    }

    public void resetEncoders() {
        m_LiftingEncoder.setPosition(0.0);
    }

    public boolean getPrimarySensor(){
        return masterHallEffectSensor.get();
    }

    public boolean getSecondarySensor(){
        return slaveHallEffectSensor.get();
    }

    public double getElevatorVelocity(){
        return m_LiftingEncoder.getVelocity();
    }


    public boolean isWithinExtensionRange(){
        if (m_LiftingEncoder.getPosition() < ElevatorConstants.ELEVATOR_MAX_TRAVEL 
            && m_LiftingEncoder.getPosition() > ElevatorConstants.ELEVATOR_MIN_TRAVEL){
            return true;
        }else{
            return false;
        }
    }

    public boolean isWithinMotorAlignment(){
        return m_LiftingEncoder.getPosition() == m_followerEncoder.getPosition();
    }

    public void setCoastMode(boolean CoastModeEnabled){
        if (CoastModeEnabled) {
            m_masterLiftingSparkMax.configure(
                Configs.ElevatorSubsystem.masterLiftingCoastModeConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
            m_slaveLiftingSparkMax.configure(
                Configs.ElevatorSubsystem.slaveLiftingCoastModeConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
          } else {
            m_masterLiftingSparkMax.configure(
                Configs.ElevatorSubsystem.masterLiftingConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
            m_slaveLiftingSparkMax.configure(
                Configs.ElevatorSubsystem.masterLiftingConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
        }
    }

    public void setNeutralMode() {
        stopElevator();
        new WaitCommand(1.00).schedule();
        setCoastMode(true);
      }

    public boolean isHomed(){
        return MathUtil.isNear(ElevatorConstants.softZeroLinearPosition,
                 m_LiftingEncoder.getPosition(), 0.05);
      }
    
    //Back up 
    public void setLazyPositionSetpoint(double requestedSetpoint) {
        SmartDashboard.putNumber("Elevator /requestedSetpoint", requestedSetpoint);
        if (isWithinExtensionRange()) {
            if(isHomed()){
                m_LiftingPIDController.setIAccum(0);
            }
            m_LiftingPIDController.setReference(requestedSetpoint, ControlType.kMAXMotionPositionControl,
             ClosedLoopSlot.kSlot0, ElevatorConstants.kIncrementalPositionFF,
             SparkClosedLoopController.ArbFFUnits.kVoltage);
            //m_LiftingPIDController.setReference(requestedSetpoint, ControlType.kPosition, ClosedLoopSlot.kSlot0, volting);
        } else {
            System.out.println("ELEVATOR POSITION OUT OF TOLERANCE - SETPOINT REQUEST");
        }
    }

    public void profiledPIDCalculation(double goalPosition){
        if(isWithinExtensionRange()){
            //possible divide the feed forward by 2 because it is a 2 stage cascading elevator
            //feed forward  m_feedForward.calculateWithVelocities(rpmToVelocity(m_LiftingEncoder.getVelocity()), m_profiledPIDController.getSetpoint().velocity)
            m_masterLiftingSparkMax.setVoltage(
                m_profiledPIDController.calculate(
                    rotToMeters(m_LiftingEncoder.getPosition()),
                    rotToMeters(goalPosition))+ 0.7); //What is the 0.68 for?
        }else{
            System.out.println("ELEVATOR POSITION OUT OF TOLERANCE - PROFILED PID REQUEST");
        }
        //System.out.println("calculating = "+ m_profiledPIDController.calculate(rotToMeters(m_LiftingEncoder.getPosition()), rotToMeters(goalPosition)));
        trapezoid = m_profiledPIDController.calculate(rotToMeters(m_LiftingEncoder.getPosition()), rotToMeters(goalPosition))+ .7; // doesn't match up with the other one???
    }

    private double rpmToVelocity(double rpm){
        //multiply by 2 for 2 stage cascading elevator
        //cascading secondary stages run 2x faster than the first stage
        var elevatorScalar = 2;
        return elevatorScalar * ((rpm * (2*Math.PI*Constants.ElevatorConstants.gearRadius))/60);
    }

    private double rotToMeters(double rot){
        return (((rot/Constants.ElevatorConstants.gearRatio)/(Math.PI*2*Constants.ElevatorConstants.gearRadius))/2);
    }

    //Add the Rest & Add Button Bindings
    public void setLazyElevatorState(States.ElevatorPos requestedState) {
        SmartDashboard.putNumber("Elevator /Position", requestedState.val);
        currentIntSetpointElevator = requestedState.val;
        switch (requestedState) {
            case STOW:
                profiledPIDCalculation(ElevatorConstants.softZeroLinearPosition);
                //setLazyPositionSetpoint(ElevatorConstants.softZeroLinearPosition);
                break;
            case L1Score:
                profiledPIDCalculation(ElevatorConstants.L1Score);
                //setLazyPositionSetpoint(ElevatorConstants.L1Score);
                break;
            case L2Score:
                System.out.println();
                profiledPIDCalculation(ElevatorConstants.L2Score);
                //setLazyPositionSetpoint(ElevatorConstants.L2Score);
                break;
            case L3SCORE:
                profiledPIDCalculation(ElevatorConstants.L3Score);
                //setLazyPositionSetpoint(ElevatorConstants.L3Score);
                break;
            case L1HANDOFF:
                profiledPIDCalculation(ElevatorConstants.L1Handoff);
                break;
            case L1FLICK:
                profiledPIDCalculation(ElevatorConstants.L1Flick);
                break;
            default:
                //setLazyPositionSetpoint(ElevatorConstants.softZeroLinearPosition);
                break;
        }
    }
 // Possible PID tuner but Currently merging.
// public void setPIDParameters(double P, double I, double D){
//     Configs.ElevatorSubsystem.masterLiftingConfig.closedLoop
//     .feedbackSensor(FeedbackSensor.kPrimaryEncoder)
//     .pid(P,
//          I,
//          D)
//     .outputRange(ElevatorConstants.kUniversalPIDOutputLow, ElevatorConstants.kUniversalPIDOutputHigh);
//     m_masterLiftingSparkMax.configure(Configs.ElevatorSubsystem.masterLiftingConfig,com.revrobotics.spark.SparkBase.ResetMode.kResetSafeParameters,PersistMode.kNoPersistParameters);
// }

}
