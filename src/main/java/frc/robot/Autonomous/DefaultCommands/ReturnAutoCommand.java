package frc.robot.Autonomous.DefaultCommands;

import com.pathplanner.lib.commands.PathPlannerAuto;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.RobotContainer;

public final class ReturnAutoCommand{

    public static Command getPathPlannerCommand() {
        return new PathPlannerAuto("T_ReturnAuto");
    }

    public static Command runDefaultedAutoCommand(){
        return new SequentialCommandGroup(
        GoAutoCommand.getPathPlannerCommand(),
        new WaitCommand(0)
        );
    }
}