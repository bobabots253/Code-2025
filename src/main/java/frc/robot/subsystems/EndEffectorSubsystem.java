package frc.robot.subsystems;

import com.revrobotics.AbsoluteEncoder;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.spark.SparkBase.ControlType;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.SparkClosedLoopController;
import com.revrobotics.spark.SparkMax;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import com.revrobotics.spark.SparkLowLevel.MotorType;

import frc.robot.Configs;
import frc.robot.Constants.EndEffectorConstants;

public class EndEffectorSubsystem {
private static SparkMax m_pivotSparkMax;
private static SparkMax m_intakeRollerSparkMax;
private static SparkMax m_algaeRollerSparkMax;
private final RelativeEncoder m_intakeRollerEncoder;
private final RelativeEncoder m_algaeRollerEncoder;
private final AbsoluteEncoder m_pivotEncoder;
private final SparkClosedLoopController m_pivotPIDController;
private final SparkClosedLoopController m_intakeRollerPIDController;
private final SparkClosedLoopController m_algaeRollerPIDController;

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

    m_pivotSparkMax.configure(Configs.EndEffectorSubsystem.pivotConfig, ResetMode.kResetSafeParameters,
    PersistMode.kPersistParameters);
    m_intakeRollerSparkMax.configure(Configs.EndEffectorSubsystem.intakeRollerConfig, ResetMode.kResetSafeParameters,
    PersistMode.kPersistParameters);
    m_algaeRollerSparkMax.configure(Configs.EndEffectorSubsystem.algaeRollerConfig, ResetMode.kResetSafeParameters,
    PersistMode.kPersistParameters);
}

    public void setPivotLazyPercentageOpenLoop(double value) {
            SmartDashboard.putNumber("Pivot Running Speed", value);
            m_pivotSparkMax.set(value);
    }

    public void setAlgaeLazyPercentageOpenLoop(double value) {
            SmartDashboard.putNumber("Algae Running Speed", value);
            m_algaeRollerSparkMax.set(value);
    }

    public void setIntakeLazyPercentageOpenLoop(double value) {
            SmartDashboard.putNumber("Intake Running Speed", value);
            m_intakeRollerSparkMax.set(value);
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

    public void setLazyPositionSetpoint(double position) {
        m_pivotPIDController.setReference(position, ControlType.kPosition);
        SmartDashboard.putNumber("Pivot SetPoint", position);
    }

    public void setLazyIntakeVelocitySetpoint(double velocity){
        m_intakeRollerPIDController.setReference(velocity, ControlType.kVelocity);
        SmartDashboard.putNumber("Intake SetPoint", velocity);
    }

    public void setLazyAlgaeVelocitySetpoint(double velocity){
        m_intakeRollerPIDController.setReference(velocity, ControlType.kVelocity);
        SmartDashboard.putNumber("Algae SetPoint", velocity);
    }
}