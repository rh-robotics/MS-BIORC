package org.firstinspires.ftc.teamcode.teleop.demonstrationfiles;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

/**
 * TeleOp OpMode for the Glow Run outreach event, including strafe drive code and lights code.
 */
@TeleOp(name = "Glow Run Code", group = "demonstration")
public class GlowRun extends OpMode {
    DcMotorEx leftFront, rightFront, rightRear, leftRear;
    private Servo headlight1;
    private Servo headlight2;

    // init() Runs ONCE after the driver hits initialize
    @Override
    public void init() {
        // Tell the driver the Op is initializing
        telemetry.addData("Status", "Initializing");

        headlight1 = hardwareMap.get(Servo.class, "headlight1");
        headlight2 = hardwareMap.get(Servo.class, "headlight2");

        headlight1.setPosition(1);
        headlight2.setPosition(1);

        // Do all init stuff
        leftFront = hardwareMap.get(DcMotorEx.class, "leftFront");
        rightFront = hardwareMap.get(DcMotorEx.class, "rightFront");
        leftRear = hardwareMap.get(DcMotorEx.class, "leftRear");
        rightRear = hardwareMap.get(DcMotorEx.class, "rightRear");

        leftFront.setDirection(DcMotorEx.Direction.FORWARD);
        rightFront.setDirection(DcMotorEx.Direction.REVERSE);
        leftRear.setDirection(DcMotorEx.Direction.FORWARD);
        rightRear.setDirection(DcMotorEx.Direction.REVERSE);

        // Tell the driver the robot is ready
        telemetry.addData("Status", "Initialized");
    }

    // init_loop() - Runs continuously until the driver hits play
    @Override
    public void init_loop() {
    }

    // loop() - Runs continuously while the OpMode is active
    @Override
    public void loop() {
        double leftFPower;
        double rightFPower;
        double leftBPower;
        double rightBPower;
        double drive = -gamepad1.left_stick_y;
        double turn = -gamepad1.left_stick_x * 0.6;
        double strafe = gamepad1.right_stick_x * 0.8;

        if (gamepad1.left_trigger > 0.5 || gamepad1.right_trigger > 0.5) {
            headlight1.setPosition(1);
            headlight2.setPosition(1);
        } else {
            headlight1.setPosition(0.375);
            headlight2.setPosition(0.375);
        }

        // Calculate drive power
        if (drive != 0 || turn != 0) {
            leftFPower = Range.clip(drive + turn, -1.0, 1.0);
            rightFPower = Range.clip(drive - turn, -1.0, 1.0);
            leftBPower = Range.clip(drive + turn, -1.0, 1.0);
            rightBPower = Range.clip(drive - turn, -1.0, 1.0);
        } else if (strafe != 0) {
            // Strafing
            leftFPower = -strafe;
            rightFPower = strafe;
            leftBPower = strafe;
            rightBPower = -strafe;
        } else {
            leftFPower = 0;
            rightFPower = 0;
            leftBPower = 0;
            rightBPower = 0;
        }

        // Set power to values calculated above
        leftFront.setPower(leftFPower);
        leftRear.setPower(leftBPower);
        rightFront.setPower(rightFPower);
        rightRear.setPower(rightBPower);
    }
}