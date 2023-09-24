package org.firstinspires.ftc.teamcode.Season;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.Range;

@TeleOp
public class A_Drive extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {

        //Motor Declaration
        DcMotor frontLeftMotor = hardwareMap.dcMotor.get("Leftfront");
        DcMotor backLeftMotor = hardwareMap.dcMotor.get("Leftback");
        DcMotor frontRightMotor = hardwareMap.dcMotor.get("Rightfront");
        DcMotor backRightMotor = hardwareMap.dcMotor.get("Rightback");

        //Motor Reverse
        frontRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        backRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        //Verify Robot Waiting
        telemetry.addData(">", "Robot Ready.  Press Play.");
        telemetry.update();

        waitForStart();

        if (isStopRequested()) return;

        while (opModeIsActive()) {

            //Game pad Declaration
            double y = gamepad1.left_stick_y;
            double x = -gamepad1.left_stick_x * 1.1; //Strafing Correction
            double rx = gamepad1.right_stick_x;

            //Mecanum Calculations
            double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
            double frontLeftPower = (y + x + rx) / denominator;
            double backLeftPower = (y - x + rx) / denominator;
            double frontRightPower = (y - x - rx) / denominator;
            double backRightPower = (y + x - rx) / denominator;

            //Mecanum Driving with Triggers
            if (gamepad1.left_trigger>0.1){
                frontLeftMotor.setPower(-gamepad1.left_trigger);
                frontRightMotor.setPower(gamepad1.left_trigger);
                backLeftMotor.setPower(gamepad1.left_trigger);
                backRightMotor.setPower(-gamepad1.left_trigger);
            }
            else if (gamepad1.right_trigger>0.1){
                frontLeftMotor.setPower(gamepad1.right_trigger);
                frontRightMotor.setPower(-gamepad1.right_trigger);
                backLeftMotor.setPower(-gamepad1.right_trigger);
                backRightMotor.setPower(gamepad1.right_trigger);
            }
            else{
                double drive = -gamepad1.left_stick_y;
                double turn = gamepad1.right_stick_x;
                frontLeftMotor.setPower(Range.clip(drive+turn,-1.0,1.0));
                backLeftMotor.setPower(Range.clip(drive+turn,-1.0,1.0));
                frontRightMotor.setPower(Range.clip(drive-turn,-1.0,1.0));
                backRightMotor.setPower(Range.clip(drive-turn,-1.0,1.0));
            }

            //Telemetry Update
            telemetry.addData("Left Stick X", y);
            telemetry.addData("Left Stick Y", x);
            telemetry.addData("Right Stick X", rx);
            telemetry.update();
            }
        }
    }

