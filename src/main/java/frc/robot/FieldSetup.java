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
        // public static final Translation2d bluePassPosition = new Translation2d(0.6, 7.5);
        // public static final Translation2d redPassPosition = new Translation2d(16, 7.5);
        public static final Translation2d blueBargeAPosition = new Translation2d(8.775, 7.270);
        public static final Translation2d blueBargeBPosition = new Translation2d(8.775, 6.175);
        public static final Translation2d blueBargeCPosition = new Translation2d(8.775, 5.075);
        public static final Translation2d RedBargeAPosition = new Translation2d(8.775, 3.000);
        public static final Translation2d RedBargeBPosition = new Translation2d(8.775, 1.905);
        public static final Translation2d RedBargeCPosition = new Translation2d(8.775, 0.810);

        // public static final Supplier<Translation2d> alliancePassPositionSupplier = () -> DriverStation.getAlliance()
        //         .orElse(DriverStation.Alliance.Blue) == DriverStation.Alliance.Blue
        //                 ? bluePassPosition
        //                 : redPassPosition;

        public static final TreeMap<Integer, Translation2d> kRedAprilTagLayout = new TreeMap<Integer, Translation2d>() {
            {
                put(12, new Translation2d(Units.inchesToMeters(33.51),Units.inchesToMeters(25.80)));
                put(13, new Translation2d(Units.inchesToMeters(33.51),Units.inchesToMeters(291.20)));
                put(14, new Translation2d(Units.inchesToMeters(325.68),Units.inchesToMeters(241.64)));
                put(15, new Translation2d(Units.inchesToMeters(325.68),Units.inchesToMeters(75.39)));
                put(16, new Translation2d(Units.inchesToMeters(235.73),Units.inchesToMeters(-0.15)));
                put(17, new Translation2d(Units.inchesToMeters(160.39),Units.inchesToMeters(130.17)));
                put(18, new Translation2d(Units.inchesToMeters(144.00),Units.inchesToMeters(158.50)));
                put(19, new Translation2d(Units.inchesToMeters(160.39),Units.inchesToMeters(186.83)));
                put(20, new Translation2d(Units.inchesToMeters(193.10),Units.inchesToMeters(186.83)));
                put(21, new Translation2d(Units.inchesToMeters(209.49),Units.inchesToMeters(158.50)));
                put(22, new Translation2d(Units.inchesToMeters(193.10),Units.inchesToMeters(130.17)));
                put(51, new Translation2d(Units.inchesToMeters(566.77), Units.inchesToMeters(287.00)));
            }

        };

        public static final TreeMap<Integer, Translation2d> kBlueAprilTagLayout = new TreeMap<Integer, Translation2d>() {
            {
                put(1, new Translation2d(Units.inchesToMeters(657.37),Units.inchesToMeters(25.80)));
                put(2, new Translation2d(Units.inchesToMeters(657.37),Units.inchesToMeters(291.20)));
                put(3, new Translation2d(Units.inchesToMeters(455.15),Units.inchesToMeters(317.15)));
                put(4, new Translation2d(Units.inchesToMeters(365.20),Units.inchesToMeters(241.64)));
                put(5, new Translation2d(Units.inchesToMeters(365.20),Units.inchesToMeters(75.39)));
                put(6, new Translation2d(Units.inchesToMeters(530.49),Units.inchesToMeters(130.17)));
                put(7, new Translation2d(Units.inchesToMeters(546.87),Units.inchesToMeters(158.50)));
                put(8, new Translation2d(Units.inchesToMeters(530.49),Units.inchesToMeters(186.83)));
                put(9, new Translation2d(Units.inchesToMeters(497.77),Units.inchesToMeters(186.83)));
                put(10, new Translation2d(Units.inchesToMeters(481.39),Units.inchesToMeters(150.50)));
                put(11, new Translation2d(Units.inchesToMeters(497.77),Units.inchesToMeters(130.17)));
                put(61, new Translation2d(Units.inchesToMeters(0), Units.inchesToMeters(287)));
            }
        };

        //put(, new Translation2d(Units.inchesToMeters(X),Units.inchesToMeters(Y)));
        // 6 Total Starting Configs: For each cage
        //Important POI:
        // All Reef Scoring Position (A-L)
        //Proccessor
        // Top & Bottom Feeding Station


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