package frc.robot.subsystems;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.motorcontrol.Spark;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Configs;
import frc.robot.Constants;
import frc.robot.States;
import frc.utils.Util;

import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.ClosedLoopConfig.FeedbackSensor;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.SparkFlex;
import com.revrobotics.spark.SparkLowLevel.MotorType;

import java.io.ObjectInputFilter.Config;

import com.revrobotics.RelativeEncoder;
import com.revrobotics.spark.SparkBase.ControlType;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.SparkClosedLoopController;


public class ElevatorSubsystem extends SubsystemBase{
    private static final SparkMax motorL = Util.createSparkMAX(Constants.ElevatorConstants.leftMotorID, MotorType.kBrushless, false);
    private static final SparkMax motorR = Util.createSparkMAX(Constants.ElevatorConstants.rightMotorID, MotorType.kBrushless, true);
    private static RelativeEncoder relativeEncoder;
    private SparkClosedLoopController pidController;
    private static final DigitalInput hallEffectSensor = new DigitalInput(0);
    
    private static ElevatorSubsystem instance;

    public static ElevatorSubsystem getInstance(){
        if(instance == null){
            instance = new ElevatorSubsystem();
        }
        return instance;
    }
    

    private ElevatorSubsystem(){
        //resetEncoders();
        relativeEncoder = motorR.getEncoder();
        pidController = motorR.getClosedLoopController();
        motorR.configure(Configs.ElevatorSubsystem.motorRConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
        motorL.configure(Configs.ElevatorSubsystem.motorLConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
        register();
    }

    public void setOpenLoop(double value){
        motorL.set(value);
        motorR.set(value);
    }
    public void stop(){
        setOpenLoop(0);
    }

    public void resetEncoders(){
        relativeEncoder.setPosition(0);
    }

    public void setPosition(double position){
        pidController.setReference(position, ControlType.kPosition);
    }


    @Override
    public void periodic() {
        if(hallEffectSensor.get()==false){
            resetEncoders();
        }
    }

    public void setState(States.ElevatorPos state){
        //put code in switch case statment 
        switch (state) {
            case STOW:
                    setPosition(0);
                break;
            
            case L1:
                setPosition(0);
                break;
            
            case L2:
                setPosition(0);
                break;

            case L3:
                setPosition(0);
                break;
        
            default:
                    setPosition(0);
                break;
        }
    }

}
