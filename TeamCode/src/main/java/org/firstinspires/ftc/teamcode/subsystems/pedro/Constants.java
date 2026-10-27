package org.firstinspires.ftc.teamcode.subsystems.pedro;

import com.pedropathing.algorithm.Foresight;
import com.pedropathing.algorithm.ForesightConfig;
import com.pedropathing.controllers.Controller;
import com.pedropathing.follower.Follower;
import com.pedropathing.math.Matrix;
import com.pedropathing.math.Vector2D;
import com.pedropathing.revhub.drivetrains.Mecanum;
import com.pedropathing.revhub.drivetrains.MecanumConfig;
import com.pedropathing.revhub.localizers.PinpointConfig;
import com.pedropathing.revhub.localizers.PinpointLocalizer;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class Constants {
    public static MecanumConfig drivetrainConfig = new MecanumConfig(c -> {
        c.frontLeftName.set("leftFront");
        c.frontRightName.set("rightFront");
        c.backLeftName.set("leftRear");
        c.backRightName.set("rightRear");
        c.frontLeftDirection.set(DcMotorSimple.Direction.FORWARD);
        c.frontRightDirection.set(DcMotorSimple.Direction.REVERSE);
        c.backLeftDirection.set(DcMotorSimple.Direction.FORWARD);
        c.backRightDirection.set(DcMotorSimple.Direction.REVERSE);

        c.manualBrakeMode.set(true);
    });

    public static PinpointConfig localizerConfig = new PinpointConfig(c -> {
        c.name.set("pinpoint");
        c.podType.set(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD);
        c.xPodOffset.set(4.8125);
        c.yPodOffset.set(-2.5625);
        c.xPodDirection.set(GoBildaPinpointDriver.EncoderDirection.REVERSED);
        c.yPodDirection.set(GoBildaPinpointDriver.EncoderDirection.FORWARD);
        c.globalDistanceUnit.set(DistanceUnit.INCH);
        c.offsetUnits.set(DistanceUnit.INCH);
    });

    public static ForesightConfig foresightConfig = new ForesightConfig(
            c -> {
                Controller primaryTranslationalForward = Controller.proportional(0.20015922894608776);
                Controller secondaryTranslationalForward = Controller.proportional(0.07395348674247997);
                Controller primaryTranslationalLateral = Controller.proportional(1.8817159700401627);
                Controller secondaryTranslationalLateral = Controller.proportional(0.6952437705530936);

                c.forwardTranslational.set(Controller.piecewise(secondaryTranslationalForward).put(2.5, primaryTranslationalForward));
                c.strafeTranslational.set(Controller.piecewise(secondaryTranslationalLateral).put(2.5, primaryTranslationalLateral));

                c.coast.set(Controller.proportionalFeedforward(0.012694435909688771));
                c.brake.set(Controller.proportionalFeedforward(0.010790270523235454));

                c.headingFeedback.set(Controller.proportional(3.4889889722173844));
                c.headingBrakeCoefficients.set(Vector2D.cartesian(0.045384519125941755, 0.00915466075902129));

                c.linearBrakeCoefficients.set(Matrix.diag(0.06088116251234016, 0.1603489409931017));
                c.quadraticBrakeCoefficients.set(Matrix.diag(0.0020115058255448935, 1.9187472150177255E-4));

                c.maxAchievableForwardVelocity.set(80.7934415102275);
                c.maxAchievableStrafeVelocity.set(43.29615495430702);
                c.naturalForwardDeceleration.set(29.40758505710288);
                c.naturalStrafeDeceleration.set(33.0101052102728);
            }
    );

    public static Follower create(HardwareMap h) {
        return new Follower(
                new PinpointLocalizer(h, localizerConfig),
                new Mecanum(h, drivetrainConfig),
                new Foresight(foresightConfig)
        );
    }
}