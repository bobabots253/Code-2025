package frc.robot.Autonomous;
import java.util.Optional;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
//import frc.robot.subsystems.TestSubsystem;

public final class AutoModeManager{
    public enum DesiredMode {
		DO_NOTHING,
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
            case NEW_AUTO:
                break;
            // case RETURN_PATH_AUTO:
			// 	return Optional.of(new TestPathMode());
            default:
			    System.out.println("ERROR: unexpected auto mode!");
				break;
        }
        return m_autonomousCommand;
    }

    
}