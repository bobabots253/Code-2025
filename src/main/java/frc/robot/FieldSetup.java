package frc.robot;

import java.util.TreeMap;
import java.util.function.Supplier;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;

public class FieldSetup {
        public static final Translation2d bluePassPosition = new Translation2d(0.6, 7.5);
        public static final Translation2d redPassPosition = new Translation2d(16, 7.5);

        public static final Supplier<Translation2d> alliancePassPositionSupplier = () -> DriverStation.getAlliance()
                .orElse(DriverStation.Alliance.Blue) == DriverStation.Alliance.Blue
                        ? bluePassPosition
                        : redPassPosition;

        public static final TreeMap<Integer, Translation2d> kRedAprilTagLayout = new TreeMap<Integer, Translation2d>() {
            {
                put(1, new Translation2d(Units.inchesToMeters(593.68), Units.inchesToMeters(9.68)));
                put(2, new Translation2d(Units.inchesToMeters(637.21), Units.inchesToMeters(34.79)));
                put(3, new Translation2d(Units.inchesToMeters(652.73), Units.inchesToMeters(196.17)));
                put(4, new Translation2d(Units.inchesToMeters(652.73), Units.inchesToMeters(218.42)));
                put(5, new Translation2d(Units.inchesToMeters(578.77), Units.inchesToMeters(323.00)));
                put(11, new Translation2d(Units.inchesToMeters(468.69), Units.inchesToMeters(146.19)));
                put(12, new Translation2d(Units.inchesToMeters(468.69), Units.inchesToMeters(177.10)));
                put(13, new Translation2d(Units.inchesToMeters(441.74), Units.inchesToMeters(161.62)));
                put(51, new Translation2d(Units.inchesToMeters(566.77), Units.inchesToMeters(287.00)));
            }

        };

        public static final TreeMap<Integer, Translation2d> kBlueAprilTagLayout = new TreeMap<Integer, Translation2d>() {
            {
                put(6, new Translation2d(Units.inchesToMeters(72.5), Units.inchesToMeters(323.00)));
                put(7, new Translation2d(Units.inchesToMeters(-1.50), Units.inchesToMeters(218.42)));
                put(8, new Translation2d(Units.inchesToMeters(-1.50), Units.inchesToMeters(196.17)));
                put(9, new Translation2d(Units.inchesToMeters(14.02), Units.inchesToMeters(34.79)));
                put(10, new Translation2d(Units.inchesToMeters(57.54), Units.inchesToMeters(9.68)));
                put(14, new Translation2d(Units.inchesToMeters(209.48), Units.inchesToMeters(161.62)));
                put(15, new Translation2d(Units.inchesToMeters(182.73), Units.inchesToMeters(177.10)));
                put(16, new Translation2d(Units.inchesToMeters(182.73), Units.inchesToMeters(146.19)));
                put(61, new Translation2d(Units.inchesToMeters(0), Units.inchesToMeters(287)));
            }
        };

        public static final int kRedSpeakerId = 4;
        public static final int kBlueSpeakerId = 7;
        public static final int kRedAmpId = 5;
        public static final int kBlueAmpId = 6;

        public static Supplier<Translation2d> allianceSpeakerPositionSupplier = () -> DriverStation.getAlliance()
                .orElse(DriverStation.Alliance.Blue) == Alliance.Blue
                        ? getTagTranslation(kBlueSpeakerId)
                                .minus(new Translation2d(0, .10))
                        : getTagTranslation(kRedSpeakerId);

        public static final Translation2d ampEntryOffset = new Translation2d(0, -0.5);

        public static final Pose2d ampEntryTolerance = new Pose2d(new Translation2d(0.2, 0.2),
                Rotation2d.fromDegrees(1));

        public static final Supplier<Pose2d> allianceAmpEntryPoseSupplier = () -> DriverStation.getAlliance()
                .orElse(DriverStation.Alliance.Blue) == Alliance.Blue
                        ? new Pose2d(getTagTranslation(kBlueAmpId),
                                Rotation2d.fromDegrees(90))
                        : new Pose2d(getTagTranslation(kRedAmpId),
                                Rotation2d.fromDegrees(90));
//.plus(ampEntryOffset)

        public static Translation2d getTagTranslation(int id) {
            if (kRedAprilTagLayout.containsKey(id)) {
                return kRedAprilTagLayout.get(id);
            } else if (kBlueAprilTagLayout.containsKey(id)) {
                return kBlueAprilTagLayout.get(id);
            } else {
                return null;
            }
        }
    }