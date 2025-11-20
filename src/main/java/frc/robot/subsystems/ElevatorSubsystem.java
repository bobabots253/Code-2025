package frc.robot.subsystems;

import com.revrobotics.RelativeEncoder;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.ElevatorFeedforward;
import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
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
private static ProfiledPIDController m_profiledPIDController;

public int currentIntSetpointElevator;
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

    public void setLazyPercentageOpenLoop(double OpenLoopPercentage) {
        SmartDashboard.putNumber("Elevator /Raw Output Speed (#.##)", OpenLoopPercentage);
        m_masterLiftingSparkMax.set(OpenLoopPercentage);
    }
    public void setElevatorVoltage(double volts) {
        SmartDashboard.putNumber("Elevator /Manual Voltage", volts);
        m_masterLiftingSparkMax.setVoltage(volts);
    }

    public void setSafePercentageOpenLoop(double OpenLoopPercentage){
        SmartDashboard.putNumber("Elevator / Safe Output Speed (#.##)", OpenLoopPercentage);
        if (isWithinExtensionRange() && !MathUtil.isNear(18.85, getEncoder(), 0.2)){
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

    public boolean isWithinExtensionRange(){
        if (m_LiftingEncoder.getPosition() < ElevatorConstants.ELEVATOR_MAX_TRAVEL 
            && m_LiftingEncoder.getPosition() > ElevatorConstants.ELEVATOR_MIN_TRAVEL){
            return true;
        }else{
            return false;
        }
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

    public void profiledPIDCalculation(double goalPosition){
        if(isWithinExtensionRange()){
            m_masterLiftingSparkMax.setVoltage(
                m_profiledPIDController.calculate(
                    rotToMeters(m_LiftingEncoder.getPosition()),
                    rotToMeters(goalPosition))+ 0.7); 
        }else{
            System.out.println("ELEVATOR POSITION OUT OF TOLERANCE - PROFILED PID REQUEST");
        }
        trapezoid = m_profiledPIDController.calculate(rotToMeters(m_LiftingEncoder.getPosition()), rotToMeters(goalPosition))+ .7; 
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
                break;
            case L1Score:
                profiledPIDCalculation(ElevatorConstants.L1Score);
                break;
            case L2Score:
                System.out.println();
                profiledPIDCalculation(ElevatorConstants.L2Score);
                break;
            case L3SCORE:
                profiledPIDCalculation(ElevatorConstants.L3Score);
                break;
            case L1HANDOFF:
                profiledPIDCalculation(ElevatorConstants.L1Handoff);
                break;
            case L1FLICK:
                profiledPIDCalculation(ElevatorConstants.L1Flick);
                break;
            default:
                break;
        }
    }
}
