package frc.robot.Autonomous;
import java.util.Optional;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
//import frc.robot.subsystems.TestSubsystem;

public final class AutoModeManager{
    public enum DesiredMode {
		DO_NOTHING,
        GO_AUTO,
        RETURN_AUTO,
        AB_BOTAUTO,
        NEW_AUTO
	}

    public static AutoModeManager AutoQueue;
    public static AutoModeManager getInstance() {
        if(AutoQueue == null) AutoQueue = new AutoModeManager();
        return AutoQueue;
    }

    private DesiredMode defaultMode = DesiredMode.DO_NOTHING;
    //private Optional<AutoModeBase> mAutoMode = Optional.empty();
    public static SendableChooser<DesiredMode> mModeChooser = new SendableChooser<>();
    public static Command m_autonomousCommand;

    public AutoModeManager() {
    mModeChooser.setDefaultOption("Default Auto", DesiredMode.DO_NOTHING);
    mModeChooser.addOption("Return Auto", DesiredMode.RETURN_AUTO);
    mModeChooser.addOption("Go Auto", DesiredMode.GO_AUTO);
    mModeChooser.addOption("Bottom Feeder", DesiredMode.AB_BOTAUTO);
    }

    public static void updateAutoMode(){
        DesiredMode desiredMode = mModeChooser.getSelected();
        if (desiredMode == null) {
			    desiredMode = DesiredMode.DO_NOTHING;
        }else{
        System.out.println("Auto Chosen");
        }   
        m_autonomousCommand = grabAutoMode(desiredMode);
    }

    public static Command grabAutoMode(DesiredMode data){
        switch(data){
            case DO_NOTHING:
				m_autonomousCommand = DoNothingCommand.NoAuto();
                break;
            case GO_AUTO:
                m_autonomousCommand = GoAutoCommand.runDefaultedAutoCommand();
                break;
            case RETURN_AUTO:
                m_autonomousCommand = ReturnAutoCommand.runDefaultedAutoCommand();
            break;
            case AB_BOTAUTO:
                m_autonomousCommand = BlueBottomCommand.runDefaultedAutoCommand();
                break;
            default:
			    System.out.println("ERROR: unexpected auto mode!");
				break;
        }
        return m_autonomousCommand;
    }

    
}