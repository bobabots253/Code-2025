package frc.robot.subsystems;

// package frc.robot.subsystems;

import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.SparkMax;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import com.revrobotics.spark.SparkLowLevel.MotorType;

import frc.robot.Configs;
import frc.robot.Constants.EndEffectorConstants;

public class EndEffectorSubsystem extends SubsystemBase{
private static SparkMax m_pivotSparkMax;
private static SparkMax m_intakeRollerSparkMax;

    private static EndEffectorSubsystem instance;

    public static EndEffectorSubsystem getInstance() {
        if(instance == null) instance = new EndEffectorSubsystem();
        return instance;
    }

private EndEffectorSubsystem(){
    m_pivotSparkMax = new SparkMax(EndEffectorConstants.pivotCANId,MotorType.kBrushless);
    m_intakeRollerSparkMax = new SparkMax(EndEffectorConstants.intakeRollerCANId,MotorType.kBrushless);


    m_pivotSparkMax.configure(Configs.EndEffectorSubsystemConfig.pivotConfig, ResetMode.kResetSafeParameters,
    PersistMode.kPersistParameters);
    m_intakeRollerSparkMax.configure(Configs.EndEffectorSubsystemConfig.intakeRollerConfig, ResetMode.kResetSafeParameters,
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

}
    
