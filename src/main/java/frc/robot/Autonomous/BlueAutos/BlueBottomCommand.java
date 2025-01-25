package frc.robot.Autonomous.BlueAutos;

import com.pathplanner.lib.commands.PathPlannerAuto;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.RobotContainer;

public final class BlueBottomCommand{

    public static Command getPathPlannerCommandLengthA() {
        return new PathPlannerAuto("AB_BotL1");
    }
    public static Command getPathPlannerCommandLengthB() {
        return new PathPlannerAuto("AB_BotL2");
    }
    public static Command getPathPlannerCommandLengthC() {
        return new PathPlannerAuto("AB_BotL3");
    }
    public static Command getPathPlannerCommandLengthD() {
        return new PathPlannerAuto("AB_BotL4");
    }
    public static Command getPathPlannerCommandLengthE() {
        return new PathPlannerAuto("AB_BotL5");
    }

    public static Command getFullAutoCommand(){
        return new PathPlannerAuto("AB_BotFull");
    }
    public static Command runDefaultedAutoCommand(){
        return new SequentialCommandGroup(
        BlueBottomCommand.getPathPlannerCommandLengthA(),
        new WaitCommand(0.1),
        BlueBottomCommand.getPathPlannerCommandLengthB(),
        new WaitCommand(0.1),
        BlueBottomCommand.getPathPlannerCommandLengthC(),
        new WaitCommand(0.1),
        BlueBottomCommand.getPathPlannerCommandLengthD(),
        new WaitCommand(0.1),
        BlueBottomCommand.getPathPlannerCommandLengthE(),
        new WaitCommand(0.1)
        );
    }

    public static Command runFullAutoCommand(){
        return new SequentialCommandGroup(
          BlueBottomCommand.getFullAutoCommand()  
        );
    }
}