package frc.robot.subsystems;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Configs;
import frc.robot.Constants;
import frc.robot.Constants.ElevatorConstants;
import frc.robot.Constants.EndEffectorConstants;
import frc.robot.Constants.ModuleConstants;
import frc.robot.States;

import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkFlex;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkBaseConfig;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.config.ClosedLoopConfig.FeedbackSensor;
import com.revrobotics.spark.SparkBase.ControlType;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.SparkClosedLoopController;
import com.revrobotics.RelativeEncoder;


public class ElevatorSubsystem extends SubsystemBase{

private static SparkMax m_masterLiftingSparkMax;
private static SparkMax m_slaveLiftingSparkMax;
private final RelativeEncoder m_LiftingEncoder;
private static DigitalInput masterHallEffectSensor;
private static DigitalInput slaveHallEffectSensor;
private final SparkClosedLoopController m_LiftingPIDController;

private static ElevatorSubsystem instance;

public static ElevatorSubsystem getInstance() {
    if(instance == null) instance = new ElevatorSubsystem();
    return instance;
}

private ElevatorSubsystem() {
    m_masterLiftingSparkMax = new SparkMax(ElevatorConstants.masterLiftingCANId, MotorType.kBrushless);
    m_slaveLiftingSparkMax = new SparkMax(ElevatorConstants.slaveLiftingCANId, MotorType.kBrushless);
    // Setup encoders and PID controllers for the driving SPARKS MAX.
    m_LiftingEncoder = m_masterLiftingSparkMax.getEncoder();
    m_LiftingPIDController = m_masterLiftingSparkMax.getClosedLoopController();

    m_masterLiftingSparkMax.configure(Configs.ElevatorSubsystem.masterLiftingConfig, ResetMode.kResetSafeParameters,
    PersistMode.kPersistParameters);
    m_slaveLiftingSparkMax.configure(Configs.ElevatorSubsystem.slaveLiftingConfig, ResetMode.kResetSafeParameters,
    PersistMode.kPersistParameters);

    //Homing & Safe Code Stop
    masterHallEffectSensor = new DigitalInput(ElevatorConstants.pivotMasterHallEffectDIO);
    slaveHallEffectSensor = new DigitalInput(ElevatorConstants.pivotSlaveHallEffectDIO);
}

    public void setLazyPercentageOpenLoop(double value) {
        SmartDashboard.putNumber("Elevator Running Speed", value);
        m_masterLiftingSparkMax.set(value);
    }

    public void stopArm() {
        setLazyPercentageOpenLoop(0);
    }

    public void resetEncoders() {
        m_LiftingEncoder.setPosition(0.0);
    }

    public boolean isWithinHardDeck(){
        return masterHallEffectSensor.get();
    }

    @Override
    public void periodic() {

        if (isWithinHardDeck()){
            System.out.println("Hitting Code Stop");
        }
        
        SmartDashboard.putNumber("Elevator Relative Position", m_LiftingEncoder.getPosition());
        SmartDashboard.putNumber("Elevator Master Current", m_masterLiftingSparkMax.getOutputCurrent());
        SmartDashboard.putNumber("Elevator Follower Current", m_slaveLiftingSparkMax.getOutputCurrent());
    }

    public void setLazyPositionSetpoint(double position) {
        m_LiftingPIDController.setReference(position, ControlType.kPosition);
        SmartDashboard.putNumber("Elevator SetPoint", position);
    }

    public void setLazyElevatorState(States.ElevatorPos state) {
        SmartDashboard.putNumber("Position", state.val);
        switch (state) {
            case STOW:
                setLazyPositionSetpoint(ElevatorConstants.softZeroLinearPosition);
                break;
            case L1Score:
                setLazyPositionSetpoint(ElevatorConstants.L1Score);
                break;
            case L2Score:
                setLazyPositionSetpoint(ElevatorConstants.L2Score);
                break;
            default:
                setLazyPositionSetpoint(ElevatorConstants.softZeroLinearPosition);
                break;
        }
    }


}
