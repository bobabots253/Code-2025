package frc.robot.subsystems.endeffector;

// package frc.robot.subsystems;

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

public class EndEffectorSubsystem extends SubsystemBase{
private static SparkMax m_pivotSparkMax;
private static SparkMax m_intakeRollerSparkMax;
private final RelativeEncoder m_intakeRollerEncoder;
private final AbsoluteEncoder m_pivotEncoder;
private final SparkClosedLoopController m_pivotPIDController;

    private static EndEffectorSubsystem instance;

    public static EndEffectorSubsystem getInstance() {
        if(instance == null) instance = new EndEffectorSubsystem();
        return instance;
    }

private EndEffectorSubsystem(){
    m_pivotSparkMax = new SparkMax(EndeffectorConstants.pivotCANId,MotorType.kBrushless);
    m_intakeRollerSparkMax = new SparkMax(EndeffectorConstants.intakeRollerCANId,MotorType.kBrushless);

    m_pivotEncoder = m_pivotSparkMax.getAbsoluteEncoder();
    m_intakeRollerEncoder = m_intakeRollerSparkMax.getEncoder();

    m_pivotPIDController = m_pivotSparkMax.getClosedLoopController();

    m_pivotSparkMax.configure(EndeffectorConfigs.pivotConfig, ResetMode.kResetSafeParameters,
    PersistMode.kPersistParameters);
    m_intakeRollerSparkMax.configure(EndeffectorConfigs.intakeRollerConfig, ResetMode.kResetSafeParameters,
    PersistMode.kPersistParameters);
}

// @Override
public void periodic() {
}


    
    public void setIntakeLazyPercentageOpenLoop(double value) {
            SmartDashboard.putNumber("Intake Running Speed", value);
            m_intakeRollerSparkMax.set(value);
    }

    public void setPivotLazyPercentageOpenLoop(double inputvalue) {
        SmartDashboard.putNumber("Pivot Running Speed", inputvalue);
        m_pivotSparkMax.set(inputvalue);
    }

    public void resetIntakeRollerEncoders() {
        m_intakeRollerEncoder.setPosition(0.0);
    }

}
    