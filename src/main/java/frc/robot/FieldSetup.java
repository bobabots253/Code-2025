package frc.robot;

import java.util.ArrayList;
import java.util.List;
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
                //39.3701
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
                put(10, new Translation2d(Units.inchesToMeters(481.39),Units.inchesToMeters(158.50)));
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

        public static final int kBlueTopFeedingStationId = 13;
        public static final int kBlueBotFeedingStationID = 12;
        public static final int kBlueProccessorId = 16;
        public static final int kBlueReefPosAId = 18;
        public static final int kBlueReefPosBId = 18;
        public static final int kBlueReefPosCId = 17;
        public static final int kBlueReefPosDId = 17;
        public static final int kBlueReefPosEId = 22;
        public static final int kBlueReefPosFId = 22;
        public static final int kBlueReefPosGId = 21;
        public static final int kBlueReefPosHId = 21;
        public static final int kBlueReefPosIId = 20;
        public static final int kBlueReefPosJId = 20;
        public static final int kBlueReefPosKId = 19;
        public static final int kBlueReefPosLId = 19;

        public static final int kRedTopFeedingStationId = 2;
        public static final int kRedBotFeedingStationID = 1;
        public static final int kRedProccessorId = 3;
        public static final int kRedReefPosAId = 7;
        public static final int kRedReefPosBId = 7;
        public static final int kRedReefPosCId = 8;
        public static final int kRedReefPosDId = 8;
        public static final int kRedReefPosEId = 9;
        public static final int kRedReefPosFId = 9;
        public static final int kRedReefPosGId = 10;
        public static final int kRedReefPosHId = 10;
        public static final int kRedReefPosIId = 11;
        public static final int kRedReefPosJId = 11;
        public static final int kRedReefPosKId = 6;
        public static final int kRedReefPosLId = 6;

        public static final Pose2d kReefFarEntranceTolerance = new Pose2d(new Translation2d(0.1, 0.1),
                Rotation2d.fromDegrees(0));

        // }
        //Centered with the Tag
        //Use this format for examples.
        // public static Supplier<Pose2d> allianceTopFeedingStationSupplier = () -> DriverStation.getAlliance()
        //         .orElse(DriverStation.Alliance.Blue) == Alliance.Blue
        //                 ? new Pose2d(new Translation2d(1.170, 7.010), 
        //                         new Rotation2d(306 * (Math.PI/180)))
        //                 : new Pose2d(new Translation2d(16.380, 7.010),
        //                         new Rotation2d(234 * (Math.PI/180)));

        // public static Supplier<Pose2d> allianceBottomFeedingStationSupplier = () -> DriverStation.getAlliance()
        //         .orElse(DriverStation.Alliance.Blue) == Alliance.Blue
        //                 ? new Pose2d(new Translation2d(1.070, 0.970),
        //                         new Rotation2d(0))
        //                 : new Pose2d(new Translation2d(16.470, 0.970),
        //                         new Rotation2d(0));

        // public static Supplier<Pose2d> allianceProccessorSupplier = () -> DriverStation.getAlliance()
        //         .orElse(DriverStation.Alliance.Blue) == Alliance.Blue
        //                 ? new Pose2d((new Translation2d(5.988, 0.511)),
        //                         new Rotation2d(0))
        //                 : new Pose2d((new Translation2d(11.561, 7.534)),
        //                          new Rotation2d(0));

        // public static Supplier<Pose2d> allianceReefFarSupplier = () -> DriverStation.getAlliance()
        //         .orElse(DriverStation.Alliance.Blue) == Alliance.Blue
        //                 ? new Pose2d((new Translation2d(5.803, 4.025)), 
        //                 new Rotation2d(0))
        //                 : new Pose2d ((new Translation2d( 11.746,4.025)), 
        //                 new Rotation2d(0));

        // public static Supplier<Pose2d> allianceReefCloseSupplier = () -> DriverStation.getAlliance()
        //         .orElse(DriverStation.Alliance.Blue) == Alliance.Blue
        //                 ? new Pose2d((new Translation2d(3.127,4.025)), 
        //                 new Rotation2d(0))
        //                 : new Pose2d ((new Translation2d( 14.379,4.025)), 
        //                 new Rotation2d(0));
        
        // public static Supplier<Pose2d> allianceReefLeftFrontSupplier = () -> DriverStation.getAlliance()
        //         .orElse(DriverStation.Alliance.Blue) == Alliance.Blue
        //                 ? new Pose2d((new Translation2d(5.147,5.163)), 
        //                 new Rotation2d(0))
        //                 : new Pose2d ((new Translation2d( 12.403,2.887)), 
        //                 new Rotation2d(0));
        
        // public static Supplier<Pose2d> allianceReefLeftBackSupplier = () -> DriverStation.getAlliance()
        //         .orElse(DriverStation.Alliance.Blue) == Alliance.Blue
        //                 ? new Pose2d((new Translation2d(3.832, 5.166)), 
        //                 new Rotation2d(0))
        //                 : new Pose2d (getTagTranslation(kRedReefPosKId)
        //                 .plus(new Translation2d(13.718, 2.885)), 
        //                 new Rotation2d(0));

        // public static Supplier<Pose2d> allianceReefRightFrontSupplier = () -> DriverStation.getAlliance()
        //         .orElse(DriverStation.Alliance.Blue) == Alliance.Blue
        //                 ? new Pose2d((new Translation2d(5.147, 2.889)), 
        //                 new Rotation2d(0))
        //                 : new Pose2d ((new Translation2d(12.403, 5.159)), 
        //                 new Rotation2d(0));

        // public static Supplier<Pose2d> allianceReefRightBackSupplier = () -> DriverStation.getAlliance()
        //         .orElse(DriverStation.Alliance.Blue) == Alliance.Blue
        //                 ? new Pose2d(new Translation2d(3.831, 2.884), 
        //                 new Rotation2d(0))
        //                 : new Pose2d (getTagTranslation(kRedReefPosDId)
        //                 .plus(new Translation2d(13.715, 5.164)), 
        //                 new Rotation2d(0));

        public static Supplier<Pose2d> allianceReefASupplier = () -> DriverStation.getAlliance()
                .orElse(DriverStation.Alliance.Blue) == Alliance.Blue
                        ? new Pose2d((new Translation2d(3.2812, 4.190238)), // last:(new Translation2d(3.2812, 4.190238)
                        new Rotation2d(0))
                        : new Pose2d ((new Translation2d(14.256818, 3.8549)), 
                        new Rotation2d(180 * (Math.PI/180)));

        public static Supplier<Pose2d> allianceAlgaeABSupplier = () -> DriverStation.getAlliance()
                .orElse(DriverStation.Alliance.Blue) == Alliance.Blue
                        ? new Pose2d((new Translation2d(3.2812, 4.025)), 
                        new Rotation2d(0))
                        : new Pose2d((new Translation2d(14.256818, 4.0200)), 
                        new Rotation2d(180 * (Math.PI/180)));
                        
        
        public static Supplier<Pose2d> allianceReefBSupplier = () -> DriverStation.getAlliance()
                .orElse(DriverStation.Alliance.Blue) == Alliance.Blue
                        ? new Pose2d((new Translation2d( 3.2812, 3.861562)), 
                        new Rotation2d(0))
                        : new Pose2d ((new Translation2d(14.256818, 4.1851)), 
                        new Rotation2d(180 * (Math.PI/180)));

        public static Supplier<Pose2d> allianceReefCSupplier = () -> DriverStation.getAlliance()
                .orElse(DriverStation.Alliance.Blue) == Alliance.Blue
                        ? new Pose2d((new Translation2d(3.74221189, 3.062764156)), 
                        new Rotation2d(60 * (Math.PI/180)))
                        : new Pose2d ((new Translation2d(13.80570011, 4.98901196)), 
                        new Rotation2d(-120 * (Math.PI/180)));
        
        public static Supplier<Pose2d> allianceAlgaeCDSupplier = () -> DriverStation.getAlliance()
                .orElse(DriverStation.Alliance.Blue) == Alliance.Blue
                        ? new Pose2d((new Translation2d(3.8857056, 2.980214156)), 
                        new Rotation2d(60 * (Math.PI/180)))
                        : new Pose2d((new Translation2d(13.662646, 5.07156196)), 
                        new Rotation2d(-120 * (Math.PI/180)));

        public static Supplier<Pose2d> allianceReefDSupplier = () -> DriverStation.getAlliance()
                .orElse(DriverStation.Alliance.Blue) == Alliance.Blue
                        ? new Pose2d((new Translation2d(4.02919931, 2.897664156)), 
                        new Rotation2d(60 * (Math.PI/180)))
                        : new Pose2d ((new Translation2d(13.51959189, 5.15411196)), 
                        new Rotation2d(-120 * (Math.PI/180)));

        public static Supplier<Pose2d> allianceReefESupplier = () -> DriverStation.getAlliance()
                .orElse(DriverStation.Alliance.Blue) == Alliance.Blue
                        ? new Pose2d((new Translation2d(4.94939989, 2.89768804)), 
                        new Rotation2d(120 * (Math.PI/180)))
                        : new Pose2d ((new Translation2d(12.59816289, 5.15411196)), 
                        new Rotation2d(-60 * (Math.PI/180)));

        public static Supplier<Pose2d> allianceAlgaeEFSupplier = () -> DriverStation.getAlliance()
                .orElse(DriverStation.Alliance.Blue) == Alliance.Blue
                        ? new Pose2d((new Translation2d(5.092454, 2.98023804)), 
                        new Rotation2d(120 * (Math.PI/180)))
                        : new Pose2d((new Translation2d(12.455158, 5.07156196)), 
                        new Rotation2d(-60 * (Math.PI/180)));

        public static Supplier<Pose2d> allianceReefFSupplier = () -> DriverStation.getAlliance()
                .orElse(DriverStation.Alliance.Blue) == Alliance.Blue
                        ? new Pose2d((new Translation2d(5.23550811, 3.06278804)), 
                        new Rotation2d(120 * (Math.PI/180)))
                        : new Pose2d ((new Translation2d(12.31215311, 4.98901196)), 
                        new Rotation2d(-60 * (Math.PI/180)));

        public static Supplier<Pose2d> allianceReefGSupplier = () -> DriverStation.getAlliance()
                .orElse(DriverStation.Alliance.Blue) == Alliance.Blue
                        ? new Pose2d((new Translation2d(5.686646, 3.8549)), 
                        new Rotation2d(180 * (Math.PI/180)))
                        : new Pose2d (getTagTranslation(kRedReefPosGId)
                        .plus(new Translation2d(11.850906, 4.1851)),
                        new Rotation2d(0));

        public static Supplier<Pose2d> allianceAlgaeGHSupplier = () -> DriverStation.getAlliance()
                .orElse(DriverStation.Alliance.Blue) == Alliance.Blue
                        ? new Pose2d((new Translation2d(5.686646, 4.0200)), 
                        new Rotation2d(180 * (Math.PI/180)))
                        : new Pose2d((new Translation2d(11.850906, 4.0200)), 
                        new Rotation2d(0 * (Math.PI/180)));

        public static Supplier<Pose2d> allianceReefHSupplier = () -> DriverStation.getAlliance()
                .orElse(DriverStation.Alliance.Blue) == Alliance.Blue
                        ? new Pose2d((new Translation2d(5.686646, 4.1851)), 
                        new Rotation2d(180 * (Math.PI/180)))
                        : new Pose2d ((new Translation2d(11.850906, 3.8549)), 
                        new Rotation2d(0));

        public static Supplier<Pose2d> allianceReefISupplier = () -> DriverStation.getAlliance()
                .orElse(DriverStation.Alliance.Blue) == Alliance.Blue
                        ? new Pose2d((new Translation2d(5.23550811, 4.98901196)), 
                        new Rotation2d(-120 * (Math.PI/180)))
                        : new Pose2d ((new Translation2d(12.31215311, 3.06278804)), 
                        new Rotation2d(60 * (Math.PI/180)));
        
        public static Supplier<Pose2d> allianceAlgaeIJSupplier = () -> DriverStation.getAlliance()
                .orElse(DriverStation.Alliance.Blue) == Alliance.Blue
                        ? new Pose2d((new Translation2d(5.092454, 5.07156196)), 
                        new Rotation2d(180 * (Math.PI/180)))
                        : new Pose2d((new Translation2d(12.455158, 2.98023804)), 
                        new Rotation2d(0 * (Math.PI/180)));

        public static Supplier<Pose2d> allianceReefJSupplier = () -> DriverStation.getAlliance()
                .orElse(DriverStation.Alliance.Blue) == Alliance.Blue
                        ? new Pose2d((new Translation2d(4.94939989, 5.15411196)), 
                        new Rotation2d(-120 * (Math.PI/180)))
                        : new Pose2d ((new Translation2d(12.59816289, 2.89768804)), 
                        new Rotation2d(60 * (Math.PI/180)));

        public static Supplier<Pose2d> allianceReefKSupplier = () -> DriverStation.getAlliance()
                .orElse(DriverStation.Alliance.Blue) == Alliance.Blue
                        ? new Pose2d((new Translation2d(3.74265111, 4.98902281)), //(4.02876089, 5.15412281)
                        new Rotation2d(-60 * (Math.PI/180)))
                        : new Pose2d ((new Translation2d(13.51964111, 2.89768804)), 
                        new Rotation2d(120 * (Math.PI/180)));

        public static Supplier<Pose2d> allianceAlgaeKLSupplier = () -> DriverStation.getAlliance()
                .orElse(DriverStation.Alliance.Blue) == Alliance.Blue
                        ? new Pose2d((new Translation2d(3.885706, 5.07157281)), 
                        new Rotation2d(180 * (Math.PI/180)))
                        : new Pose2d((new Translation2d(13.662646, 2.98023804)), 
                        new Rotation2d(0 * (Math.PI/180)));

        public static Supplier<Pose2d> allianceReefLSupplier = () -> DriverStation.getAlliance()
                .orElse(DriverStation.Alliance.Blue) == Alliance.Blue
                        ? new Pose2d((new Translation2d(4.02876089, 5.15412281)), //Translation2d(3.74265111, 4.98902281)
                        new Rotation2d(-60 * (Math.PI/180)))
                        : new Pose2d ((new Translation2d(13.80565089, 3.06278804)), 
                        new Rotation2d(120 * (Math.PI/180)));

        public static Supplier<Pose2d> majorStartingPosASupplier = () -> DriverStation.getAlliance()
                .orElse(DriverStation.Alliance.Blue) == Alliance.Blue
                        ? new Pose2d((new Translation2d(7.556, 6.169)), 
                        new Rotation2d(0))
                        : new Pose2d ((new Translation2d(9.994,6.169)), 
                        new Rotation2d(0));

        public static Supplier<Pose2d> majorStartingPosBSupplier = () -> DriverStation.getAlliance()
                .orElse(DriverStation.Alliance.Blue) == Alliance.Blue
                        ? new Pose2d((new Translation2d(7.556,4.025)), 
                        new Rotation2d(0))
                        : new Pose2d ((new Translation2d(9.994,4.025)), 
                        new Rotation2d(0));

       public static Supplier<Pose2d> majorStartingPosCSupplier = () -> DriverStation.getAlliance()
                .orElse(DriverStation.Alliance.Blue) == Alliance.Blue
                        ? new Pose2d((new Translation2d(7.556,2.013)), 
                        new Rotation2d(0))
                        : new Pose2d ((new Translation2d(9.994,2.013)), 
                        new Rotation2d(0));

       public static Supplier<Pose2d> minorStartingPosASupplier = () -> DriverStation.getAlliance()
                .orElse(DriverStation.Alliance.Blue) == Alliance.Blue
                        ? new Pose2d((new Translation2d(7.556,7.265)), 
                        new Rotation2d(0))
                        : new Pose2d ((new Translation2d(9.994,7.265)), 
                        new Rotation2d(0));

       public static Supplier<Pose2d> minorStartingPosBSupplier = () -> DriverStation.getAlliance()
                .orElse(DriverStation.Alliance.Blue) == Alliance.Blue
                        ? new Pose2d((new Translation2d(7.556,6.169)), 
                        new Rotation2d(0))
                        : new Pose2d ((new Translation2d(9.994,6.169)), 
                        new Rotation2d(0));

       public static Supplier<Pose2d> minorStartingPosCSupplier = () -> DriverStation.getAlliance()
                .orElse(DriverStation.Alliance.Blue) == Alliance.Blue
                        ? new Pose2d((new Translation2d(7.556,5.074)), 
                        new Rotation2d(0))
                        : new Pose2d ((new Translation2d(9.994,5.074)), 
                        new Rotation2d(0));
                
       public static Supplier<Pose2d> minorStartingPosDSupplier = () -> DriverStation.getAlliance()
                .orElse(DriverStation.Alliance.Blue) == Alliance.Blue
                        ? new Pose2d((new Translation2d(7.556, 4.025)), 
                        new Rotation2d(0))
                        : new Pose2d ((new Translation2d(9.994,4.0250)), 
                        new Rotation2d(0));

       public static Supplier<Pose2d> minorStartingPosESupplier = () -> DriverStation.getAlliance()
                .orElse(DriverStation.Alliance.Blue) == Alliance.Blue
                        ? new Pose2d((new Translation2d(7.556, 3.000)), 
                        new Rotation2d(0))
                        : new Pose2d ((new Translation2d(9.994,3.000)), 
                        new Rotation2d(0));

       public static Supplier<Pose2d> minorStartingPosFSupplier = () -> DriverStation.getAlliance()
                .orElse(DriverStation.Alliance.Blue) == Alliance.Blue
                        ? new Pose2d((new Translation2d(7.556, 1.904)),
                         new Rotation2d(0))
                        : new Pose2d ((new Translation2d(9.994,1.904)), 
                        new Rotation2d(0));

       public static Supplier<Pose2d> minorStartingPosGSupplier = () -> DriverStation.getAlliance()
                .orElse(DriverStation.Alliance.Blue) == Alliance.Blue
                        ? new Pose2d((new Translation2d(7.556, 0.808)), 
                        new Rotation2d(0))
                        : new Pose2d ((new Translation2d(9.994,0.808)), 
                        new Rotation2d(0));

       public static Supplier<Pose2d> midStartingLineSupplier = () -> DriverStation.getAlliance()
                .orElse(DriverStation.Alliance.Blue) == Alliance.Blue
                        ? new Pose2d((new Translation2d(7.556, 4.025)), 
                        new Rotation2d(0))
                        : new Pose2d ((new Translation2d(9.994, 4.025)), 
                        new Rotation2d(0));

       public static Supplier<Pose2d> spikeASupplier = () -> DriverStation.getAlliance()
                .orElse(DriverStation.Alliance.Blue) == Alliance.Blue
                        ? new Pose2d(getTagTranslation(kBlueReefPosDId)
                        .plus(new Translation2d(0.000,0.000)), new Rotation2d(0))
                        : new Pose2d (getTagTranslation(kRedReefPosDId)
                        .plus(new Translation2d( -0.000,0.000)), new Rotation2d(0));

       public static Supplier<Pose2d> spikeBSupplier = () -> DriverStation.getAlliance()
                .orElse(DriverStation.Alliance.Blue) == Alliance.Blue
                        ? new Pose2d(getTagTranslation(kBlueReefPosDId)
                        .plus(new Translation2d(0.000,0.000)), new Rotation2d(0))
                        : new Pose2d (getTagTranslation(kRedReefPosDId)
                        .plus(new Translation2d( -0.000,0.000)), new Rotation2d(0));

       public static Supplier<Pose2d> spikeCSupplier = () -> DriverStation.getAlliance()
                .orElse(DriverStation.Alliance.Blue) == Alliance.Blue
                        ? new Pose2d(getTagTranslation(kBlueReefPosDId)
                        .plus(new Translation2d(0.000,0.000)), new Rotation2d(0))
                        : new Pose2d (getTagTranslation(kRedReefPosDId)
                        .plus(new Translation2d( -0.000,0.000)), new Rotation2d(0));
//end here
//        public static Supplier<Pose2d> spikeCSupplier = () -> DriverStation.getAlliance()
//                 .orElse(DriverStation.Alliance.Blue) == Alliance.Blue
//                         ? new Pose2d(getTagTranslation(kBlueReefPosDId)
//                         .plus(new Translation2d(0.000,0.000)), new Rotation2d())
//                         : new Pose2d (getTagTranslation(kRedReefPosDId)
//                         .plus(new Translation2d( -0.000,0.000)), new Rotation2d());
        //Finish Reef Suppliers


        // public static final Supplier<Pose2d> allianceAmpEntryPoseSupplier = () -> DriverStation.getAlliance()
        //         .orElse(DriverStation.Alliance.Blue) == Alliance.Blue
        //                 ? new Pose2d(getTagTranslation(kBlueAmpId),
        //                         Rotation2d.fromDegrees(90))
        //                 : new Pose2d(getTagTranslation(kRedAmpId),
        //                         Rotation2d.fromDegrees(90));
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