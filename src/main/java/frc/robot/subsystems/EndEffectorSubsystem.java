package frc.robot.subsystems;

import org.ejml.data.DGrowArray;

import com.revrobotics.AbsoluteEncoder;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.spark.SparkBase.ControlType;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.SparkClosedLoopController;
import com.revrobotics.spark.SparkMax;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import com.revrobotics.spark.SparkLowLevel.MotorType;

import frc.robot.Configs;
import frc.robot.Constants.ElevatorConstants;
import frc.robot.Constants.EndEffectorConstants;
import frc.robot.States;

public class EndEffectorSubsystem extends SubsystemBase{
private static SparkMax m_pivotSparkMax;
private static SparkMax m_intakeRollerSparkMax;
private static SparkMax m_algaeRollerSparkMax;
private final RelativeEncoder m_intakeRollerEncoder;
private final RelativeEncoder m_algaeRollerEncoder;
private final AbsoluteEncoder m_pivotEncoder;
private final SparkClosedLoopController m_pivotPIDController;
private final SparkClosedLoopController m_intakeRollerPIDController;
private final SparkClosedLoopController m_algaeRollerPIDController;
private final DigitalInput frontIntakeBeamBreak;
private final DigitalInput backIntakeBeamBreak;

private static EndEffectorSubsystem instance;

public static EndEffectorSubsystem getInstance() {
    if(instance == null) instance = new EndEffectorSubsystem();
    return instance;
}

private EndEffectorSubsystem(){
    m_pivotSparkMax = new SparkMax(EndEffectorConstants.pivotCANId,MotorType.kBrushless);
    m_intakeRollerSparkMax = new SparkMax(EndEffectorConstants.intakeRollerCANId,MotorType.kBrushless);
    m_algaeRollerSparkMax = new SparkMax(EndEffectorConstants.algaeRollerCANId,MotorType.kBrushless);

    m_pivotEncoder = m_pivotSparkMax.getAbsoluteEncoder();
    m_intakeRollerEncoder = m_intakeRollerSparkMax.getEncoder();
    m_algaeRollerEncoder = m_algaeRollerSparkMax.getEncoder();

    m_pivotPIDController = m_pivotSparkMax.getClosedLoopController();
    m_intakeRollerPIDController = m_intakeRollerSparkMax.getClosedLoopController();
    m_algaeRollerPIDController = m_algaeRollerSparkMax.getClosedLoopController();

    m_pivotSparkMax.configure(Configs.EndEffectorSubsystemConfig.pivotConfig, ResetMode.kResetSafeParameters,
    PersistMode.kPersistParameters);
    m_intakeRollerSparkMax.configure(Configs.EndEffectorSubsystemConfig.intakeRollerConfig, ResetMode.kResetSafeParameters,
    PersistMode.kPersistParameters);
    m_algaeRollerSparkMax.configure(Configs.EndEffectorSubsystemConfig.algaeRollerConfig, ResetMode.kResetSafeParameters,
    PersistMode.kPersistParameters);

    frontIntakeBeamBreak = new DigitalInput(EndEffectorConstants.frontBeamBreakSensor);
    backIntakeBeamBreak = new DigitalInput(EndEffectorConstants.backBeamBreakSensor);
}

@Override
public void periodic() {

    SmartDashboard.putNumber("Algae /absolutePosition", m_pivotEncoder.getPosition());
    SmartDashboard.putNumber("Algae /masterCurrent", m_algaeRollerSparkMax.getOutputCurrent());
    SmartDashboard.putBoolean("EndEffector /isIntakedDIO", isCoralInsideIntake());
}

    public void setPivotLazyPercentageOpenLoop(double value) {
            SmartDashboard.putNumber("Pivot Running Speed", value);
            m_pivotSparkMax.set(value);
    }

    public void setAlgaeLazyPercentageOpenLoop(double value) {
            SmartDashboard.putNumber("Algae Running Speed", value);
            m_algaeRollerSparkMax.set(value);
    }

    public void setSafePercentagePivotOpenLoop(double OpenLoopPercentage){
        SmartDashboard.putNumber("Algae / Safe Output Speed (#.##)", OpenLoopPercentage);
        if (isWithinPivotRange() && !MathUtil.isNear(EndEffectorConstants.PIVOT_MAX_TRAVEL, getPivotAbsoluteEncoder(), 0.15)){
            m_pivotSparkMax.set(
                MathUtil.clamp(OpenLoopPercentage,
                EndEffectorConstants.PIVOT_OUTPUT_LOW, EndEffectorConstants.PIVOT_OUTPUT_HIGH));
            }else{
            while(MathUtil.isNear(EndEffectorConstants.PIVOT_MAX_TRAVEL, getPivotAbsoluteEncoder(), 0.15)){
            m_pivotSparkMax.set(
                MathUtil.clamp(-0.075,
                EndEffectorConstants.PIVOT_OUTPUT_LOW, EndEffectorConstants.PIVOT_OUTPUT_HIGH));
                }
            }        
        }
    
    public void setIntakeLazyPercentageOpenLoop(double value) {
            SmartDashboard.putNumber("Intake Running Speed", value);
            m_intakeRollerSparkMax.set(value);
    }

    public double getPivotAbsoluteEncoder(){
        return m_pivotEncoder.getPosition();
    }

    public boolean isFrontBeamBreakBlocked(){
        return frontIntakeBeamBreak.get();
    }

    public boolean isBackBeamBreakBlocked(){
        return backIntakeBeamBreak.get();
    }
    
    public boolean isCoralInsideIntake(){
        if (isFrontBeamBreakBlocked() && isBackBeamBreakBlocked() != isBackBeamBreakBlocked()){
            return true;
        }else{
            return false;
        }
    }

    public boolean isWithinPivotRange(){
        if (m_pivotEncoder.getPosition() < EndEffectorConstants.PIVOT_MAX_TRAVEL 
            && m_pivotEncoder.getPosition() > EndEffectorConstants.PIVOT_MIN_TRAVEL){
            return true;
        }else{
            return false;
        }
    }

    public void stopPivot(){
            m_pivotSparkMax.set(0);
    }
    public void stopAlgaeRoller(){
            m_pivotSparkMax.set(0);
    }
    public void stopIntakeRoller(){
            m_pivotSparkMax.set(0);
    }

    public void resetRollerEncoders() {
        m_algaeRollerEncoder.setPosition(0);
        m_intakeRollerEncoder.setPosition(0);
    }

    public void resetIntakeRollerEncoders() {
        m_intakeRollerEncoder.setPosition(0.0);
    }

    public void resetAlgaeRollerEncoders() {
        m_algaeRollerEncoder.setPosition(0.0);
    }

    public void setLazyPivotPositionSetpoint(double PositionSetpoint){
        double correctedSetpoint = MathUtil.clamp(PositionSetpoint,
                 EndEffectorConstants.PIVOT_MIN_TRAVEL, EndEffectorConstants.PIVOT_MAX_TRAVEL);
        m_pivotPIDController.setReference(correctedSetpoint, ControlType.kPosition);
        SmartDashboard.putNumber("Pivot Setpoint", PositionSetpoint);
    }

    public void setLazyIntakeVelocitySetpoint(double velocity){
        m_intakeRollerPIDController.setReference(velocity, ControlType.kVelocity);
        SmartDashboard.putNumber("Intake SetPoint", velocity);
    }

    public void setLazyAlgaeVelocitySetpoint(double velocity){
        m_intakeRollerPIDController.setReference(velocity, ControlType.kVelocity);
        SmartDashboard.putNumber("Algae SetPoint", velocity);
    }

    public void setLazyEndEffectorState(States.EndEffectorPos requestedState) {
        SmartDashboard.putNumber("Elevator /requestedPosition", requestedState.val);
        switch (requestedState) {
            case STOW:
                setLazyPivotPositionSetpoint(EndEffectorConstants.softZeroPivotPosition);
                setAlgaeLazyPercentageOpenLoop(0.0);
                break;
            case L1Score:
                setLazyPivotPositionSetpoint(EndEffectorConstants.softZeroPivotPosition);
                break;
            case L2Score:
                setLazyPivotPositionSetpoint(EndEffectorConstants.softZeroPivotPosition);
                break;
            case L3Score:
                setLazyPivotPositionSetpoint(EndEffectorConstants.softZeroPivotPosition);
            case INTAKE:
                setLazyPivotPositionSetpoint(EndEffectorConstants.softZeroPivotPosition);
                setIntakeLazyPercentageOpenLoop(0.5);
                break;
            case PUSH:
                setLazyPivotPositionSetpoint(ElevatorConstants.softZeroLinearPosition);
                setIntakeLazyPercentageOpenLoop(0.2);
                break;
            case FLY_BIRDY_FLY: //Scoring Enum
                setIntakeLazyPercentageOpenLoop(-0.6);
            case HARD_REMOVE:
            setIntakeLazyPercentageOpenLoop(1.0);
                break;
            case SMART_INTAKE:
                if (!isCoralInsideIntake()){
                    setIntakeLazyPercentageOpenLoop(1.0);
                }else{
                    setIntakeLazyPercentageOpenLoop(0);
                }
            case SOFT_REMOVE:
                setIntakeLazyPercentageOpenLoop(0.7);
            case EXTENDED_PIVOT:
                setLazyPivotPositionSetpoint(EndEffectorConstants.extendedPIvotPosition);
            case PIVOT_ROLLERS:
                setAlgaeLazyPercentageOpenLoop(.5);
                break;
            default:
                setLazyPivotPositionSetpoint(EndEffectorConstants.softZeroPivotPosition);
                break;
        }
    }
}