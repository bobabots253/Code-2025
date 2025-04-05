package frc.robot.Autonomous.BlueAutos;

import com.pathplanner.lib.commands.PathPlannerAuto;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.RobotContainer;

public final class CrossLineCommand{
    public static Command getPathPlannerCommand() {
        final boolean fieldFlipped = DriverStation.getAlliance().get() == Alliance.Red;
        return fieldFlipped 
            ? new PathPlannerAuto("CrossLineRed") 
                : new PathPlannerAuto("CrossLineBlue");
    }

    public static Command runDefaultedAutoCommand(){
        return new SequentialCommandGroup(
        CrossLineCommand.getPathPlannerCommand(),
        new WaitCommand(0)
        );
    }
}